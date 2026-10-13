package com.sl.task;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.editor.QuillEditor;
import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.ProjectGroup;
import com.sl.entity.ProjectTag;
import com.sl.entity.ProjectTaskFile;
import com.sl.entity.ProjectType;
import com.sl.entity.ProjectUser;
import com.sl.entity.Task;
import com.sl.entity.User;
import com.sl.service.ProjectProcess;
import com.sl.service.TaskProcess;
import com.sl.service.UserManageMentProcess;
import com.sl.util.Constants;
import com.sl.util.Util;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

/**
 * 任务的创建和更新，注意更新需要把任务ID或任务对象设置进来
 * @author Administrator
 *
 */
@SpringComponent
@Scope("prototype")
public class TaskUpdateComponent extends VerticalLayout{

	private static final Logger log = LoggerFactory.getLogger(TaskUpdateComponent.class);

	private static final long serialVersionUID = 5166621585268664577L;
	private TextField taskName;
	private ComboBox<String> prior;
	private TextField pctComplete;
	private QuillEditor remark;
	private Button saveBtn;
	private TaskProcess taskProcess;
	private ProjectProcess projectProcess;
	private UserManageMentProcess manageMentProcess;
	private ComboBox<String> taskGroup;
	private ComboBox<String> tagBox;
	private ComboBox<String> typeBox;
	private ComboBox<String> statusBox;
	private DatePicker dateStart;
	private DatePicker dateEnd;
	private TextField estimateHours;
	private TextField actualHours;
	private MultiSelectComboBox<User> userBox;
	private Button groupBtn;
	private Button typeBtn;
	private Button tagBtn;
	private Checkbox privateBox;
	private List<ProjectTag> projectTagList;
	private List<ProjectGroup> projectGroupList;
	private List<ProjectType> projectTypeList;
	private String parentPath;
	private Task task;
	private String idTaskNew;//如果是新建任务就直接创建一个作业ID
//	private TaskMgmtComponent taskMgmtComponent;

	private HorizontalLayout contentLayout;
	
	public TaskUpdateComponent(@Autowired TaskProcess taskProcess,@Autowired ProjectProcess projectProcess,@Autowired UserManageMentProcess userManageMentProcess) {
		this.manageMentProcess = userManageMentProcess;
		this.projectProcess = projectProcess;
		this.taskProcess = taskProcess;
		setWidthFull();
		setDefaultHorizontalComponentAlignment(Alignment.START);
		if (null == task) {//update
			add(ComFactory.getTitle("新建任务"));
			idTaskNew = IdUtil.getSnowflakeNextIdStr();
		}else {
			add(ComFactory.getTitle("修改任务"));
		}
		add(ComFactory.getHorizontalLine());
		contentLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		add(contentLayout);
		initLayoutDetail();
		initContent();
		registerHandler();
	}

