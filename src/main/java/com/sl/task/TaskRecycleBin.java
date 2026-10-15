package com.sl.task;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.Project;
import com.sl.entity.Task;
import com.sl.service.ProjectProcess;
import com.sl.service.TaskProcess;
import com.sl.user.UserManagementEnums;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;

/**
 * 任务回收站（S1）。
 * <p>
 * 任务删除是软删除（FLAG_DELETE='Y'），在这里可以恢复或彻底删除；
 * 超过保留期（30 天）的任务由 {@link TaskRecycleScheduler} 每日自动物理清理。
 * 可见范围与项目回收站一致：仅全局管理员。
 */
@SpringComponent
@Scope("prototype")
public class TaskRecycleBin extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(TaskRecycleBin.class);

	private final TaskProcess taskProcess;
	private final ProjectProcess projectProcess;

	private Grid<Task> grid;
	private Task selectedTask;

	public TaskRecycleBin(@Autowired TaskProcess taskProcess,
			@Autowired ProjectProcess projectProcess) {
		this.taskProcess = taskProcess;
		this.projectProcess = projectProcess;
		addClassName("list-view");
		setMargin(false);

		add(ComFactory.getTitle("任务回收站"));
		add(ComFactory.getHorizontalLine());
		add(ComFactory.getPageTip("已删除任务保留 30 天，超期后系统自动物理清理，请及时恢复。"));

		grid = ComFactory.getGrid(Task.class);
		grid.setHeight("620px");
		grid.setSelectionMode(SelectionMode.NONE);
		grid.addColumn(Task::getNameTask).setHeader("任务名称").setResizable(true);
		grid.addColumn(t -> projectName(t.getIdProject())).setHeader("所属项目").setResizable(true);
		grid.addColumn(Task::getCdStatus).setHeader("状态").setWidth("90px").setResizable(true);
		grid.addColumn(Task::getIdUser).setHeader("创建人").setWidth("110px").setResizable(true);
		grid.addColumn(t -> null == t.getDtDelete() ? "" : DateUtil.formatDateTime(t.getDtDelete()))
				.setHeader("删除时间").setWidth("150px").setResizable(true);
		grid.addComponentColumn(t -> {
			Button recover = ComFactory.getPrimaryBtn("恢复");
			recover.addClickListener(e -> {
				selectedTask = t;
				recoverDialog.setText("确定恢复任务「" + t.getNameTask() + "」吗？恢复后重新出现在任务列表中。");
				recoverDialog.open();
			});
			Button purge = ComFactory.getPrimaryBtn("彻底删除");
			purge.addThemeVariants(ButtonVariant.LUMO_ERROR);
			purge.addClickListener(e -> {
				selectedTask = t;
				purgeDialog.setText("⚠️ 彻底删除任务「" + t.getNameTask() + "」？其子任务、评论、清单、执行人关联将一并删除，且不可恢复！");
				purgeDialog.open();
			});
			HorizontalLayout ops = new HorizontalLayout(recover, purge);
			ops.setSpacing(false);
			ops.setPadding(false);
			return ops;
		}).setHeader("操作").setWidth("200px").setFlexGrow(0);

		add(grid);
		add(ComFactory.getEmptyHint("回收站为空"));

		if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
			add(ComFactory.getLabel("当前账号无权查看任务回收站"));
			grid.setVisible(false);
			return;
		}
		reload();

		recoverDialog = ComFactory.getConfirmationDialog("", conf -> {
			Task t = selectedTask;
			if (null != t) {
				int rows = taskProcess.recoverTask(t);
				if (rows > 0) {
					ComFactory.notificationSuccess("恢复成功");
				} else {
					ComFactory.notificationError("恢复失败，请重试");
				}
			}
			reload();
		}, cancel -> recoverDialog.close());

		purgeDialog = ComFactory.getConfirmationDialog("", conf -> {
			Task t = selectedTask;
			if (null != t) {
				try {
					taskProcess.deleteTaskCompletely(t);
					ComFactory.notificationSuccess("已彻底删除");
				} catch (Exception ex) {
					log.error("彻底删除任务失败，idTask={}", t.getIdTask(), ex);
					ComFactory.notificationError("删除失败，请重试");
				}
			}
			reload();
		}, cancel -> purgeDialog.close());
	}

	private void reload() {
		String institution = ComponentUtil.getCurrentInstitution();
		List<Task> deleted = taskProcess.getDeletedTasks(institution);
		grid.setItems(deleted);
	}

	/** 项目 ID → 名称映射，一次查出避免每行回调单查 */
	private Map<String, String> projectNameMap;

	private String projectName(String idProject) {
		if (null == projectNameMap) {
			List<Project> projects = projectProcess.getProjectList();
			// 已删项目也在回收站语境下可能被引用，补一份全量（含软删）映射
			Map<String, String> all = projects.stream()
					.collect(Collectors.toMap(Project::getIdProject, Project::getNameProject, (a, b) -> a));
			projectProcess.getDeletedProjectList().forEach(p -> all.putIfAbsent(p.getIdProject(), p.getNameProject()));
			projectNameMap = all;
		}
		return projectNameMap.getOrDefault(idProject, idProject);
	}

	private ConfirmDialog recoverDialog;
	private ConfirmDialog purgeDialog;
}
