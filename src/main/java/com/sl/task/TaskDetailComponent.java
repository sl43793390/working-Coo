package com.sl.task;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.editor.QuillEditor;
import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.ProjectGroup;
import com.sl.entity.ProjectTag;
import com.sl.entity.ProjectTaskFile;
import com.sl.entity.OperationLog;
import com.sl.entity.ProjectType;
import com.sl.entity.Task;
import com.sl.entity.TaskComment;
import com.sl.entity.TaskDependency;
import com.sl.entity.User;
import com.sl.service.NotificationService;
import com.sl.service.OperationLogService;
import com.sl.service.ProjectProcess;
import com.sl.service.TaskDependencyService;
import com.sl.service.TaskProcess;
import com.sl.service.UserManageMentProcess;
import com.sl.util.Util;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;

/**
 * 任务详情
 * @author Administrator
 *
 */

@SpringComponent
@Scope("prototype")
public class TaskDetailComponent extends HorizontalLayout {

	
	private static final Logger log = LoggerFactory.getLogger(TaskDetailComponent.class);

	private static final long serialVersionUID = 5166621585268664577L;
	private TextField taskName;
	private ComboBox<String> prior;
	private TextField pctComplete;
	private QuillEditor remark;
	private TaskProcess taskProcess;
	private ProjectProcess projectProcess;
	private UserManageMentProcess userManageMentProcess;
	private ComboBox<String> taskGroup;
	private ComboBox<String> tagBox;
	private ComboBox<String> typeBox;
	private ComboBox<String> statusBox;
	private DatePicker dateStart;
	private DatePicker dateEnd;
	private TextField estimateHours;
	private TextField actualHours;
	private TextArea userBox;
	private Checkbox privateBox;
	private List<ProjectTag> projectTagList;
	private List<ProjectGroup> projectGroupList;
	private List<ProjectType> projectTypeList;
	private Task task;

	private Grid<ProjectTaskFile> docGrid;
	private HorizontalLayout contentLayout;

	/** 前置任务候选下拉 */
	private ComboBox<Task> preTaskBox;
	private Grid<Task> preTaskGrid;
	private Grid<Task> sucTaskGrid;
	private Grid<OperationLog> historyGrid;

	private TaskDependencyService dependencyService;
	private OperationLogService operationLogService;
	private NotificationService notificationService;

	private TextArea commentRemark;
	public TaskDetailComponent(@Autowired TaskProcess taskProcess,
			@Autowired ProjectProcess projectProcess,
			@Autowired UserManageMentProcess userManageMentProcess,
			@Autowired TaskDependencyService dependencyService,
			@Autowired OperationLogService operationLogService,
			@Autowired NotificationService notificationService) {
		this.userManageMentProcess = userManageMentProcess;
		this.projectProcess = projectProcess;
		this.taskProcess = taskProcess;
		this.dependencyService = dependencyService;
		this.operationLogService = operationLogService;
		this.notificationService = notificationService;
		// 只约束宽度，高度交给内容撑开：
		// 之前 setHeightFull 会把组件锁在容器高度里，表单/表格超出部分被切断且无法滚动
		setWidthFull();
		add(ComFactory.getTitle("任务详情"));
		add(ComFactory.getHorizontalLine());
		contentLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(contentLayout);
		
		initMainLayout();
	}

	/**
	 * 布局
	 */
	private void initMainLayout() {
		initLeftLayout();
		initRight();
		initDependencyAndHistory();
	}

