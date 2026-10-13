package com.sl.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.Permission;
import com.sl.entity.PermissionExample;
import com.sl.entity.Role;
import com.sl.entity.RoleExample;
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
public class UserManageMentProcess{

    
	@Autowired
	UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private UsersRoleMapper usersRoleMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private RolesPermissionMapper rolesPermissionMapper;


	public void insertSelective(User user) {
		userMapper.insertSelective(user);
	}

	public User selectUserById(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}
	public List<User> selectAllUser() {
		return userMapper.selectList(new QueryWrapper<User>());
	}
	public List<User> selectUserByIdInstitution(String idInstitution) {
		UserExample example = new UserExample();
		example.createCriteria().andIdInstitutionEqualTo(idInstitution);
		return userMapper.selectByExample(example);
	}

	public void updateUserByUserId(User user) {
		userMapper.updateByPrimaryKeySelective(user);
	}

	public List<User> getUserByIds(Set<String> userIds){
		List<String> ids = new ArrayList<String>(userIds);
		UserExample example = new UserExample();
		example.createCriteria().andUserIdIn(ids);
		List<User> selectByExample = userMapper.selectByExample(example);
		return selectByExample;
	}
	public void deleteUserById(String userId) {
		userMapper.deleteByPrimaryKey(userId);
	}

	public Role selectRoleByRoleId(String roleId) {
		return roleMapper.selectByPrimaryKey(ComponentUtil.getCurrentInstitution(), roleId);
	}

	public List<String> selectRoleIdsByUserId(String userId) {
		UsersRoleExample ex = new UsersRoleExample();
		ex.createCriteria().andUserIdEqualTo(userId);
		List<UsersRole> selectByExample = usersRoleMapper.selectByExample(ex);
        List<String> roleIds = selectByExample.stream().map(e ->e.getRoleId()).collect(Collectors.toList());
		return roleIds;
	}
	
	public List<String> selectUserIdByRoleId(String roleId) {
		UsersRoleExample ex = new UsersRoleExample();
		ex.createCriteria().andRoleIdEqualTo(roleId);
		List<UsersRole> selectByExample = usersRoleMapper.selectByExample(ex);
		List<String> userIds = selectByExample.stream().map(e ->e.getUserId()).collect(Collectors.toList());
		return userIds;
	}
	
	public List<User> selectUsersByRoleId(String roleId) {
		List<String> userIds = selectUserIdByRoleId(roleId);
		if (null == userIds || userIds.isEmpty()) {
			return new ArrayList<User>();
		}
		UserExample example = new UserExample();
		example.createCriteria().andUserIdIn(userIds);
		List<User> selectByExample = userMapper.selectByExample(example);
		return selectByExample;
	}
	
	public List<Role> selectRolesByUserId(String userId) {
		UsersRoleExample ex = new UsersRoleExample();
		ex.createCriteria().andUserIdEqualTo(userId);
		List<UsersRole> selectByExample = usersRoleMapper.selectByExample(ex);
		List<String> roleIds = selectByExample.stream().map(e ->e.getRoleId()).collect(Collectors.toList());
		RoleExample example = new RoleExample();
		example.createCriteria().andRoleIdIn(roleIds);
		List<Role> selectByExample2 = roleMapper.selectByExample(example);
		return selectByExample2;
	}

	public void deleteRoleByRoleId(String roleId) {
		roleMapper.deleteByPrimaryKey(ComponentUtil.getCurrentInstitution(), roleId);
	}

	// 删除role和permission关系表
	public int deleteRolePermissionByRoleId(String roleId) {
		RolesPermissionExample example = new RolesPermissionExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		int deleteByExample = rolesPermissionMapper.deleteByExample(example);
		return deleteByExample;
	}

	public Long selectUserCounts(String platform) {
		return userMapper.selectCount(new QueryWrapper<User>());
	}

	public List<Permission> selectAllPermissions() {
		PermissionExample example = new PermissionExample();
		example.setOrderByClause(" NBR_ORDER ASC");
		return permissionMapper.selectByExample(example);
	}
	
