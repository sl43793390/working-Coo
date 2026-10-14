package com.sl.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.app.views.AppLayoutAccordionView;
import com.sl.entity.User;
import com.sl.security.PasswordHasher;
import com.sl.service.UserManageMentProcess;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
public class UserManageMentComponent extends VerticalLayout {

	private static final long serialVersionUID = 5187805936625809077L;
	private static final Logger logger = LoggerFactory.getLogger(UserManageMentComponent.class);
	private VerticalLayout workingAreaLayout;

	private Grid<User> userListTable;
	private Button addBtn;
	private Button searchButton;
	private TextField searchField;
	private UserManageMentProcess usermanageProcess;
	/** T3-2：新增/修改用户时用它把明文散列成 BCrypt */
	private PasswordHasher passwordHasher;
	private String fuzzyValue;
	private ConfirmDialog confirmationDialog;
	private ConfirmDialog removeDialog;

	public UserManageMentComponent(@Autowired UserManageMentProcess usermanageProcess,
			@Autowired PasswordHasher passwordHasher) {
		this.usermanageProcess = usermanageProcess;
		this.passwordHasher = passwordHasher;
		add(ComFactory.getTitle("用户列表"));
		add(ComFactory.getHorizontalLine());
		// 此处分为三部分layout；1、用户列表标题及搜索框；2、用户列表table；3、操作按钮区；
		initTitleAndSearchLayout();
		initUserListTableLayout();
		initContent();
		registerHandler();
	}

	private void initTitleAndSearchLayout() {
		HorizontalLayout layoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(layoutLeft);
		layoutLeft.add(ComFactory.getLabel("用户名："));
		
		searchField = ComFactory.getTextField();
		searchButton = ComFactory.getPrimaryBtn("搜索");
		searchButton.setIcon(new Icon(VaadinIcon.SEARCH));
		addBtn = ComFactory.getPrimaryBtn("新增");
		layoutLeft.add(searchField,searchButton,addBtn);
	}

	private void initUserListTableLayout() {
		userListTable = ComFactory.getGrid(User.class);
		userListTable.setHeight("600px");
		userListTable.addColumn(User::getUserId).setHeader("用户ID");
		userListTable.addColumn(User::getUserName).setHeader("用户名");
		userListTable.addColumn(User::getEmail).setHeader("邮箱");
		userListTable.addColumn(User::getOrganization).setHeader("机构");
	userListTable.addComponentColumn(p -> {
		HorizontalLayout ops = new HorizontalLayout();
		ops.setSpacing(false);
		ops.setPadding(false);
		// 图标化操作钮：两个蓝底文字按钮太占宽，图标 + 悬停提示更清爽（同「我的任务」操作列）
		ops.getStyle().set("flex-wrap", "nowrap");
		Button updateBtn = iconBtn(VaadinIcon.EDIT, "修改");
		updateBtn.addClickListener(e -> {
			if (!SecurityUtils.getSubject().isPermitted("updateUser")) {
				ComFactory.notification("权限不足，请联系管理员");
				return;
			}
			addUpdateTab(p.getUserId());
		});
		Button removeBtn = iconBtn(VaadinIcon.TRASH, "删除");
		removeBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
		removeBtn.addClickListener(e -> {
			if (!SecurityUtils.getSubject().isPermitted("deleteUser")) {
				ComFactory.notification("权限不足，请联系管理员");
				return;
			}
			// ConfirmDialog 点「取消」时会自动关闭，无需在回调里再调 close()
			ConfirmDialog confirmDialog = ComFactory.getConfirmationDialog(event -> {
				deleteUser(p.getUserId());
				ComFactory.notification("删除成功");
			}, cancel -> {
			});
			confirmDialog.open();
		});
		ops.add(updateBtn, removeBtn);
		return ops;
	}).setHeader("操作").setAutoWidth(true).setFlexGrow(0);
	add(userListTable);
}

/** 图标化操作钮：tertiary 图标按钮 + 悬停提示 */
private Button iconBtn(VaadinIcon icon, String tooltip) {
	Button b = new Button(new Icon(icon));
	b.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
	b.setTooltipText(tooltip);
	return b;
}

	public void initContent() {
		initContentTable();
	}

	public void initContentTable() {
		userListTable.setItems(new ArrayList<User>());
		// checkedAllUser 代表超级管理员，checkedInstitutionUser代表机构管理员
		if (SecurityUtils.getSubject().isPermitted("checkedAllUser")) {
			List<User> selectUserByPage = usermanageProcess.selectAllUser();
			updateUserList(selectUserByPage);
		} else if (SecurityUtils.getSubject().isPermitted("checkedInstitutionUser")) {
			List<User> userlistInstitution = usermanageProcess.selectUserByIdInstitution(ComponentUtil.getCurrentInstitution());
			updateUserList(userlistInstitution);
		} else {
			User userOnly = usermanageProcess.selectUserById(ComponentUtil.getCurrentUser().getUserId());
			ArrayList<User> arrayList = new ArrayList<User>();
			arrayList.add(userOnly);
			userListTable.setItems(arrayList);
		}
	}

