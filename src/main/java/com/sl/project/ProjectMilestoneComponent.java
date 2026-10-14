package com.sl.project;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.editor.QuillEditor;
import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.entity.Project;
import com.sl.entity.ProjectMilestone;
import com.sl.service.MilestoneService;
import com.sl.util.Util;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 项目里程碑（T2-6）：列表 + 新增/编辑/删除 + 顶部横向时间轴。
 */
@SpringComponent
@Scope("prototype")
public class ProjectMilestoneComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ProjectMilestoneComponent.class);

	private final MilestoneService milestoneService;

	private Grid<ProjectMilestone> grid;
	private HorizontalLayout timeline;
	private Span emptyHint;

	public ProjectMilestoneComponent(@Autowired MilestoneService milestoneService) {
		this.milestoneService = milestoneService;
		setWidthFull();
		setPadding(true);
		add(ComFactory.getTitle("项目里程碑"));
		add(ComFactory.getHorizontalLine());

		timeline = new HorizontalLayout();
		timeline.setWidthFull();
		timeline.addClassName("milestone-timeline");
		add(timeline);

		HorizontalLayout toolbar = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(toolbar);
		Button addBtn = ComFactory.getPrimaryBtn("新增里程碑", VaadinIcon.PLUS);
		addBtn.addClickListener(e -> openEditDialog(new ProjectMilestone()));
		Button refreshBtn = ComFactory.getPrimaryBtn("刷新", VaadinIcon.REFRESH);
		refreshBtn.addClickListener(e -> render());
		toolbar.add(addBtn, refreshBtn);

		grid = ComFactory.getGrid(ProjectMilestone.class);
		grid.setHeight("420px");
		grid.addColumn(ProjectMilestone::getNameMilestone).setHeader("里程碑名称");
		grid.addColumn(m -> null == m.getDtMilestone() ? "" : Util.formatDate(m.getDtMilestone()))
				.setHeader("日期").setSortable(true);
		grid.addComponentColumn(m -> {
			Span badge = "已达成".equals(m.getCdStatus())
					? ComFactory.getConfirmBadge("已达成")
					: ComFactory.getGrayBadge("未达成");
			return badge;
		}).setHeader("状态");
		grid.addColumn(m -> QuillEditor.htmlToPlainText(m.getCdDescription())).setHeader("说明");
		grid.addComponentColumn(m -> {
			HorizontalLayout ops = new HorizontalLayout();
			ops.setSpacing(false);
			ops.setPadding(false);
			ops.getStyle().set("flex-wrap", "nowrap");
			Button edit = ComFactory.getTertriayBtn("编辑", VaadinIcon.EDIT);
			edit.addClickListener(e -> openEditDialog(m));
			Button del = ComFactory.getTertriayBtn("删除", VaadinIcon.TRASH);
			del.addClickListener(e -> confirmDelete(m));
			ops.add(edit, del);
			return ops;
		}).setHeader("操作").setAutoWidth(true).setFlexGrow(0);
		add(grid);

		emptyHint = ComFactory.getEmptyHint("当前项目还没有里程碑，点「新增里程碑」添加第一个");
		add(emptyHint);
		render();
	}

	@Override
	public void refresh() {
		render();
	}

	private void render() {
		Project p = ComponentUtil.getCurrentProject();
		if (null == p) {
			ComFactory.notification("请先选择一个项目");
			return;
		}
		List<ProjectMilestone> list;
		try {
			list = milestoneService.listByProject(p.getIdProject());
		} catch (Exception e) {
			log.error("加载里程碑失败，project={}", p.getIdProject(), e);
			ComFactory.notificationError("加载里程碑失败", e);
			return;
		}
		grid.setItems(list);
		ComFactory.toggleEmptyHint(emptyHint, list.size());
		renderTimeline(list);
	}

	/** 顶部横向时间轴：按日期顺序排，已达成绿色、临近橙色、逾期红色 */
	private void renderTimeline(List<ProjectMilestone> list) {
		timeline.removeAll();
		if (list.isEmpty()) {
			return;
		}
		LocalDate today = LocalDate.now();
		for (ProjectMilestone m : list) {
			Span node = new Span(m.getNameMilestone() + "（" + Util.formatDate(m.getDtMilestone()) + "）");
			node.addClassName("milestone-node");
			if ("已达成".equals(m.getCdStatus())) {
				node.addClassName("milestone-node-done");
			} else if (null != m.getDtMilestone()
					&& Util.dateToLocalDate(m.getDtMilestone()).isBefore(today)) {
				node.addClassName("milestone-node-overdue");
			} else if (null != m.getDtMilestone()
					&& !Util.dateToLocalDate(m.getDtMilestone()).isAfter(today.plusDays(7))) {
				node.addClassName("milestone-node-soon");
			} else {
				node.addClassName("milestone-node-normal");
			}
			timeline.add(node);
		}
	}

	private void openEditDialog(ProjectMilestone m) {
		Project p = ComponentUtil.getCurrentProject();
		if (null == p) {
			ComFactory.notification("请先选择一个项目");
			return;
		}
		FormLayout form = new FormLayout();
		TextField name = ComFactory.getStandardTextField("里程碑名称：");
		ComFactory.bindRequired(name, "请填写里程碑名称");
		name.setValue(null == m.getNameMilestone() ? "" : m.getNameMilestone());
		DatePicker date = ComFactory.getDatePicker("里程碑日期：");
		ComFactory.bindRequired(date, date, "请选择里程碑日期");
		if (null != m.getDtMilestone()) {
			date.setValue(Util.dateToLocalDate(m.getDtMilestone()));
		} else {
			date.setValue(LocalDate.now());
		}
		ComboBox<String> status = ComFactory.getCombobox("状态：");
		status.setItems(ProjectMilestone.STATUS_UNDONE, ProjectMilestone.STATUS_DONE);
		status.setValue(null == m.getCdStatus() ? ProjectMilestone.STATUS_UNDONE : m.getCdStatus());
		// 说明用通用富文本编辑器（Quill 2），占满表单一行
		QuillEditor desc = new QuillEditor()
				.withValue(null == m.getCdDescription() ? "" : m.getCdDescription())
				.withPlaceholder("请输入里程碑说明，支持富文本")
				.withHeight("200px");
		desc.setWidth("100%");
		Span descLabel = new Span("说明：");
		VerticalLayout descWrap = new VerticalLayout(descLabel, desc);
		descWrap.setPadding(false);
		descWrap.setSpacing(false);
		descWrap.setWidthFull();
		form.add(name, date, status, descWrap);
		form.setColspan(descWrap, 2);

		Button save = ComFactory.getPrimaryBtn("保存");
		Button cancel = ComFactory.getPrimaryBtn("取消");
		Dialog dialog = ComFactory.getDialog("里程碑", form, save, cancel);
		dialog.setWidth("760px");
		cancel.addClickListener(e -> dialog.close());
		save.addClickListener(e -> {
			if (!ComFactory.validateFields(name, date)) {
				ComFactory.notificationError("请先修正表单中标红的项");
				return;
			}
			m.setIdProject(p.getIdProject());
			m.setNameMilestone(name.getValue());
			m.setDtMilestone(Util.locaDateToDate(date.getValue()));
			m.setCdStatus(status.getValue());
			// 纯文本为空时存 null，避免库里落一堆 <p><br></p>
			m.setCdDescription(desc.isEmpty() ? null : desc.getValue());
			milestoneService.save(m);
			ComFactory.notificationSuccess("保存成功");
			dialog.close();
			render();
		});
		dialog.open();
	}

	private void confirmDelete(ProjectMilestone m) {
		ConfirmDialogHolder.confirm("确认删除里程碑「" + m.getNameMilestone() + "」？", () -> {
			milestoneService.delete(m.getIdMilestone());
			ComFactory.notificationSuccess("已删除");
			render();
		});
	}

	/** 简单的确认框封装，避免每次都 new 一个 ConfirmDialog 出来 */
	static class ConfirmDialogHolder {
		static void confirm(String text, Runnable onOk) {
			com.vaadin.flow.component.confirmdialog.ConfirmDialog dialog =
					new com.vaadin.flow.component.confirmdialog.ConfirmDialog();
			dialog.setHeader("提示");
			dialog.setText(text);
			dialog.setConfirmText("确认");
			dialog.setCancelable(true);
			dialog.setCancelButton("取消", e -> dialog.close());
			dialog.setConfirmButton("确认", e -> {
				onOk.run();
				dialog.close();
			});
			dialog.open();
		}
	}
}
