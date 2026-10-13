package com.sl.app.component.template;

import com.sl.app.component.util.ComFactory;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisTitle;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.Legend;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;
import com.vaadin.flow.component.charts.model.PlotOptionsSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsSpline;
import com.vaadin.flow.component.charts.model.SeriesTooltip;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.Div;

public class ChartDemo extends Div {
	
	private static final long serialVersionUID = 8785584651369336655L;
	private Chart ratingDistrubuteChart;
    public ChartDemo() {
    	
        initChart();
		
        add(ratingDistrubuteChart);
        
        addColumnChart();
        
        addLineChart();
        
        addPieChart();
    }

	private void initChart() {
		ratingDistrubuteChart = ComFactory.getChart(ChartType.COLUMN);
		ratingDistrubuteChart.setWidth("50%");
		ratingDistrubuteChart.setHeight("550px");
		Configuration conf = ratingDistrubuteChart.getConfiguration();
		
		XAxis x = new XAxis();
		x.setCategories("A","B","C","D","E");	
		conf.addxAxis(x);
		conf.setTitle("");

		YAxis y1 = new YAxis();
		y1.setTitle(new AxisTitle("样本比例 (%)"));
//		y1.setTickPositions(new Number[] {12,15,37,32,13});
		conf.addyAxis(y1);
		
		YAxis y2 = new YAxis();
//		y2.setTickPositions(new Number[] {25,35,37,23,26});
		y2.setTitle("累计比例(%)");
		y2.setOpposite(true);
		conf.addyAxis(y2);
		
		conf.getTooltip()
				.setFormatter(
						"'<b>'+ this.series.name+'</b><br/>'+this.x +': '+ Highcharts.numberFormat(this.y,1) +'%'");
		Legend legend = new Legend();
        legend.setBackgroundColor(new SolidColor("#f3fafd"));
        conf.setLegend(legend);

		PlotOptionsColumn p1 = new PlotOptionsColumn();
		PlotOptionsColumn p2 = new PlotOptionsColumn();
		ListSeries rquestPctList = new ListSeries("test(左轴)",new Number[] {12,15,37,32,13});
		ListSeries externalPctList = new ListSeries("test2(左轴)",new Number[] {25,35,37,23,26});
		rquestPctList.setPlotOptions(p1);
		externalPctList.setPlotOptions(p2);		
		conf.addSeries(rquestPctList);
		conf.addSeries(externalPctList);
		
		PlotOptionsSpline p3 = new PlotOptionsSpline();
		PlotOptionsSpline p4 = new PlotOptionsSpline();
		ListSeries rquestSumPctList = new ListSeries("test3(右轴)",new Number[] {11,22,37,35,23});
		ListSeries externalSumPctList = new ListSeries("test4(右轴)",new Number[] {15,25,31,13,29});
		rquestSumPctList.setPlotOptions(p3);
		rquestSumPctList.setyAxis(1);
		externalSumPctList.setPlotOptions(p4);
		externalSumPctList.setyAxis(1);
		conf.addSeries(rquestSumPctList);
		conf.addSeries(externalSumPctList);
		
		ratingDistrubuteChart.drawChart();
	}

	private void addPieChart() {
		Chart chartPine = new Chart(ChartType.PIE); 
        DataSeries dataSeries = new DataSeries();
        dataSeries.add(new DataSeriesItem("A公司", 200));
        dataSeries.add( new DataSeriesItem("B公司", 500));
        chartPine.getConfiguration().setSeries(dataSeries);
        add(chartPine);
	}

	private void addLineChart() {
		Chart chart = new Chart();
        chart.setHeight("500px");
        chart.setWidth("500px");
        ListSeries seriess = new ListSeries("Tokyo", 49.9, 71.5, 106.4);
        SeriesTooltip seriesTooltip = new SeriesTooltip();
        seriesTooltip.setPointFormatter("function() { return this.x + ' km²' }");

        PlotOptionsSeries options = new PlotOptionsSeries();
        options.setTooltip(seriesTooltip);
        seriess.setPlotOptions(options);
        Configuration configuration = chart.getConfiguration();
        configuration.addPlotOptions(options);
        configuration.addSeries(seriess);
        chart.drawChart();
        add(chart);
	}

	private void addColumnChart() {
		Chart c = new Chart(ChartType.COLUMN);
        c.setHeight("300px");
        c.setWidth("300px");
        Configuration conf = c.getConfiguration();
        DataSeries series = new DataSeries("示例1");
        DataSeriesItem item = new DataSeriesItem("A", 2.3);
        DataSeriesItem item2 = new DataSeriesItem("b", 12.3);
        DataSeriesItem item3 = new DataSeriesItem("c", 22.3);
        series.add(item);
        series.add(item2);
        series.add(item3);
        DataSeries series2 = new DataSeries("示例12");
        DataSeries series3 = new DataSeries("示例123");
        DataSeriesItem item222 = new DataSeriesItem("A2", 22.3);
        DataSeriesItem item22 = new DataSeriesItem("b2", 12.32);
        DataSeriesItem item32 = new DataSeriesItem("c2", 22.32);
        DataSeriesItem item33 = new DataSeriesItem("333", 33.32);
        series3.add(item33);
        series2.add(item22);
        series2.add(item222);
        series2.add(item32);
        conf.addSeries(series);
        conf.addSeries(series2);
        conf.addSeries(series3);
        conf.setTitle("demo chart test");
        Tooltip tool = new Tooltip();
        conf.setTooltip(tool);
        c.drawChart();
        add(c);
	}
}