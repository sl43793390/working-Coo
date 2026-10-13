package com.sl.project;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.entity.Project;
import com.sl.entity.ProjectUser;
import com.sl.service.ProjectRoleService;
import com.sl.service.ProjectRoleService.MemberVO;
import com.sl.service.UserManageMentProcess;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 项目成员与项目级角色（T2-5）。
 * <p>
 * 原先只有全局 RBAC，项目内谁说了算完全没表达。这里负责：
 * 列成员 -> 改角色（OWNER / MEMBER / VIEWER）-> 移除成员；顶部还能往项目里加人。
 */
@SpringComponent
@Scope("prototype")
public class ProjectMemberComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ProjectMemberComponent.class);

	private final ProjectRoleService projectRoleService;
	private final UserManageMentProcess userManageMentProcess;

	private Grid<MemberVO> grid;
	private ComboBox<com.sl.entity.User> addUserBox;
	private ComboBox<String> addRoleBox;
	private Span emptyHint;
	private Span roleTip;

	public ProjectMemberComponent(@Autowired ProjectRoleService projectRoleService,
			@Autowired UserManageMentProcess userManageMentProcess) {
		this.projectRoleService = projectRoleService;
		this.userManageMentProcess = userManageMentProcess;
		setWidthFull();
		setPadding(true);
		add(ComFactory.getTitle("项目成员"));
		add(ComFactory.getHorizontalLine());

		roleTip = new Span();
		roleTip.addClassName("member-role-tip");
		add(roleTip);

		initToolbar();

		grid = ComFactory.getGrid(MemberVO.class);
		grid.setHeight("460px");
		grid.addColumn(MemberVO::getIdUser).setHeader("用户ID");
		grid.addColumn(MemberVO::getNameUser).setHeader("姓名");
		grid.addComponentColumn(m -> {
			Span badge = ProjectUser.ROLE_OWNER.equals(m.getCdProjectRole())
					? ComFactory.getConfirmBadge(m.getRoleName())
					: (ProjectUser.ROLE_VIEWER.equals(m.getCdProjectRole())
							? ComFactory.getErrorBadge(m.getRoleName())
							: ComFactory.getNormalBadge(m.getRoleName()));
			return badge;
		}).setHeader("项目内角色");
		grid.addComponentColumn(m -> {
			ComboBox<String> box = ComFactory.getCombobox();
			box.setItems(ProjectUser.ROLE_OWNER, ProjectUser.ROLE_MEMBER, ProjectUser.ROLE_VIEWER);
			box.setItemLabelGenerator(ProjectMemberComponent::roleLabel);
			box.setValue(m.getCdProjectRole());
			box.addValueChangeListener(e -> changeRole(m, e.getValue()));
			return box;
		}).setHeader("调整角色");
		grid.addComponentColumn(m -> {
			Button b = ComFactory.getTertriayBtn("移除", VaadinIcon.TRASH);
			b.addClickListener(e -> removeMember(m));
			return b;
		}).setHeader("操作");
		add(grid);

		emptyHint = ComFactory.getEmptyHint("当前项目还没有成员");
		add(emptyHint);
		render();
	}

	private static String roleLabel(String role) {
		if (ProjectUser.ROLE_OWNER.equals(role)) {
			return "项目负责人";
		}
		if (ProjectUser.ROLE_VIEWER.equals(role)) {
			return "只读访客";
		}
		return "项目成员";
	}

	private void initToolbar() {
		HorizontalLayout toolbar = ComFactory.getResponsiveHorizontalLayoutLeft();
		toolbar.setAlignItems(Alignment.CENTER);
		add(toolbar);
		addUserBox = ComFactory.getCombobox(com.sl.entity.User.class);
		addUserBox.setPlaceholder("选择要加入的用户");
		addUserBox.setItemLabelGenerator(u -> u.getUserName() + "（" + u.getUserId() + "）");
		addRoleBox = ComFactory.getCombobox();
		addRoleBox.setItems(ProjectUser.ROLE_OWNER, ProjectUser.ROLE_MEMBER, ProjectUser.ROLE_VIEWER);
		addRoleBox.setItemLabelGenerator(ProjectMemberComponent::roleLabel);
		addRoleBox.setValue(ProjectUser.ROLE_MEMBER);
		Button addBtn = ComFactory.getPrimaryBtn("添加成员", VaadinIcon.PLUS);
		addBtn.addClickListener(e -> addMember());
		addBtnRef = addBtn;
		Button refreshBtn = ComFactory.getPrimaryBtn("刷新", VaadinIcon.REFRESH);
		refreshBtn.addClickListener(e -> render());
		toolbar.add(ComFactory.getLabel("添加成员："), addUserBox, addRoleBox, addBtn, refreshBtn);
	}

	@Override
	public void refresh() {
		render();
	}

	private void render() {
		Project p = ComponentUtil.getCurrentProject();
		if (null == p) {
			ComFactory.notification("请先选择一个项目");
			return;
		}
		List<MemberVO> members;
		try {
			members = projectRoleService.listMembers(p.getIdProject());
		} catch (Exception e) {
			log.error("加载项目成员失败，project={}", p.getIdProject(), e);
			ComFactory.notificationError("加载成员失败", e);
			return;
		}
		grid.setItems(members);
		ComFactory.toggleEmptyHint(emptyHint, members.size());

		String myRole = projectRoleService.getCurrentRole();
		boolean admin = projectRoleService.isGlobalAdmin();
		boolean canManage = admin || ProjectUser.ROLE_OWNER.equals(myRole);
		roleTip.setText("你在当前项目的角色："
				+ (null == myRole ? "非项目成员" : roleLabel(myRole))
				+ (admin ? "（全局管理员，拥有全部权限）" : ""));
		// 非负责人只读：不允许加人、改角色、移除
		addUserBox.setEnabled(canManage);
		addRoleBox.setEnabled(canManage);
		addBtnEnabled(canManage);

		// 候选用户：机构下、还没加入本项目的
		Set<String> joined = members.stream().map(MemberVO::getIdUser).collect(Collectors.toSet());
		try {
			List<com.sl.entity.User> candidates = userManageMentProcess
					.selectUserByIdInstitution(ComponentUtil.getCurrentInstitution()).stream()
					.filter(u -> !joined.contains(u.getUserId()))
					.collect(Collectors.toList());
			addUserBox.setItems(candidates);
		} catch (Exception e) {
			log.warn("加载候选用户失败", e);
		}
	}

	private Button addBtnRef;

	private void addBtnEnabled(boolean enabled) {
		if (null != addBtnRef) {
			addBtnRef.setEnabled(enabled);
		}
	}

	private void addMember() {
		Project p = ComponentUtil.getCurrentProject();
		com.sl.entity.User u = addUserBox.getValue();
		if (null == p || null == u) {
			ComFactory.notification("请先选择要添加的用户");
			return;
		}
		try {
			projectRoleService.addMember(p.getIdProject(), u.getUserId(), addRoleBox.getValue());
			ComFactory.notificationSuccess("已添加");
			addUserBox.clear();
			render();
		} catch (Exception e) {
			log.error("添加项目成员失败", e);
			ComFactory.notificationError("添加失败", e);
		}
	}

	private void changeRole(MemberVO m, String newRole) {
		Project p = ComponentUtil.getCurrentProject();
		if (null == p || null == newRole || newRole.equals(m.getCdProjectRole())) {
			return;
		}
		if (!projectRoleService.canManageProject(p.getIdProject(), ComponentUtil.getCurrentUser().getUserId())) {
			ComFactory.notificationError("只有项目负责人或管理员可以调整角色");
			render();
			return;
		}
		// 不允许把最后一个负责人降下来，否则项目没人管
		if (ProjectUser.ROLE_OWNER.equals(m.getCdProjectRole())
				&& !ProjectUser.ROLE_OWNER.equals(newRole)
				&& !projectRoleService.hasOtherOwner(p.getIdProject(), m.getIdUser())) {
			ComFactory.notificationError("项目至少要保留一个负责人");
			render();
			return;
		}
		projectRoleService.updateRole(p.getIdProject(), m.getIdUser(), newRole);
		ComFactory.notificationSuccess("角色已更新");
		render();
	}

	private void removeMember(MemberVO m) {
		Project p = ComponentUtil.getCurrentProject();
		if (null == p) {
			return;
		}
		if (!projectRoleService.canManageProject(p.getIdProject(), ComponentUtil.getCurrentUser().getUserId())) {
			ComFactory.notificationError("只有项目负责人或管理员可以移除成员");
			return;
		}
		if (ProjectUser.ROLE_OWNER.equals(m.getCdProjectRole())
				&& !projectRoleService.hasOtherOwner(p.getIdProject(), m.getIdUser())) {
			ComFactory.notificationError("项目至少要保留一个负责人");
			return;
		}
		projectRoleService.removeMember(p.getIdProject(), m.getIdUser());
		ComFactory.notificationSuccess("已移除");
		render();
	}
}
