package com.sl.task;

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
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Project;
import com.sl.entity.ProjectUser;
import com.sl.entity.Task;
import com.sl.service.ProjectProcess;
import com.sl.service.ProjectRoleService;
import com.sl.service.TaskProcess;
import com.sl.service.UserManageMentProcess;
import com.sl.util.Constants;
import com.sl.util.Util;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 我的任务主界面。
 * <p>
 * 可见性口径（与 TaskMapper.xml 的 Task_Page_Condition、TaskProcess#getTasksVisibleTo 一致）：
 * <ul>
 *   <li>管理员（checkedAllUser）：当前项目下全部任务，不限公开性；</li>
 *   <li>普通用户：分派给自己的任务 + 项目内公开任务 + 自己创建的任务。
 *       其中「分派给自己的」含被别人设为私有的任务——按「分配即可见」放行，
 *       否则别人建的私有任务指派给你，你在自己任务列表里反而看不到。</li>
 * </ul>
 * 删除权限：管理员或任务创建人本人，操作列里的「删除」按钮按行控制显隐。
 * @author Administrator
 *
 */
@SpringComponent
@Scope("prototype")
public class MyTaskComponent extends VerticalLayout implements Refreshable {
	private static final long serialVersionUID = 5129313376883460264L;
	private static final Logger log = LoggerFactory.getLogger(MyTaskComponent.class);
	private VerticalLayout contentLayout;
	private TextField nameField;
	private ComboBox<String> typeBox;
	private ComboBox<String> tagBox;
	private ComboBox<String> statusBox;
	private UserManageMentProcess userManageMentProcess;
	private ProjectProcess projectProcess;
	private TaskProcess taskProcess;
	private ProjectRoleService projectRoleService;
	private Grid<Task> grid;
	/** 列表空态提示：没数据时区分「确实没有」和「加载失败」 */
	private Span emptyHint;
	private Button searchBtn;
	private ComboBox<String> groupBox;
	private List<Task> taskByuser;
	private ComboBox<String> typeCreateBox;
	private ComboBox<ProjectUser> userBox;
	private ConfirmDialog dialog;
	/** 删除任务二次确认框 */
	private ConfirmDialog deleteDialog;
	private Task selectedTask;
	/** 当前筛选条件，懒加载 DataProvider 每次取数都会用到 */
	private Task searchTask = new Task();
	/** 筛选条件里的执行人（为空表示不限） */
	private String filterExeUser;
	/** 是否全局管理员：决定可见口径（项目下全部任务 vs 分派给自己+公开+自己创建） */
	private boolean admin;
	
	
	public MyTaskComponent(	
			@Autowired UserManageMentProcess userManageMentProcess,
			@Autowired TaskProcess taskProcess,
			@Autowired ProjectProcess projectProcess,
			@Autowired ProjectRoleService projectRoleService) {
		this.userManageMentProcess = userManageMentProcess;
		this.taskProcess = taskProcess;
		this.projectProcess = projectProcess;
		this.projectRoleService = projectRoleService;
		add(ComFactory.getTitle("我的任务"));
		add(ComFactory.getHorizontalLine());
		initMainLayout();
		
		initContent();
		registerHandler();
	}

	private void initMainLayout() {
		initFilterLayout();
		initGridLayout();
	}

	private void initFilterLayout() {
		HorizontalLayout layoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(layoutLeft);
		nameField = ComFactory.getTextField();
		typeBox = ComFactory.getCombobox();
		tagBox = ComFactory.getCombobox();
		groupBox = ComFactory.getCombobox();
		
		HorizontalLayout secondLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(secondLayout);
		statusBox = ComFactory.getCombobox();
		statusBox.setItems(Constants.STATUS_LIST);
		statusBox.setValue("进行中");
		// 允许清空状态条件（等价于原先往 STATUS_LIST 里塞一个空串的意图）
		statusBox.setClearButtonVisible(true);
		typeCreateBox = ComFactory.getCombobox();
		typeCreateBox.setItems("我创建的任务");
		
		layoutLeft.add(ComFactory.getLabel("任务名称："));
		layoutLeft.add(nameField);
		layoutLeft.add(ComFactory.getLabel("任务类型："));
		layoutLeft.add(typeBox);
		layoutLeft.add(ComFactory.getLabel("任务标签："));
		layoutLeft.add(tagBox);
		layoutLeft.add(ComFactory.getLabel("任务分组："));
		layoutLeft.add(groupBox);
		secondLayout.add(ComFactory.getLabel("任务状态："));
		secondLayout.add(statusBox);
		secondLayout.add(ComFactory.getLabel("任务类型："));
		secondLayout.add(typeCreateBox);
		
		searchBtn = ComFactory.getPrimaryBtn("搜索");
		secondLayout.add(searchBtn);
		userBox = ComFactory.getCombobox(ProjectUser.class);
//		 if (SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
			secondLayout.add(ComFactory.getLabel("执行人："));
			secondLayout.add(userBox);
//		}
	}

