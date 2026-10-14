package com.sl.project;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import com.sl.entity.ProjectTaskFile;
import com.sl.entity.ProjectUser;
import com.sl.entity.Task;
import com.sl.service.ProjectProcess;
import com.sl.service.ProjectRoleService;
import com.sl.service.TaskProcess;
import com.sl.util.Constants;
import com.sl.util.Util;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsSeries;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 项目统计信息（仪表盘式重设计）。
 * <p>
 * 参考 Teambition / Jira Dashboard 的版式，自上而下：
 * <ol>
 *   <li>项目概况：六张指标卡（剩余时间 / 持续时间 / 参与人数 / 任务总数 / 完成率 / 文档数）；</li>
 *   <li>任务完成率：进度条 + 已完成/总数说明；</li>
 *   <li>状态分布 + 优先级分布：两列并排；</li>
 *   <li>执行人任务数 Top10；</li>
 *   <li>工时投入：预估 vs 实际；</li>
 *   <li>每日新增任务趋势：折线图（沿用原 getTaskNumOrder 数据源）。</li>
 * </ol>
 * 分布条复用 styles.css 的 stat-* 系列（与「任务统计分析」同一套视觉），
 * 任务口径与「我的任务」列表一致：管理员看全部，普通用户看 公开 + 自建 + 分派给自己。
 */
