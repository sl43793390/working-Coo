package com.sl.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Permission;
import com.sl.entity.Role;
import com.sl.service.UserManageMentProcess;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.spring.annotation.SpringComponent;


@SpringComponent
@Scope("prototype")
public class RoleAddComponent extends VerticalLayout {
	private static final long serialVersionUID = -8346629684366302231L;

	private final Logger logger = LoggerFactory.getLogger(RoleAddComponent.class);
	
	private TextField roleIdField;
	private TextField roleNameField;
	private TextArea roleDescArea;
	private TreeGrid<Permission> newPermissionTable;
	private Button  saveRoleBtn;
	
	private UserManageMentProcess usermanageProcess;
	private List<Permission> allPermissions;
	private Collection<String> allPermissionIds = new ArrayList<String>();

	private HorizontalLayout layoutLeft;
	
	public RoleAddComponent(@Autowired UserManageMentProcess usermanageProcess) {
		this.usermanageProcess = usermanageProcess;
		add(ComFactory.getTitle(UserManagementEnums.ROLE_INFO));
		add(ComFactory.getHorizontalLine());
		add(ComFactory.getTitle(TabsUtil.CAPTION_ADD_ROLE));
		initAddRoleAuthLayout();
		
		initContent();
		registerHandler();
	}
	private void initAddRoleAuthLayout() {
		layoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(layoutLeft);
		initAddRoleLayout();
		
		initAddAuthLayout();//权限表
	}

	private void initAddRoleLayout() {
		FormLayout form = ComFactory.getFormLayout();
		form.setWidth("300px");
		layoutLeft.add(form);
		roleIdField = ComFactory.getTextField("角色ID：");
		roleNameField = ComFactory.getTextField("角色名称：");
		//角色描述
		roleDescArea = ComFactory.getTextArea("角色描述：");
		saveRoleBtn = ComFactory.getPrimaryBtn("保存角色");
		saveRoleBtn.setWidth("90px");
		form.add(roleIdField,roleNameField,roleDescArea,saveRoleBtn);
	}
	
	private void initAddAuthLayout() {
//		Label authDescLb = ComFactory.getStandardLabel("角色权限：");
//		authAddLayout.addComponent(authDescLb);
		
		newPermissionTable = ComFactory.getTreeGrid(Permission.class,SelectionMode.MULTI);
		newPermissionTable.setHeight("700px");
		newPermissionTable.addHierarchyColumn(Permission::getAction).setHeader(UserManagementEnums.AUTHDESC).setSortable(false).setTextAlign(ColumnTextAlign.START);
		layoutLeft.add(newPermissionTable);
	}
	
	public void initContent() {
		allPermissions = usermanageProcess.selectAllPermissions();
		initPermissionTable();
	}
	
	private void initPermissionTable() {
		allPermissions = usermanageProcess.selectAllPermissions();
		List<Permission> result = allPermissions;
		if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
			result = allPermissions.stream().filter(e -> !e.getPermissionId().equals("800")).collect(Collectors.toList());
		}
		ComponentUtil.populateTreeGridContent(newPermissionTable, result);
	}
	
	public void registerHandler() {
		saveRoleBtn.addClickListener(e ->{addNewRoleEvent();});
	}
	
	private void getSelectedPermissionIds(Set<String> permissionIds) {
		Set<Permission> selectedItems = newPermissionTable.getSelectedItems();
		for (Permission object : selectedItems) {
				permissionIds.add(object.getPermissionId());
		}
	}
	
	private void addNewRoleEvent() {
		if (roleIdField.getValue()==null||roleIdField.getValue().toString().trim().equals("")
				||roleNameField.getValue()==null||roleNameField.getValue().toString().trim().equals("")) {
			ComFactory.notification("角色ID,角色名称不能为空");
			return;
		}
		if (roleIdField.getValue()!=null) {
			String roleId = roleIdField.getValue().toString();
			String roleName = roleNameField.getValue().toString();
			String roleDesc = roleDescArea.getValue().toString();
			if (roleId==null||roleId.equals("")) {
				ComFactory.notification("角色ID不能为空");
				return;
			}
			Role roleByRoleId = usermanageProcess.selectRoleByRoleId(roleId);
			if (roleByRoleId!=null) {
				ComFactory.notification("该角色已经存在，请修改角色ID");
				return;
			}
			Role role = new Role();
			role.setRoleId(roleId);
			role.setIdInstitution(ComponentUtil.getCurrentInstitution());
			if (roleName!=null) {
				role.setRoleName(roleName);
			}
			if (roleDesc!=null) {
				role.setRoleDesc(roleDesc);
			}
			try {
				HashSet<String> permissionIds = new HashSet<>();
				getSelectedPermissionIds(permissionIds);
				if(permissionIds.isEmpty()) {
					ComFactory.notification("请至少选择一个权限！");
					return;
				}
				usermanageProcess.insertRoleAndPermissions(role,permissionIds);
				ComFactory.notification("保存角色成功");
				logger.info("The user:{} has created a role:{}", ComponentUtil.getCurrentUser().getUserId(), role.getRoleId());
				TabsUtil.closeTargetTab(TabsUtil.CAPTION_ADD_ROLE);
				
		          if (TabsUtil.checkTabExists(TabsUtil.CAPTION_ROLE_MANAGEMENT)) {
		        	  UserRoleManagementComponent compoent = (UserRoleManagementComponent) TabsUtil.getComponentByName(TabsUtil.CAPTION_ROLE_MANAGEMENT);
	                    compoent.refreshTable();
	                    TabsUtil.closeCurrentTab();
	                } else {
	                    TabsUtil.closeCurrentTab();
	                }
			} catch (Exception e) {
				ComFactory.notification("保存角色失败");
				logger.error("save role and permissions error!", e);
			}
		}
	}
	
	/**
	 * @param usermanageProcess the usermanageProcess to set
	 */
	public void setUsermanageProcess(UserManageMentProcess usermanageProcess) {
		this.usermanageProcess = usermanageProcess;
	}
	
	
}
