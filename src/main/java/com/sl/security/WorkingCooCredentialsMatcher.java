package com.sl.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.entity.User;
import com.sl.mapper.UserMapper;

/**
 * Shiro 凭证匹配器（T3-2 / T3-3）。
 * <p>
 * 替换默认的 SimpleCredentialsMatcher（它只会做字符串相等比较，
 * 也就是说只有把明文直接入库才用得上，SM3 / BCrypt 都得自己实现）。
 * <p>
 * 这里顺带把两件事收在一个地方：
 * <ol>
 *   <li>算法兼容 + 静默升级：SM3 老密码校验通过后立刻重写为 BCrypt；</li>
 *   <li>失败计数：不匹配就记一次失败，成功就清零 —— 限流不用散落在 LoginView 里。</li>
 * </ol>
 */
@Service
public class WorkingCooCredentialsMatcher implements CredentialsMatcher {

	private static final Logger log = LoggerFactory.getLogger(WorkingCooCredentialsMatcher.class);

	@Autowired
	private PasswordHasher passwordHasher;
	@Autowired
	private LoginAttemptService loginAttemptService;
	@Autowired
	private UserMapper userMapper;

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		if (!(token instanceof UsernamePasswordToken upt)) {
			return false;
		}
		char[] pwd = upt.getPassword();
		if (null == pwd || pwd.length == 0) {
			return false;
		}
		String userId = upt.getUsername();
		String raw = new String(pwd);
		Object storedObj = info.getCredentials();
		String stored = null == storedObj ? null : storedObj.toString();

		boolean ok = passwordHasher.matches(raw, stored);
		if (!ok) {
			loginAttemptService.loginFailed(userId);
			return false;
		}
		loginAttemptService.loginSucceeded(userId);
		upgradeLegacy(userId, raw, stored);
		return true;
	}

	/**
	 * 老算法（SM3）校验通过 → 用本次输入的明文生成 BCrypt 写回。
	 * 只在「确实用旧算法匹配上了」时才写，避免每次登录都无谓更新。
	 */
	private void upgradeLegacy(String userId, String raw, String stored) {
		if (null == userId || null == raw || !passwordHasher.isLegacy(stored)) {
			return;
		}
		try {
			User u = new User();
			u.setUserId(userId);
			u.setPassword(passwordHasher.hash(raw));
			int n = userMapper.updateByPrimaryKeySelective(u);
			if (n > 0) {
				log.info("账号 {} 的密码已升级为 BCrypt", userId);
			}
		} catch (Exception e) {
			// 升级失败不影响本次登录，下次登录还会再试一次
			log.warn("密码算法升级失败，user={}", userId, e);
		}
	}
}
