package com.sl.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Permission;
import com.sl.entity.Role;
import com.sl.entity.User;
import com.sl.service.UserManageMentProcess;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;

@Service
@Scope("prototype")
public class UserRoleManagementComponent extends VerticalLayout {

	private static final long serialVersionUID = 3468668978909303956L;
	private static final Logger logger = LoggerFactory.getLogger(UserRoleManagementComponent.class);
	
	private Grid<Role> roleTable;
	private TreeGrid<Permission> authTable;
	private Grid<User> userTable;	
	private Button  newRoleBtn;
	private Button  deleteRoleBtn;
	private Button  saveModifyBtn;
	private UserManageMentProcess usermanageProcess;
	private List<Permission> allPermissions;
	private Collection<String> allPermissionIds = new ArrayList<String>();
	private ConfirmDialog confirmationDialog;
	
	public UserRoleManagementComponent(@Autowired UserManageMentProcess usermanageProcess) {
		this.usermanageProcess = usermanageProcess;
		add(ComFactory.getTitle(UserManagementEnums.ROLE_INFO));
		add(ComFactory.getHorizontalLine());
		
		initLableLayout();
		initRoleAuthUserLayout();
		initContent();
		registerHandler();
	}

	private void initLableLayout() {
		Span roleLb = ComFactory.getLabel("已创建角色：");
		Span authLb = ComFactory.getLabel("角色权限：");
		authLb.getStyle().set("margin-left", "26%");
		Span userLb = ComFactory.getLabel("拥有该角色的用户：");
		userLb.getStyle().set("margin-left", "28%").set("width", "150px");
		HorizontalLayout responsiveHorizontalLayoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		responsiveHorizontalLayoutLeft.add(roleLb,authLb,userLb);
		add(responsiveHorizontalLayoutLeft);
	}
	private void initRoleAuthUserLayout() {
		HorizontalLayout horizontalLayoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(horizontalLayoutLeft);
		//已创建角色列表
		initRoleTableLayout();
		horizontalLayoutLeft.add(roleTable);
		
		//角色权限列表
		initAuthTableLayout();
		horizontalLayoutLeft.add(authTable);
		
		//拥有该角色的用户列表
		initUserTableLayout();
		horizontalLayoutLeft.add(userTable);
		
		HorizontalLayout buttonLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(buttonLayout);
		newRoleBtn = ComFactory.getPrimaryBtn("新增角色");
		buttonLayout.add(newRoleBtn);
		
		deleteRoleBtn = ComFactory.getPrimaryBtn("删除角色");
		buttonLayout.add(deleteRoleBtn);
		
		saveModifyBtn = ComFactory.getPrimaryBtn("保存修改");
		buttonLayout.add(saveModifyBtn);
	}
	
	private void initRoleTableLayout() {
		roleTable = ComFactory.getGrid(Role.class);
		roleTable.setHeight("550px");
		roleTable.setSelectionMode(SelectionMode.SINGLE);
		roleTable.addColumn(Role::getRoleId).setHeader(UserManagementEnums.ROLE_ID);
		roleTable.addColumn(Role::getRoleName).setHeader(UserManagementEnums.ROLE_NAME);
		roleTable.addColumn(Role::getRoleDesc).setHeader(UserManagementEnums.ROLE_DESC);
	}
	
	private void initAuthTableLayout() {
		authTable = ComFactory.getTreeGrid(Permission.class,SelectionMode.MULTI);
		authTable.setHeight("550px");
		authTable.setSelectionMode(SelectionMode.MULTI);
		Column<Permission> setHeader = authTable.addHierarchyColumn(Permission::getAction).setHeader(UserManagementEnums.AUTHERIZATION);
		setHeader.setTextAlign(ColumnTextAlign.START);
//		authTable.setHierarchyColumn(UserManagementEnums.AUTHERIZATION);
	}
	
	private void initUserTableLayout() {
		userTable = ComFactory.getGrid(User.class);
		userTable.setHeight("550px");
		userTable.addColumn(User::getUserId).setHeader(UserManagementEnums.CAP_USERID);
		userTable.addColumn(User::getUserName).setHeader(UserManagementEnums.CAP_USERNAME);
		userTable.addColumn(User::getDepartment).setHeader(UserManagementEnums.CAP_DEPARTMENT);
	}
	
	public void initContent() {
		allPermissions = usermanageProcess.selectAllPermissions();
		initRoleTableContent();
		initPermissionTable();
	}

