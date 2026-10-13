package com.sl.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Role;
import com.sl.entity.User;
import com.sl.security.PasswordHasher;
import com.sl.service.UserManageMentProcess;
import com.sl.util.Util;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import cn.hutool.crypto.SmUtil;

public class UserAddUpdateComponent extends VerticalLayout {
	private static final long serialVersionUID = -8106113907006232950L;
	private static final Logger logger = LoggerFactory.getLogger(UserAddUpdateComponent.class);

	private FormLayout formLayout;
	private Button saveUserButton;
	private TextField usetIdField;
	private TextField userNameField;
	private TextField emailField;
	private PasswordField passwordField;
	private PasswordField t4_Repeat;
	private TextField departMentField;
	private TextField organizationField;
	private TextField organizationIdField;
	private Grid<Role> grantRoleTable;

	//true 新增
	private Boolean flag;
	private Boolean flagAdd = false;
	private UserManageMentProcess usermanageProcess;
	/** T3-2：密码散列。本组件是 new 出来的非 Spring Bean，所以由调用方注入 */
	private PasswordHasher passwordHasher;
	private Span authLb;
	
	public void initLayout() {
		String newTitle;
		if (flag) {
			newTitle = "新增用户";
		} else {
			newTitle = "修改用户";
		}
		add(ComFactory.getTitle(newTitle));
		add(ComFactory.getHorizontalLine());
		initUserEditorLayout();
	}

	private void initUserEditorLayout() {
		HorizontalLayout layoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(layoutLeft);
		formLayout = new FormLayout();
		layoutLeft.add(formLayout);
		formLayout.setWidth("350px");
		initUserMessageLayout();

		VerticalLayout verticalLayout = ComFactory.getVerticalLayout();
		verticalLayout.setWidth("68%");
		layoutLeft.add(verticalLayout);
		authLb = ComFactory.getLabel("授予角色：");
		verticalLayout.add(authLb);
		Grid<Role> initGrantRoleTableLayout = initGrantRoleTableLayout();
		verticalLayout.add(initGrantRoleTableLayout);
		saveUserButton = ComFactory.getPrimaryBtn("保存");
		saveUserButton.getStyle().set("margin-left", "90%");
		verticalLayout.add(saveUserButton);
	}

	private void initUserMessageLayout() {
		formLayout.add(usetIdField = ComFactory.getStandardTextField(UserManagementEnums.CAP_USERID));
		formLayout.add(userNameField = ComFactory.getStandardTextField(UserManagementEnums.CAP_USERNAME));
		formLayout.add(emailField = ComFactory.getStandardTextField(UserManagementEnums.CAP_EMAIL));
		formLayout
				.add(passwordField = ComFactory.getPasswordField(UserManagementEnums.CAP_PASSWORD));
		formLayout.add(
				t4_Repeat = ComFactory.getPasswordField(UserManagementEnums.CAP_CONFIRM_PASSWORD));
		formLayout.add(departMentField = ComFactory.getStandardTextField(UserManagementEnums.CAP_DEPARTMENT));
		formLayout
				.add(organizationField = ComFactory.getStandardTextField(UserManagementEnums.CAP_ORGANIZATION));
		formLayout.add(organizationIdField = ComFactory.getStandardTextField(UserManagementEnums.CAP_INSTITUTION));
		usetIdField.setRequiredIndicatorVisible(true);
		usetIdField.setPlaceholder("用户名为必填项");
		usetIdField.setWidth("100%");
		userNameField.setPlaceholder("姓名为必填项");
		userNameField.setRequiredIndicatorVisible(true);
		userNameField.setWidth("100%");
		emailField.setWidth("100%");
		passwordField.setPlaceholder("密码为必填项");
		passwordField.setRequiredIndicatorVisible(true);
		passwordField.setWidth("100%");
		t4_Repeat.setPlaceholder("密码为必填项");
		passwordField.setRequiredIndicatorVisible(true);
		t4_Repeat.setWidth("100%");
		departMentField.setWidth("100%");
		organizationField.setPlaceholder("机构名称为必填项");
		organizationField.setWidth("100%");
		organizationIdField.setPlaceholder("机构ID为必填项");
		organizationIdField.setWidth("100%");

	}

	public void initContent() {
		initGrantTableContent();
	}

