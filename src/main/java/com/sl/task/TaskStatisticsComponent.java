package com.sl.task;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.entity.Project;
import com.sl.entity.Task;
import com.sl.service.ProjectRoleService;
import com.sl.service.TaskProcess;
import com.sl.util.Constants;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 任务统计分析（原菜单误配成 MyTaskComponent，打开的是「我的任务」页面）。
 * <p>
 * 纯 CSS 条形展示：指标卡 + 状态/优先级/执行人三个维度的分布条。
 * 不用 Vaadin Charts——它是商业组件（未配 license 时图上会浮水印），
 * 统计这种简单分布用自绘条形更可控，样式集中在 styles.css 的 stat-* 系列。
 */
@SpringComponent
@Scope("prototype")
public class TaskStatisticsComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = 1L;

	private final TaskProcess taskProcess;
	private final ProjectRoleService projectRoleService;

	private final VerticalLayout contentArea = new VerticalLayout();
	private final Span emptyHint;

	public TaskStatisticsComponent(@Autowired TaskProcess taskProcess,
			@Autowired ProjectRoleService projectRoleService) {
		this.taskProcess = taskProcess;
		this.projectRoleService = projectRoleService;
		setWidthFull();
		setPadding(true);
		add(ComFactory.getTitle("任务统计分析"));
		add(ComFactory.getHorizontalLine());

		contentArea.setPadding(false);
		contentArea.setSpacing(true);
		contentArea.setWidthFull();
		add(contentArea);
		emptyHint = ComFactory.getEmptyHint("当前项目下暂无任务，可在左侧菜单点击「新增任务」创建");
		add(emptyHint);
		render();
	}

	@Override
	public void refresh() {
		render();
	}

	private void render() {
		contentArea.removeAll();
		Project current = ComponentUtil.getCurrentProject();
		if (null == current) {
			ComFactory.notification("请先选择一个项目");
			emptyHint.setVisible(true);
			return;
		}
		List<Task> tasks;
		try {
			// 与「我的任务」列表同一可见口径：管理员看全部；普通用户看 公开 + 自己创建 + 分派给自己。
			// 原 getTaskByIdProject 只查 FLAG_PRIVATE='Y'，历史任务多为 'N'，统计页会显示为空
			String me = ComponentUtil.getCurrentUser() == null ? null
					: ComponentUtil.getCurrentUser().getUserId();
			tasks = taskProcess.getTasksVisibleTo(current.getIdProject(), me, projectRoleService.isGlobalAdmin());
		} catch (Exception e) {
			ComFactory.notificationError("加载任务统计失败", e);
			emptyHint.setVisible(true);
			return;
		}
		ComFactory.toggleEmptyHint(emptyHint, tasks.size());
		if (tasks.isEmpty()) {
			return;
		}
		taskProcess.fillExecutorNames(tasks);

		contentArea.add(buildCards(tasks));
		contentArea.add(buildSection("状态分布", buildStatusBars(tasks)));
		contentArea.add(buildSection("优先级分布", buildPriorBars(tasks)));
		contentArea.add(buildSection("执行人任务数", buildUserBars(tasks)));
	}

	/** 顶部四张指标卡：总数 / 进行中 / 已结束 / 逾期未完成 */
	private HorizontalLayout buildCards(List<Task> tasks) {
		int total = tasks.size();
		int doing = 0;
		int done = 0;
		int overdue = 0;
		for (Task t : tasks) {
			String st = t.getCdStatus();
			if ("进行中".equals(st)) {
				doing++;
			}
			if ("已结束".equals(st)) {
				done++;
			}
			if (isOverdue(t)) {
				overdue++;
			}
		}
		HorizontalLayout cards = new HorizontalLayout();
		cards.setSpacing(true);
		cards.setPadding(false);
		cards.setWidthFull();
		cards.addClassName("stat-cards");
		cards.add(buildCard(String.valueOf(total), "任务总数", "stat-card-blue"));
		cards.add(buildCard(String.valueOf(doing), "进行中", "stat-card-green"));
		cards.add(buildCard(String.valueOf(done), "已结束", "stat-card-gray"));
		cards.add(buildCard(String.valueOf(overdue), "逾期未完成", "stat-card-red"));
		return cards;
	}

	private VerticalLayout buildCard(String value, String label, String colorClass) {
		Span v = new Span(value);
		v.addClassName("stat-card-value");
		Span l = new Span(label);
		l.addClassName("stat-card-label");
		VerticalLayout card = new VerticalLayout(v, l);
		card.setPadding(false);
		card.setSpacing(false);
		card.addClassName("stat-card");
		card.addClassName(colorClass);
		return card;
	}

	private VerticalLayout buildSection(String title, VerticalLayout bars) {
		Span t = new Span(title);
		t.addClassName("stat-section-title");
		VerticalLayout section = new VerticalLayout(t, bars);
		section.setPadding(false);
		section.setSpacing(false);
		section.setWidthFull();
		return section;
	}

	/** 状态分布：固定按 STATUS_LIST 顺序展示，没数据的维度也保留（值为 0） */
	private VerticalLayout buildStatusBars(List<Task> tasks) {
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (String s : Constants.STATUS_LIST) {
			counts.put(s, 0);
		}
		for (Task t : tasks) {
			String key = (null == t.getCdStatus() || t.getCdStatus().isBlank()) ? "未开始" : t.getCdStatus();
			counts.merge(key, 1, Integer::sum);
		}
		return buildBars(counts, "stat-fill-status-%s");
	}

	private VerticalLayout buildPriorBars(List<Task> tasks) {
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (String p : List.of("高", "中", "低")) {
			counts.put(p, 0);
		}
		for (Task t : tasks) {
			String key = (null == t.getCdPrior() || t.getCdPrior().isBlank()) ? "低" : t.getCdPrior();
			counts.merge(key, 1, Integer::sum);
		}
		return buildBars(counts, "stat-fill-prior-" + "%s");
	}

	/** 执行人任务数：nameExeUsers 是逗号/顿号分隔的姓名串，按人拆开累计 */
	private VerticalLayout buildUserBars(List<Task> tasks) {
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (Task t : tasks) {
			String exe = t.getNameExeUsers();
			if (null == exe || exe.isBlank()) {
				counts.merge("未分配", 1, Integer::sum);
				continue;
			}
			for (String name : exe.split("[,，、]")) {
				String n = name.trim();
				if (!n.isEmpty()) {
					counts.merge(n, 1, Integer::sum);
				}
			}
		}
		// 任务数降序，最多展示 10 人
		List<Map.Entry<String, Integer>> top = new ArrayList<>(counts.entrySet());
		top.sort((a, b) -> b.getValue() - a.getValue());
		if (top.size() > 10) {
			top = top.subList(0, 10);
		}
		Map<String, Integer> ordered = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> e : top) {
			ordered.put(e.getKey(), e.getValue());
		}
		return buildBars(ordered, "stat-fill-user");
	}

	/**
	 * 把「名称 -> 数量」画成条形：label + track(百分比宽的 fill) + 数值。
	 * colorPattern 可传 "stat-fill-status-%s" 这类带占位符的主题类名，null 则用默认色。
	 */
	private VerticalLayout buildBars(Map<String, Integer> counts, String colorPattern) {
		int max = counts.values().stream().mapToInt(Integer::intValue).max().orElse(1);
		VerticalLayout chart = new VerticalLayout();
		chart.setPadding(false);
		chart.setSpacing(false);
		chart.setWidthFull();
		chart.addClassName("stat-bars");
		for (Map.Entry<String, Integer> e : counts.entrySet()) {
			Span label = new Span(e.getKey());
			label.addClassName("stat-bar-label");
			label.setTitle(e.getKey());

			Span fill = new Span();
			fill.addClassName("stat-bar-fill");
			if (null != colorPattern && colorPattern.contains("%s")) {
				// 按名称取主题色类，如 stat-fill-status-进行中；取不到统一落默认色
				fill.addClassName(colorPattern.formatted(e.getKey()));
			} else if (null != colorPattern) {
				fill.addClassName(colorPattern);
			}
			fill.getStyle().set("width", (e.getValue() * 100.0 / max) + "%");

			Span value = new Span(String.valueOf(e.getValue()));
			value.addClassName("stat-bar-value");

			VerticalLayout track = new VerticalLayout(fill);
			track.setPadding(false);
			track.setSpacing(false);
			track.addClassName("stat-bar-track");

			HorizontalLayout row = new HorizontalLayout(label, track, value);
			row.setSpacing(false);
			row.setPadding(false);
			row.setWidthFull();
			row.addClassName("stat-bar-row");
			row.setVerticalComponentAlignment(Alignment.CENTER, label, track, value);
			chart.add(row);
		}
		return chart;
	}

	private boolean isOverdue(Task t) {
		if (null == t.getDtEnd()) {
			return false;
		}
		String st = t.getCdStatus();
		if ("已结束".equals(st) || "已废弃".equals(st)) {
			return false;
		}
		return t.getDtEnd().before(Date.from(LocalDate.now()
				.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}
}