	private void initRoleTableContent() {
		List<Role> allRoles = usermanageProcess.getAllRoles();
		if (allRoles==null||allRoles.isEmpty()) {
			ComFactory.notification("未创建角色");
			return;
		} 
		if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
			allRoles = allRoles.stream().filter(e -> !e.getRoleId().contains("admin")).collect(Collectors.toList());
//			List<String> permissionByRoleId = usermanageProcess.selectPermissionByRoleId(role.getRoleId());
//			if (permissionByRoleId.contains(UserManagementEnums.MANAGE_PERMISSION_ID)) {
//				continue;
//			}
		}
		roleTable.setItems(allRoles);
	}

	private void initPermissionTable() {
		allPermissions = usermanageProcess.selectAllPermissions();
		List<Permission> result = allPermissions;
		if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
			result = allPermissions.stream().filter(e -> !e.getPermissionId().equals("800")).collect(Collectors.toList());
		}
		ComponentUtil.populateTreeGridContent(authTable, result);
	}

	/**
	 * 树结构生成方式之一：
	 * TreeDataProvider<Project> dataProvider = (TreeDataProvider<Project>) treeGrid.getDataProvider();
		TreeData<Project> data = dataProvider.getTreeData();
		// add new items
		data.addItem(null, newProject);
		data.addItems(newProject, newProject.getChildren());
		
		// after adding / removing data, data provider needs to be refreshed
		dataProvider.refreshAll();
	 * @param roleId
	 */
	private void initAuthTableContent(String roleId) {
		List<String> permissionByRoleId = usermanageProcess.selectPermissionIdsByRoleId(roleId);
		if(permissionByRoleId == null || permissionByRoleId.isEmpty()) {
			logger.info("No permission for the role:{}", roleId);
			return;
		}
		if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
			permissionByRoleId = permissionByRoleId.stream().filter(e -> !e.equals("800")).collect(Collectors.toList());
		}
		ComponentUtil.populateTreeGridContentWithSelectItems(authTable, allPermissions, permissionByRoleId);
	}
	
	private void initUserTableContent(String roleId) {
		List<User> users = usermanageProcess.selectUsersByRoleId(roleId);
		userTable.setItems(users);
	}
	
	
	public void registerHandler() {
		
		newRoleBtn.addClickListener(e ->{
//			AppLayoutAccordionView bean = ComponentUtil.applicationContext.getBean(AppLayoutAccordionView.class);
//			Tab tabByName = TabsUtil.getTabByName(TabsUtil.CAPTION_ADD_ROLE);
//			if (null != tabByName) {
//				TabsUtil.getTabsheet().setSelectedTab(tabByName);
//				return;
//			}
			if(!TabsUtil.checkTabExists(TabsUtil.CAPTION_ADD_ROLE)){
				RoleAddComponent roleadd = ComponentUtil.applicationContext.getBean(RoleAddComponent.class);
				TabsUtil.addComponentTab(TabsUtil.CAPTION_ADD_ROLE, roleadd);
			}else{
				TabsUtil.setSelectedTab(TabsUtil.CAPTION_ADD_ROLE);
			}
		});
		
		deleteRoleBtn.addClickListener(e ->{
			if(ComponentUtil.getSelectedItemForGrid(roleTable)!= null) {
				String clickedRoleId = ComponentUtil.getSelectedItemForGrid(roleTable).getRoleId();
					if (usermanageProcess.checkRoleUser(clickedRoleId)) {
						confirmationDialog = ComFactory.getConfirmationDialog(event -> {
							try {
								List<String> permissionByRoleId = usermanageProcess.selectPermissionIdsByRoleId(clickedRoleId);
								if (permissionByRoleId.contains(UserManagementEnums.MANAGE_PERMISSION_ID)) {
									ComFactory.notification("不能删除管理员角色");
									return;
								}
								usermanageProcess.deleteRole(clickedRoleId);
//								if(roleTable.firstItemId()!=null) {
//									refreshContent(roleTable.firstItemId().toString());
//								}
								refreshTable();
								ComFactory.notification("删除角色成功");
								logger.info("The user:{} has deleted a role:{}", ComponentUtil.getCurrentUser().getUserId(), clickedRoleId);
							} catch (Exception e2) {
								ComFactory.notification("删除角色失败");
								logger.error("Delete role failure!", e2);
							}
						}, e1 ->{
							confirmationDialog.close();
						});
						confirmationDialog.open();
//						popupWindow = new ConfirmationDialogPopupWindow("提示:", "有用户使用该角色，请确认是否删除该角色 ?", "是", "否", true);
					}else{
//						popupWindow = new ConfirmationDialogPopupWindow("提示:", "无用户使用该角色，请确认是否删除该角色 ?", "是", "否", true);
					}
				
			}else{
				ComFactory.notification("请选择要删除的角色");
			}
		});
		
		saveModifyBtn.addClickListener(e ->{
//			保存当前用户点击的角色及权限信息；
			saveModifyRoleAndPermissions();
		});
//		roleTable.addItemClickListener(event ->{
//			Role item = event.getItem();
//			if (null != item) {
//				initAuthTableContent(item.getRoleId());
//				initUserTableContent(item.getRoleId());
//			}
//		});
		roleTable.addSelectionListener(event ->{
			Optional<Role> firstSelectedItem = event.getFirstSelectedItem();
			if (firstSelectedItem.isPresent()) {
				Role role = firstSelectedItem.get();
				initAuthTableContent(role.getRoleId());
				initUserTableContent(role.getRoleId());
			}
		});
		
	}
	
	private Dialog createDialog() {
		Dialog dialog = new Dialog();

		dialog.setCloseOnOutsideClick(false);
//		dialog.setWidth("600px");
		dialog.setHeaderTitle("添加信息");

		VerticalLayout dialogLayout = createDialogLayout();
		dialog.add(dialogLayout);

		Button saveButton = new Button("保存", e -> dialog.close());
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		Button cancelButton = new Button("取消", e -> dialog.close());
		dialog.getFooter().add(cancelButton);
		dialog.getFooter().add(saveButton);
		return dialog;
	}
	private VerticalLayout createDialogLayout() {

		TextField firstNameField = new TextField("用户名");
		TextField lastNameField = new TextField("密码");

		VerticalLayout dialogLayout = new VerticalLayout(firstNameField, lastNameField);
		dialogLayout.setPadding(false);
		dialogLayout.setSpacing(false);
		dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

		return dialogLayout;
	}
	
	private void saveModifyRoleAndPermissions() {
		Role itemForGrid = ComponentUtil.getSelectedItemForGrid(roleTable);
		if(itemForGrid == null) {
			ComFactory.notification("请选择一个角色");
			return;
		}
		String selectedRoleId = itemForGrid.getRoleId();
		List<String> permissions = usermanageProcess.selectPermissionIdsByRoleId(selectedRoleId);
		HashSet<String> permissionIds = new HashSet<>();
		if (permissions.contains(UserManagementEnums.MANAGE_PERMISSION_ID)) {
			permissionIds.add(UserManagementEnums.MANAGE_PERMISSION_ID);
		}
		try {
			getSelectedPermissionIds(permissionIds);
			usermanageProcess.updateRoleAndPermissions(selectedRoleId, permissionIds);
			ComFactory.notification("保存修改成功");
			logger.info("The user:{} modified the permissions of the role:{}", ComponentUtil.getCurrentUser().getUserId(), selectedRoleId);
		} catch (Exception e) {
			ComFactory.notification("保存修改失败");
			logger.error("Modifying permissions failure!", e);
		}
		
	}
	
	private void getSelectedPermissionIds(Set<String> permissionIds) {
//		Collection<?> treeTableItemIds = allPermissionIds;
		Set<Permission> selectedItems = authTable.getSelectedItems();
		for (Permission object : selectedItems) {
				permissionIds.add(object.getPermissionId());
		}
	}
	
	private void refreshContent(String roleId) {
		initRoleTableContent();
		roleTable.select(usermanageProcess.selectRoleByRoleId(roleId));
		initAuthTableContent(roleId);
		initUserTableContent(roleId);
	}
	
	/**
	 * @return the usermanageProcess
	 */
	public UserManageMentProcess getUsermanageProcess() {
		return usermanageProcess;
	}

	/**
	 * @param usermanageProcess the usermanageProcess to set
	 */
	public void setUsermanageProcess(UserManageMentProcess usermanageProcess) {
		this.usermanageProcess = usermanageProcess;
	}
	
	public void refreshTable() {
		initRoleTableContent();
		initPermissionTable();
		userTable.setItems(new ArrayList<User>());
	}
	
}
