package com.sl.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码散列（T3-2）。
 * <p>
 * 背景：库里存量密码是 <b>SM3 明文摘要再转大写</b>（LoginView / UserAddUpdateComponent 里
 * 直接 {@code SmUtil.sm3(pwd).toUpperCase()}）。SM3 是摘要不是 KDF，没有 salt、没有工作因子，
 * 彩虹表一撞就穿；而且同一个密码在所有用户那里散列值一样，能直接看出谁用了弱口令。
 * <p>
 * 迁移策略 —— <b>不强制改密码，登录时静默升级</b>：
 * <ol>
 *   <li>新密码一律写 BCrypt（自带随机 salt + 成本因子）；</li>
 *   <li>校验时按前缀判断：{@code $2a$/$2b$/$2y$} 走 BCrypt，否则退化为旧的 SM3 大写比对；</li>
 *   <li>SM3 比对了但通过了，由调用方用 {@link #hash} 重新写回库里，用户无感知。</li>
 * </ol>
 * 这样存量账号下次登录就自动换算法，不需要停机改数据，也不需要群发"请重置密码"。
 */
@Service
public class PasswordHasher {

	private static final Logger log = LoggerFactory.getLogger(PasswordHasher.class);

	/** BCrypt 哈希的固定前缀，用来区分新旧算法 */
	private static final String[] BCRYPT_PREFIX = {"$2a$", "$2b$", "$2y$"};

	/** 成本因子。10 约等于单机 60~100ms/次，对登录频率足够安全又不拖慢响应 */
	private static final int LOG_ROUNDS = 10;

	/**
	 * 生成新密码的散列值（BCrypt）。
	 */
	public String hash(String rawPassword) {
		if (null == rawPassword) {
			return null;
		}
		return BCrypt.hashpw(rawPassword, BCrypt.gensalt(LOG_ROUNDS));
	}

	/**
	 * 比对明文与库中存储值。兼容存量 SM3。
	 *
	 * @param rawPassword 用户本次输入的明文
	 * @param stored      库里的存储值（BCrypt 串或旧 SM3 大写摘要）
	 */
	public boolean matches(String rawPassword, String stored) {
		if (null == rawPassword || null == stored || stored.isEmpty()) {
			return false;
		}
		if (isBcrypt(stored)) {
			try {
				return BCrypt.checkpw(rawPassword, stored);
			} catch (Exception e) {
				// 存储值被人为改坏了，别让异常冒泡成 500，按不匹配处理
				log.warn("BCrypt 校验异常，按密码错误处理", e);
				return false;
			}
		}
		// 存量：SM3 摘要后转大写
		return stored.equalsIgnoreCase(SmUtil.sm3(rawPassword).toUpperCase());
	}

	/**
	 * 存储值是不是旧算法 —— 是的话登录成功后要重写为 BCrypt。
	 */
	public boolean isLegacy(String stored) {
		return null != stored && !stored.isEmpty() && !isBcrypt(stored);
	}

	private static boolean isBcrypt(String stored) {
		for (String p : BCRYPT_PREFIX) {
			if (stored.startsWith(p)) {
				return true;
			}
		}
		return false;
	}
}
