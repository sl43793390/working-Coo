package com.sl.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.sl.entity.Task;
import com.sl.service.ProjectRoleService;
import com.sl.service.TaskProcess;
import com.sl.util.Constants;
import com.sl.util.Util;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 任务看板（T2-2）。
 * <p>
 * 卡片可拖拽到另一列直接改状态；也提供「移动到」下拉作为兜底（触屏 / 无障碍场景拖不动）。
 * 拖拽走原生 HTML5 drag 事件：dragstart 时把任务 ID 塞进 dataTransfer，
 * drop 时用 addEventData 把值带回服务端，避免依赖服务端字段保存「当前拖的是谁」——
 * 那种做法在两个事件分属不同请求时存在竞态。
 */
@SpringComponent
@Scope("prototype")
public class TaskKanbanComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(TaskKanbanComponent.class);

	private static final String GROUP_BY_STATUS = "按状态";
	private static final String GROUP_BY_PRIOR = "按优先级";
	private static final String GROUP_BY_USER = "按执行人";

	private final TaskProcess taskProcess;
	private final ProjectRoleService projectRoleService;

	private ComboBox<String> groupByBox;
	private Checkbox onlyMineBox;
	private Button refreshBtn;
	private HorizontalLayout board;
	private Span emptyHint;

	private List<Task> allTasks = new ArrayList<>();

	public TaskKanbanComponent(@Autowired TaskProcess taskProcess,
			@Autowired ProjectRoleService projectRoleService) {
		this.taskProcess = taskProcess;
		this.projectRoleService = projectRoleService;
		setWidthFull();
		setPadding(true);
		add(ComFactory.getTitle("任务看板"));
		add(ComFactory.getHorizontalLine());
		initToolbar();
		board = new HorizontalLayout();
		board.setWidthFull();
		board.addClassName("kanban-board");
		board.setSpacing(true);
		add(board);
		emptyHint = ComFactory.getEmptyHint("当前项目下暂无任务，可在左侧菜单点击「新增任务」创建");
		add(emptyHint);
		render();
	}

	private void initToolbar() {
		HorizontalLayout toolbar = ComFactory.getResponsiveHorizontalLayoutLeft();
		toolbar.setAlignItems(Alignment.CENTER);
		add(toolbar);
		groupByBox = ComFactory.getCombobox();
		groupByBox.setItems(GROUP_BY_STATUS, GROUP_BY_PRIOR, GROUP_BY_USER);
		groupByBox.setValue(GROUP_BY_STATUS);
		groupByBox.addValueChangeListener(e -> render());
		onlyMineBox = ComFactory.getCheckbox("只看我参与的");
		onlyMineBox.addValueChangeListener(e -> render());
		refreshBtn = ComFactory.getPrimaryBtn("刷新", VaadinIcon.REFRESH);
		refreshBtn.addClickListener(e -> render());
		toolbar.add(ComFactory.getLabel("分组维度："), groupByBox, onlyMineBox, refreshBtn);
	}

	/** 顶栏切换项目时重新取数 */
	@Override
	public void refresh() {
		render();
	}

	private void render() {
		Project current = ComponentUtil.getCurrentProject();
		board.removeAll();
		if (null == current) {
			ComFactory.notification("请先选择一个项目");
			emptyHint.setVisible(true);
			return;
		}
		try {
			allTasks = taskProcess.getTasksWithSchedule(current.getIdProject());
		} catch (Exception e) {
			ComFactory.notificationError("加载任务失败", e);
			emptyHint.setVisible(true);
			return;
		}
		List<Task> visible = filterVisible(allTasks);
		ComFactory.toggleEmptyHint(emptyHint, visible.size());
		if (visible.isEmpty()) {
			return;
		}
		Map<String, List<Task>> grouped = groupTasks(visible);
		for (Map.Entry<String, List<Task>> entry : grouped.entrySet()) {
			board.add(buildColumn(entry.getKey(), entry.getValue()));
		}
	}

	private List<Task> filterVisible(List<Task> tasks) {
		if (!onlyMineBox.getValue()) {
			return new ArrayList<>(tasks);
		}
		com.sl.entity.User me = ComponentUtil.getCurrentUser();
		if (null == me) {
			return new ArrayList<>(tasks);
		}
		String myId = me.getUserId();
		String myName = me.getUserName();
		return tasks.stream().filter(t -> {
			if (myId != null && myId.equals(t.getIdUser())) {
				return true;
			}
			if (myId != null && myId.equals(t.getIdExeUser())) {
				return true;
			}
			return myName != null && t.getNameExeUsers() != null && t.getNameExeUsers().contains(myName);
		}).collect(Collectors.toList());
	}

	/**
	 * 按维度分组。LinkedHashMap 保证列的顺序稳定：
	 * 状态用 Constants.STATUS_LIST 的顺序，其它维度按出现顺序。
	 */
	private Map<String, List<Task>> groupTasks(List<Task> tasks) {
		Map<String, List<Task>> grouped = new LinkedHashMap<>();
		String by = groupByBox.getValue();
		if (GROUP_BY_STATUS.equals(by)) {
			for (String s : Constants.STATUS_LIST) {
				grouped.put(s, new ArrayList<>());
			}
			for (Task t : tasks) {
				String key = (null == t.getCdStatus() || t.getCdStatus().isBlank()) ? "未开始" : t.getCdStatus();
				grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
			}
		} else if (GROUP_BY_PRIOR.equals(by)) {
			for (String p : List.of("高", "中", "低")) {
				grouped.put(p, new ArrayList<>());
			}
			for (Task t : tasks) {
				String key = (null == t.getCdPrior() || t.getCdPrior().isBlank()) ? "低" : t.getCdPrior();
				grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
			}
		} else {
			for (Task t : tasks) {
				String key = (null == t.getNameExeUsers() || t.getNameExeUsers().isBlank())
						? "未分配"
						: t.getNameExeUsers();
				grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
			}
		}
		// 去掉没有任务的列，否则按执行人分组时会冒出一大堆空列
		grouped.entrySet().removeIf(e -> e.getValue().isEmpty());
		return grouped;
	}

	private Component buildColumn(String title, List<Task> tasks) {
		VerticalLayout column = new VerticalLayout();
		column.setPadding(false);
		column.setSpacing(false);
		column.addClassName("kanban-column");
		column.setWidth("280px");
		column.setMinWidth("240px");

		HorizontalLayout header = new HorizontalLayout();
		header.setWidthFull();
		header.setAlignItems(Alignment.CENTER);
		header.setJustifyContentMode(JustifyContentMode.BETWEEN);
		header.addClassName("kanban-column-header");
		Span titleSpan = new Span(title);
		titleSpan.addClassName("kanban-column-title");
		Span count = new Span(String.valueOf(tasks.size()));
		count.addClassName("kanban-column-count");
		header.add(titleSpan, count);
		column.add(header);

		VerticalLayout body = new VerticalLayout();
		body.setPadding(false);
		body.addClassName("kanban-column-body");
		body.setWidthFull();
		// drop 事件要生效，dragover 必须 preventDefault
		body.getElement().executeJs("this.addEventListener('dragover', function(e){ e.preventDefault(); });");
		body.getElement().addEventListener("drop", ev -> {
			String taskId = ev.getEventData().getString("event.dataTransfer.getData('text/plain')");
			if (null != taskId && !taskId.isBlank()) {
				onDrop(taskId, title);
			}
		}).addEventData("event.dataTransfer.getData('text/plain')");

		for (Task t : tasks) {
			body.add(buildCard(t));
		}
		column.add(body);
		return column;
	}

	private Component buildCard(Task t) {
		VerticalLayout card = new VerticalLayout();
		card.setPadding(false);
		card.setSpacing(false);
		card.addClassName("kanban-card");
		card.setWidthFull();
		card.getElement().setAttribute("draggable", "true");
		card.getElement().setAttribute("data-task-id", t.getIdTask());
		// 客户端把任务 ID 放进 dataTransfer，服务端 drop 时再取出来
		card.getElement().executeJs(
				"this.addEventListener('dragstart', function(e){"
						+ " e.dataTransfer.setData('text/plain', this.getAttribute('data-task-id'));"
						+ " e.dataTransfer.effectAllowed = 'move'; this.classList.add('kanban-card-dragging'); });"
						+ "this.addEventListener('dragend', function(e){ this.classList.remove('kanban-card-dragging'); });");

		Span name = new Span(t.getNameTask());
		name.addClassName("kanban-card-title");
		name.addClickListener(e -> openDetail(t));
		card.add(name);

		Span meta = new Span(buildMeta(t));
		meta.addClassName("kanban-card-meta");
		card.add(meta);

		if (null != t.getDtEnd()) {
			Span due = new Span("截止 " + Util.dateToLocalDate(t.getDtEnd()));
			due.addClassName(isOverdue(t) ? "kanban-card-due-overdue" : "kanban-card-due");
			card.add(due);
		}

		// 拖拽的兜底：点下拉直接改状态
		ComboBox<String> moveBox = ComFactory.getCombobox();
		moveBox.setPlaceholder("移动到…");
		moveBox.setWidthFull();
		moveBox.setItems(new ArrayList<>(Constants.STATUS_LIST));
		moveBox.addValueChangeListener(e -> {
			String target = e.getValue();
			if (null == target || target.equals(t.getCdStatus())) {
				return;
			}
			moveTask(t.getIdTask(), target);
		});
		card.add(moveBox);
		return card;
	}

	private String buildMeta(Task t) {
		StringBuilder sb = new StringBuilder();
		if (null != t.getCdPrior() && !t.getCdPrior().isBlank()) {
			sb.append(t.getCdPrior()).append(" · ");
		}
		String exe = t.getNameExeUsers();
		sb.append((null == exe || exe.isBlank()) ? "未分配" : exe);
		if (null != t.getPctComplete()) {
			sb.append(" · ").append(t.getPctComplete()).append("%");
		}
		return sb.toString();
	}

	private boolean isOverdue(Task t) {
		if (null == t.getDtEnd()) {
			return false;
		}
		String st = t.getCdStatus();
		if ("已结束".equals(st) || "已废弃".equals(st)) {
			return false;
		}
		return t.getDtEnd().before(new java.util.Date());
	}

	/**
	 * 卡片落到某一列。只有「按状态」分组时落列才等价于改状态，
	 * 其它维度下拖拽只做提示，避免误把执行人/优先级当成状态写进去。
	 */
	private void onDrop(String taskId, String columnTitle) {
		if (!GROUP_BY_STATUS.equals(groupByBox.getValue())) {
			ComFactory.notification("当前按「" + groupByBox.getValue() + "」分组，请用卡片上的「移动到」改状态");
			return;
		}
		Task t = findTask(taskId);
		if (null == t) {
			return;
		}
		if (columnTitle.equals(t.getCdStatus())) {
			return;
		}
		moveTask(taskId, columnTitle);
	}

	private void moveTask(String taskId, String targetStatus) {
		Project p = ComponentUtil.getCurrentProject();
		if (null != p && !projectRoleService.canEdit(p.getIdProject(), ComponentUtil.getCurrentUser().getUserId())) {
			ComFactory.notificationError("你在当前项目是只读访客，不能修改任务状态");
			render();
			return;
		}
		try {
			taskProcess.updateTaskStatus(taskId, targetStatus);
			ComFactory.notificationSuccess("已移动到「" + targetStatus + "」");
			render();
		} catch (Exception e) {
			log.error("移动任务失败，task={}, status={}", taskId, targetStatus, e);
			ComFactory.notificationError("移动失败", e);
			render();
		}
	}

	private Task findTask(String idTask) {
		for (Task t : allTasks) {
			if (t.getIdTask().equals(idTask)) {
				return t;
			}
		}
		return null;
	}

	private void openDetail(Task t) {
		TaskDetailComponent c = ComponentUtil.applicationContext.getBean(TaskDetailComponent.class);
		c.setTask(t);
		c.initContent();
		TabsUtil.addComponentTab("任务详情-" + t.getIdTask(), "任务详情", c);
	}

	/** 供外部（例如新增任务后）触发刷新 */
	public void reload() {
		render();
	}
}
