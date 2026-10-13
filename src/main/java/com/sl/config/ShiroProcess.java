package com.sl.config;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sl.entity.Permission;
import com.sl.security.LoginAttemptService;
import com.sl.security.WorkingCooCredentialsMatcher;

import jakarta.annotation.PostConstruct;
import com.sl.entity.PermissionExample;
import com.sl.entity.RolesPermission;
import com.sl.entity.RolesPermissionExample;
import com.sl.entity.User;
import com.sl.entity.UsersRole;
import com.sl.entity.UsersRoleExample;
import com.sl.mapper.PermissionMapper;
import com.sl.mapper.RoleMapper;
import com.sl.mapper.RolesPermissionMapper;
import com.sl.mapper.UserMapper;
import com.sl.mapper.UsersRoleMapper;

@Service
public class ShiroProcess extends AuthorizingRealm{
	private static final Logger logger = LoggerFactory.getLogger(ShiroProcess.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UsersRoleMapper usersRolesMapper;
	@Autowired
	private RolesPermissionMapper rolesPermissionsMapper;
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private PermissionMapper permissionMapper;

	/**
	 * T3-2：凭证匹配器。默认的 SimpleCredentialsMatcher 只会做字符串相等比较，
	 * 撑不起 SM3 / BCrypt，必须在这里换掉 —— 放在 @PostConstruct 里是因为
	 * AuthenticatingRealm 的 matcher 默认在构造时就已初始化成 SimpleCredentialsMatcher，
	 * 晚于构造再替换同样生效（login 时才读取），但必须在第一次 login 之前完成。
	 */
	@Autowired
	private WorkingCooCredentialsMatcher credentialsMatcher;

	/**
	 * T3-3：登录失败限流。锁定检查放在认证入口而不是 LoginView，
	 * 这样任何调用 SecurityUtils.getSubject().login() 的地方都受保护。
	 */
	@Autowired
	private LoginAttemptService loginAttemptService;

	@PostConstruct
	public void initCredentialsMatcher() {
		setCredentialsMatcher(credentialsMatcher);
	}
	
	
	/**
	 * 用户认证
	 */
		@Override
		protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
			if (logger.isDebugEnabled()) {
				logger.debug("开始认证用户信息");
			}
			 //UsernamePasswordToken对象用来存放提交的登录信息
	        UsernamePasswordToken token1=(UsernamePasswordToken) token;

	        // T3-3：连续失败达阈值后锁定，直接抛 LockedAccountException，
	        // 不再查库比对，既省一次 DB 往返也不泄露"密码到底对不对"
	        if (loginAttemptService.isLocked(token1.getUsername())) {
	        	logger.warn("账号 {} 处于锁定状态，拒绝认证", token1.getUsername());
	        	throw new LockedAccountException("账号已锁定，请稍后再试");
	        }

	        //查出是否有此用户
	       User user = userMapper.selectByPrimaryKey(token1.getUsername());
	        if(user!=null){
	            // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
	            return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
	        }
	        return null;
		}
		
	
	/**
	 * 用户授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (logger.isDebugEnabled()) {
			logger.debug("开始为用户授权=====");
		}
		// 添加权限 和 角色信息
				SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
				// 获取当前登陆用户
				User user  =(User) SecurityUtils.getSubject().getPrincipal();
        if(user!=null){
            return grantedAuthority(authorizationInfo, user);
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
	}

	@Cacheable(value="commonCache")
	private AuthorizationInfo grantedAuthority(SimpleAuthorizationInfo authorizationInfo, User user) {
		//给用户授予角色和权限
		UsersRoleExample ex = new UsersRoleExample();
		ex.createCriteria().andUserIdEqualTo(user.getUserId());
		List<UsersRole> userRolesList = usersRolesMapper.selectByExample(ex);
		
		List<String> roleids = userRolesList.stream().map(e1 ->e1.getRoleId()).collect(Collectors.toList());
		Iterator<String> iterator = roleids.iterator();
		while (iterator.hasNext()) {
			String roleId = iterator.next();
			authorizationInfo.addRole(roleId);
			RolesPermissionExample exe = new RolesPermissionExample();
			exe.createCriteria().andRoleIdEqualTo(roleId);
			List<RolesPermission> selectByExample = rolesPermissionsMapper.selectByExample(exe);
			List<String> permissionids = selectByExample.stream().map(e ->e.getPermissionId()).collect(Collectors.toList());
			
			PermissionExample pe = new PermissionExample();
			pe.createCriteria().andPermissionIdIn(permissionids);
			List<Permission> selectByExample2 = permissionMapper.selectByExample(pe);
			
			if (selectByExample2.size()!=0) {
				for (Permission permission : selectByExample2) {
					authorizationInfo.addStringPermission(permission.getResourceName());
				}
			}
		}
		
		return authorizationInfo;
	}
}