	private void initLayoutDetail() {
		FormLayout form = new FormLayout();
		form.setWidth("30%");
		form.setResponsiveSteps( new ResponsiveStep("0", 1));
		contentLayout.add(form);
		taskName = ComFactory.getStandardTextField("任务名称：");
		ComFactory.bindRequired(taskName, "请填写任务名称");
		taskGroup = ComFactory.getStandardComboBox("任务分组：");//增加
		typeBox = ComFactory.getStandardComboBox("任务类型：");//增加
		tagBox = ComFactory.getStandardComboBox("任务标签：");//增加
		statusBox = ComFactory.getStandardComboBox("任务状态：");
		statusBox.setItems(Constants.STATUS_LIST);
		statusBox.setValue("未开始");
		prior = ComFactory.getStandardComboBox("优先级：");
		prior.setItems("高","中","低");
		prior.setValue("低");
		
//		starBox = ComFactory.getStandardComboBox("任务星标：");//增加
//		starBox.setItems("1","2","3","4","5");
		pctComplete = ComFactory.getStandardTextField("完成比例：");
		pctComplete.setPlaceholder("请输入0 ~ 100之间的数");
		pctComplete.setValue("0");
		ComFactory.bindNumberRange(pctComplete, 0, 100, "完成比例只能输入 0 到 100 之间的数字");
		
		dateStart = ComFactory.getDatePicker("计划开始日期：");
		dateEnd = ComFactory.getDatePicker("结束日期：");
		ComFactory.bindRequired(dateEnd, dateEnd, "请选择任务结束日期");

		// 工时：燃尽图的基准线与实际线。空值合法（老任务没填），填了就必须是非负数
		estimateHours = ComFactory.getStandardTextField("预计工时：");
		estimateHours.setPlaceholder("单位：小时，可不填");
		ComFactory.bindNumberRange(estimateHours, 0, 100000, "预计工时必须是不小于 0 的数字");

		actualHours = ComFactory.getStandardTextField("实际工时：");
		actualHours.setPlaceholder("单位：小时，可不填");
		ComFactory.bindNumberRange(actualHours, 0, 100000, "实际工时必须是不小于 0 的数字");
		//****************************第二部分********************************
//		FormLayout form2 = new FormLayout();
//		form2.setWidth("30%");
//		form2.setResponsiveSteps( new ResponsiveStep("0", 1));
//		contentLayout.add(form2);
		userBox = ComFactory.getComboboxMultiSelect("执行人：",User.class);
		userBox.setItemLabelGenerator(u -> u.getUserName());
		form.add(taskName,taskGroup,typeBox,tagBox,statusBox,prior,pctComplete,
				dateStart,dateEnd,estimateHours,actualHours,userBox);
//		form2.add(prior,pctComplete,dateEnd,userBox);
		Upload upload = null;
		if (null != task) {//update
			parentPath = ComponentUtil.getCurrentUserFilePath()+File.separator+task.getIdTask();
			upload = ComFactory.getUpload(parentPath, f -> uploadFile(task.getIdTask(), f));
		}else {//新增任务
			parentPath = ComponentUtil.getCurrentUserFilePath()+File.separator+idTaskNew;
			upload = ComFactory.getUpload(parentPath, f -> uploadFile(idTaskNew, f));
		}
		//第三部分
		VerticalLayout layoutLeft = ComFactory.getVerticalLayout();
		layoutLeft.setWidth("1000px");
		contentLayout.add(layoutLeft);
		remark = new QuillEditor().withToolbar("full").withHeight("440px");//通用 Quill 富文本编辑器
		remark.setWidth("97%");
		Span label = ComFactory.getLabel("任务描述:");
		
		HorizontalLayout horizontalLayoutLeft = ComFactory.getResponsiveHorizontalLayoutLeft();
		groupBtn = ComFactory.getPrimaryBtn("新建分组");
		typeBtn = ComFactory.getPrimaryBtn("新建类型");
		tagBtn = ComFactory.getPrimaryBtn("新建标签");
		privateBox = ComFactory.getCheckbox("是否私有：");
		privateBox.setTooltipText("私有仅自己可见，非私有则项目组都可见");
		horizontalLayoutLeft.add(groupBtn,typeBtn,tagBtn,privateBox,upload);
		saveBtn = ComFactory.getPrimaryBtn("保存");
		layoutLeft.add(label,remark,horizontalLayoutLeft,saveBtn);
		
	}

	/**
	 * 上传成功后把文件记录写库。f 为已保存到 parentPath 下的文件（含客户端原始文件名）。
	 */
	private void uploadFile(String idTask, File f) {
		ProjectTaskFile pfile = new ProjectTaskFile();
		pfile.setCdFilePath(parentPath);
		pfile.setDtUpload(new Date());
		pfile.setIdFile(IdUtil.getSnowflakeNextIdStr());
		pfile.setNameFile(f.getName());
		pfile.setIdTask(idTask);
		pfile.setIdProject(ComponentUtil.getCurrentProject().getIdProject());
		projectProcess.saveFile(pfile);
		ComFactory.notification("上传文件成功");
	}

	public void initContent() {
		projectTagList = projectProcess.getProjectTagsList(ComponentUtil.getCurrentProject().getIdProject());
		projectGroupList = projectProcess.getProjectGroupList(ComponentUtil.getCurrentProject().getIdProject());
		projectTypeList = projectProcess.getProjectTypeList(ComponentUtil.getCurrentProject().getIdProject());
		populateContentBox();
		selectBoxContent();
		initUserbox();
	}
	
