package com.sl.project;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Project;
import com.sl.entity.User;
import com.sl.service.ProjectProcess;
import com.sl.user.UserManagementEnums;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;
import com.sl.util.Util;

import java.time.LocalDate;

@SpringComponent
@Scope("prototype")
public class MyProject extends VerticalLayout{

	private static final long serialVersionUID = -9006226841577552848L;
	private static final Logger log = LoggerFactory.getLogger(MyProject.class);

	ProjectProcess projectProcess;
	private TextField pname;
	private ComboBox<String> prior;
	private Button searchBtn;
	private GridPro<Project> gridPro;
	private ConfirmDialog dialog;
	private Project selectedProject;
	/** 拷贝项目弹窗：先让用户改名称、时间、状态等信息，再入库 */
	private Dialog copyDialog;
	private TextField copyName;
	private DatePicker copyStart;
	private DatePicker copyEnd;
	private ComboBox<String> copyStatus;
	private ComboBox<String> copyPrior;
	private Checkbox copyPrivate;
	private Project copySource;
	/** 当前筛选条件，懒加载 DataProvider 每次取数都会用到 */
	private Project searchCondition = new Project();
	/** 列表空态提示 */
	private Span emptyHint;
	
	public MyProject(@Autowired ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
		addClassName("list-view");
        setDefaultHorizontalComponentAlignment(Alignment.START); 
        setMargin(false);
        
        HorizontalLayout tile = ComFactory.getTitle("全部项目");
        Span horizontalLine = ComFactory.getHorizontalLine();
        add(tile,horizontalLine);
        //筛选栏
       initFilterLayout();
        
        gridPro = ComFactory.getGridPro(Project.class, false);
        gridPro.setHeight("600px");
        // 懒加载：滚动到哪加载到哪，不再一次性把全部项目查出来塞进 Grid
        gridPro.setPageSize(PAGE_SIZE);
        emptyHint = ComFactory.getEmptyHint("暂无项目，可先创建一个项目，或联系管理员把你加入项目");
        initGridContent(gridPro,projectProcess);
        add(emptyHint);
        add(gridPro);
	}

	/** 每页行数，同时也是懒加载每次向后端取的数量 */
	private static final int PAGE_SIZE = 20;

	private void initFilterLayout() {
		 HorizontalLayout filterLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		 add(filterLayout);
		 filterLayout.add(ComFactory.getLabel("项目名称："));
		 pname = ComFactory.getTextField();
		 filterLayout.add(pname);
		 filterLayout.add(ComFactory.getLabel("优先级："));
		 prior = ComFactory.getCombobox();
		 prior.setItems("高","中","低","全部");
		 prior.setValue("全部");
		 filterLayout.add(prior);
		 
		 searchBtn = ComFactory.getPrimaryBtn("查询");
		 filterLayout.add(searchBtn);
		 searchBtn.addClickListener(e ->{
			 callConditionSearch();
		 });
	}

	private void callConditionSearch() {
		Project p = new Project();
		 if (pname.getValue() != null && !pname.getValue().equals("")) {
			 p.setNameProject(pname.getValue());
		}
		 if (prior.getValue() != null) {
			p.setCdPrior(prior.getValue());
		}
		 this.searchCondition = p;
		 applyDataProvider();
	}

	private void initGridContent(GridPro<Project> gridPro, ProjectProcess projectProcess) {
		// 不再在构造时一次性查出全部项目，改由 applyDataProvider 懒加载
		dialog = ComFactory.getConfirmationDialog(conf ->{
			// 服务端会校验「管理员或创建人」，不通过时抛 IllegalArgumentException
			try {
				projectProcess.deleteProjectLogic(selectedProject);
				callConditionSearch();
				ComFactory.notificationSuccess("删除成功");
			} catch (Exception ex) {
				log.error("删除项目失败, idProject={}", selectedProject.getIdProject(), ex);
				ComFactory.notificationError(null != ex.getMessage() ? ex.getMessage() : "删除失败，请重试");
			} finally {
				dialog.close();
			}
		}, canc ->{
			dialog.close();
		});
//		List<Project> projectList = projectProcess.getCurrentUserProjects();
//		gridPro.setEditOnClick(true);//设置可编辑
		gridPro.addColumn(Project::getNameProject).setHeader("项目名称").setResizable(true);
		gridPro.addColumn(v ->{	return DateUtil.formatDate(v.getDtStart());}).setHeader("项目开始时间").setResizable(true);//格式转换
//		gridPro.addEditColumn(Project::getCdStatus).text(Project::setCdStatus).setHeader("项目状态").setResizable(true);
		gridPro.addColumn(Project::getCdStatus).setHeader("项目状态").setResizable(true);
		gridPro.addColumn(Project::getCdCreateUser).setHeader("创建人").setResizable(true);
//		gridPro.addEditColumn(Project::getCdPrior).text(Project::setCdPrior).setHeader("优先级").setResizable(true);
		gridPro.addColumn(Project::getCdPrior).setHeader("优先级").setResizable(true);
		gridPro.addComponentColumn(p ->{
			HorizontalLayout ops = new HorizontalLayout();
			ops.setSpacing(true);
			ops.setPadding(false);
			ops.setDefaultVerticalComponentAlignment(Alignment.CENTER);
			// 单元格内排成一行， tertiary 紧凑按钮宽度小，不允许折行
			ops.getStyle().set("flex-wrap", "nowrap");
			Button updateBtn = ComFactory.getTertriayBtn("修改/查看");
			updateBtn.addClickListener(e -> {
				ProjectCreateComponent projectCreateComponent = ComponentUtil.applicationContext.getBean(ProjectCreateComponent.class);
				projectCreateComponent.setProject(p);
				projectCreateComponent.setUpdateFlag(true);
				// key 带上项目 id：不同项目各开一个标签页，同一项目重复点击只激活不新增
				TabsUtil.addComponentTab("修改/查看项目-" + p.getIdProject(), "修改/查看项目", projectCreateComponent);
			});
			Button copyBtn = ComFactory.getTertriayBtn("拷贝");
			copyBtn.addClickListener(e -> {
				openCopyDialog(p);
			});
			Button deleteBtn = ComFactory.getTertriayBtn("删除");
			deleteBtn.addClickListener(e -> {
				this.selectedProject = p;
				dialog.open();
			});
			deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
			// 与服务端口径一致：管理员或项目创建人本人才显示删除按钮
			if (canDelete(p)) {
				ops.add(updateBtn, copyBtn, deleteBtn);
			} else {
				ops.add(updateBtn, copyBtn);
			}
			return ops;
		}).setHeader("操作").setResizable(true).setAutoWidth(true).setFlexGrow(0);
		applyDataProvider();
	}

