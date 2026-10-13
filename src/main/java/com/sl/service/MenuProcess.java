package com.sl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.entity.Menu;
import com.sl.mapper.MenuMapper;

@Service
public class MenuProcess {

	@Autowired
	private MenuMapper menuMapper;
	
	public List<Menu> getMenuByRoleId(String roleId){
		List<Menu> menuByRoleId = menuMapper.getMenuByRoleId(roleId);
		return menuByRoleId;
	}
	/**
	 * Function Name     : getMenuListByParent
	 * Description       : get menu list through menu parent
	 * @author jixiaonan
	 */
	public List<Menu> getMenuListByParent(String parentId, List<Menu> menuList){
		List<Menu> childList = new ArrayList<Menu>();
		for ( Menu menu : menuList){
			if(parentId.equals(menu.getIdParent())){
				childList.add(menu);
			}
		}
		return childList;
		
	}
	public Menu getMenuById(String menuId) {
		Menu selectByPrimaryKey = menuMapper.selectByPrimaryKey(menuId);
		return selectByPrimaryKey;
	}
	
	public List<Menu> selectAllMenusByUserId(String UserId){
		return menuMapper.selectAllMenusByUserId(UserId);
	}
	
}