	private void initUserbox() {
		List<ProjectUser> projectUserListByIdProject = projectProcess.getProjectUserListByIdProject(ComponentUtil.getCurrentProject().getIdProject());
		Set<String> userIds = projectUserListByIdProject.stream().map(e ->e.getIdUser()).collect(Collectors.toSet());
		List<User> userByIds = manageMentProcess.getUserByIds(userIds);
		userBox.setItems(userByIds);
		if (null != task) {//修改
			List<String> idsByIdTask = taskProcess.getTaskUserIdsByIdTask(task.getIdTask());
			for (User user : userByIds) {
				if (idsByIdTask.contains(user.getUserId())) {
					userBox.select(user);
				}
			}
		}
	}
	private void populateContentBox() {
		List<String> collect = projectTagList.stream().map(e ->e.getCdTag()).collect(Collectors.toList());
		if (null != collect) {
			tagBox.setItems(collect);
		}
		List<String> collect2 = projectGroupList.stream().map(e ->e.getCdGroup()).collect(Collectors.toList());
		if (null != collect2) {
			taskGroup.setItems(collect2);
		}
		List<String> collect3 = projectTypeList.stream().map(e ->e.getCdType()).collect(Collectors.toList());
		if (null != collect3) {
			typeBox.setItems(collect3);
		}
	}
	
	private void selectBoxContent() {
		if (null != task) {
			taskName.setValue(task.getNameTask());
			if (null != task.getCdStatus()) {
				statusBox.setValue(task.getCdStatus());
			}
			if (null != task.getCdPrior()) {
				prior.setValue(task.getCdPrior());
			}
			if (null != task.getPctComplete()) {
				pctComplete.setValue(task.getPctComplete()+"");
			}
			if (null != task.getCdDescription()) {
				remark.setValue(task.getCdDescription());
			}
			dateEnd.setValue(Util.dateToLocalDate(task.getDtEnd()));
			if (null != task.getDtStart()) {
				dateStart.setValue(Util.dateToLocalDate(task.getDtStart()));
			}
			if (null != task.getNbrEstimateHours()) {
				estimateHours.setValue(String.valueOf(task.getNbrEstimateHours()));
			}
			if (null != task.getNbrActualHours()) {
				actualHours.setValue(String.valueOf(task.getNbrActualHours()));
			}
			tagBox.setValue(task.getCdTag());
			taskGroup.setValue(task.getCdTaskGroup());
			typeBox.setValue(task.getCdTaskType());
			privateBox.setValue("N".equals(task.getFlagPrivate()));
		}
	}

	public void registerHandler() {
		saveBtn.addClickListener(e ->{
			// 字段级校验已在输入时给出提示，这里只做保存前的兜底拦截
			if (!ComFactory.validateFields(taskName, pctComplete, dateEnd, estimateHours, actualHours)) {
				ComFactory.notificationError("请先修正表单中标红的项");
				return;
			}
			if (typeBox.getValue() == null || typeBox.getValue().equals("")) {
				ComFactory.notification("请填写任务类型，若没有可以先新建类型后填写");
				return;
			}
			if (null != task) {//update
				if(updateTask()) {
					ComFactory.notification("保存成功");
					// 让已打开的列表页（我的任务/看板/甘特图等）立即反映修改结果
					TabsUtil.refreshRefreshableTabs();
					TabsUtil.closeCurrentTab();
				}else {
					ComFactory.notification("保存失败！");
				}
			}else {
				saveTask();
				ComFactory.notification("保存成功");
				// 新增完成后立刻刷新已打开视图，我的任务等列表马上能看到新任务
				TabsUtil.refreshRefreshableTabs();
				TabsUtil.closeCurrentTab();
			}
		});
		groupBtn.addClickListener(e ->{
			popUpWindow("任务分组", "分组名称");
		});
		typeBtn.addClickListener(e ->{
			popUpWindow("任务类型", "类型名称");
			
		});
		tagBtn.addClickListener(e ->{
			popUpWindow("任务标签", "标签名称");
			
		});
	}
	
