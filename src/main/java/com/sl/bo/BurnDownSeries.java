package com.sl.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * 燃尽图的计算结果（T2-7 / T3-4）。
 * <p>
 * 从 BurnDownComponent 里抽出来是为了能脱离 Vaadin 做单测 ——
 * 组件要 new Chart，必须在 UI 线程里，纯计算不该被这个约束绑死。
 */
public class BurnDownSeries {

	private final List<String> labels = new ArrayList<>();
	private final List<Number> ideal = new ArrayList<>();
	private final List<Number> actual = new ArrayList<>();

	/** 参与统计的任务数（填了预计工时的） */
	private int countedTasks;
	/** 项目内任务总数，用于提示"有 N 个没填工时未纳入" */
	private int totalTasks;
	/** 总预计工时 */
	private double totalHours;

	public List<String> getLabels() {
		return labels;
	}

	public List<Number> getIdeal() {
		return ideal;
	}

	public List<Number> getActual() {
		return actual;
	}

	public int getCountedTasks() {
		return countedTasks;
	}

	public int getTotalTasks() {
		return totalTasks;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public boolean isEmpty() {
		return labels.isEmpty();
	}

	public void setCountedTasks(int countedTasks) {
		this.countedTasks = countedTasks;
	}

	public void setTotalTasks(int totalTasks) {
		this.totalTasks = totalTasks;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}
}