	private void initGridLayout() {
		grid = ComFactory.getGrid(Task.class);
		grid.setHeight("600px");
		// 懒加载：滚动到哪加载到哪，不再一次性把全部任务查出来塞进 Grid
		grid.setPageSize(20);
		add(grid);
		emptyHint = ComFactory.getEmptyHint("当前项目下暂无任务，可在左侧菜单点击「新增任务」创建");
		add(emptyHint);
		// 领取确认：文案按任务名动态设置（工厂默认文案是「删除所选内容」，语义不符）
		dialog = ComFactory.getConfirmationDialog("确认领取该任务？", conf ->{
			// 返回值约定见 saveTaskUser：1=新领取，2=已领取过（幂等），0=失败
			int saveTaskUser = taskProcess.saveTaskUser(ComponentUtil.getCurrentProject().getIdProject(), this.selectedTask.getIdTask(), ComponentUtil.getCurrentUser().getUserId());
			if (saveTaskUser == 1) {
				ComFactory.notification("领取成功,请刷新页面");
			} else if (saveTaskUser == 2) {
				ComFactory.notification("该任务已在你的执行列表中，无需重复领取");
			} else {
				ComFactory.notificationError("领取失败，请重试");
			}
			dialog.close();
		}, canc ->{
			dialog.close();
		});
		// 删除确认：文案在点「删除」时按任务名动态设置
		deleteDialog = ComFactory.getConfirmationDialog("确认删除该任务？", conf -> {
			Task t = this.selectedTask;
			if (null == t) {
				deleteDialog.close();
				return;
			}
			try {
				taskProcess.deleteTask(t);
				ComFactory.notificationSuccess("删除成功");
				// 重新挂 DataProvider：删除后总数变了，顺手把空态提示也刷新
				applyDataProvider();
			} catch (Exception ex) {
				log.error("删除任务失败, idTask={}", t.getIdTask(), ex);
				ComFactory.notificationError("删除失败", ex);
			}
			deleteDialog.close();
		}, canc -> deleteDialog.close());
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addColumn(Task::getNameTask).setHeader("任务名称").setResizable(true);
		grid.addColumn(Task::getCdTaskType).setHeader("任务类型").setResizable(true);
		grid.addColumn(Task::getCdTaskGroup).setHeader("任务分组").setResizable(true);
		grid.addColumn(Task::getCdTag).setHeader("任务标签").setResizable(true);
		grid.addColumn(Task::getCdStatus).setHeader("任务状态").setResizable(true);
		// 执行人以 task_user 为准（支持多人），ID_EXE_USER 只作为没有关联记录时的兜底
		grid.addColumn(t -> {
			String names = t.getNameExeUsers();
			return (null == names || names.isBlank()) ? t.getIdExeUser() : names;
		}).setHeader("执行人").setTooltipGenerator(Task::getNameTask).setResizable(true);
		// 计划排期：flexGrow=1 吃掉剩余空间，完整显示日期区间。
		// 默认所有列 flex-grow=0、总宽超限时被同比压缩，日期列文字长最先变省略号；
		// tooltip 再兜底，悬停也能看全
		grid.addColumn(MyTaskComponent::dateRange).setHeader("计划排期")
			.setFlexGrow(1)
			.setTooltipGenerator(MyTaskComponent::dateRange).setResizable(true);
		// 是否公开只有「是/否」两个字，固定窄列，把空间让给排期列
		grid.addColumn(Task::getFlagPrivate).setHeader("是否公开").setWidth("90px").setFlexGrow(0).setResizable(true);
		grid.addColumn(Task::getIdUser).setHeader("创建人").setResizable(true);
		grid.addComponentColumn(com ->{
			HorizontalLayout ops = new HorizontalLayout();
			ops.setSpacing(true);
			ops.setPadding(false);
			// tertiary 紧凑按钮排成一行，不折行
			ops.getStyle().set("flex-wrap", "nowrap");
			Button detailBtn = ComFactory.getTertriayBtn("详情");
			detailBtn.addClickListener(e -> {
				TaskDetailComponent c = ComponentUtil.applicationContext.getBean(TaskDetailComponent.class);
				c.setTask(com);//TODO 并填充内容
				c.initContent();
				TabsUtil.addComponentTab("任务详情-" + com.getIdTask(), "任务详情", c);
			});
			Button receiveBtn = ComFactory.getTertriayBtn("领取");
			receiveBtn.addClickListener(e -> {
				this.selectedTask = com;
				dialog.setText("确认领取任务「" + com.getNameTask() + "」？领取后任务将出现在你的执行列表。");
				dialog.open();
			});
			Button editBtn = ComFactory.getTertriayBtn("修改");
			editBtn.addClickListener(e -> {
				TaskUpdateComponent c = ComponentUtil.applicationContext.getBean(TaskUpdateComponent.class);
				c.setTask(com);//TODO 并填充内容
				c.recoverField();
				TabsUtil.addComponentTab("修改任务-" + com.getIdTask(), "修改任务", c);
			});
			Button commentBtn = ComFactory.getTertriayBtn("留言");
			commentBtn.addClickListener(e -> {
				TaskCommentComponent c = ComponentUtil.applicationContext.getBean(TaskCommentComponent.class);
				c.setTaskId(com.getIdTask());//TODO 并填充内容
				c.initContent();
				c.registerHandler();
				TabsUtil.addComponentTab("任务评论-" + com.getIdTask(), "任务评论或留言", c);
			});
			ops.add(detailBtn, receiveBtn, editBtn, commentBtn);
		// 删除：仅管理员或任务创建人可见（deleteTask 带审计日志，级联删子任务/执行人/留言）
		if (canDelete(com)) {
			Button deleteBtn = ComFactory.getTertriayBtn("删除");
			// LUMO_ERROR：tertiary + error = 红字，和其它蓝色操作按钮区分开
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
			deleteBtn.addClickListener(e -> {
					this.selectedTask = com;
					deleteDialog.setText("确认删除任务「" + com.getNameTask() + "」？删除后不可恢复，其子任务、执行人关联和留言将一并删除。");
					deleteDialog.open();
				});
				ops.add(deleteBtn);
			}
			return ops;
		}).setHeader("操作").setAutoWidth(true).setFlexGrow(0);
	}