	public void popUpWindow(String title,String captionLb) {
		FormLayout absoluteLayout = ComFactory.getFormLayout();
		absoluteLayout.setHeight("175px");
//		Label label = ComFactory.getLabel(captionLb);
		TextField field = ComFactory.getTextField(captionLb);
		Button saveBtn = ComFactory.getPrimaryBtn("保存");
		Button btnCancel = ComFactory.getPrimaryBtn("取消");
		absoluteLayout.add(field);
		Dialog dialog = ComFactory.getDialog(title, absoluteLayout, saveBtn, btnCancel);
		dialog.setWidth("450px");
		dialog.setHeight("300px");
		btnCancel.addClickListener(e -> dialog.close());
		saveBtn.addClickListener(e ->{
			if (field.getValue() == null || field.getValue().toString().trim().equals("")) {
				ComFactory.notification("请填写内容后保存");
				return;
			}
			String idProject = ComponentUtil.getCurrentProject().getIdProject();
			if (title.contains("分组")) {
				ProjectGroup group = new ProjectGroup();
				group.setIdProject(idProject);
				group.setCdGroup(field.getValue());
				projectProcess.saveProjectGroup(group);
				projectGroupList.add(group);
			}
			if (title.contains("类型")) {
				ProjectType type = new ProjectType();
				type.setIdProject(idProject);
				type.setCdType(field.getValue());
				projectProcess.saveProjectType(type);
				projectTypeList.add(type);
			}
			if (title.contains("标签")) {
				ProjectTag tag = new ProjectTag();
				tag.setIdProject(idProject);
				tag.setCdTag(field.getValue());
				projectProcess.saveProjectTags(tag);
				projectTagList.add(tag);
			}
			populateContentBox();
			ComFactory.notification("保存成功");
			dialog.close();
		});
		dialog.setModal(true);
		dialog.open();
	}

	/**
	 * 把「计划开始日期 / 预计工时 / 实际工时」写进任务对象。
	 * 三个字段都允许为空（存量任务没填过），填了才写，避免把已有的值覆盖成 null。
	 */
	private void applyScheduleFields(Task t) {
		if (null != dateStart.getValue()) {
			t.setDtStart(Util.locaDateToDate(dateStart.getValue()));
		}
		t.setNbrEstimateHours(parseHours(estimateHours.getValue()));
		t.setNbrActualHours(parseHours(actualHours.getValue()));
	}