	/**
	 * 前置依赖（T2-3）+ 变更记录时间线（T2-10）。
	 * 放在详情右侧作为独立一列，不挤占原有的表单与评论区。
	 */
	private void initDependencyAndHistory() {
		VerticalLayout col = ComFactory.getVerticalLayout();
		col.setWidth("30%");
		col.setPadding(false);
		contentLayout.add(col);

		col.add(ComFactory.getLabel("前置任务："));
		preTaskBox = ComFactory.getCombobox(Task.class);
		preTaskBox.setPlaceholder("选择前置任务后点「添加」");
		preTaskBox.setWidthFull();
		preTaskBox.setItemLabelGenerator(Task::getNameTask);
		Button addDepBtn = ComFactory.getPrimaryBtn("添加前置", VaadinIcon.PLUS);
		addDepBtn.addClickListener(e -> addDependency());
		HorizontalLayout addBar = new HorizontalLayout(preTaskBox, addDepBtn);
		addBar.setWidthFull();
		col.add(addBar);

		preTaskGrid = ComFactory.getGrid(Task.class);
		preTaskGrid.setHeight("150px");
		preTaskGrid.setSelectionMode(SelectionMode.NONE);
		preTaskGrid.addColumn(Task::getNameTask).setHeader("名称");
		preTaskGrid.addColumn(Task::getCdStatus).setHeader("状态");
		preTaskGrid.addComponentColumn(t -> {
			Button b = ComFactory.getTertriayBtn("移除", VaadinIcon.TRASH);
			b.addClickListener(e -> removeDependency(t.getIdTask()));
			return b;
		}).setHeader("操作");
		col.add(preTaskGrid);

		col.add(ComFactory.getLabel("后继任务："));
		sucTaskGrid = ComFactory.getGrid(Task.class);
		sucTaskGrid.setHeight("120px");
		sucTaskGrid.setSelectionMode(SelectionMode.NONE);
		sucTaskGrid.addColumn(Task::getNameTask).setHeader("名称");
		sucTaskGrid.addColumn(Task::getCdStatus).setHeader("状态");
		col.add(sucTaskGrid);

		col.add(ComFactory.getLabel("变更记录："));
		historyGrid = ComFactory.getGrid(OperationLog.class);
		historyGrid.setHeight("180px");
		historyGrid.setSelectionMode(SelectionMode.NONE);
		historyGrid.addColumn(l -> null == l.getCdAction() ? "" : l.getCdAction()).setHeader("动作");
		historyGrid.addColumn(l -> null == l.getNameUser() ? "" : l.getNameUser()).setHeader("操作人").setWidth("90px");
		historyGrid.addColumn(l -> null == l.getDtOperation() ? "" : DateUtil.formatDateTime(l.getDtOperation()))
				.setHeader("时间").setWidth("150px");
		historyGrid.addColumn(l -> null == l.getCdDetail() ? "" : l.getCdDetail()).setHeader("明细").setResizable(true);
		col.add(historyGrid);
	}

	private void addDependency() {
		Task pre = preTaskBox.getValue();
		if (null == pre) {
			ComFactory.notification("请先选择前置任务");
			return;
		}
		String err = dependencyService.addDependency(
				ComponentUtil.getCurrentProject().getIdProject(), task.getIdTask(), pre.getIdTask());
		if (null != err) {
			ComFactory.notificationError(err);
			return;
		}
		ComFactory.notificationSuccess("已添加前置任务");
		loadDependencyAndHistory();
	}

	private void removeDependency(String idPreTask) {
		List<TaskDependency> deps = dependencyService.getPreDependencies(task.getIdTask());
		for (TaskDependency d : deps) {
			if (d.getIdPreTask().equals(idPreTask)) {
				dependencyService.deleteDependency(d.getIdDependency());
			}
		}
		ComFactory.notificationSuccess("已移除");
		loadDependencyAndHistory();
	}

	/**
	 * 载入依赖与变更记录。候选前置任务排除自己、已有前置和后继（否则一定成环）。
	 */
	private void loadDependencyAndHistory() {
		if (null == task) {
			return;
		}
		List<Task> preTasks = dependencyService.getPreTasks(task.getIdTask());
		preTaskGrid.setItems(preTasks);
		sucTaskGrid.setItems(dependencyService.getSuccessorTasks(task.getIdTask()));

		List<Task> candidates = taskProcess.getTasksWithSchedule(ComponentUtil.getCurrentProject().getIdProject());
		Set<String> excluded = new HashSet<>();
		excluded.add(task.getIdTask());
		preTasks.forEach(t -> excluded.add(t.getIdTask()));
		dependencyService.getSuccessorTasks(task.getIdTask()).forEach(t -> excluded.add(t.getIdTask()));
		preTaskBox.setItems(candidates.stream().filter(t -> !excluded.contains(t.getIdTask())).toList());
		preTaskBox.setValue(null);

		historyGrid.setItems(operationLogService.timeline(OperationLog.BIZ_TASK, task.getIdTask(), 50));
	}

