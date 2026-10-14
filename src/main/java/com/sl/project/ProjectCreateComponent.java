package com.sl.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Project;
import com.sl.entity.ProjectUser;
import com.sl.entity.User;
import com.sl.service.ProjectProcess;
import com.sl.util.Util;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

/**
 * 新增/修改项目（分组表单重设计）。
 * <p>
 * 原来是 SplitLayout 左表单右用户表格，比例僵、字段平铺没有层次。
 * 现在按成熟 PM 系统的习惯分三块卡片：基本信息（两列表单）→
 * 项目备注（富文本整行）→ 选择成员（带姓名搜索的多选表格），
 * 底部固定主操作栏。保存逻辑与原实现完全一致。
 */
@SpringComponent
@Scope("prototype")
public class ProjectCreateComponent extends VerticalLayout {

	private static final Logger log = LoggerFactory.getLogger(ProjectCreateComponent.class);

	private static final long serialVersionUID = -6688666917976642733L;
	private TextField pname;
	private ComboBox<String> prior;
	private TextField pctComplete;
	private DatePicker startDatepicker;
	private DatePicker endDatepicker;
	private RichTextEditor remark;
	private Button saveBtn;
	private Grid<User> userGrid;
	private TextField userFilter;
	private boolean updateFlag = false;
	private Project project;
	private Span titleSpan;
	ProjectProcess projectProcess;

	private ComboBox<String> pstatus;

	private Checkbox checkbox;

	public ProjectCreateComponent(@Autowired ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
		addClassName("list-view");
		setDefaultHorizontalComponentAlignment(Alignment.START);
		setMargin(false);
		initMainLayout();
	}

	/**
	 * 布局：标题 + 三个分组卡片 + 底部操作栏
	 */
	private void initMainLayout() {
		titleSpan = new Span("新增项目");
		titleSpan.addClassName("page-title");
		add(titleSpan, ComFactory.getHorizontalLine("95%"));

		add(buildBasicSection());
		add(buildRemarkSection());
		add(buildMemberSection());

		// 底部操作栏：主按钮右对齐，符合「内容在上、动作在下」的表单习惯
		saveBtn = ComFactory.getPrimaryBtn("保存");
		HorizontalLayout actionBar = new HorizontalLayout(saveBtn);
		actionBar.setWidthFull();
		actionBar.setJustifyContentMode(JustifyContentMode.END);
		actionBar.setPadding(false);
		add(actionBar);

		initContent();
		registerHandler();
		if (updateFlag) {
			populateCurrentProject();
		}
	}

	/** 分组卡片外壳 */
	private VerticalLayout section(String title) {
		Span t = new Span(title);
		t.addClassName("form-section-title");
		VerticalLayout section = new VerticalLayout(t);
		section.setPadding(false);
		section.setSpacing(false);
		section.setWidthFull();
		section.addClassName("form-section");
		return section;
	}

	/** 基本信息：两列表单，相关字段相邻摆放 */
	private VerticalLayout buildBasicSection() {
		VerticalLayout section = section("基本信息");
		FormLayout form = ComFactory.getFormLayout();
		pname = ComFactory.getStandardTextField("项目名称：");
		ComFactory.bindRequired(pname, "请填写项目名称");
		pstatus = ComFactory.getStandardComboBox("状态：");
		pstatus.setItems("未开始", "进行中", "暂停", "已完成");
		prior = ComFactory.getStandardComboBox("优先级：");
		prior.setItems("高", "中", "低");
		prior.setValue("低");
		pctComplete = ComFactory.getStandardTextField("完成比例（%）：");
		pctComplete.setValue("");
		pctComplete.setPlaceholder("0 ~ 100");
		ComFactory.bindNumberRange(pctComplete, 0, 100, "完成比例只能输入 0 到 100 之间的数字");
		startDatepicker = ComFactory.getDateField("开始时间", LocalDate.now());
		endDatepicker = ComFactory.getDateField("结束时间", LocalDate.now().plusMonths(1));
		checkbox = ComFactory.getCheckbox("私有项目（仅项目成员可见）");
		form.add(pname, pstatus, prior, pctComplete, startDatepicker, endDatepicker, checkbox);
		section.add(form);
		return section;
	}

