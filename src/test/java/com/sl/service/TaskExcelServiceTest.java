package com.sl.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import cn.idev.excel.EasyExcel;
import com.sl.bo.TaskExcelRow;
import com.sl.entity.Task;

/**
 * Excel 导入导出（T2-9 / T3-4）。
 * <p>
 * 走真实的 EasyExcel 读写，只把 Service 依赖 mock 掉 ——
 * 这类测试的价值在于验证「导出的数据能被导回来」，也就是模板字段没有对不上，
 * 而不是验证 EasyExcel 本身。
 */
class TaskExcelServiceTest {

	private TaskExcelService svc;

	@BeforeEach
	void setUp() {
		svc = new TaskExcelService();
		TaskProcess taskProcess = mock(TaskProcess.class);
		when(taskProcess.getTaskUserIdsByIdTask(anyString())).thenReturn(Collections.emptyList());
		ReflectionTestUtils.setField(svc, "taskProcess", taskProcess);
	}

	private static Task task(String name, String status, String desc) {
		Task t = new Task();
		t.setIdTask("T-" + name);
		t.setNameTask(name);
		t.setCdStatus(status);
		t.setCdTaskGroup("默认分组");
		t.setCdTaskType("需求");
		t.setCdTag("默认标签");
		t.setCdPrior("中");
		t.setPctComplete(30d);
		t.setNbrEstimateHours(8d);
		t.setNbrActualHours(2d);
		t.setFlagPrivate("N");
		t.setCdDescription(desc);
		t.setDtStart(java.sql.Timestamp.valueOf("2026-10-01 00:00:00"));
		t.setDtEnd(java.sql.Timestamp.valueOf("2026-10-08 00:00:00"));
		return t;
	}

	@Test
	void exportedWorkbookCanBeReadBack() {
		List<Task> tasks = Arrays.asList(
				task("登录页改版", "进行中", "<p>把 <b>验证码</b> 挪到下面</p>"),
				task("导入功能", "未开始", null));

		byte[] bytes = svc.exportTasks(tasks);
		assertTrue(bytes.length > 0, "导出内容不应为空");

		List<TaskExcelRow> read = new ArrayList<>();
		EasyExcel.read(new ByteArrayInputStream(bytes), TaskExcelRow.class,
				new cn.idev.excel.read.listener.PageReadListener<TaskExcelRow>(read::addAll))
				.sheet().doRead();

		assertEquals(2, read.size(), "导出两行就该读回两行");
		TaskExcelRow r0 = read.get(0);
		assertEquals("登录页改版", r0.getNameTask());
		assertEquals("进行中", r0.getCdStatus());
		assertEquals("2026-10-01", r0.getDtStart());
		assertEquals("2026-10-08", r0.getDtEnd());
		assertEquals("8.0", r0.getEstimateHours());

		// 富文本描述导出时必须剥掉标签，否则单元格里是一坨 HTML
		assertFalse(r0.getCdDescription().contains("<"), "描述里不该残留 HTML 标签");
		assertTrue(r0.getCdDescription().contains("验证码"), "剥标签不该把正文也剥掉");
	}

	@Test
	void emptyTaskListProducesEmptyWorkbook() {
		byte[] bytes = svc.exportTasks(Collections.emptyList());
		assertTrue(bytes.length > 0, "空列表也要产出一个合法 xlsx，否则前端下载到损坏文件");

		List<TaskExcelRow> read = new ArrayList<>();
		EasyExcel.read(new ByteArrayInputStream(bytes), TaskExcelRow.class,
				new cn.idev.excel.read.listener.PageReadListener<TaskExcelRow>(read::addAll))
				.sheet().doRead();
		assertTrue(read.isEmpty());
	}

	@Test
	void unreadableStreamIsReportedAsErrorNotException() {
		TaskExcelService.ImportResult r = svc.importTasks(
				new ByteArrayInputStream("这不是一个 Excel 文件".getBytes()), "P1");
		assertTrue(r.hasError(), "垃圾输入应转成错误提示，不能抛异常");
		assertEquals(0, r.success);
	}
}
