package com.sl.project;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.TabsUtil;
import com.sl.entity.Project;
import com.sl.service.ProjectProcess;
import com.sl.user.UserManagementEnums;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

import cn.hutool.core.date.DateUtil;

/**
 * 项目回收站
 * @author Administrator
 *
 */
@SpringComponent
@Scope("prototype")
public class ProjectRecycleBin extends VerticalLayout{

	private static final long serialVersionUID = -1902552338733320540L;

	private static final Logger log = LoggerFactory.getLogger(ProjectRecycleBin.class);

	ProjectProcess projectProcess;
	private TextField pname;
	private Button searchBtn;
	private GridPro<Project> gridPro;

	private ConfirmDialog dialog;

	private Project selectedProject;
	
	public ProjectRecycleBin(@Autowired ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
		addClassName("list-view");
        setDefaultHorizontalComponentAlignment(Alignment.START); 
        setMargin(false);

        HorizontalLayout tile = ComFactory.getTitle("已删除项目");
        Span horizontalLine = ComFactory.getHorizontalLine();
        add(tile,horizontalLine);
        //筛选栏
       initFilterLayout();
        
        gridPro = ComFactory.getGridPro(Project.class, false);
		gridPro.setHeight("700px");
        initGridContent(gridPro,projectProcess);
        add(gridPro);
	}

	private void initFilterLayout() {
		 HorizontalLayout filterLayout = ComFactory.getResponsiveHorizontalLayoutLeft();
		 add(filterLayout);
		 filterLayout.add(ComFactory.getLabel("项目名称："));
		 pname = ComFactory.getTextField();
		 filterLayout.add(pname);
		 
		 searchBtn = ComFactory.getPrimaryBtn("查询");
		 filterLayout.add(searchBtn);
		 searchBtn.addClickListener(e ->{
			 if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
				 ComFactory.notificationError("当前账号无权查看已删除项目");
				 return;
			 }
			 // 原实现构造了 Project 条件对象却根本没用上，点了查询等于无条件刷新
			 List<Project> deleted = projectProcess.getDeletedProjectList();
			 String keyword = pname.getValue();
			 if (null != keyword && !keyword.trim().isEmpty()) {
				 String kw = keyword.trim();
				 deleted = deleted.stream()
						 .filter(x -> null != x.getNameProject() && x.getNameProject().contains(kw))
						 .collect(Collectors.toList());
			 }
			 gridPro.setItems(deleted);
		 });
	}

	private void initGridContent(GridPro<Project> gridPro, ProjectProcess projectProcess) {
		 if (!SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
			 // 原实现非管理员进来是彻底的空白页，连列都没有，这里给出明确提示
			 add(ComFactory.getLabel("当前账号无权查看已删除项目"));
			 return;
		 }
		 List<Project> projectList = projectProcess.getDeletedProjectList();
//		List<Project> projectList = projectProcess.getCurrentUserProjects();
//		gridPro.setEditOnClick(true);//设置可编辑
		 gridPro.addColumn(Project::getNameProject).setHeader("项目名称").setResizable(true);
		 gridPro.addColumn(v ->{	return DateUtil.formatDate(v.getDtStart());}).setHeader("项目开始时间").setResizable(true);//格式转换
		 gridPro.addColumn(Project::getCdStatus).setHeader("项目状态").setResizable(true);
		 gridPro.addColumn(Project::getCdCreateUser).setHeader("创建人").setResizable(true);
		 gridPro.addColumn(Project::getCdPrior).setHeader("优先级").setResizable(true);
		 gridPro.addComponentColumn(p ->{
			 Button b = ComFactory.getPrimaryBtn("修改/查看");
			 b.addClickListener(e -> {
				 ProjectCreateComponent projectCreateComponent = ComponentUtil.applicationContext.getBean(ProjectCreateComponent.class);
				 projectCreateComponent.setProject(p);
				 projectCreateComponent.setUpdateFlag(true);
				 TabsUtil.addComponentTab("修改/查看项目-" + p.getIdProject(), "修改/查看项目", projectCreateComponent);
			 });
			 return b;
		 }).setHeader("修改/查看");
		 // 恢复确认框只创建一次、所有行共用。
		 // 原实现放在 addComponentColumn 的 lambda 里，每渲染一行就 new 一个 ConfirmDialog。
		 dialog = ComFactory.getConfirmationDialog("确定要恢复该项目吗？", conf ->{
			 Project target = this.selectedProject;
			 if (null == target) {
				 return;
			 }
			 int rows = projectProcess.recoverProjectLogic(target);
			 if (rows > 0) {
				 // 只刷新列表。原实现在这里调用 TabsUtil.closeCurrentTab()，
				 // 关掉的是当前选中的标签页，往往并不是回收站本身。
				 gridPro.setItems(projectProcess.getDeletedProjectList());
				 ComFactory.notificationSuccess("恢复成功");
			 } else {
				 ComFactory.notificationError("恢复失败，请重试");
			 }
		 },cancel ->{
			 dialog.close();
		 });
		 gridPro.addComponentColumn(p ->{
			 Button b = ComFactory.getPrimaryBtn("恢复");
			 b.addClickListener(e -> {
				 this.selectedProject = p;
				 dialog.open();
			 });
			 return b;
		 }).setHeader("恢复项目");
		 // 彻底删除是危险操作：确认文案带项目名和任务数（见 purgeRow），每次点击单独弹框
		 gridPro.addComponentColumn(p ->{
			 Button b = ComFactory.getPrimaryBtn("彻底删除");
			 b.addThemeVariants(ButtonVariant.LUMO_ERROR);
			 b.addClickListener(e -> purgeRow(p));
			 return b;
		 }).setHeader("彻底删除");
		 gridPro.setItems(projectList);
	}

	/**
	 * 彻底删除某个项目：文案带项目名 + 项目下任务数，确认后连项目带全部相关数据一次删光。
	 * ConfirmDialog 文案必须按行动态生成，所以这里每次点击都新建一个（低频危险操作，开销可忽略）。
	 */
	private void purgeRow(Project p) {
		long taskCount = projectProcess.countTasksOfProject(p.getIdProject());
		ComFactory.getConfirmationDialog(
				"⚠️ 彻底删除「" + p.getNameProject() + "」？\n将同时删除其下 " + taskCount
						+ " 个任务及评论、附件、子任务、依赖、里程碑等全部相关数据，且不可恢复！",
				conf -> {
					try {
						projectProcess.deleteProjectCompletely(p);
						ComFactory.notificationSuccess("项目已彻底删除");
						gridPro.setItems(projectProcess.getDeletedProjectList());
					} catch (Exception ex) {
						log.error("彻底删除项目失败，idProject={}", p.getIdProject(), ex);
						ComFactory.notificationError("删除失败，请重试");
					}
				}, cancel -> {
					// 取消时 ConfirmDialog 自动关闭，无需处理
				}).open();
	}

	public ProjectProcess getProjectProcess() {
		return projectProcess;
	}

	public void setProjectProcess(ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
	}

	
	
}
