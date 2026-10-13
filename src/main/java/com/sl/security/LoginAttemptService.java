package com.sl.security;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 登录失败限流（T3-3）。
 * <p>
 * 规则：同一账号连续失败 {@value #MAX_ATTEMPTS} 次后锁定 {@value #LOCK_MINUTES} 分钟。
 * 成功一次即清零。
 * <p>
 * 为什么放内存而不是 Redis/DB：单机部署够用，且避免"限流表自己被打爆"。
 * 多实例部署时把 {@link #ATTEMPTS} 换成 Redis 自增即可，接口不用动。
 * 内存计数的代价是应用重启后计数清零 —— 对"防暴力破解"这个目标来说可以接受，
 * 因为攻击者重启不了我们的进程。
 */
@Service
public class LoginAttemptService {

	private static final Logger log = LoggerFactory.getLogger(LoginAttemptService.class);

	/** 允许的最大连续失败次数 */
	public static final int MAX_ATTEMPTS = 5;
	/** 锁定时长（分钟） */
	public static final long LOCK_MINUTES = 15;

	private static final long LOCK_MILLIS = LOCK_MINUTES * 60_000L;

	private static final class Counter {
		int failures;
		long lockUntil;
		volatile long lastTouch = System.currentTimeMillis();
	}

	private final Map<String, Counter> attempts = new ConcurrentHashMap<>();

	/** 账号当前是否被锁 */
	public boolean isLocked(String userId) {
		Counter c = attempts.get(norm(userId));
		if (null == c) {
			return false;
		}
		if (c.lockUntil > 0 && System.currentTimeMillis() < c.lockUntil) {
			return true;
		}
		if (c.lockUntil > 0) {
			// 锁已过期，顺手清掉
			attempts.remove(norm(userId));
		}
		return false;
	}

	/** 还剩多少秒解锁；没被锁返回 0 */
	public long remainSeconds(String userId) {
		Counter c = attempts.get(norm(userId));
		if (null == c || c.lockUntil <= 0) {
			return 0;
		}
		long ms = c.lockUntil - System.currentTimeMillis();
		return ms > 0 ? ms / 1000 + 1 : 0;
	}

	/** 剩余可尝试次数；已锁定返回 0 */
	public int remainAttempts(String userId) {
		Counter c = attempts.get(norm(userId));
		if (null == c) {
			return MAX_ATTEMPTS;
		}
		return Math.max(0, MAX_ATTEMPTS - c.failures);
	}

	/**
	 * 记录一次失败。达到阈值时打锁定时间戳。
	 * @return 本次失败后是否被锁定
	 */
	public boolean loginFailed(String userId) {
		String key = norm(userId);
		Counter c = attempts.computeIfAbsent(key, k -> new Counter());
		c.failures++;
		c.lastTouch = System.currentTimeMillis();
		if (c.failures >= MAX_ATTEMPTS) {
			c.lockUntil = System.currentTimeMillis() + LOCK_MILLIS;
			log.warn("账号 {} 连续失败 {} 次，锁定 {} 分钟，解锁时间 {}",
					key, c.failures, LOCK_MINUTES, new Date(c.lockUntil));
			return true;
		}
		log.info("账号 {} 登录失败，剩余可尝试 {} 次", key, MAX_ATTEMPTS - c.failures);
		return false;
	}

	/** 登录成功：清零计数 */
	public void loginSucceeded(String userId) {
		if (null == userId) {
			return;
		}
		attempts.remove(norm(userId));
	}

	/**
	 * 每小时清理一次过期条目，避免长期运行下 Map 只增不减。
	 * 只清"已解锁且 1 小时没动静"的，锁定中的不会被误删。
	 */
	@Scheduled(initialDelay = 600_000, fixedDelay = 3_600_000)
	public void sweep() {
		long expire = System.currentTimeMillis() - 3_600_000L;
		int before = attempts.size();
		Iterator<Map.Entry<String, Counter>> it = attempts.entrySet().iterator();
		while (it.hasNext()) {
			Counter c = it.next().getValue();
			boolean unlocked = c.lockUntil <= 0 || c.lockUntil < System.currentTimeMillis();
			if (unlocked && c.lastTouch < expire) {
				it.remove();
			}
		}
		log.debug("登录限流计数清理：{} -> {}", before, attempts.size());
	}

	private static String norm(String userId) {
		return null == userId ? "" : userId.trim();
	}
}
