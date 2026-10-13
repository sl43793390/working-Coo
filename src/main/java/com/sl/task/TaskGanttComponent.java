package com.sl.task;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.entity.Task;
import com.sl.entity.TaskDependency;
import com.sl.service.TaskDependencyService;
import com.sl.service.TaskProcess;
import com.sl.util.Util;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 甘特图（T2-7）。
 * <p>
 * 没有用 Vaadin Chart 的 bar 去「假装」甘特：Chart 的浮动条依赖 columnrange / xrange
 * 这类扩展类型，Vaadin 24 的官方 charts 组件不一定带得上，日期轴对齐也很折腾。
 * 这里直接用绝对定位的 Div 画：一行一个任务，条形按 [计划开始, 结束] 在时间轴上的
 * 百分比定位，渲染结果确定、不依赖图表库方言。
 */
@SpringComponent
@Scope("prototype")
public class TaskGanttComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(TaskGanttComponent.class);

	/** 时间轴刻度最多画这么多格，再多浏览器也放不下 */
	private static final int MAX_TICKS = 60;

	private final TaskProcess taskProcess;
	private final TaskDependencyService dependencyService;

	private ComboBox<String> scaleBox;
	private VerticalLayout chartArea;
	private Span emptyHint;

	public TaskGanttComponent(@Autowired TaskProcess taskProcess,
			@Autowired TaskDependencyService dependencyService) {
		this.taskProcess = taskProcess;
		this.dependencyService = dependencyService;
		setWidthFull();
		setPadding(true);
		add(ComFactory.getTitle("甘特图"));
		add(ComFactory.getHorizontalLine());
		initToolbar();
		chartArea = new VerticalLayout();
		chartArea.setPadding(false);
		chartArea.setWidthFull();
		chartArea.addClassName("gantt-area");
		add(chartArea);
		emptyHint = ComFactory.getEmptyHint("当前项目下暂无任务，可在左侧菜单点击「新增任务」创建");
		add(emptyHint);
		render();
	}

	private void initToolbar() {
		HorizontalLayout toolbar = ComFactory.getResponsiveHorizontalLayoutLeft();
		toolbar.setAlignItems(Alignment.CENTER);
		add(toolbar);
		scaleBox = ComFactory.getCombobox();
		scaleBox.setItems("按天", "按周");
		scaleBox.setValue("按天");
		scaleBox.addValueChangeListener(e -> render());
		Button refreshBtn = ComFactory.getPrimaryBtn("刷新", VaadinIcon.REFRESH);
		refreshBtn.addClickListener(e -> render());
		toolbar.add(ComFactory.getLabel("刻度："), scaleBox, refreshBtn);
	}

	@Override
	public void refresh() {
		render();
	}

	private void render() {
		chartArea.removeAll();
		if (null == ComponentUtil.getCurrentProject()) {
			ComFactory.notification("请先选择一个项目");
			emptyHint.setVisible(true);
			return;
		}
		String idProject = ComponentUtil.getCurrentProject().getIdProject();
		List<Task> tasks;
		try {
			tasks = taskProcess.getTasksWithSchedule(idProject);
		} catch (Exception e) {
			log.error("加载甘特图数据失败，project={}", idProject, e);
			ComFactory.notificationError("加载任务失败", e);
			emptyHint.setVisible(true);
			return;
		}
		ComFactory.toggleEmptyHint(emptyHint, tasks.size());
		if (tasks.isEmpty()) {
			return;
		}

		List<Task> drawable = tasks.stream()
				.filter(t -> null != t.getDtStart() && null != t.getDtEnd())
				.toList();
		long noSchedule = tasks.size() - drawable.size();
		if (drawable.isEmpty()) {
			chartArea.add(new Span("共 " + tasks.size()
					+ " 个任务，但都没有填写「计划开始日期 / 结束日期」，无法绘制甘特图。"));
			return;
		}
		if (noSchedule > 0) {
			Span tip = new Span("另有 " + noSchedule + " 个任务缺少排期，未在本图显示");
			tip.addClassName("gantt-tip");
			chartArea.add(tip);
		}

		LocalDate min = drawable.stream().map(t -> toLocalDate(t.getDtStart())).min(LocalDate::compareTo).orElseThrow();
		LocalDate max = drawable.stream().map(t -> toLocalDate(t.getDtEnd())).max(LocalDate::compareTo).orElseThrow();
		long totalDays = ChronoUnit.DAYS.between(min, max) + 1;

		// 「今天」在轴上的位置：落在 [min, max] 内才画，否则今天不在该项目排期范围内
		LocalDate today = LocalDate.now();
		long todayOffset = ChronoUnit.DAYS.between(min, today);
		boolean showToday = todayOffset >= 0 && todayOffset < totalDays;
		// 线画在当天格子的中线，和按天平分的刻度对齐
		double todayPct = (todayOffset + 0.5) * 100.0 / totalDays;

		Set<String> blocked = resolveBlocked(idProject, tasks);
		chartArea.add(buildHeader(min, totalDays, today, showToday, todayPct));
		for (Task t : drawable) {
			chartArea.add(buildRow(t, min, totalDays, blocked.contains(t.getIdTask()), showToday, todayPct));
		}
	}

	/**
	 * 被未完成前置任务挡住的任务集合。
	 * 依赖表按项目一次性取回，再在内存里算，避免每行调一次 isBlocked（N+1）。
	 */
	private Set<String> resolveBlocked(String idProject, List<Task> tasks) {
		Set<String> blocked = new HashSet<>();
		try {
			Map<String, String> statusById = new HashMap<>();
			for (Task t : tasks) {
				statusById.put(t.getIdTask(), t.getCdStatus());
			}
			for (TaskDependency d : dependencyService.listByProject(idProject)) {
				String st = statusById.get(d.getIdPreTask());
				// 查不到说明前置任务不在当前项目（数据脏），保守按已完成处理，避免整片飘红
				boolean done = (null == st) || "已结束".equals(st) || "已废弃".equals(st);
				if (!done) {
					blocked.add(d.getIdTask());
				}
			}
		} catch (Exception e) {
			log.warn("计算任务阻塞状态失败，本次不标注", e);
		}
		return blocked;
	}

	private HorizontalLayout buildHeader(LocalDate min, long totalDays, LocalDate today,
			boolean showToday, double todayPct) {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidthFull();
		header.setSpacing(false);
		header.addClassName("gantt-header");
		Span label = new Span("任务");
		label.addClassName("gantt-label-cell");
		header.add(label);

		VerticalLayout scale = new VerticalLayout();
		scale.setPadding(false);
		scale.setSpacing(false);
		scale.addClassName("gantt-scale");
		scale.setWidthFull();
		HorizontalLayout ticksLayout = new HorizontalLayout();
		ticksLayout.setSpacing(false);
		ticksLayout.setWidthFull();
		long step = "按周".equals(scaleBox.getValue()) ? 7 : 1;
		// 刻度太多浏览器放不下：按 MAX_TICKS 放大步长（保持 step 的整数倍）。
		// 直接截断成前 N 个刻度是错的——刻度宽度按 1/N 平分整条轴，而条形按全区间
		// 百分比定位，超过 N 天后表头刻度和条形会错位，且多出来的日期没有刻度
		long wantTicks = (totalDays + step - 1) / step;
		long tickStep = step;
		if (wantTicks > MAX_TICKS) {
			tickStep = ((totalDays + MAX_TICKS - 1) / MAX_TICKS + step - 1) / step * step;
		}
		long ticks = Math.min(MAX_TICKS, (totalDays + tickStep - 1) / tickStep);
		for (long i = 0; i < ticks; i++) {
			long dayOffset = i * tickStep;
			LocalDate day = min.plusDays(dayOffset);
			Span tick = new Span(tickStep >= 7
					? day.getMonthValue() + "/" + day.getDayOfMonth()
					: String.valueOf(day.getDayOfMonth()));
			tick.addClassName("gantt-tick");
			// 当天的刻度加粗变绿，一眼看到现在走到哪
			if (day.isEqual(today)) {
				tick.addClassName("gantt-tick-today");
			}
			// 宽度按「占整条轴的比例」算，刻度位置才能和条形对齐；
			// 最后一个刻度不越出轴末端
			long covered = Math.min(tickStep, totalDays - dayOffset);
			tick.setWidth((covered * 100.0 / totalDays) + "%");
			ticksLayout.add(tick);
		}
		scale.add(ticksLayout);
		if (showToday) {
			scale.add(todayMarker(todayPct));
		}
		header.add(scale);
		return header;
	}

	private HorizontalLayout buildRow(Task t, LocalDate min, long totalDays, boolean blocked,
			boolean showToday, double todayPct) {
		HorizontalLayout row = new HorizontalLayout();
		row.setWidthFull();
		row.setSpacing(false);
		row.addClassName("gantt-row");

		Span label = new Span(t.getNameTask());
		label.addClassName("gantt-label-cell");
		label.setTitle(t.getNameTask());
		row.add(label);

		VerticalLayout track = new VerticalLayout();
		track.setPadding(false);
		track.setSpacing(false);
		track.addClassName("gantt-track");
		track.setWidthFull();

		LocalDate s = toLocalDate(t.getDtStart());
		LocalDate e = toLocalDate(t.getDtEnd());
		long offset = ChronoUnit.DAYS.between(min, s);
		long span = ChronoUnit.DAYS.between(s, e) + 1;
		double left = totalDays <= 0 ? 0 : (offset * 100.0 / totalDays);
		double width = totalDays <= 0 ? 100 : (span * 100.0 / totalDays);
		width = Math.max(width, 0.8);

		Span bar = new Span(blocked ? "⛔ " + t.getNameTask() : t.getNameTask());
		bar.addClassName("gantt-bar");
		bar.addClassName(barClass(t));
		if (blocked) {
			bar.addClassName("gantt-bar-blocked");
		}
		bar.getStyle().set("left", left + "%").set("width", width + "%");
		bar.setTitle(t.getNameTask() + "：" + s + " ~ " + e + (blocked ? "（前置任务未完成）" : ""));
		track.add(bar);
		if (showToday) {
			track.add(todayMarker(todayPct));
		}
		row.add(track);
		return row;
	}

	/**
	 * 「今天」竖线：绝对定位在时间轴百分比处，贯穿整行，条形上方也能看到当前进度位置。
	 * 指针事件关掉，避免挡住条形上的悬停提示。
	 */
	private Span todayMarker(double todayPct) {
		Span line = new Span();
		line.addClassName("gantt-today-line");
		line.getStyle().set("left", todayPct + "%");
		return line;
	}

	private String barClass(Task t) {
		String st = t.getCdStatus();
		if ("已结束".equals(st)) {
			return "gantt-bar-done";
		}
		if ("已废弃".equals(st) || "已中断".equals(st)) {
			return "gantt-bar-stopped";
		}
		if (null != t.getDtEnd() && t.getDtEnd().before(new Date())) {
			return "gantt-bar-overdue";
		}
		return "gantt-bar-normal";
	}

	private LocalDate toLocalDate(Date d) {
		// 走 Util.dateToInstant：实体日期字段多数是 Timestamp，但统一走加固过的方法，
		// 杜绝哪天换成 java.sql.Date 裸跑 toInstant() 直接抛 UnsupportedOperationException
		return Util.dateToInstant(d).atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