	/** 项目备注：富文本整行 */
	private VerticalLayout buildRemarkSection() {
		VerticalLayout section = section("项目备注");
		remark = ComFactory.getRichTextArea("98%", "260px");
		section.add(remark);
		return section;
	}

	/** 选择成员：说明 + 姓名搜索 + 多选表格 */
	private VerticalLayout buildMemberSection() {
		VerticalLayout section = section("选择成员");
		Span hint = new Span("勾选要加入项目的用户（可多选），保存后自动建立关联");
		hint.addClassName("form-hint");
		userFilter = ComFactory.getTextField();
		userFilter.setPlaceholder("按用户名筛选");
		userFilter.setClearButtonVisible(true);
		userFilter.setWidth("240px");
		userGrid = getUserGrid();
		// 输入即在内存中过滤，机构人数不多，不必走后端
		userFilter.addValueChangeListener(e -> {
			String kw = null == e.getValue() ? "" : e.getValue().trim();
			List<User> all = projectProcess.getuserListByIdInstitution(ComponentUtil.getCurrentUser().getIdInstitution());
			if (kw.isEmpty()) {
				userGrid.setItems(all);
			} else {
				userGrid.setItems(all.stream()
						.filter(u -> (null != u.getUserName() && u.getUserName().contains(kw))
								|| (null != u.getUserId() && u.getUserId().contains(kw)))
						.collect(Collectors.toList()));
			}
		});
		HorizontalLayout filterBar = new HorizontalLayout(userFilter);
		filterBar.setPadding(false);
		section.add(hint, filterBar, userGrid);
		return section;
	}

	private Grid<User> getUserGrid() {
		userGrid = ComFactory.getGrid(User.class);
		userGrid.setHeight("320px");
		userGrid.setSelectionMode(SelectionMode.MULTI);
		userGrid.addColumn(User::getUserName).setHeader("用户名");
		userGrid.addColumn(User::getOrganization).setHeader("机构");
		userGrid.addColumn(User::getEmail).setHeader("邮箱");
		return userGrid;
	}

	public void initContent() {
		List<User> userList = projectProcess.getuserListByIdInstitution(ComponentUtil.getCurrentUser().getIdInstitution());
		userGrid.setItems(userList);
	}

