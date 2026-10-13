package com.sl.project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.entity.Project;
import com.sl.entity.ProjectUser;
import com.sl.service.ProjectProcess;
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
 * 项目统计信息，包括项目剩余期限，任务工时统计
 * @author Administrator
 *
 */
@SpringComponent
@Scope("prototype")
public class ProjectStatiticsComponent  extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = -904431911870048899L;
	ProjectProcess projectProcess;
	private Span projectRemainingTime;
	private Span timeOfDuration;
	private Span numberOfParticipants;
	public ProjectStatiticsComponent(@Autowired ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
		addClassName("list-view");
        setDefaultHorizontalComponentAlignment(Alignment.START); 
        
        HorizontalLayout tile = ComFactory.getTitle("项目统计信息");
        Span horizontalLine = ComFactory.getHorizontalLine();
        add(tile,horizontalLine);
        
        initLayout();
        initContent();
        registerHandler();
	}
	/**
	 * 展示项目下每日新增任务的趋势、项目已用工时、参与项目的人员数量、项目文档数量、
	 */
	private void initLayout() {
		addRemainingTime();
		addTaskTrend();
	}
	private void addRemainingTime() {
		HorizontalLayout horizontalLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		Span label = ComFactory.getTitleLabel("项目剩余时间：");
		projectRemainingTime = ComFactory.getTitleLabel();
		horizontalLayout.add(label,projectRemainingTime);
		add(horizontalLayout);
		
		Span label2 = ComFactory.getTitleLabel("项目持续时间：");
		timeOfDuration = ComFactory.getTitleLabel();
		horizontalLayout.add(ComFactory.getBlankLabel("60px"),label2,timeOfDuration);
		
		Span label3 = ComFactory.getTitleLabel("项目参与人数：");
		numberOfParticipants = ComFactory.getTitleLabel();
		horizontalLayout.add(ComFactory.getBlankLabel("60px"),label3,numberOfParticipants);
	}
	/**
	 * 根据日期展示每日的作业创建数量
	 */
	private void addTaskTrend() {
		Project currentProject = ComponentUtil.getCurrentProject();
		if (null == currentProject) {
			add(ComFactory.getLabel("当前未选择项目，无法展示任务趋势"));
			return;
		}
		Chart chart = new Chart();
        chart.setHeight("500px");
        chart.setWidth("500px");
//        ListSeries seriess = new ListSeries("任务数量");
        DataSeries series = new DataSeries("任务数量");
        List<Map<String,Object>> taskNumOrder = projectProcess.getTaskNumOrder(currentProject.getIdProject());
        if (null == taskNumOrder || taskNumOrder.isEmpty()) {
        	add(ComFactory.getLabel("当前项目暂无任务数据"));
        	return;
        }
//        SeriesTooltip seriesTooltip = new SeriesTooltip();
//        seriesTooltip.setPointFormatter("function() { return this.x + '' }");
        for (Map<String, Object> map : taskNumOrder) {
        	Date date = (Date)map.get("DT_CREATE");
        	Long count = (Long)map.get("COUNT");
        	if (null == date || null == count) {
        		continue;
        	}
			// DT_CREATE 是 DATE 列，Map 结果里是 java.sql.Date，它的 toInstant()
			// 会抛 UnsupportedOperationException，必须走 Util.dateToInstant（经 getTime()）
			DataSeriesItem item = new DataSeriesItem(Util.dateToInstant(date), count);
			series.add(item);
		}
        PlotOptionsSeries options = new PlotOptionsSeries();
//        options.setTooltip(seriesTooltip);
        series.setPlotOptions(options);
        Configuration configuration = chart.getConfiguration();
        configuration.addPlotOptions(options);
        configuration.addSeries(series);
        chart.drawChart();
        add(chart);
	}
	
	/**
	 * 顶栏切换项目时被调用：重新计算三个核心指标。
	 */
	@Override
	public void refresh() {
		initContent();
	}

	/**
	 * 填充三个核心指标：剩余时间、持续时间、参与人数。
	 * 原实现是空方法，三个 Span 只有标签没有值，页面上永远显示三行空标题。
	 */
	private void initContent() {
		Project currentProject = ComponentUtil.getCurrentProject();
		if (null == currentProject) {
			projectRemainingTime.setText(NO_DATA);
			timeOfDuration.setText(NO_DATA);
			numberOfParticipants.setText(NO_DATA);
			return;
		}
		projectRemainingTime.setText(calcRemainingDays(currentProject.getDtEnd()));
		timeOfDuration.setText(calcDuration(currentProject.getDtStart(), currentProject.getDtEnd()));
		numberOfParticipants.setText(calcParticipants(currentProject.getIdProject()));
	}

	private static final String NO_DATA = "—";
	private static final String NOT_SET = "未设置";

	/**
	 * 剩余天数 = 项目结束日期 - 今天。结束日期缺失或已过期都给出明确文案，不再留空。
	 */
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
		return "已逾期 " + Math.abs(days) + " 天";
	}

	/**
	 * 持续时间 = 结束日期 - 开始日期，按自然日计算（含首尾故 +1）。
	 */
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

	/**
	 * 参与人数取自 project_user 关联表。
	 */
	private String calcParticipants(String idProject) {
		try {
			List<ProjectUser> members = projectProcess.getProjectUserListByIdProject(idProject);
			if (null == members || members.isEmpty()) {
				return "0 人";
			}
			return members.size() + " 人";
		} catch (Exception e) {
			return "获取失败";
		}
	}

	private void registerHandler() {
		// 统计页为只读展示，无需事件绑定
	}

}
