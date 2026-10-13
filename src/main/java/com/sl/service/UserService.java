package com.sl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.entity.RolesPermission;
import com.sl.entity.RolesPermissionExample;
import com.sl.entity.User;
import com.sl.entity.UserExample;
import com.sl.entity.UsersRole;
import com.sl.entity.UsersRoleExample;
import com.sl.mapper.PermissionMapper;
import com.sl.mapper.RoleMapper;
import com.sl.mapper.RolesPermissionMapper;
import com.sl.mapper.UserMapper;
import com.sl.mapper.UsersRoleMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private UsersRoleMapper userRoleMapper;
	@Autowired
	private RolesPermissionMapper rolePermissionMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	
	public User getUserById(String userId) {
		UserExample ex = new UserExample();
		ex.createCriteria().andUserIdEqualTo(userId);
		List<User> selectByExample = userMapper.selectByExample(ex);
		if (null != selectByExample && !selectByExample.isEmpty()) {
			return selectByExample.get(0);
		}
		return null;
	}
	
	public UsersRole getRoleByUserId(String userId) {
		UsersRoleExample ex = new UsersRoleExample();
		ex.createCriteria().andUserIdEqualTo(userId);
		List<UsersRole> selectByExample = userRoleMapper.selectByExample(ex);
		if (null != selectByExample && !selectByExample.isEmpty()) {
			return selectByExample.get(0);
		}
		return null;
	}
	
	public List<RolesPermission> getRolePermissionByRoleId(String roleId){
		RolesPermissionExample ex = new RolesPermissionExample();
		ex.createCriteria().andRoleIdEqualTo(roleId);
		List<RolesPermission> selectByExample = rolePermissionMapper.selectByExample(ex);
		return selectByExample;
	}
	
}
