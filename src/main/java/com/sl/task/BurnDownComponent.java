package com.sl.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.bo.BurnDownSeries;
import com.sl.entity.Task;
import com.sl.service.BurnDownCalculator;
import com.sl.service.TaskProcess;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 燃尽图（T2-7）。
 * <p>
 * 两条线：
 * <ul>
 *   <li>理想剩余 —— 从总工时线性递减到 0，是基准线；</li>
 *   <li>实际剩余 —— 按任务结束日期排序，累计「已到期的任务还剩多少工时」。</li>
 * </ul>
 * 没有填「预计工时」的任务不参与统计（算不出剩余量），页面顶部会提示数量。
 */
@SpringComponent
@Scope("prototype")
public class BurnDownComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(BurnDownComponent.class);

	private final TaskProcess taskProcess;
	private final BurnDownCalculator burnDownCalculator;

	private VerticalLayout chartHolder;
	private Span emptyHint;

	public BurnDownComponent(@Autowired TaskProcess taskProcess,
			@Autowired BurnDownCalculator burnDownCalculator) {
		this.taskProcess = taskProcess;
		this.burnDownCalculator = burnDownCalculator;
		setWidthFull();
		setPadding(true);
		add(ComFactory.getTitle("燃尽图"));
		add(ComFactory.getHorizontalLine());
		chartHolder = new VerticalLayout();
		chartHolder.setPadding(false);
		chartHolder.setWidthFull();
		add(chartHolder);
		emptyHint = ComFactory.getEmptyHint("当前项目下暂无任务，可在左侧菜单点击「新增任务」创建");
		add(emptyHint);
		render();
	}

	@Override
	public void refresh() {
		render();
	}

	private void render() {
		chartHolder.removeAll();
		if (null == ComponentUtil.getCurrentProject()) {
			ComFactory.notification("请先选择一个项目");
			emptyHint.setVisible(true);
			return;
		}
		List<Task> tasks;
		try {
			tasks = taskProcess.getTasksWithSchedule(ComponentUtil.getCurrentProject().getIdProject());
		} catch (Exception e) {
			log.error("加载燃尽图数据失败", e);
			ComFactory.notificationError("加载任务失败", e);
			emptyHint.setVisible(true);
			return;
		}
		ComFactory.toggleEmptyHint(emptyHint, tasks.size());
		if (tasks.isEmpty()) {
			return;
		}

		// 计算逻辑单独抽到 BurnDownCalculator：纯函数，可单测，不依赖 Vaadin UI 线程
		BurnDownSeries s = burnDownCalculator.compute(tasks);
		if (s.getCountedTasks() == 0) {
			chartHolder.add(new Span("共 " + tasks.size()
					+ " 个任务，但没有一个填写了「预计工时」，无法计算剩余工作量。"));
			return;
		}
		if (s.getCountedTasks() < tasks.size()) {
			Span tip = new Span("另有 " + (tasks.size() - s.getCountedTasks()) + " 个任务未填预计工时，未纳入统计");
			tip.addClassName("gantt-tip");
			chartHolder.add(tip);
		}

		chartHolder.add(buildChart(s.getLabels(), s.getIdeal(), s.getActual()));
	}

	private Chart buildChart(List<String> labels, List<Number> ideal, List<Number> actual) {
		Chart chart = new Chart(ChartType.LINE);
		chart.setWidthFull();
		chart.setHeight("420px");
		Configuration conf = chart.getConfiguration();
		conf.setTitle("燃尽图（剩余工时）");
		conf.getLegend().setEnabled(true);

		XAxis x = new XAxis();
		x.setTitle("日期");
		x.setCategories(labels.toArray(new String[0]));
		conf.addxAxis(x);

		YAxis y = new YAxis();
		y.setTitle("剩余工时（小时）");
		// 锚定 0 起点：默认自动缩放可能从非 0 开始，剩余工时被夸大，和理想线不可比
		y.setMin(0);
		conf.addyAxis(y);

		// 显式配 tooltip：shared 模式下鼠标靠近任一天，同时显示该日期（x）和
		// 理想/实际两条线的剩余工时（y）。之前没配过，依赖库默认值，
		// 实际悬浮不出提示，看不出某天两条线各是多少
		Tooltip tooltip = new Tooltip();
		tooltip.setEnabled(true);
		tooltip.setShared(true);
		tooltip.setHeaderFormat("<b>{point.key}</b>");
		tooltip.setXDateFormat("%Y-%m-%d");
		tooltip.setValueSuffix(" 小时");
		conf.setTooltip(tooltip);

		conf.addSeries(new ListSeries("理想剩余", ideal.toArray(new Number[0])));
		conf.addSeries(new ListSeries("实际剩余", actual.toArray(new Number[0])));
		return chart;
	}

}