	/**
	 * 删除权限：管理员，或项目创建人本人（服务端 checkProjectDeletePermission 同口径兜底）。
	 */
	private boolean canDelete(Project p) {
		User u = ComponentUtil.getCurrentUser();
		String me = (null == u) ? null : u.getUserId();
		if (null == me) {
			return false;
		}
		try {
			if (SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return me.equals(p.getCdCreateUser());
	}

	/**
	 * 拷贝项目弹窗：预填原项目信息，用户确认名称、起止时间、状态等后再入库。
	 */
	private void openCopyDialog(Project source) {
		if (null == copyDialog) {
			initCopyDialog();
		}
		this.copySource = source;
		copyName.setValue(source.getNameProject() + "-拷贝");
		copyStart.setValue(null == source.getDtStart() ? LocalDate.now() : Util.dateToLocalDate(source.getDtStart()));
		copyEnd.setValue(null == source.getDtEnd() ? LocalDate.now().plusMonths(1) : Util.dateToLocalDate(source.getDtEnd()));
		copyStatus.setValue(null == source.getCdStatus() || source.getCdStatus().isBlank() ? "未开始" : source.getCdStatus());
		copyPrior.setValue(null == source.getCdPrior() || source.getCdPrior().isBlank() ? "低" : source.getCdPrior());
		copyPrivate.setValue("Y".equals(source.getFlagPrivate()));
		copyDialog.open();
	}

	private void initCopyDialog() {
		VerticalLayout form = ComFactory.getVerticalLayout();
		form.setWidth("100%");
		copyName = ComFactory.getStandardTextField("项目名称：","410px");
		ComFactory.bindRequired(copyName, "请填写项目名称");
		copyStart = ComFactory.getDateField("开始时间", LocalDate.now(),"410px");
		copyEnd = ComFactory.getDateField("结束时间", LocalDate.now().plusMonths(1),"410px");
		copyStatus = ComFactory.getStandardComboBox("状态：","410px");
		copyStatus.setItems("未开始", "进行中", "暂停", "已完成");
		copyPrior = ComFactory.getStandardComboBox("优先级：","410px");
		copyPrior.setItems("高", "中", "低");
		copyPrivate = ComFactory.getCheckbox("是否私有");
		form.add(copyName, copyStart, copyEnd, copyStatus, copyPrior, copyPrivate);

		Button saveBtn = ComFactory.getPrimaryBtn("确认拷贝");
		Button cancelBtn = ComFactory.getTertriayBtn("取消");
		copyDialog = ComFactory.getDialog("拷贝项目", form, saveBtn, cancelBtn);
		copyDialog.setWidth("500px");
		saveBtn.addClickListener(e -> {
			if (!ComFactory.validateFields(copyName)) {
				ComFactory.notificationError("请先修正表单中标红的项");
				return;
			}
			Project custom = new Project();
			custom.setNameProject(copyName.getValue().trim());
			if (copyStart.getOptionalValue().isPresent()) {
				custom.setDtStart(DateUtil.parseDate(copyStart.getValue().toString()));
			}
			if (copyEnd.getOptionalValue().isPresent()) {
				custom.setDtEnd(DateUtil.parseDate(copyEnd.getValue().toString()));
			}
			custom.setCdStatus(copyStatus.getValue());
			custom.setCdPrior(copyPrior.getValue());
			custom.setFlagPrivate(copyPrivate.getValue() ? "Y" : "N");
			if (projectProcess.cloneProject(copySource, custom)) {
				copyDialog.close();
				applyDataProvider();
				ComFactory.notification("拷贝成功");
			} else {
				ComFactory.notificationError("拷贝失败，请重试");
			}
		});
		cancelBtn.addClickListener(e -> copyDialog.close());
	}

	/**
	 * 用懒加载 DataProvider 装载数据：首屏只查一页，往下滚动再取下一页。
	 * 原先是 getProjectByCondition 全量查出来再 setItems，项目多了首屏会明显变慢。
	 */
	private void applyDataProvider() {
		boolean admin = SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER);
		Project cond = this.searchCondition;
		CallbackDataProvider<Project, Void> dataProvider = DataProvider.fromCallbacks(
				query -> projectProcess.getProjectByConditionPaged(cond, admin, query.getOffset(), query.getLimit()).stream(),
				query -> (int) projectProcess.countProjectByCondition(cond, admin));
		gridPro.setItems(dataProvider);
		ComFactory.toggleEmptyHint(emptyHint, projectProcess.countProjectByCondition(cond, admin));
	}

	public ProjectProcess getProjectProcess() {
		return projectProcess;
	}

	public void setProjectProcess(ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
	}

}