	private Double parseHours(String v) {
		if (null == v || v.isBlank()) {
			return null;
		}
		try {
			return Double.valueOf(v.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	protected boolean updateTask() {
		task.setIdProject(ComponentUtil.getCurrentProject().getIdProject());
		task.setNameTask(taskName.getValue());
		task.setCdTaskGroup(taskGroup.getValue() == null ?"默认分组":taskGroup.getValue());
		task.setCdTaskType(typeBox.getValue());
		task.setCdTag(tagBox.getValue() == null?"默认标签":tagBox.getValue());
		task.setCdStatus(statusBox.getValue());
		task.setCdPrior(prior.getValue());
		String value = pctComplete.getValue();
		if (value != null) {
			try {
				double parseDouble = Double.parseDouble(value);
				if (parseDouble<0 || parseDouble >100) {
					ComFactory.notification("完成比例只能输入0到100之间的数");
					return false;
				}
			} catch (NumberFormatException e) {
				ComFactory.notification("完成比例只能输入0到100之间的数");
			}
		}
		task.setPctComplete(Double.parseDouble(value));
		task.setCdDescription(remark.getValue() == null ? "" :remark.getValue());
		task.setDtCreate(new Date());
		task.setDtEnd(DateUtil.parseByPatterns(dateEnd.getValue().toString(), "yyyy-MM-dd").getTime());
		applyScheduleFields(task);
		Set<User> value2 = userBox.getValue();
		if (value2 != null && !value2.isEmpty()) {
			task.setIdExeUsers(value2);
		}
//		task.setIdExeUser(userBox.getValue() == null? null :userBox.getValue().getUserId());
		// FLAG_PRIVATE 约定：'Y'=公开，'N'=私有（同 saveTask）
		if (privateBox.getValue()) {
			task.setFlagPrivate("N");
		}else {
			task.setFlagPrivate("Y");
		}
		task.setIdUser(ComponentUtil.getCurrentUser().getUserId());
		taskProcess.updateTask(task);
		return true;
	}
	/**
	 * 如果是更新则要调用此方法对传入的task进行恢复展示
	 */
	public void recoverField() {
		if (null != task) {
			taskName.setValue(task.getNameTask());
			taskGroup.setValue(task.getCdTaskGroup());
			typeBox.setValue(task.getCdTaskType());
			tagBox.setValue(task.getCdTag());
			statusBox.setValue(task.getCdStatus());
			prior.setValue(task.getCdPrior());
			pctComplete.setValue(task.getPctComplete()+"");
			remark.setValue(task.getCdDescription());
			dateEnd.setValue(Util.dateToLocalDate(task.getDtEnd()));
			if (null != task.getDtStart()) {
				dateStart.setValue(Util.dateToLocalDate(task.getDtStart()));
			}
			if (null != task.getNbrEstimateHours()) {
				estimateHours.setValue(String.valueOf(task.getNbrEstimateHours()));
			}
			if (null != task.getNbrActualHours()) {
				actualHours.setValue(String.valueOf(task.getNbrActualHours()));
			}
			if ("N".equals(task.getFlagPrivate())) {
				privateBox.setValue(true);
			}else {
				privateBox.setValue(false);
			}
		}
	}
	/**
	 * 新增任务
	 * @return
	 */
	protected boolean saveTask() {
		Task t = new Task();
		t.setIdTask(IdUtil.getSnowflakeNextIdStr());
		t.setIdProject(ComponentUtil.getCurrentProject().getIdProject());
		t.setNameTask(taskName.getValue());
		t.setCdTaskGroup(taskGroup.getValue() == null ?"默认分组":taskGroup.getValue());
		t.setCdTaskType(typeBox.getValue());
		t.setCdTag(tagBox.getValue() == null?"默认标签":tagBox.getValue());
		t.setCdStatus(statusBox.getValue());
		t.setCdPrior(prior.getValue());
		String value = pctComplete.getValue();
		if (value != null) {
			try {
				double parseDouble = Double.parseDouble(value);
				if (parseDouble<0 || parseDouble >100) {
					ComFactory.notification("完成比例只能输入0到100之间的数");
					return false;
				}
			} catch (NumberFormatException e) {
				ComFactory.notification("完成比例只能输入0到100之间的数");
				return false;
			}
		}
		t.setPctComplete(Double.parseDouble(value));
		t.setCdDescription(remark.getValue() == null ? "" :remark.getValue());
		t.setDtCreate(new Date());
		t.setDtEnd(DateUtil.parseByPatterns(dateEnd.getValue().toString(), "yyyy-MM-dd").getTime());
		applyScheduleFields(t);
		Set<User> value2 = userBox.getValue();
		if (value2 != null && !value2.isEmpty()) {
			t.setIdExeUsers(value2);
		}
//		task.setIdExeUser(userBox.getValue() == null? null :userBox.getValue().getUserId());
		// FLAG_PRIVATE 约定：'Y'=公开（项目组可见），'N'=私有（仅自己可见）。
		// 所有任务列表查询都按 FLAG_PRIVATE='Y' 过滤，私有任务额外放行创建者本人。
		if (privateBox.getValue()) {
			t.setFlagPrivate("N");
		}else {
			t.setFlagPrivate("Y");
		}
		t.setIdUser(ComponentUtil.getCurrentUser().getUserId());
		taskProcess.saveTask(t);
		return true;
	}


//	public OutputStream receiveUpload(String filename, String mimeType) {
//		String keypath;
//		if (null != parentPath) {
//			FileUtil.mkdir(parentPath);
//			keypath = parentPath+ File.separator + filename;
//		}else {//如果没有设置文件存放目录则在当前程序运行目录下存放
//			String userdir = System.getProperty("user.dir");
//			keypath = userdir + File.separator + filename;
//		}
//		log.info("上传文件路径为："+keypath);
//		file = new File(keypath);
//		FileOutputStream fileStream = null;
//		try {
//			fileStream = new FileOutputStream(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return fileStream;
//	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

//	public void populateCurrentProject(Project project) {
//		pname.setValue(project.getNameProject());
//		prior.setValue(project.getNbrPrior() == null ? "" :project.getNbrPrior()+"");
//		pctComplete.setValue(project.getPctComplete() == null ? "" :project.getPctComplete()+"");
//		remark.setValue(project.getCdComment() == null ? "": project.getCdComment());
//	}

	
}