	public void registerHandler() {
		saveBtn.addClickListener(e -> {
			// 字段级校验已经在输入时给出提示，这里只做保存前的兜底拦截
			if (!ComFactory.validateFields(pname, pctComplete)) {
				Notification.show("请先修正表单中标红的项", 3000, Position.MIDDLE);
				return;
			}

			if (updateFlag) {//更新逻辑
				project.setNameProject(pname.getValue());
				project.setCdPrior(prior.getValue().equals("") ? "" : prior.getValue());
				project.setCdComment(remark.asHtml().getValue());
				project.setCdStatus(pstatus.getValue());
				if (checkbox.getValue()) {
					project.setFlagPrivate("Y");
				} else {
					project.setFlagPrivate("N");
				}
				if (null != pctComplete.getValue() && !pctComplete.getValue().equals("")) {
					try {
						project.setPctComplete(Double.parseDouble(pctComplete.getValue().equals("") ? "0.0" : pctComplete.getValue()));
					} catch (NumberFormatException e2) {
						log.error(ExceptionUtils.getStackTrace(e2));
						Notification.show("完成比例只能是数字", 3000, Position.MIDDLE);
						return;
					}
				}
				if (startDatepicker.getOptionalValue().isPresent()) {
					project.setDtStart(DateUtil.parseDate(startDatepicker.getValue().toString()));
				}
				if (endDatepicker.getOptionalValue().isPresent()) {
					project.setDtEnd(DateUtil.parseDate(endDatepicker.getValue().toString()));
				}
				projectProcess.updateProject(project);
				Set<User> selectedItems = userGrid.getSelectedItems();
				if (null != selectedItems && !selectedItems.isEmpty()) {
					List<ProjectUser> list = new ArrayList<ProjectUser>();
					for (User user : selectedItems) {
						list.add(new ProjectUser(project.getIdProject(), user.getUserId()));
					}
					projectProcess.saveProjectUserList(list);
				}
				Notification.show("修改成功", 3000, Position.MIDDLE);
				//将内容重置，updateflag = false;
				resetContent();
			} else {//新增逻辑
				Project p = new Project();
				p.setIdProject(IdUtil.getSnowflakeNextIdStr());
				p.setIdInstitution(ComponentUtil.getCurrentInstitution());
				p.setCdCreateUser(ComponentUtil.getCurrentUser().getUserId());
				p.setNameProject(pname.getValue());
				p.setDtCreate(new Date());
				if (startDatepicker.getOptionalValue().isPresent()) {
					p.setDtStart(DateUtil.parseDate(startDatepicker.getValue().toString()));
				}
				if (endDatepicker.getOptionalValue().isPresent()) {
					p.setDtEnd(DateUtil.parseDate(endDatepicker.getValue().toString()));
				}
				p.setCdPrior(prior.getValue().equals("") ? "" : prior.getValue());
				p.setCdStatus(pstatus.getValue());
				p.setCdComment(remark.asHtml().getValue());
				p.setFlagDelete("N");
				if (checkbox.getValue()) {
					p.setFlagPrivate("Y");
				} else {
					p.setFlagPrivate("N");
				}
				if (null != pctComplete.getValue() && !pctComplete.getValue().equals("")) {
					try {
						p.setPctComplete(Double.parseDouble(pctComplete.getValue().equals("") ? "0.0" : pctComplete.getValue()));
					} catch (NumberFormatException e1) {
						log.error(ExceptionUtils.getStackTrace(e1));
						Notification.show("完成比例只能是数字", 3000, Position.MIDDLE);
						return;
					}
				}
				if (projectProcess.checkProjectExists(p)) {
					Notification.show("项目名称重复，请重新填写", 3000, Position.MIDDLE);
					return;
				}
				projectProcess.saveProjectWithCreateUser(p);
				Set<User> selectedItems = userGrid.getSelectedItems();
				if (null != selectedItems && !selectedItems.isEmpty()) {
					for (User user : selectedItems) {
						projectProcess.saveProjectUser(new ProjectUser(p.getIdProject(), user.getUserId()));
					}
				}
				Notification.show("保存成功", 3000, Position.MIDDLE);
				//将内容重置，updateflag = false;
				resetContent();
			}
		});
	}

	protected void resetContent() {
		updateFlag = false;
		TabsUtil.closeCurrentTab();
	}

	/**
	 * 修改项目进入页面还原内容
	 */
	public void populateCurrentProject() {
		titleSpan.setText(updateFlag ? "修改项目" : "新增项目");
		pname.setValue(project.getNameProject());
		prior.setValue(project.getCdPrior() == null ? "" : project.getCdPrior());
		pctComplete.setValue(project.getPctComplete() == null ? "" : project.getPctComplete() + "");
		remark.asHtml().setValue(project.getCdComment() == null ? "" : project.getCdComment());
		pstatus.setValue(project.getCdStatus() == null ? "" : project.getCdStatus());
		String flag = project.getFlagPrivate() == null ? "N" : project.getFlagPrivate();
		if (flag.equals("N")) {
			checkbox.setValue(false);
		} else {
			checkbox.setValue(true);
		}
		if (null != project.getDtStart()) {
			startDatepicker.setValue(Util.dateToLocalDate(project.getDtStart()));
		}
		if (null != project.getDtEnd()) {
			endDatepicker.setValue(Util.dateToLocalDate(project.getDtEnd()));
		}
		//还原之前选择的用户信息
		List<User> userList = projectProcess.getuserListByIdInstitution(ComponentUtil.getCurrentUser().getIdInstitution());
		List<ProjectUser> selectedItems = projectProcess.getProjectUserListByIdProject(project.getIdProject());
		List<String> selectedIds = selectedItems.stream().map(u -> u.getIdUser()).collect(Collectors.toList());
		ComponentUtil.populateGridContentWithSelectItems(userGrid, userList, selectedIds);
	}

	public boolean isUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
		populateCurrentProject();
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
