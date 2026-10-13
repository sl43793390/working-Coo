package com.sl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sl.bo.BurnDownSeries;
import com.sl.entity.Task;

/**
 * 燃尽图计算（T3-4）。
 * <p>
 * 这里锁的是口径，不是实现细节：
 * 理想线必须线性递减到 0、没填工时的任务必须被排除、区间被压成 0 天时必须能算（不能 NaN）。
 */
class BurnDownCalculatorTest {

	private final BurnDownCalculator calc = new BurnDownCalculator();

	private static Date d(String ymd) {
		return Date.from(LocalDate.parse(ymd).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	private static Task task(String name, Double estimate, String start, String end, Double pctComplete) {
		Task t = new Task();
		t.setIdTask(name);
		t.setNameTask(name);
		t.setNbrEstimateHours(estimate);
		t.setDtStart(null == start ? null : d(start));
		t.setDtEnd(null == end ? null : d(end));
		if (null != pctComplete) {
			t.setPctComplete(pctComplete);
		}
		return t;
	}

	@Test
	void emptyInputYieldsEmptySeries() {
		assertTrue(calc.compute(Collections.emptyList()).isEmpty());
		assertTrue(calc.compute(null).isEmpty());
	}

	@Test
	void tasksWithoutEstimateAreExcludedButCounted() {
		List<Task> tasks = Arrays.asList(
				task("A", 10d, "2026-01-01", "2026-01-03", null),
				task("B", 0d, "2026-01-01", "2026-01-03", null),
				task("C", null, "2026-01-01", "2026-01-03", null));
		BurnDownSeries s = calc.compute(tasks);

		assertEquals(3, s.getTotalTasks());
		assertEquals(1, s.getCountedTasks(), "只有填了预计工时的才进统计");
		assertEquals(10.0, s.getTotalHours(), 0.001);
	}

	@Test
	void idealLineDecreasesLinearlyToZero() {
		List<Task> tasks = Collections.singletonList(task("A", 10d, "2026-01-01", "2026-01-03", null));
		BurnDownSeries s = calc.compute(tasks);

		// 1/1 ~ 1/3 共 2 天 → 3 个点
		assertEquals(3, s.getLabels().size());
		assertEquals("2026-01-01", s.getLabels().get(0));
		assertEquals("2026-01-03", s.getLabels().get(2));

		assertEquals(10.0, s.getIdeal().get(0).doubleValue(), 0.001, "第一天剩余全部");
		assertEquals(5.0, s.getIdeal().get(1).doubleValue(), 0.001, "中间点应为一半");
		assertEquals(0.0, s.getIdeal().get(2).doubleValue(), 0.001, "最后一天归零");
	}

	@Test
	void actualLineCountsUndueTasksAtFullEstimate() {
		// A：1/1 到期，完成 50% → 剩 5；B：1/3 到期，已完成 → 1/3 起归零
		List<Task> tasks = Arrays.asList(
				task("A", 10d, "2026-01-01", "2026-01-01", 0.5),
				task("B", 10d, "2026-01-01", "2026-01-03", 1.0));
		BurnDownSeries s = calc.compute(tasks);

		// 第一天：A 到期剩 5，B 未到期全额 10 → 15。
		// 未到期部分必须全额计入，否则曲线从 0 起步往上翘，与理想线反向
		assertEquals(15.0, s.getActual().get(0).doubleValue(), 0.001);
		// 第二天：仍是 15
		assertEquals(15.0, s.getActual().get(1).doubleValue(), 0.001);
		// 第三天：B 到期且已完成归零，曲线在这一步烧下去，只剩 A 的 5
		assertEquals(5.0, s.getActual().get(2).doubleValue(), 0.001);
	}

	@Test
	void taskWithoutEndDateBurnsAtAxisEndNotToday() {
		// C 没填结束日期：按轴末端（1/3）到期，不允许把时间轴拉到“今天”
		List<Task> tasks = Arrays.asList(
				task("A", 10d, "2026-01-01", "2026-01-03", 1.0),
				task("C", 10d, "2026-01-01", null, null));
		BurnDownSeries s = calc.compute(tasks);

		assertEquals(3, s.getLabels().size(), "轴不能被没填截止日期的任务拉长");
		assertEquals("2026-01-03", s.getLabels().get(2));
		// 1/1、1/2：A、C 都未到期，全额 20；1/3：A 已归零，C 到期剩 10
		assertEquals(20.0, s.getActual().get(0).doubleValue(), 0.001);
		assertEquals(20.0, s.getActual().get(1).doubleValue(), 0.001);
		assertEquals(10.0, s.getActual().get(2).doubleValue(), 0.001);
	}

	@Test
	void singleDayRangeDoesNotProduceNaN() {
		// 起止同一天：如果不做兜底，days=0 会让 ideal 变成 0/0 = NaN
		List<Task> tasks = Collections.singletonList(task("A", 8d, "2026-01-01", "2026-01-01", null));
		BurnDownSeries s = calc.compute(tasks);

		assertEquals(2, s.getLabels().size(), "区间被强制拉成 2 天");
		for (Number n : s.getIdeal()) {
			assertFalse(Double.isNaN(n.doubleValue()), "理想线出现 NaN");
		}
		for (Number n : s.getActual()) {
			assertFalse(Double.isNaN(n.doubleValue()), "实际线出现 NaN");
		}
	}
}
