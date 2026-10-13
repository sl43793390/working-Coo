package com.sl.task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.Task;
import com.sl.service.ProjectRoleService;
import com.sl.service.TaskExcelService;
import com.sl.service.TaskExcelService.ImportResult;
import com.sl.service.TaskProcess;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.InputStreamDownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 任务 Excel 导入导出（T2-9）。
 * <p>
 * 导出：把当前项目的任务导成 xlsx，浏览器直接下载（走 StreamResource，不落服务器临时文件）。
 * 导入：上传 xlsx 后逐行校验写入，错误行号 + 原因直接列在页面上。
 */
@SpringComponent
@Scope("prototype")
public class TaskExcelComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(TaskExcelComponent.class);

	private final TaskExcelService excelService;
	private final TaskProcess taskProcess;
	private final ProjectRoleService projectRoleService;

	private VerticalLayout resultArea;

	public TaskExcelComponent(@Autowired TaskExcelService excelService,
			@Autowired TaskProcess taskProcess,
			@Autowired ProjectRoleService projectRoleService) {
		this.excelService = excelService;
		this.taskProcess = taskProcess;
		this.projectRoleService = projectRoleService;
		setWidthFull();
		setPadding(true);
		add(ComFactory.getTitle("任务导入导出"));
		add(ComFactory.getHorizontalLine());

		HorizontalLayout exportBar = ComFactory.getResponsiveHorizontalLayoutLeft();
		exportBar.setAlignItems(Alignment.CENTER);
		add(exportBar);
		Button exportBtn = ComFactory.getPrimaryBtn("导出当前项目任务", VaadinIcon.DOWNLOAD);
		exportBtn.addClickListener(e -> export());
		exportBar.add(exportBtn);
		Span exportTip = new Span("导出的文件可直接作为导入模板：改完再传回来即可批量建任务。");
		exportTip.addClassName("search-tip");
		add(exportTip);

		add(ComFactory.getHorizontalLine("100%"));

		HorizontalLayout importBar = ComFactory.getResponsiveHorizontalLayoutLeft();
		importBar.setAlignItems(Alignment.CENTER);
		add(importBar);
		// 新 API（24.8+）：内存接收，回调里直接解析 Excel；成功/失败都由回调路径决定
		UI ui = UI.getCurrent();
		Upload upload = new Upload(UploadHandler.inMemory((metadata, bytes) -> {
			// 回调可能不在 UI 线程，doImport 要操作页面组件，包一层 access
			ui.access(() -> doImport(new ByteArrayInputStream(bytes.clone()), metadata.fileName()));
		}));
		upload.setAcceptedFileTypes(".xlsx", ".xls");
		upload.setMaxFiles(1);
		upload.setAutoUpload(true);
		upload.addFileRejectedListener(e -> ComFactory.notificationError("只支持 .xlsx / .xls 文件"));
		importBar.add(ComFactory.getLabel("选择文件："), upload);

		resultArea = new VerticalLayout();
		resultArea.setPadding(false);
		resultArea.setWidthFull();
		add(resultArea);
	}

	private void export() {
		if (null == ComponentUtil.getCurrentProject()) {
			ComFactory.notification("请先选择一个项目");
			return;
		}
		String idProject = ComponentUtil.getCurrentProject().getIdProject();
		try {
			List<Task> tasks = taskProcess.getTasksWithSchedule(idProject);
			if (tasks.isEmpty()) {
				ComFactory.notification("当前项目还没有任务");
				return;
			}
			byte[] bytes = excelService.exportTasks(tasks);
			// 新 API：DownloadHandler 替代废弃的 StreamResource + Anchor(resource, text)
			String fileName = "tasks-" + idProject + ".xlsx";
			InputStreamDownloadHandler handler = new InputStreamDownloadHandler(event ->
					new DownloadResponse(new ByteArrayInputStream(bytes), fileName,
							"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", bytes.length));
			Anchor anchor = new Anchor(handler, "点击下载（" + tasks.size() + " 条任务）");
			anchor.addClassName("excel-download-link");
			resultArea.removeAll();
			resultArea.add(anchor);
			ComFactory.notificationSuccess("已生成导出文件");
		} catch (Exception e) {
			log.error("导出任务失败", e);
			ComFactory.notificationError("导出失败", e);
		}
	}

	private void doImport(InputStream in, String fileName) {
		if (null == ComponentUtil.getCurrentProject()) {
			ComFactory.notification("请先选择一个项目");
			return;
		}
		String idProject = ComponentUtil.getCurrentProject().getIdProject();
		if (!projectRoleService.canEdit(idProject, ComponentUtil.getCurrentUser().getUserId())) {
			ComFactory.notificationError("你在当前项目是只读访客，不能导入任务");
			return;
		}
		resultArea.removeAll();
		try (InputStream input = in) {
			ImportResult result = excelService.importTasks(input, idProject);
			Span summary = new Span("导入完成：" + result.success + " 条成功"
					+ (result.hasError() ? ("，" + result.errors.size() + " 条失败") : ""));
			summary.addClassName(result.hasError() ? "import-summary-warn" : "import-summary-ok");
			resultArea.add(summary);
			if (result.hasError()) {
				VerticalLayout errBox = new VerticalLayout();
				errBox.setPadding(false);
				errBox.addClassName("import-error-box");
				// 只展示前 50 行，避免几千行错误把页面撑爆
				int show = Math.min(result.errors.size(), 50);
				for (int i = 0; i < show; i++) {
					errBox.add(new Span(result.errors.get(i)));
				}
				if (result.errors.size() > show) {
					errBox.add(new Span("……其余 " + (result.errors.size() - show) + " 条错误已省略"));
				}
				resultArea.add(errBox);
			}
		} catch (Exception e) {
			log.error("导入任务失败，file={}", fileName, e);
			ComFactory.notificationError("导入失败", e);
		}
	}
}