	/**
	 * 顶栏切换项目时被调用：按新的当前项目重新取数，标签页本身保留。
	 */
	@Override
	public void refresh() {
		initContent();
	}

	public void initContent() {
		if (null == ComponentUtil.getCurrentProject()) {
			ComFactory.notification("当前还未加入任何项目，请先加入项目");
			return;
		}
		String me = currentUserId();
		// 管理员看项目下全部任务；普通用户看 分派给自己的 + 公开 + 自己创建的
		this.admin = projectRoleService.isGlobalAdmin();
		// 下拉筛选项候选值：与列表可见口径一致
		taskByuser = taskProcess.getTasksVisibleTo(ComponentUtil.getCurrentProject().getIdProject(), me, admin);
		// 下拉筛选项仍需全量候选值（去重后通常很少），但列表本身已改为懒加载
		Set<String> type = taskByuser.stream().map(e -> e.getCdTaskType()).collect(Collectors.toSet());
		Set<String> tags = taskByuser.stream().map(e -> e.getCdTag()).collect(Collectors.toSet());
		Set<String> groups = taskByuser.stream().map(e -> e.getCdTaskGroup()).collect(Collectors.toSet());
		typeBox.setItems(type);
		tagBox.setItems(tags);
		groupBox.setItems(groups);
		List<ProjectUser> selectedItems = projectProcess.getProjectUserListByIdProject(ComponentUtil.getCurrentProject().getIdProject());
		userBox.setItems(selectedItems);
		userBox.setItemLabelGenerator(ProjectUser:: getIdUser);
		applyDataProvider();
	}

	/**
	 * 用懒加载 DataProvider 装载任务：首屏只查一页，往下滚动再取下一页。
	 * 原实现是 getTaskByIdProject 全量查出后 setItems，任务多了首屏会明显变慢。
	 * 可见性口径由 flagPrivate/viewUserId 决定：管理员不过滤；普通用户 公开/自己创建/分派给自己。
	 */
	private void applyDataProvider() {
		Project current = ComponentUtil.getCurrentProject();
		if (null == current) {
			return;
		}
		String idProject = current.getIdProject();
		Task cond = this.searchTask;
		String exeUser = this.filterExeUser;
		String flagPrivate = admin ? null : "Y";
		String viewUserId = admin ? null : currentUserId();
		CallbackDataProvider<Task, Void> dataProvider = DataProvider.fromCallbacks(
				query -> {
					List<Task> page = taskProcess.getTaskByConditionPaged(
							idProject, flagPrivate, cond, resolveTaskIds(idProject, exeUser), viewUserId, query.getOffset(), query.getLimit());
					// 执行人姓名按整页批量回填（2 条 SQL），不在每行回调里单查
					taskProcess.fillExecutorNames(page);
					return page.stream();
				},
				query -> (int) taskProcess.countTaskByCondition(
						idProject, flagPrivate, cond, resolveTaskIds(idProject, exeUser), viewUserId));
		grid.setItems(dataProvider);
		ComFactory.toggleEmptyHint(emptyHint,
				taskProcess.countTaskByCondition(idProject, flagPrivate, cond, resolveTaskIds(idProject, exeUser), viewUserId));
	}