	public void registerHandler() {

		searchButton.addClickListener(e ->{
			fuzzyValue = searchField.getValue();
			updateUserListFuzzyMode(fuzzyValue);
		});
		addBtn.addClickListener(e ->{
				if (!SecurityUtils.getSubject().isPermitted("userAdd")) {
					ComFactory.notification("权限不足，请联系管理员");
					return;
				}
				// String UserId = userListTable.getValue().toString();
//				userListTable.select(userListTable.firstItemId());
				addNewTab();
		});
	}

	// 将新查询出来的用户list重新加载到表格中；
	private void updateUserList(List<User> userlist) {
		userListTable.setItems(userlist);
	}

	private void updateUserListFuzzyMode(String value) {
		// 模糊查找
		if (value.equals("") || value == null) {
			initContentTable();
			return;
		}
		//如果有则根据条件模糊查询
		if (SecurityUtils.getSubject().isPermitted("checkedAllUser")) {
			List<User> selectUserByPage = usermanageProcess.selectAllUser();
			selectUserByPage = selectUserByPage.stream().filter(e ->e.getUserName().contains(value)).collect(Collectors.toList());
			updateUserList(selectUserByPage);
		} else if (SecurityUtils.getSubject().isPermitted("checkedInstitutionUser")) {
			List<User> userlistInstitution = usermanageProcess.selectUserByIdInstitution(ComponentUtil.getCurrentInstitution());
			userlistInstitution = userlistInstitution.stream().filter(e ->e.getUserName().contains(value)).collect(Collectors.toList());
			updateUserList(userlistInstitution);
		}
	}

	/**
	 * Function Name : deleteUser Description : 删除按钮功能
	 * 
	 * @author : sunlong
	 * @param userId2 
	 */
	public void deleteUser(String userId2) {
		if (userId2.equals(ComponentUtil.getCurrentUser().getUserId())) {
			ComFactory.notification("不可以删除自己");
			return;
		}
		try {
			usermanageProcess.deleteUserById(userId2);
			initContentTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		userListTable.select(null);
	}

	public UserManageMentProcess getUsermanageProcess() {
		return usermanageProcess;
	}

	public void setUsermanageProcess(UserManageMentProcess usermanageProcess) {
		this.usermanageProcess = usermanageProcess;
	}

	public void addNewTab() {
//		AppLayoutAccordionView mainView = ComponentUtil.applicationContext.getBean(AppLayoutAccordionView.class);
		Tab tabByName = TabsUtil.getTabByName(UserManagementEnums.USER_ADD);
		if (tabByName != null) {
			TabsUtil.getTabsheet().setSelectedTab(tabByName);
			return;
		}
		if (!TabsUtil.checkTabExists(TabsUtil.CAPTION_ADD_USER)) {
			UserAddUpdateComponent addUpdatec = new UserAddUpdateComponent();
			addUpdatec.setFlag(true);//新增
			addUpdatec.setUsermanageProcess(usermanageProcess);
			addUpdatec.setPasswordHasher(passwordHasher);
			addUpdatec.initLayout();
			addUpdatec.initContent();
			addUpdatec.registerHandler();
			addUpdatec.addNewUserSetting();
			TabsUtil.addComponentTab(TabsUtil.CAPTION_ADD_USER, addUpdatec);
		} else {
			TabsUtil.setSelectedTab(TabsUtil.CAPTION_ADD_USER);
		}
	}

	public void addUpdateTab(String userId) {
		// 原先通过 getBean(AppLayoutAccordionView.class) 拿主视图，
		// 该视图已不再注册为 Spring Bean（避免跨 UI 共享），TabSheet 改为从当前 UI 取。
		if (TabsUtil.checkTabExists(TabsUtil.CAPTION_MODIFY_USER)) {
			TabsUtil.removeTab(TabsUtil.getTabsheet(), TabsUtil.CAPTION_MODIFY_USER);
		}
		addUpdateTabInternal(userId);
	}

	private void addUpdateTabInternal(String userId) {
		UserAddUpdateComponent addUpdatec = new UserAddUpdateComponent();
		addUpdatec.setFlag(false);
		addUpdatec.setUsermanageProcess(usermanageProcess);
		addUpdatec.setPasswordHasher(passwordHasher);
		addUpdatec.initLayout();
		addUpdatec.initContent();
		addUpdatec.registerHandler();
		addUpdatec.updateUserSetting(userId);
		TabsUtil.addComponentTab(TabsUtil.CAPTION_MODIFY_USER, addUpdatec);
	}

}