	private void initGrantTableContent() {
		List<Role> allRoles = usermanageProcess.getAllRoles();
		List<Role> results = new ArrayList<Role>();
		for (Role role : allRoles) {
			if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
				List<String> permissionByRoleId = usermanageProcess.selectPermissionIdsByRoleId(role.getRoleId());
				if (permissionByRoleId.contains(UserManagementEnums.MANAGE_PERMISSION_ID)) {
					continue;
				}
			}
			results.add(role);
		}
		grantRoleTable.setItems(results);
	}

	private Set<Role> getAllSelectedRoleId() {
		Set<Role> selectedItemIds = grantRoleTable.getSelectedItems();
		if (selectedItemIds == null || selectedItemIds.isEmpty()) {
			return null;
		}
		return selectedItemIds;
	}

	/**
	 * 没注入 Hasher 时（比如直接 new 出来测试）退化成 SM3，保证不 NPE。
	 * 但正式流程里调用方必须注入，否则新密码会写回无 salt 的旧算法。
	 */
	private String hash(String raw) {
		return null == passwordHasher ? SmUtil.sm3(raw) : passwordHasher.hash(raw);
	}

	private void insertUser() {
		User user = new User();
		user.setUserId(usetIdField.getValue());
		user.setUserName(userNameField.getValue());
		user.setEmail(emailField.getValue());
		// T3-2：新密码一律 BCrypt，不再写无 salt 的 SM3 摘要
		user.setPassword(hash(passwordField.getValue()));
		user.setDepartment(departMentField.getValue());
		String organization = organizationField.getValue() == null ? ComponentUtil.getCurrentUser().getOrganization() : organizationField.getValue();
		user.setOrganization(organization);
		user.setIdInstitution(organizationIdField.getValue());
		usermanageProcess.insertSelective(user);
		Set<Role> tableSelected = getAllSelectedRoleId();
		for (Role role : tableSelected) {
			usermanageProcess.insertUserRole(user.getUserId(), role.getRoleId());
		}
	}

	private void updateUser() {
		if (usetIdField.getValue().equals(ComponentUtil.getCurrentUser().getUserId())) {
			ComponentUtil.getCurrentUser().setUserName(userNameField.getValue());
		}
		User user = new User();
		user.setUserId(usetIdField.getValue());
		user.setUserName(userNameField.getValue());
		user.setEmail(emailField.getValue());
		if (StringUtils.isNotEmpty(passwordField.getValue())) {
			user.setPassword(hash(passwordField.getValue()));
		}
		if (StringUtils.isNotEmpty(departMentField.getValue())) {
			user.setDepartment(departMentField.getValue());
		}
		if (StringUtils.isNotEmpty(organizationField.getValue())) {
			user.setOrganization(organizationField.getValue());
		}
		if (StringUtils.isNotEmpty(organizationIdField.getValue())) {
			user.setIdInstitution(organizationIdField.getValue());
		}
		User userBefore = null;
		try {
			userBefore = usermanageProcess.selectUserById(usetIdField.getValue());
			if (userBefore.getVersion() != null) {
				int version = userBefore.getVersion();
				user.setVersion(++version);
			}
		} catch (NumberFormatException e) {
			logger.error("The user(" + usetIdField.getValue() + ")'s version data exception!", e);
			user.setVersion(1);
		}
		usermanageProcess.updateUserByUserId(user);
		Set<Role> tableSelected = getAllSelectedRoleId();
		for (Role roleId : tableSelected) {
			usermanageProcess.updateUserRoleByUserId(user.getUserId(), roleId.getRoleId());
		}
	}