	/**
	 * 选中「执行人」时，先取该用户在当前项目下关联的任务 ID 作为过滤条件。
	 * 没有关联任务时用一个不可能命中的 ID 代替 null，避免退化成「不加限制」。
	 */
	private List<String> resolveTaskIds(String idProject, String exeUser) {
		if (null == exeUser || exeUser.isBlank()) {
			return null;
		}
		List<String> ids = taskProcess.getTaskIdByUserId(idProject, exeUser);
		if (null == ids || ids.isEmpty()) {
			return List.of("-1");
		}
		return ids;
	}

	/**
	 * 计划排期列文本：起始 ~ 结束，缺日期显示 "-"。
	 */
	private static String dateRange(Task t) {
		String s = (null == t.getDtStart()) ? "-" : Util.dateToLocalDate(t.getDtStart()).toString();
		String e = (null == t.getDtEnd()) ? "-" : Util.dateToLocalDate(t.getDtEnd()).toString();
		return s + " ~ " + e;
	}

	/**
	 * 删除权限：管理员，或任务创建人本人。
	 */
	private boolean canDelete(Task t) {
		if (null == t) {
			return false;
		}
		if (admin) {
			return true;
		}
		String me = currentUserId();
		return null != me && me.equals(t.getIdUser());
	}

	private String currentUserId() {
		return ComponentUtil.getCurrentUser() == null ? null : ComponentUtil.getCurrentUser().getUserId();
	}

	public void registerHandler() {

		searchBtn.addClickListener(e ->{
			// 所有筛选条件统一交给 SQL，不再先全量捞出再在内存里过滤
			ProjectUser selectedUser = userBox.getValue();
			this.filterExeUser = (null == selectedUser) ? null : selectedUser.getIdUser();

			Task t = new Task();
			t.setNameTask(checkNotNull(nameField.getValue()) ? nameField.getValue().trim() : null);
			t.setCdTag(tagBox.getValue() == null ? null : tagBox.getValue());
			t.setCdTaskType(typeBox.getValue());
			t.setCdTaskGroup(groupBox.getValue() == null ? null : groupBox.getValue());
			t.setCdStatus(statusBox.getValue() == null || statusBox.getValue().isBlank() ? null : statusBox.getValue());
			// 勾选「我创建的任务」后只查自己创建的
			String typeCreate = typeCreateBox.getValue();
			if (null != typeCreate && !typeCreate.isBlank()) {
				t.setIdUser(ComponentUtil.getCurrentUser().getUserId());
			}
			this.searchTask = t;
			applyDataProvider();
		});

		// 任务详情按钮已下放到表格【操作】列，按行打开，不再依赖行选中
	}

	private boolean checkNotNull(Object c) {
		if (c != null && !c.toString().equals("")) {
			return true;
		}
		return false;
	}
	/**
	 * 普通用户先找到自己参与的任务，然后到任务表进行条件查询
	 */
	@Deprecated
	private void filterQualifiedTask() {
		Task t = new Task();
		 t.setNameTask(nameField.getValue().equals("")? null :nameField.getValue());
		 t.setCdTag(tagBox.getValue() == null ? null :tagBox.getValue().toString());
		 t.setCdTaskType(typeBox.getValue());
		 t.setCdTaskGroup(groupBox.getValue() == null ? null :groupBox.getValue().toString());
		 t.setCdStatus(statusBox.getValue() == null ? null :statusBox.getValue().toString());
		 String value = typeCreateBox.getValue();
		 if (null != value && !value.equals("")) {
			 t.setIdUser(ComponentUtil.getCurrentUser().getUserId());
		 }
		 List<String> taskByUserId = taskProcess.getTaskIdByUserId(ComponentUtil.getCurrentProject().getIdProject(), ComponentUtil.getCurrentUser().getUserId());
		 List<Task> taskByCondition = taskProcess.getTaskByCondition(ComponentUtil.getCurrentProject().getIdProject(),t,taskByUserId);
		 grid.setItems(taskByCondition);
	}

}