	private void initLeftLayout() {
		FormLayout form = new FormLayout();
		form.setWidth("20%");
		form.setResponsiveSteps( new ResponsiveStep("0", 1));
		contentLayout.add(form);
		taskName = ComFactory.getStandardTextField("任务名称：");
		taskGroup = ComFactory.getStandardComboBox("任务分组：");//增加
		typeBox = ComFactory.getStandardComboBox("任务类型：");//增加
		tagBox = ComFactory.getStandardComboBox("任务标签：");//增加
		statusBox = ComFactory.getStandardComboBox("任务状态：");
		statusBox.setItems("未开始","进行中","已结束","已中断","已废弃");
		statusBox.setValue("未开始");
		
		prior = ComFactory.getStandardComboBox("优先级：");
		prior.setItems("高","中","低");
		prior.setValue("低");
		
		pctComplete = ComFactory.getStandardTextField("完成比例：");
		pctComplete.setPlaceholder("请输入0 ~ 1之间的数");
		pctComplete.setValue("");
		
		dateStart = new DatePicker("计划开始日期：");
		dateEnd = new DatePicker("结束日期：");
		estimateHours = ComFactory.getStandardTextField("预计工时：");
		actualHours = ComFactory.getStandardTextField("实际工时：");

		privateBox = ComFactory.getCheckbox("是否私有：");
		userBox = ComFactory.getTextArea("执行人：");

		form.add(taskName,taskGroup,typeBox,tagBox,statusBox,prior,pctComplete,
				dateStart,dateEnd,estimateHours,actualHours,userBox);
	}

	private void initRight() {
		//****************************第二部分********************************
		VerticalLayout layoutLeft = ComFactory.getVerticalLayout();
		layoutLeft.setWidth("30%");
		contentLayout.add(layoutLeft);
		//任务文件列表
		docGrid = ComFactory.getGrid(ProjectTaskFile.class);
		docGrid.setHeight("500px");
		docGrid.setSelectionMode(SelectionMode.NONE);
		docGrid.addColumn(ProjectTaskFile::getNameFile).setHeader("文件名").setResizable(true);
		docGrid.addColumn( e->{return DateUtil.formatDate(e.getDtUpload());}).setHeader("上传时间").setResizable(true);
		docGrid.addComponentColumn(e ->{
			Anchor an = new Anchor("/file/download?fileId="+e.getIdFile(), "下载", AnchorTarget.BLANK);
			return an;
		}).setHeader("下载");
		layoutLeft.add(ComFactory.getLabel("文件列表："));
		layoutLeft.add(docGrid);
		//****************************第三部分********************************
		VerticalLayout right = ComFactory.getVerticalLayout();
		right.setWidth("40%");
		contentLayout.add(right);
		remark = new QuillEditor().withToolbar("full").withHeight("290px");//通用 Quill 富文本编辑器
		remark.setWidth("100%");
		remark.getStyle().set("margin-top", "1px");
		right.add(ComFactory.getLabel("备注："));
		right.add(remark);
		commentRemark = ComFactory.getTextArea();
		commentRemark.setHeight("245px");
		right.add(ComFactory.getLabel("为该任务添加评论或留言："));
		right.add(commentRemark);
		
		Button primaryBtn = ComFactory.getPrimaryBtn("发布留言");
		primaryBtn.addClickListener(e ->{
			if (saveComment() != 0) {
				ComFactory.notification("保存成功");
			}
		});
		right.add(primaryBtn);
		
	}

	private Integer saveComment() {
		if (commentRemark.getValue() == null || commentRemark.getValue().equals("")) {
			ComFactory.notification("请填写留言后再保存");
			return 0;
		}
		TaskComment c = new TaskComment();
		c.setIdTask(task.getIdTask());
		c.setDtComment(new Date());
		c.setCommentUser(ComponentUtil.getCurrentUser().getUserId());
		c.setCdComment(commentRemark.getValue());
		int n = taskProcess.saveTaskComment(c);
		notifyMentions(commentRemark.getValue());
		return n;
	}