	public Permission selectPermissionById(String permissionID){
		return permissionMapper.selectByPrimaryKey(permissionID);
	}

	public void insertUserRole(String userId, String roleId) {
		usersRoleMapper.insert(new UsersRole(userId, roleId));
	}

	public void deleUserRoleByUserId(String userId) {
		UsersRoleExample ex = new UsersRoleExample();
		ex.createCriteria().andUserIdEqualTo(userId);
		usersRoleMapper.deleteByExample(ex);
	}


	public void insertRole(Role role) {
		roleMapper.insertSelective(role);
	}

	public List<String> selectPermissionIdsByRoleId(String roleId) {
		RolesPermissionExample example = new RolesPermissionExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		List<RolesPermission> selectByExample = rolesPermissionMapper.selectByExample(example);
		List<String> collect = selectByExample.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
		return collect;
	}
	
	public List<Permission> selectPermissionByRoleId(String roleId) {
//		RolesPermissionExample sl = new RolesPermissionExample();
//		sl.createCriteria().andRoleIdEqualTo(roleId);
//		List<RolesPermission> selectByExample = rolesPermissionMapper.selectByExample(sl);
		QueryWrapper<RolesPermission> query = new QueryWrapper<RolesPermission>();
		query.eq("ROLE_ID", roleId);
		List<RolesPermission> selectList = rolesPermissionMapper.selectList(query);
		List<String> collect = selectList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
		PermissionExample example2 = new PermissionExample();
		example2.createCriteria().andPermissionIdIn(collect);
		List<Permission> selectByExample2 = permissionMapper.selectByExample(example2);
		return selectByExample2;
	}

	public void insertRolePermission(String roleId, String permissionId) {
		rolesPermissionMapper.insert(new RolesPermission(roleId, permissionId));
	}

	// 查找所有角色
	public List<Role> getAllRoles() {
		return roleMapper.selectList(new QueryWrapper<Role>());
	}

	public boolean checkRoleUser(String roleId) {
		QueryWrapper<Role> query = new QueryWrapper<Role>();
		query.eq("ROLE_ID", roleId);
		Role userIds = roleMapper.selectOne(query);
		if (userIds != null) {
			return true;
		}
		return false;
	}

	public void deleteRole(String roleId) {
		deleteRolePermissionByRoleId(roleId);
		deleteRoleByRoleId(roleId);
	}

	public void updateUserRoleByUserId(String userId, String roleId) {
		deleUserRoleByUserId(userId);
		insertUserRole(userId, roleId);
	}
	
	public List<Permission> selectAllPermissionsByIdUser(String idUser) {
    	List<Permission> result = new ArrayList<Permission>();
    	try {
			if (idUser != null){
				List<String> idRoles = selectRoleIdsByUserId(idUser);
				if (idRoles != null && idRoles.size() > 0){
					for (String roleId : idRoles) {
						List<String> idPermissions = selectPermissionIdsByRoleId(roleId);
						if (idPermissions != null && idPermissions.size() > 0){
							for (String idPermi : idPermissions) {
								Permission permissionSet = permissionMapper.selectByPrimaryKey(idPermi);
								result.add(permissionSet);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
    }

	public void insertRoleAndPermissions(Role role, HashSet<String> permissionIds) {
		roleMapper.insert(role);
		for (String pid : permissionIds) {
			rolesPermissionMapper.insert(new RolesPermission(role.getRoleId(), pid));
		}
	}

	public void updateRoleAndPermissions(String selectedRoleId, HashSet<String> permissionIds) {
		RolesPermissionExample ex = new RolesPermissionExample();
		ex.createCriteria().andRoleIdEqualTo(selectedRoleId);
		rolesPermissionMapper.deleteByExample(ex);
		permissionIds.forEach(e -> rolesPermissionMapper.insert(new RolesPermission(selectedRoleId, e)));
	}

}