/**
 * // in single-select, only one item is selected
	grid.select(defaultPerson);
	
	// switch to multi select, clears selection
	grid.setSelectionMode(SelectionMode.MULTI);
	// Select items 2-4
	people.subList(2,3).forEach(grid::select);
 */
	private void updateGrantRoleTableLayout() {
		List<Role> roleIds = usermanageProcess.selectRolesByUserId(usetIdField.getValue());
		grantRoleTable.select(roleIds.get(0));
	}

	/**
	 * Function Name : saveUserRolePermission Description : 将用户，权限，角色等信息全部保存到数据库中；
	 * 
	 * @author : sunlong
	 */
	@Transactional(rollbackFor = { Exception.class })
	private void saveUserRole() {
		String userId = usetIdField.getValue();
		Set<Role> tableSelected = getAllSelectedRoleId();
		for (Role roleId : tableSelected) {
			usermanageProcess.updateUserRoleByUserId(userId, roleId.getRoleId());
		}
	}

	public void addNewUserSetting() {
		usetIdField.clear();
		usetIdField.setMaxLength(50);
		userNameField.clear();
		userNameField.setMaxLength(30);
		emailField.clear();
		passwordField.clear();
		t4_Repeat.clear();
		departMentField.clear();
//		t6.setValue(ComponentUtil.getCurrentUser().getOrganization());
		organizationIdField.setValue(ComponentUtil.getCurrentUser().getIdInstitution());

		if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
			organizationIdField.setEnabled(false);
			organizationField.setEnabled(false);
		}
		// editorFields.setItemDataSource(userList.getItem(UserId));
		usetIdField.setEnabled(true);
		// editorFields.setItemDataSource(userList.getItem(UserId));
		// editorLayout.setVisible(userId != null);
		flagAdd = true;
	}

	public void updateUserSetting(String userId) {
		flagAdd = false;
		if (userId != null) {
			// 通过userid查找出用户对象然后除密码外其他全部回填；
			User usersDTO = usermanageProcess.selectUserById(userId);
			usetIdField.setValue(userId);
			if (usersDTO.getUserName() != null) {
				userNameField.setValue(usersDTO.getUserName());
			} else {
				userNameField.setValue("");
			}
			if (usersDTO.getEmail() != null) {
				emailField.setValue(usersDTO.getEmail());
			} else {
				emailField.setValue("");
			}
			if (usersDTO.getDepartment() != null) {
				departMentField.setValue(usersDTO.getDepartment());
			} else {
				departMentField.setValue("");
			}
			// When the operation is the update of the user, the password is not required to
			// verify
			passwordField.setValue("");
			t4_Repeat.setValue("");

			organizationField.setValue(usersDTO.getOrganization() == null ?"" :usersDTO.getOrganization());
			organizationIdField.setValue(usersDTO.getIdInstitution());
			if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
				organizationIdField.setEnabled(false);
				organizationField.setEnabled(false);
			}

			// editorFields.setItemDataSource(userList.getItem(UserId));
			usetIdField.setEnabled(false);

			if (SecurityUtils.getSubject().isPermitted("updatePassword")
					&& !SecurityUtils.getSubject().isPermitted("checkedInstitutionUser")
					&& !SecurityUtils.getSubject().isPermitted("checkedAllUser")) {
				grantRoleTable.setVisible(false);
				departMentField.setVisible(false);
				organizationField.setVisible(false);
				organizationIdField.setVisible(false);
				authLb.setVisible(false);
			}
		}
		formLayout.setVisible(userId != null);
		updateGrantRoleTableLayout();
	}

	/**
	 * Function Name : initGrantRoleTableLayout Description : 初始化权限列表
	 * 
	 * @author : sunlong
	 * @return 
	 */
	public Grid<Role> initGrantRoleTableLayout() {
		grantRoleTable = ComFactory.getGrid(Role.class);
		grantRoleTable.setSelectionMode(SelectionMode.MULTI);
		grantRoleTable.addColumn(Role::getRoleId).setHeader(UserManagementEnums.ROLE_ID);
		grantRoleTable.addColumn(Role::getRoleName).setHeader(UserManagementEnums.ROLE_NAME);
		grantRoleTable.addColumn(Role::getRoleDesc).setHeader(UserManagementEnums.ROLE_DESC);
		return grantRoleTable;
	}

	public void registerHandler() {

		saveUserButton.addClickListener(e ->{
			String userId;
			String userName;
			try {
				if (!flagAdd) { // 修改
					if (usetIdField.getValue().equals("") || usetIdField.getValue() == null || organizationField.getValue() == null
							|| organizationField.getValue().equals("") || userNameField.getValue() == null || userNameField.getValue().equals("")
							|| organizationIdField.getValue() == null || organizationIdField.getValue().equals("")) {
						ComFactory.notification("请输入用户名、姓名、机构名、机构ID和角色ID");
						return;
					}
					// 判断用户ID只能是英文或数字的组合
					userId = usetIdField.getValue();
					if (!Util.validateIfNotCorrect("^[a-zA-Z0-9]+@{0,1}.*[a-zA-Z0-9]+$", userId)) {
						ComFactory.notification("用户名只能是英文或者英文和数字的组合或者邮箱");
						return;
					}
					userName = userNameField.getValue();
					if (!Util.validateIfNotCorrect("^[a-zA-Z\u4e00-\u9fa5]+$", userName)) {
						ComFactory.notification("姓名只能是英文或中文");
						return;
					}
					// Verify the password and confirm password, no verify if null
					if (StringUtils.isNotEmpty(passwordField.getValue())) {
						if (!StringUtils.equals(passwordField.getValue(), t4_Repeat.getValue())) {
							ComFactory.notification("两次密码输入不一致");
							return;
						}
						if (StringUtils.isNotEmpty(passwordField.getValue()) && passwordField.getValue().trim().length() < 6) {
							ComFactory.notification("密码长度不能少于六位");
							return;
						}
					}
				} else { // 新增
					if (SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
						if (usetIdField.getValue().equals("") || usetIdField.getValue() == null || passwordField.getValue().equals("")
								|| passwordField.getValue() == null || userNameField.getValue().equals("") || userNameField.getValue() == null
								|| organizationField.getValue().equals("") || organizationField.getValue() == null || organizationIdField.getValue().equals("")
								|| organizationIdField.getValue() == null) {
							ComFactory.notification("请输入用户名、姓名、密码、机构名、机构ID和角色ID"); 
							return;
						}
						if (!passwordField.getValue().equals(t4_Repeat.getValue())) {
							ComFactory.notification("两次密码输入不一致");
							return;
						}
					} else {
						if (usetIdField.getValue().equals("") || usetIdField.getValue() == null || passwordField.getValue().equals("")
								|| passwordField.getValue() == null || userNameField.getValue().equals("") || userNameField.getValue() == null) {
							ComFactory.notification("请输入用户名、姓名、密码和角色ID"); 
							return;
						}
						if (!passwordField.getValue().equals(t4_Repeat.getValue())) {
							ComFactory.notification("两次密码输入不一致");
							return;
						}
					}
					if (passwordField.getValue().trim().length() < 6) {
						ComFactory.notification("密码长度不能少于六位");
						return;
					}
					// 判断用户ID只能是英文或数字的组合
					userId = usetIdField.getValue();
					if (!Util.validateIfNotCorrect("^[a-zA-Z0-9]+@{0,1}.*[a-zA-Z0-9]+$", userId)) {
						ComFactory.notification("用户名只能是英文或者英文和数字的组合或者邮箱"); 
						return;
					}
					userName = userNameField.getValue();
					if (!Util.validateIfNotCorrect("^[a-zA-Z\u4e00-\u9fa5]+$", userName)) {
						ComFactory.notification("姓名只能是英文或中文"); 
						return;
					}
				}

				Set<Role> tableSelected = getAllSelectedRoleId();
				if (tableSelected == null || tableSelected.size() != 1) {
					ComFactory.notification("一个用户对应一个角色！"); 
					return;
				}

				if (flagAdd) {
					User user2 = usermanageProcess.selectUserById(usetIdField.getValue());
					if (user2 != null) {// TODO
						if (!user2.getIdInstitution().equals(ComponentUtil.getCurrentUser().getIdInstitution())) {
							// 添加管理员权限人员可添加用户
							if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
								ComFactory.notification("用户名已经存在"); 
								return;
							}
						}
						updateUser();
						ComFactory.notification("用户添加成功"); 
					} else {
						insertUser();
					}

					logger.info("用户管理,用户：," + ComponentUtil.getCurrentUser().getUserId() + "执行新增用户操作，新增用户："
							+ usetIdField.getValue());
				} else {
					updateUser();
					logger.info("用户管理,用户：," + ComponentUtil.getCurrentUser().getUserId() + "执行修改用户操作，修改用户："
							+ usetIdField.getValue());
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// 调用一个方法将用户选择的权限保存到数据库，原来是将role保存在一张表中；
			saveUserRole();
			flagAdd = false;

			if (TabsUtil.checkTabExists(TabsUtil.CAPTION_USER_MANAGEMENT)) {
				UserManageMentComponent compoent = (UserManageMentComponent) TabsUtil
						.getComponentByName(TabsUtil.CAPTION_USER_MANAGEMENT);
				compoent.initContent();
				TabsUtil.closeCurrentTab();
			} else {
				TabsUtil.closeCurrentTab();
			}
				
		});
	}

	public UserManageMentProcess getUsermanageProcess() {
		return usermanageProcess;
	}

	public void setUsermanageProcess(UserManageMentProcess usermanageProcess) {
		this.usermanageProcess = usermanageProcess;
	}

	public void setPasswordHasher(PasswordHasher passwordHasher) {
		this.passwordHasher = passwordHasher;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

}