@SpringComponent
@Scope("prototype")
public class ProjectStatiticsComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = -904431911870048899L;

	private final ProjectProcess projectProcess;
	private final TaskProcess taskProcess;
	private final ProjectRoleService projectRoleService;

	private final VerticalLayout contentArea = new VerticalLayout();
	private final Span emptyHint;

	private static final String NOT_SET = "未设置";

	public ProjectStatiticsComponent(@Autowired ProjectProcess projectProcess,
			@Autowired TaskProcess taskProcess,
			@Autowired ProjectRoleService projectRoleService) {
		this.projectProcess = projectProcess;
		this.taskProcess = taskProcess;
		this.projectRoleService = projectRoleService;
		setWidthFull();
		setDefaultHorizontalComponentAlignment(Alignment.START);
		addClassName("list-view");

		add(ComFactory.getTitle("项目统计信息"));
		add(ComFactory.getHorizontalLine());

		contentArea.setPadding(false);
		contentArea.setSpacing(true);
		contentArea.setWidthFull();
		add(contentArea);
		emptyHint = ComFactory.getEmptyHint("请先在顶栏选择一个项目");
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
			emptyHint.setVisible(true);
			return;
		}
		emptyHint.setVisible(false);

		// 任务可见口径与「我的任务」一致
		String me = null == ComponentUtil.getCurrentUser() ? null : ComponentUtil.getCurrentUser().getUserId();
		List<Task> tasks;
		try {
			tasks = taskProcess.getTasksVisibleTo(current.getIdProject(), me, projectRoleService.isGlobalAdmin());
		} catch (Exception e) {
			ComFactory.notificationError("加载项目统计失败", e);
			return;
		}
		taskProcess.fillExecutorNames(tasks);
		List<ProjectUser> members = projectProcess.getProjectUserListByIdProject(current.getIdProject());
		List<ProjectTaskFile> docs = projectProcess.getProjectFileListByIdProject(current.getIdProject());

		contentArea.add(buildOverviewCards(current, tasks, members, docs));
		if (tasks.isEmpty()) {
			contentArea.add(ComFactory.getEmptyHint("当前项目暂无任务，统计将在创建任务后展示"));
			return;
		}
		contentArea.add(buildCompletionSection(tasks));
		contentArea.add(buildDistributions(tasks));
		contentArea.add(buildSection("执行人任务数 Top10", buildUserBars(tasks)));
		contentArea.add(buildWorkHoursSection(tasks));
		contentArea.add(buildSection("每日新增任务趋势", buildTrendChart(current)));
	}

	/* ==================================================================
	 * 项目概况：六张指标卡
	 * ================================================================== */
	private HorizontalLayout buildOverviewCards(Project project, List<Task> tasks,
			List<ProjectUser> members, List<ProjectTaskFile> docs) {
		long done = tasks.stream().filter(t -> "已结束".equals(t.getCdStatus())).count();
		String completion = tasks.isEmpty() ? "0%"
				: Math.round(done * 100.0 / tasks.size()) + "%";

		HorizontalLayout cards = new HorizontalLayout();
		cards.setSpacing(true);
		cards.setPadding(false);
		cards.setWidthFull();
		cards.addClassName("stat-cards");
		cards.add(buildCard(calcRemainingDays(project.getDtEnd()), "项目剩余时间", "stat-card-blue"));
		cards.add(buildCard(calcDuration(project.getDtStart(), project.getDtEnd()), "项目持续时间", "stat-card-purple"));
		cards.add(buildCard((null == members ? 0 : members.size()) + " 人", "参与人数", "stat-card-teal"));
		cards.add(buildCard(String.valueOf(tasks.size()), "任务总数", "stat-card-cyan"));
		cards.add(buildCard(completion, "任务完成率", "stat-card-green"));
		cards.add(buildCard(String.valueOf(null == docs ? 0 : docs.size()), "文档数量", "stat-card-gray"));
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

	/* ==================================================================
	 * 任务完成率：进度条
	 * ================================================================== */
	private VerticalLayout buildCompletionSection(List<Task> tasks) {
		long done = tasks.stream().filter(t -> "已结束".equals(t.getCdStatus())).count();
		int pct = (int) Math.round(done * 100.0 / tasks.size());

		Span fill = new Span();
		fill.addClassName("stat-progress-fill");
		fill.getStyle().set("width", Math.max(pct, 2) + "%");

		VerticalLayout track = new VerticalLayout(fill);
		track.setPadding(false);
		track.setSpacing(false);
		track.setWidthFull();
		track.addClassName("stat-progress-track");

		Span caption = new Span("已完成 " + done + " 项 / 共 " + tasks.size() + " 项（" + pct + "%）");
		caption.addClassName("stat-card-label");

		VerticalLayout body = new VerticalLayout(track, caption);
		body.setPadding(false);
		body.setSpacing(false);
		body.setWidthFull();
		return buildSection("任务完成率", body);
	}

	/* ==================================================================
	 * 状态 + 优先级：两列并排
	 * ================================================================== */
	private HorizontalLayout buildDistributions(List<Task> tasks) {
		VerticalLayout left = buildSection("状态分布", buildStatusBars(tasks));
		VerticalLayout right = buildSection("优先级分布", buildPriorBars(tasks));
		HorizontalLayout columns = new HorizontalLayout(left, right);
		columns.setSpacing(true);
		columns.setPadding(false);
		columns.setWidthFull();
		columns.addClassName("stat-columns");
		return columns;
	}

	/* ==================================================================
	 * 工时投入：预估 vs 实际
	 * ================================================================== */
	private VerticalLayout buildWorkHoursSection(List<Task> tasks) {
		double estimate = tasks.stream().mapToDouble(t -> null == t.getNbrEstimateHours() ? 0 : t.getNbrEstimateHours()).sum();
		double actual = tasks.stream().mapToDouble(t -> null == t.getNbrActualHours() ? 0 : t.getNbrActualHours()).sum();
		Map<String, Integer> hours = new LinkedHashMap<>();
		hours.put("预估工时", (int) Math.round(estimate));
		hours.put("实际工时", (int) Math.round(actual));
		VerticalLayout bars = buildBars(hours, "stat-fill-hours-%s");
		return buildSection("工时投入（小时）", bars);
	}

	/* ==================================================================
	 * 每日新增任务趋势：折线图，数据源沿用 getTaskNumOrder
	 * ================================================================== */
	private Chart buildTrendChart(Project project) {
		Chart chart = new Chart();
		chart.setHeight("300px");
		chart.setWidthFull();
		DataSeries series = new DataSeries("任务数量");
		List<Map<String, Object>> taskNumOrder = projectProcess.getTaskNumOrder(project.getIdProject());
		if (null != taskNumOrder) {
			for (Map<String, Object> map : taskNumOrder) {
				Date date = Util.asDate(map.get("DT_CREATE"));
				Long count = (Long) map.get("COUNT");
				if (null == date || null == count) {
					continue;
				}
				// DATE 列必须经 Util.dateToInstant（getTime()），直接 toInstant() 会抛异常
				series.add(new DataSeriesItem(Util.dateToInstant(date), count));
			}
		}
		PlotOptionsSeries options = new PlotOptionsSeries();
		series.setPlotOptions(options);
		Configuration configuration = chart.getConfiguration();
		configuration.addPlotOptions(options);
		configuration.addSeries(series);
		chart.drawChart();
		return chart;
	}

	/* ==================================================================
	 * 分布条（与 TaskStatisticsComponent 同款：label + track + 数值）
	 * ================================================================== */
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
		return buildBars(counts, "stat-fill-prior-%s");
	}

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
	 * 「名称 -> 数量」画成条形。colorPattern 含 %s 时按名称取主题类。
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

	private VerticalLayout buildSection(String title, com.vaadin.flow.component.Component body) {
		Span t = new Span(title);
		t.addClassName("stat-section-title");
		VerticalLayout section = new VerticalLayout(t, body);
		section.setPadding(false);
		section.setSpacing(false);
		section.setWidthFull();
		return section;
	}

	/* ==================================================================
	 * 项目时间口径（沿用原实现）
	 * ================================================================== */
	private String calcRemainingDays(Date dtEnd) {
		if (null == dtEnd) {
			return NOT_SET;
		}
		long days = ChronoUnit.DAYS.between(LocalDate.now(), Util.dateToLocalDate(dtEnd));
		if (days > 0) {
			return days + " 天";
		}
		if (days == 0) {
			return "今天到期";
		}
		return "逾期 " + Math.abs(days) + " 天";
	}

	private String calcDuration(Date dtStart, Date dtEnd) {
		if (null == dtStart || null == dtEnd) {
			return NOT_SET;
		}
		long days = ChronoUnit.DAYS.between(Util.dateToLocalDate(dtStart), Util.dateToLocalDate(dtEnd));
		if (days < 0) {
			return NOT_SET;
		}
		return (days + 1) + " 天";
	}
}
