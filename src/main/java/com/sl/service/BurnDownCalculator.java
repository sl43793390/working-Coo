package com.sl.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sl.bo.BurnDownSeries;
import com.sl.entity.Task;
import com.sl.util.Util;

/**
 * 燃尽图计算（T2-7 / T3-4）。
 * <p>
 * 纯函数、无状态、不碰数据库 —— 从 BurnDownComponent 里拆出来就是为了让这段逻辑能被单测覆盖，
 * 组件那一层只负责把结果画成 Chart。
 * <p>
 * 口径说明（这几个约定单测里都有断言锁住）：
 * <ul>
 *   <li>只统计「预计工时 &gt; 0」的任务，没填工时的算不出剩余量，参与进来只会把曲线拉歪；</li>
 *   <li>时间轴取参与统计任务里的最早开始 ~ 最晚结束；两者同日时强制拉成 2 天，否则会除以 0；</li>
 *   <li>理想线 = 总工时线性递减到 0；</li>
 *   <li>实际线 = 全部未完工任务的剩余工时：已到期（计划结束日 ≤ 当天）的按完成率折算剩余，
 *       未到期的全额计入。只累计「已到期」部分的写法会让曲线从 0 往上翘、和理想线反向，失去对比意义。</li>
 *   <li>结束日期缺失的任务按时间轴末端到期，不用 now()——否则一个没填截止日期的任务
 *       就能把时间轴拉到「今天」，历史项目平白多出几个月的平台线。</li>
 * </ul>
 */
@Service
public class BurnDownCalculator {

	/**
	 * @param allTasks 项目内全部任务（未过滤）
	 */
	public BurnDownSeries compute(List<Task> allTasks) {
		BurnDownSeries r = new BurnDownSeries();
		if (null == allTasks || allTasks.isEmpty()) {
			return r;
		}
		List<Task> counted = new ArrayList<>();
		for (Task t : allTasks) {
			Double est = t.getNbrEstimateHours();
			if (null != est && est > 0) {
				counted.add(t);
			}
		}
		r.setTotalTasks(allTasks.size());
		r.setCountedTasks(counted.size());
		if (counted.isEmpty()) {
			return r;
		}

		double total = counted.stream().mapToDouble(Task::getNbrEstimateHours).sum();
		r.setTotalHours(total);

		LocalDate start = earliestStart(counted);
		LocalDate end = latestEnd(counted, start);
		long days = ChronoUnit.DAYS.between(start, end);

		for (long i = 0; i <= days; i++) {
			LocalDate day = start.plusDays(i);
			r.getLabels().add(day.toString());
			r.getIdeal().add(round(total * (days - i) / (double) days));
			r.getActual().add(round(remainingAt(counted, day, end)));
		}
		return r;
	}

	/**
	 * 某一天的实际剩余量：全部未完工任务的剩余工时之和。
	 * <p>
	 * 已到期（计划结束日 ≤ 当天）的按完成率折算；未到期的全额计入——
	 * 漏掉未到期部分，曲线就会从 0 起步往上翘，与理想线反向。
	 *
	 * @param counted  参与统计的任务（预计工时 &gt; 0）
	 * @param day      当天
	 * @param axisEnd  时间轴末端，结束日期缺失的任务按这天到期
	 */
	public double remainingAt(List<Task> counted, LocalDate day, LocalDate axisEnd) {
		double remaining = 0d;
		for (Task t : counted) {
			LocalDate due = dueDate(t, axisEnd);
			if (due.isAfter(day)) {
				remaining += t.getNbrEstimateHours();
				continue;
			}
			Double rem = t.getRemainingHours();
			remaining += (null == rem) ? t.getNbrEstimateHours() : rem;
		}
		return remaining;
	}

	/** 任务到期日：有 DT_END 用之；缺失的按轴末端算，杜绝 now() 把轴拉长 */
	private static LocalDate dueDate(Task t, LocalDate axisEnd) {
		return (null == t.getDtEnd()) ? axisEnd : toLocalDate(t.getDtEnd());
	}

	private LocalDate earliestStart(List<Task> counted) {
		LocalDate min = null;
		for (Task t : counted) {
			LocalDate d = (null == t.getDtStart()) ? toLocalDate(t.getDtCreate()) : toLocalDate(t.getDtStart());
			if (null == min || d.isBefore(min)) {
				min = d;
			}
		}
		return null == min ? LocalDate.now() : min;
	}

	/**
	 * 轴末端 = 填了结束日期的任务里的最晚结束日；都没填（或最晚结束日不晚于起点）时
	 * 补到 start+1，否则 days=0 会让理想线出现 0/0 = NaN。
	 * 没填结束日期的任务不参与求最大值——它们的到期日统一按轴末端算（见 dueDate）。
	 */
	private LocalDate latestEnd(List<Task> counted, LocalDate start) {
		LocalDate max = null;
		for (Task t : counted) {
			if (null == t.getDtEnd()) {
				continue;
			}
			LocalDate d = toLocalDate(t.getDtEnd());
			if (null == max || d.isAfter(max)) {
				max = d;
			}
		}
		if (null == max || !max.isAfter(start)) {
			max = start.plusDays(1);
		}
		return max;
	}

	private static LocalDate toLocalDate(Date d) {
		if (null == d) {
			return LocalDate.now();
		}
		// 走 Util.dateToInstant：MyBatis 对不同来源的日期可能给 java.sql.Date，裸 toInstant() 会炸
		return Util.dateToInstant(d).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private static double round(double v) {
		return Math.round(v * 100.0) / 100.0;
	}
}
