package com.sl.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.read.listener.PageReadListener;
import com.sl.app.component.util.ComponentUtil;
import com.sl.bo.TaskExcelRow;
import com.sl.entity.Task;
import com.sl.entity.User;
import com.sl.util.Constants;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

/**
 * 任务 Excel 导入导出（T2-9）。
 * <p>
 * 导出：当前项目全量任务 -> xlsx，浏览器直接下载。
 * 导入：按模板逐行解析，**单行失败不影响其它行**，最后把错误行号 + 原因回显给用户。
 */
@Service
public class TaskExcelService {

	private static final Logger log = LoggerFactory.getLogger(TaskExcelService.class);

	@Autowired
	private TaskProcess taskProcess;
	@Autowired
	private ProjectProcess projectProcess;
	@Autowired
	private UserManageMentProcess userManageMentProcess;

	private static final String DATE_FMT = "yyyy-MM-dd";

	/**
	 * 导出成 xlsx 字节流。
	 */
	public byte[] exportTasks(List<Task> tasks) {
		List<TaskExcelRow> rows = new ArrayList<>();
		for (Task t : tasks) {
			TaskExcelRow r = new TaskExcelRow();
			r.setNameTask(t.getNameTask());
			r.setCdTaskGroup(t.getCdTaskGroup());
			r.setCdTaskType(t.getCdTaskType());
			r.setCdTag(t.getCdTag());
			r.setCdStatus(t.getCdStatus());
			r.setCdPrior(t.getCdPrior());
			r.setPctComplete(t.getPctComplete() == null ? "" : String.valueOf(t.getPctComplete()));
			r.setDtStart(fmtDate(t.getDtStart()));
			r.setDtEnd(fmtDate(t.getDtEnd()));
			r.setEstimateHours(t.getNbrEstimateHours() == null ? "" : String.valueOf(t.getNbrEstimateHours()));
			r.setActualHours(t.getNbrActualHours() == null ? "" : String.valueOf(t.getNbrActualHours()));
			// 执行人写 ID 串：导出-修改-导入这条链路里，姓名有重名风险，ID 是唯一键
			r.setExeUserIds(String.join(",", taskProcess.getTaskUserIdsByIdTask(t.getIdTask())));
			r.setFlagPrivate(t.getFlagPrivate());
			r.setCdDescription(plainText(t.getCdDescription()));
			rows.add(r);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		EasyExcel.write(out, TaskExcelRow.class).sheet("任务").doWrite(rows);
		return out.toByteArray();
	}

	/**
	 * 导入。返回导入结果（成功数 + 逐行错误）。
	 */
	public ImportResult importTasks(InputStream in, String idProject) {
		ImportResult result = new ImportResult();
		List<TaskExcelRow> rows = new ArrayList<>();
		try {
			EasyExcel.read(in, TaskExcelRow.class, new PageReadListener<TaskExcelRow>(rows::addAll)).sheet().doRead();
		} catch (Exception e) {
			log.error("解析 Excel 失败", e);
			result.addError(0, "文件解析失败：" + e.getMessage());
			return result;
		}
		if (rows.isEmpty()) {
			result.addError(0, "没有读到任何数据行");
			return result;
		}
		// 项目内已有的分组 / 类型 / 标签：导入时缺哪个就自动补哪个，避免大量「值不存在」错误
		Set<String> groups = new HashSet<>(projectProcess.getProjectGroupList(idProject).stream()
				.map(e -> e.getCdGroup()).toList());
		Set<String> types = new HashSet<>(projectProcess.getProjectTypeList(idProject).stream()
				.map(e -> e.getCdType()).toList());
		Set<String> tags = new HashSet<>(projectProcess.getProjectTagsList(idProject).stream()
				.map(e -> e.getCdTag()).toList());

		int line = 1;
		for (TaskExcelRow r : rows) {
			line++;
			try {
				String err = saveRow(r, idProject, groups, types, tags);
				if (null == err) {
					result.success++;
				} else {
					result.addError(line, err);
				}
			} catch (Exception e) {
				log.error("导入第 {} 行失败", line, e);
				result.addError(line, "写入失败：" + e.getMessage());
			}
		}
		return result;
	}

	private String saveRow(TaskExcelRow r, String idProject,
			Set<String> groups, Set<String> types, Set<String> tags) {
		if (null == r.getNameTask() || r.getNameTask().isBlank()) {
			return "任务名称为空";
		}
		Task t = new Task();
		t.setIdTask(IdUtil.getSnowflakeNextIdStr());
		t.setIdProject(idProject);
		t.setNameTask(r.getNameTask());

		String group = blankToDefault(r.getCdTaskGroup(), "默认分组");
		String type = blank(r.getCdTaskType());
		String tag = blankToDefault(r.getCdTag(), "默认标签");
		if (null == type) {
			return "任务类型为空（模板里必须填，没有请先在项目里新建类型）";
		}
		if (groups.add(group)) {
			com.sl.entity.ProjectGroup g = new com.sl.entity.ProjectGroup();
			g.setIdProject(idProject);
			g.setCdGroup(group);
			projectProcess.saveProjectGroup(g);
		}
		if (types.add(type)) {
			com.sl.entity.ProjectType pt = new com.sl.entity.ProjectType();
			pt.setIdProject(idProject);
			pt.setCdType(type);
			projectProcess.saveProjectType(pt);
		}
		if (tags.add(tag)) {
			com.sl.entity.ProjectTag ptag = new com.sl.entity.ProjectTag();
			ptag.setIdProject(idProject);
			ptag.setCdTag(tag);
			projectProcess.saveProjectTags(ptag);
		}
		t.setCdTaskGroup(group);
		t.setCdTaskType(type);
		t.setCdTag(tag);

		String status = blankToDefault(r.getCdStatus(), "未开始");
		if (!Constants.STATUS_LIST.contains(status)) {
			return "任务状态「" + status + "」不合法，可选值：" + String.join("/", Constants.STATUS_LIST);
		}
		t.setCdStatus(status);
		t.setCdPrior(blankToDefault(r.getCdPrior(), "低"));

		// 完成比例：空按 0，非数字直接报错
		String pct = blank(r.getPctComplete());
		if (null == pct) {
			t.setPctComplete(0d);
		} else {
			try {
				double d = Double.parseDouble(pct);
				if (d < 0 || d > 100) {
					return "完成比例必须在 0 ~ 100 之间";
				}
				t.setPctComplete(d);
			} catch (NumberFormatException e) {
				return "完成比例不是数字：" + pct;
			}
		}

		Date dtStart = parseDate(r.getDtStart(), false);
		Date dtEnd = parseDate(r.getDtEnd(), true);
		if (null == dtEnd) {
			return "结束日期为空或格式不对（应为 yyyy-MM-dd）";
		}
		t.setDtStart(dtStart);
		t.setDtEnd(dtEnd);

		t.setNbrEstimateHours(parseHours(r.getEstimateHours()));
		t.setNbrActualHours(parseHours(r.getActualHours()));

		String exeIds = blank(r.getExeUserIds());
		if (null != exeIds) {
			Set<User> users = new HashSet<>();
			for (String id : exeIds.split("[,，\\s]+")) {
				if (id.isBlank()) {
					continue;
				}
				User u = userManageMentProcess.selectUserById(id.trim());
				if (null == u) {
					return "执行人 ID 不存在：" + id;
				}
				users.add(u);
			}
			if (!users.isEmpty()) {
				t.setIdExeUsers(users);
			}
		}

		t.setFlagPrivate("Y".equalsIgnoreCase(blank(r.getFlagPrivate())) ? "Y" : "N");
		t.setCdDescription(r.getCdDescription());
		t.setDtCreate(new Date());
		t.setIdUser(ComponentUtil.getCurrentUser() == null ? null : ComponentUtil.getCurrentUser().getUserId());
		taskProcess.saveTask(t);
		return null;
	}

	private static String blank(String s) {
		if (null == s || s.isBlank()) {
			return null;
		}
		return s.trim();
	}

	private static String blankToDefault(String s, String def) {
		String v = blank(s);
		return null == v ? def : v;
	}

	private static Date parseDate(String s, boolean required) {
		String v = blank(s);
		if (null == v) {
			return required ? null : new Date();
		}
		try {
			return DateUtil.parse(v, DATE_FMT);
		} catch (Exception e) {
			// Excel 里日期经常被读成「2026-10-01 00:00:00」，补一个带时间的格式
			try {
				return DateUtil.parse(v, "yyyy-MM-dd HH:mm:ss");
			} catch (Exception e2) {
				return null;
			}
		}
	}

	private static Double parseHours(String s) {
		String v = blank(s);
		if (null == v) {
			return null;
		}
		try {
			return Double.valueOf(v);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static String fmtDate(Date d) {
		if (null == d) {
			return "";
		}
		return new SimpleDateFormat(DATE_FMT).format(d);
	}

	/**
	 * 富文本描述导出前剥掉标签，否则 Excel 单元格里会堆一坨 HTML。
	 */
	private static String plainText(String html) {
		if (null == html || html.isBlank()) {
			return "";
		}
		return html.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
	}

	/** 导入结果 */
	public static class ImportResult {
		public int success;
		public final List<String> errors = new ArrayList<>();

		public void addError(int line, String msg) {
			errors.add("第 " + line + " 行：" + msg);
		}

		public boolean hasError() {
			return !errors.isEmpty();
		}
	}
}
