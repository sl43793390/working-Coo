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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

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
	private boolean updateFlag = false;
	private Project project;
	private String title;
	ProjectProcess projectProcess;
	private SplitLayout splitLayout;

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
	 * 布局
	 */
	private void initMainLayout() {
		title = "新增项目";
		HorizontalLayout title2 = ComFactory.getTitle(title);
		if (updateFlag) {
			title = "修改项目";
			title2 = ComFactory.getTitle(title);
		}
		Span horizontalLine = ComFactory.getHorizontalLine("95%");
	    add(title2,horizontalLine);
	    splitLayout = new SplitLayout();
	    splitLayout.setWidthFull();
	    splitLayout.setHeightFull();
		add(splitLayout);//拆分为左右两个布局
		initLeftLayout();
		
		initContent();
		registerHandler();
		if (updateFlag) {
			populateCurrentProject();
		}
	}

	private void initLeftLayout() {
		VerticalLayout leftLayout = ComFactory.getVerticalLayout();
		FormLayout form = ComFactory.getFormLayout();
		leftLayout.add(form);
		pname = ComFactory.getStandardTextField("项目名称：");
		// 输入即校验，不必等到点保存才弹提示
		ComFactory.bindRequired(pname, "请填写项目名称");
		startDatepicker = ComFactory.getDateField("开始时间", LocalDate.now());
		endDatepicker = ComFactory.getDateField("结束时间", LocalDate.now().plusMonths(1));
		prior = ComFactory.getStandardComboBox("优先级：");
		prior.setItems("高","中","低");
		prior.setValue("低");
		pctComplete = ComFactory.getStandardTextField("完成比例：");
		pctComplete.setValue("");
		pctComplete.setPlaceholder("0 ~ 100");
		ComFactory.bindNumberRange(pctComplete, 0, 100, "完成比例只能输入 0 到 100 之间的数字");
		pstatus = ComFactory.getStandardComboBox("状态：");
		pstatus.setItems("未开始","进行中","暂停","已完成");
		remark = ComFactory.getRichTextArea("98%","450px");
		checkbox = ComFactory.getCheckbox("是否私有");
		form.add(pname,startDatepicker,endDatepicker,prior,pctComplete,pstatus,checkbox);
		
		leftLayout.add(ComFactory.getLabel("备注："));
		leftLayout.add(remark);
		saveBtn = ComFactory.getPrimaryBtn("保存");
		splitLayout.addToPrimary(leftLayout);
		
		VerticalLayout verticalLayout = ComFactory.getVerticalLayout();
		splitLayout.addToSecondary(verticalLayout);
		verticalLayout.add(ComFactory.getLabel("选择用户"),getUserGrid(),saveBtn);
		splitLayout.setSplitterPosition(60);
	}

	private Grid<User> getUserGrid() {
		//右侧布局
		userGrid = ComFactory.getGrid(User.class);
		userGrid.setHeight("430px");
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
					Notification.show("请先修正表单中标红的项",3000,Position.MIDDLE);
					return;
				}
				
				if (updateFlag) {//更新逻辑
					project.setNameProject(pname.getValue());
					project.setCdPrior(prior.getValue().equals("") ? "" :prior.getValue());
					project.setCdComment(remark.asHtml().getValue());
					project.setCdStatus(pstatus.getValue());
					if (checkbox.getValue()) {
						project.setFlagPrivate("Y");
					}else {
						project.setFlagPrivate("N");
					}
					if (null != pctComplete.getValue() && !pctComplete.getValue().equals("")) {
						try {
								project.setPctComplete(Double.parseDouble(pctComplete.getValue().equals("")? "0.0":pctComplete.getValue()));
								
						} catch (NumberFormatException e2) {
							log.error(ExceptionUtils.getStackTrace(e2));
							Notification.show("完成比例只能是数字",3000,Position.MIDDLE);
							return;
						}
					}
					if (startDatepicker.getOptionalValue().isPresent()) {
						project.setDtStart(DateUtil.parseDate(startDatepicker.getValue().toString()));
					}
					if (endDatepicker.getOptionalValue().isPresent()) {
						project.setDtEnd(DateUtil.parseDate(endDatepicker.getValue().toString()));
					}
					int saveProject = projectProcess.updateProject(project);
					Set<User> selectedItems = userGrid.getSelectedItems();
					if (null != selectedItems && !selectedItems.isEmpty()) {
						List<ProjectUser> list = new ArrayList<ProjectUser>();
						for (User user : selectedItems) {
							list.add(new ProjectUser(project.getIdProject(), user.getUserId()));
						}
						projectProcess.saveProjectUserList(list);
					}
					Notification.show("修改成功",3000,Position.MIDDLE);
					//将内容重置，updateflag = false;
					resetContent();
				}else {//新增逻辑
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
					p.setCdPrior(prior.getValue().equals("") ? "" :prior.getValue());
					p.setCdComment(remark.asHtml().getValue());
					p.setFlagDelete("N");
					if (checkbox.getValue()) {
						p.setFlagPrivate("Y");
					}else {
						p.setFlagPrivate("N");
					}
					if (null != pctComplete.getValue() && !pctComplete.getValue().equals("")) {
						try {
							p.setPctComplete(Double.parseDouble(pctComplete.getValue().equals("")? "0.0":pctComplete.getValue()));
						} catch (NumberFormatException e1) {
							log.error(ExceptionUtils.getStackTrace(e1));
							Notification.show("完成比例只能是数字",3000,Position.MIDDLE);
							return;
						}
					}
					if(projectProcess.checkProjectExists(p)) {
						Notification.show("项目名称重复，请重新填写",3000,Position.MIDDLE);
						return;
					}
					int saveProject = projectProcess.saveProjectWithCreateUser(p);
					Set<User> selectedItems = userGrid.getSelectedItems();
					if (null != selectedItems && !selectedItems.isEmpty()) {
						for (User user : selectedItems) {
							projectProcess.saveProjectUser(new ProjectUser(p.getIdProject(), user.getUserId()));
						}
					}
					Notification.show("保存成功",3000,Position.MIDDLE);
					//将内容重置，updateflag = false;
					resetContent();
				}
				//TODO 保存成功后跳转到项目列表页面并刷新
		});
	}

	protected void resetContent() {
		updateFlag = false;
//		pname.setValue("");
//		prior.setValue("");
//		pctComplete.setValue("0");
//		remark.asHtml().setValue("");
//		initContent();
		TabsUtil.closeCurrentTab();
	}

	/**
	 * 修改项目进入页面还原内容
	 */
	public void populateCurrentProject() {
		pname.setValue(project.getNameProject());
		prior.setValue(project.getCdPrior() == null ? "" :project.getCdPrior());
		pctComplete.setValue(project.getPctComplete() == null ? "" :project.getPctComplete()+"");
		remark.asHtml().setValue(project.getCdComment() == null ? "": project.getCdComment());
		pstatus.setValue(project.getCdStatus() == null ? "" :project.getCdStatus());
		String flag = project.getFlagPrivate() == null ?"N" :project.getFlagPrivate();
		if (flag.equals("N")) {
			checkbox.setValue(false);
		}else {
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