	/**
	 * 评论里 @ 到的人收到通知（T2-4）。
	 * 匹配 {@code @姓名}，姓名允许用户包含中文、字母、数字和下划线。
	 * 匹配不到用户就静默跳过——评论的主要目的不是通知，不该因为名字写错而失败。
	 */
	private void notifyMentions(String comment) {
		java.util.regex.Matcher m = java.util.regex.Pattern
				.compile("@([\\u4e00-\\u9fa5A-Za-z0-9_]{1,32})")
				.matcher(comment);
		Set<String> names = new HashSet<>();
		while (m.find()) {
			names.add(m.group(1));
		}
		if (names.isEmpty()) {
			return;
		}
		List<User> members = userManageMentProcess.selectUserByIdInstitution(ComponentUtil.getCurrentInstitution());
		for (User u : members) {
			if (null != u.getUserName() && names.contains(u.getUserName())) {
				notificationService.pushCommentMention(u.getUserId(), task, comment);
			}
		}
	}

	public void initContent() {
		projectTagList = projectProcess.getProjectTagsList(ComponentUtil.getCurrentProject().getIdProject());
		projectGroupList = projectProcess.getProjectGroupList(ComponentUtil.getCurrentProject().getIdProject());
		projectTypeList = projectProcess.getProjectTypeList(ComponentUtil.getCurrentProject().getIdProject());
		populateContentBox();
		selectBoxContent();
		List<ProjectTaskFile> fileList = projectProcess.getTaskFileList(ComponentUtil.getCurrentProject().getIdProject(),task.getIdTask());
		docGrid.setItems(fileList);
		loadDependencyAndHistory();
	}

	private void selectBoxContent() {
		taskName.setValue(task.getNameTask());
		if (null != task.getCdStatus()) {
			statusBox.setValue(task.getCdStatus());
		}
		if (null != task.getCdPrior()) {
			prior.setValue(task.getCdPrior());
		}
		if (null != task.getPctComplete()) {
			pctComplete.setValue(task.getPctComplete()+"");
		}
		if (null != task.getCdDescription()) {
			remark.setValue(task.getCdDescription());
		}
		dateEnd.setValue(Util.dateToLocalDate(task.getDtEnd()));
		if (null != task.getDtStart()) {
			dateStart.setValue(Util.dateToLocalDate(task.getDtStart()));
		}
		estimateHours.setValue(task.getNbrEstimateHours() == null ? "" : String.valueOf(task.getNbrEstimateHours()));
		actualHours.setValue(task.getNbrActualHours() == null ? "" : String.valueOf(task.getNbrActualHours()));
		tagBox.setValue(task.getCdTag());
		taskGroup.setValue(task.getCdTaskGroup());
		typeBox.setValue(task.getCdTaskType());
		privateBox.setValue("N".equals(task.getFlagPrivate()));// 'N'=私有 → 勾选
		List<User> userByIdTask = taskProcess.getTaskUserByIdTask(task.getIdTask());
		if (null != userByIdTask && !userByIdTask.isEmpty()) {
			StringBuffer bf = new StringBuffer();
			int i = 0;
			for (User user : userByIdTask) {
				bf.append(user.getUserName());
				if (i != userByIdTask.size() - 1) {
					bf.append("，");
				}
				i++;
			}
			userBox.setValue(bf.toString());
		}
		
		taskName.setReadOnly(true);
		taskGroup.setReadOnly(true);
		tagBox.setReadOnly(true);
		typeBox.setReadOnly(true);
		statusBox.setReadOnly(true);
		prior.setReadOnly(true);
		pctComplete.setReadOnly(true);
		privateBox.setReadOnly(true);
//		remark.setReadOnly(true);
		dateStart.setReadOnly(true);
		dateEnd.setReadOnly(true);
		estimateHours.setReadOnly(true);
		actualHours.setReadOnly(true);
		userBox.setReadOnly(true);
	}

	private void populateContentBox() {
		List<String> collect = projectTagList.stream().map(e ->e.getCdTag()).collect(Collectors.toList());
		if (null != collect) {
			tagBox.setItems(collect);
		}
		List<String> collect2 = projectGroupList.stream().map(e ->e.getCdGroup()).collect(Collectors.toList());
		if (null != collect2) {
			taskGroup.setItems(collect2);
		}
		List<String> collect3 = projectTypeList.stream().map(e ->e.getCdType()).collect(Collectors.toList());
		if (null != collect3) {
			typeBox.setItems(collect3);
		}
		//初始化执行人
		
	}
	

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
}