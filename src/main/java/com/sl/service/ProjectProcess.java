package com.sl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sl.aop.AuditLog;
import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.OperationLog;
import com.sl.entity.Project;
import com.sl.entity.ProjectExample;
import com.sl.entity.ProjectExample.Criteria;
import com.sl.entity.ProjectGroup;
import com.sl.entity.ProjectGroupExample;
import com.sl.entity.ProjectTag;
import com.sl.entity.ProjectTagExample;
import com.sl.entity.ProjectTaskFile;
import com.sl.entity.ProjectType;
import com.sl.entity.ProjectTypeExample;
import com.sl.entity.ProjectUser;
import com.sl.entity.ProjectUserExample;
import com.sl.entity.TaskExample;
import com.sl.entity.User;
import com.sl.entity.UserExample;
import com.sl.mapper.ProjectGroupMapper;
import com.sl.mapper.ProjectMapper;
import com.sl.mapper.ProjectTagMapper;
import com.sl.mapper.ProjectTaskFileMapper;
import com.sl.mapper.ProjectTypeMapper;
import com.sl.mapper.ProjectUserMapper;
import com.sl.mapper.TaskMapper;
import com.sl.mapper.TaskSubtaskMapper;
import com.sl.mapper.UserMapper;
import com.sl.user.UserManagementEnums;

import cn.hutool.core.util.IdUtil;

@Service
public class ProjectProcess {

	
	private static final Logger log = LoggerFactory.getLogger(ProjectProcess.class);

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private TaskSubtaskMapper subtaskMapper;
	@Autowired
	private ProjectUserMapper projectUserMapper;
	@Autowired
	private ProjectTaskFileMapper projectTaskFileMapper;
	@Autowired
	private ProjectTagMapper projectTagMapper;
	@Autowired
	private ProjectTypeMapper projectTypeMapper;
	@Autowired
	private ProjectGroupMapper projectGroupMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private com.sl.mapper.TaskUserMapper taskUserMapper;
	@Autowired
	private com.sl.mapper.TaskCommentMapper taskCommentMapper;
	@Autowired
	private com.sl.mapper.TaskBugfixMapper taskBugfixMapper;
	@Autowired
	private com.sl.mapper.TaskDependencyMapper taskDependencyMapper;
	@Autowired
	private com.sl.mapper.ProjectMilestoneMapper projectMilestoneMapper;
	@Autowired
	private com.sl.mapper.NotificationMapper notificationMapper;
	@Autowired
	private com.sl.mapper.TaskChecklistMapper taskChecklistMapper;
	
	/**
	 * 获取当前登录用户的机构创建的所有项目
	 * @return
	 */
	public List<Project> getProjectList(){
		ProjectExample example = new ProjectExample();
		example.createCriteria().andIdInstitutionEqualTo(ComponentUtil.getCurrentInstitution()).andFlagDeleteNotEqualTo("Y");
		List<Project> selectByExample = projectMapper.selectByExample(example);
//		List<Project> selectList = projectMapper.selectList(new QueryWrapper<Project>());
		return selectByExample;
	}
	/**
	 * 查询当前机构已经删除的项目
	 * @return
	 */
	public List<Project> getDeletedProjectList(){
		ProjectExample example = new ProjectExample();
		example.createCriteria().andIdInstitutionEqualTo(ComponentUtil.getCurrentInstitution()).andFlagDeleteEqualTo("Y");
		List<Project> selectByExample = projectMapper.selectByExample(example);
//		List<Project> selectList = projectMapper.selectList(new QueryWrapper<Project>());
		return selectByExample;
	}
	public List<Project> getDeletedProjectListForUser(){
//		List<Project> userProjectOnly = getCurrentUserProjectOnly();
		ProjectExample example = new ProjectExample();
		example.createCriteria().andIdInstitutionEqualTo(ComponentUtil.getCurrentInstitution()).andFlagDeleteEqualTo("Y");
		List<Project> selectByExample = projectMapper.selectByExample(example);
//		List<Project> selectList = projectMapper.selectList(new QueryWrapper<Project>());
		return selectByExample;
	}
	/**
	 * 
	 * @param p
	 * @param flag 是否管理员
	 * @return
	 */
	public List<Project> getProjectByCondition(Project p,boolean flag){
		List<Project> results ;
		if (flag) {//管理员
			if (p.getCdPrior() != null && !p.getCdPrior().equals("全部")) {
				results = projectMapper.selectByConditionForadmin(ComponentUtil.getCurrentInstitution(),p.getNameProject(), p.getCdPrior());
			}else {
				results = projectMapper.selectByConditionForadmin(ComponentUtil.getCurrentInstitution(),p.getNameProject(), null);
			}
		}else {
			if (p.getCdPrior() != null && !p.getCdPrior().equals("全部")) {
				results = projectMapper.selectByCondition(null, ComponentUtil.getCurrentUser().getUserId(), p.getNameProject(), p.getCdPrior());
			}else {
				results = projectMapper.selectByCondition(null, ComponentUtil.getCurrentUser().getUserId(), p.getNameProject(), null);
			}
		}
		return results;
	}
	
	/**
	 * 分页查询项目（Grid 懒加载用），只取 [offset, offset+limit) 这一段。
	 * 原先列表页一次性把全部项目查出来塞进 Grid，项目多了以后首屏会明显变慢。
	 */
	public List<Project> getProjectByConditionPaged(Project p, boolean admin, int offset, int limit) {
		String prior = normalizePrior(p.getCdPrior());
		if (admin) {
			return projectMapper.selectByConditionForadminPage(
					ComponentUtil.getCurrentInstitution(), p.getNameProject(), prior, offset, limit);
		}
		return projectMapper.selectByConditionPage(
				null, ComponentUtil.getCurrentUser().getUserId(), p.getNameProject(), prior, offset, limit);
	}

	/**
	 * 与 getProjectByConditionPaged 配套的总数查询。
	 */
	public long countProjectByCondition(Project p, boolean admin) {
		String prior = normalizePrior(p.getCdPrior());
		if (admin) {
			return projectMapper.countByConditionForadmin(
					ComponentUtil.getCurrentInstitution(), p.getNameProject(), prior);
		}
		return projectMapper.countByCondition(
				null, ComponentUtil.getCurrentUser().getUserId(), p.getNameProject(), prior);
	}

	/** 「全部」代表不按优先级过滤，等价于 SQL 里不拼该条件 */
	private String normalizePrior(String cdPrior) {
		if (null == cdPrior || cdPrior.isBlank() || "全部".equals(cdPrior)) {
			return null;
		}
		return cdPrior;
	}

	/**
	 * 拷贝项目。
	 * @param source 被拷贝的源项目（只读，方法内不再修改它，避免污染表格里正在显示的对象）
	 * @param custom 弹窗里用户确认后的信息：名称、起止时间、状态、优先级、是否私有
	 */
	public boolean cloneProject(Project source, Project custom) {
		String newProjectId = IdUtil.getSnowflakeNextIdStr();
		try {
			List<ProjectUser> userListByIdProject = getProjectUserListByIdProject(source.getIdProject());
			for (ProjectUser projectUser : userListByIdProject) {
				projectUser.setIdProject(newProjectId);
				projectUser.setDtUpdate(new Date());
				saveProjectUser(projectUser);//复制项目相关用户
			}
			Project copy = new Project();
			copy.setIdProject(newProjectId);
			copy.setIdInstitution(source.getIdInstitution());
			copy.setCdCreateUser(ComponentUtil.getCurrentUser().getUserId());
			copy.setDtCreate(new Date());
			copy.setNameProject(custom.getNameProject());
			copy.setDtStart(custom.getDtStart());
			copy.setDtEnd(custom.getDtEnd());
			copy.setCdStatus(custom.getCdStatus());
			copy.setCdPrior(custom.getCdPrior());
			copy.setFlagPrivate(custom.getFlagPrivate());
			copy.setCdComment(source.getCdComment());
			copy.setPctComplete(0.0);
			copy.setFlagDelete("N");
			projectMapper.insert(copy);
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
		return true;
	}
	
	@AuditLog(bizType = OperationLog.BIZ_PROJECT, action = "新建项目")
	public int saveProjectWithCreateUser(Project p) {
		 projectMapper.insert(p);
		// 创建人默认就是项目负责人（T2-5），否则新项目没人有 OWNER 权限
		return projectUserMapper.insert(new ProjectUser(p.getIdProject(), p.getCdCreateUser(), ProjectUser.ROLE_OWNER));
	}

	@AuditLog(bizType = OperationLog.BIZ_PROJECT, action = "修改项目")
	public int updateProject(Project p) {
		return projectMapper.updateByPrimaryKeySelective(p);
	}
	
	public int saveProjectUser(ProjectUser p) {
		projectUserMapper.deleteByPrimaryKey(p.getIdProject(), p.getIdUser());
		try {
			return projectUserMapper.insert(p);
		} catch (Exception e) {
			log.error("保存项目成员失败", e);
		}
		return 0;
	}
	public int saveProjectUserList(List<ProjectUser> pu) {
		if (!pu.isEmpty()) {
			ProjectUserExample usere = new ProjectUserExample();
			usere.createCriteria().andIdProjectEqualTo(pu.get(0).getIdProject());
			projectUserMapper.deleteByExample(usere);
			try {
				return projectUserMapper.insertBatch(pu);
			} catch (Exception e) {
				log.error("保存项目成员失败", e);
			}
		}
		return 0;
	}
	/**
	 * 1.先查询机构所有项目；
	 * 2.再根据project_user下和当前用户绑定的项目返回，不是该用户的项目不在列表内
	 * @return
	 */
	public List<Project> getCurrentUserProjects(){
		if (null == ComponentUtil.getCurrentUser()) {
			return new ArrayList<Project>();
		}
		List<Project> projectList = getProjectList();
		ProjectUserExample ex = new ProjectUserExample();
		ex.createCriteria().andIdUserEqualTo(ComponentUtil.getCurrentUser().getUserId());
		List<ProjectUser> projectsByUserId = projectUserMapper.selectByExample(ex);
		List<String> ProjectsIds = projectsByUserId.stream().map(e -> e.getIdProject()).collect(Collectors.toList());
		List<String> collect = projectList.stream().map(e -> e.getIdProject()).collect(Collectors.toList());
		collect.retainAll(ProjectsIds);
		
		ProjectExample example = new ProjectExample();
		Criteria andIdProjectIn = example.createCriteria().andIdProjectIn(collect);
		List<Project> selectByExample = projectMapper.selectByExample(example);
//		List<Project> collect2 = projectList.stream().filter(e -> ProjectsIds.contains(e.getIdProject())).collect(Collectors.toList());
		return selectByExample;
	}
	
	public List<Project> getCurrentUserProjectOnly(){
		ProjectUserExample ex = new ProjectUserExample();
		ex.createCriteria().andIdUserEqualTo(ComponentUtil.getCurrentUser().getUserId());
		List<ProjectUser> projectsByUserId = projectUserMapper.selectByExample(ex);
		List<String> collect = projectsByUserId.stream().map(e ->e.getIdProject()).collect(Collectors.toList());
		ProjectExample example = new ProjectExample();
		Criteria andIdProjectIn = example.createCriteria().andIdProjectIn(collect).andFlagDeleteNotEqualTo("Y");
		List<Project> selectByExample = projectMapper.selectByExample(example);
		return selectByExample;
	}
	
	@AuditLog(bizType = OperationLog.BIZ_DOC, action = "上传项目文档")
	public int saveFile(ProjectTaskFile file) {
		return projectTaskFileMapper.insertSelective(file);
	}
	
//	public List<ProjectTaskFile> getProjectFileList(String idProject){
//		ProjectTaskFileExample sl = new ProjectTaskFileExample();
//		sl.createCriteria().andIdProjectEqualTo(idProject);
//		List<ProjectTaskFile> selectByExample = projectTaskFileMapper.selectByExample(sl);
//		return selectByExample;
//	}
	public List<ProjectTaskFile> getProjectFileListByIdProject(String idProject){
		List<ProjectTaskFile> selectByExample = projectTaskFileMapper.selectByProjectAndTadkId(idProject, null);
		return selectByExample;
	}
	
	public ProjectTaskFile getProjectFileByFileId(String idFile){
		ProjectTaskFile selectByExample = projectTaskFileMapper.selectByPrimaryKey(idFile);
		return selectByExample;
	}

	/** 按项目 ID 取项目（S5 全局搜索跳转用） */
	public Project getProjectById(String idProject) {
		if (null == idProject || idProject.isBlank()) {
			return null;
		}
		return projectMapper.selectByPrimaryKey(idProject);
	}
	
	public List<ProjectTaskFile> getTaskFileList(String idProject,String taskId){
		List<ProjectTaskFile> selectByExample = projectTaskFileMapper.selectByProjectAndTadkId(idProject,taskId);
		return selectByExample;
	}
	
	public List<ProjectUser> getCurrentProjectOrer(){
		ProjectUserExample example = new ProjectUserExample();
		example.createCriteria().andIdUserEqualTo(ComponentUtil.getCurrentUser().getUserId());
		example.setOrderByClause(" DT_UPDATE DESC");
		List<ProjectUser> selectByExample = projectUserMapper.selectByExample(example);
		return selectByExample;
		
	}
	
	public Integer saveProjectTags(ProjectTag tag) {
		return projectTagMapper.insert(tag);
	}
	
	public List<ProjectTag> getProjectTagsList(String idProject){
		ProjectTagExample example = new ProjectTagExample();
		example.createCriteria().andIdProjectEqualTo(idProject);
		List<ProjectTag> selectByExample = projectTagMapper.selectByExample(example);
		if (null == selectByExample || selectByExample.isEmpty()) {
			return new ArrayList<ProjectTag>();
		}
		return selectByExample;
	}
	
	public Integer saveProjectGroup(ProjectGroup g) {
		return projectGroupMapper.insert(g);
	}
	
	public List<ProjectGroup> getProjectGroupList(String idProject){
		ProjectGroupExample example = new ProjectGroupExample();
		example.createCriteria().andIdProjectEqualTo(idProject);
		List<ProjectGroup> selectByExample = projectGroupMapper.selectByExample(example);
		if (null == selectByExample || selectByExample.isEmpty()) {
			return new ArrayList<ProjectGroup>();
		}
		return selectByExample;
	}
	
	public Integer saveProjectType(ProjectType g) {
		return projectTypeMapper.insert(g);
	}
	
	public List<ProjectType> getProjectTypeList(String idProject){
		ProjectTypeExample example = new ProjectTypeExample();
		example.createCriteria().andIdProjectEqualTo(idProject);
		List<ProjectType> selectByExample = projectTypeMapper.selectByExample(example);
		if (null == selectByExample || selectByExample.isEmpty()) {
			return new ArrayList<ProjectType>();
		}
		return selectByExample;
	}

	public List<User> getuserListByIdInstitution(String idInstitution) {
		UserExample example = new UserExample();
		example.createCriteria().andIdInstitutionEqualTo(idInstitution);
		List<User> selectByExample = userMapper.selectByExample(example);
		return selectByExample;
	}

	public List<ProjectUser> getProjectUserListByIdProject(String idProject) {
		ProjectUserExample usere = new ProjectUserExample();
		usere.createCriteria().andIdProjectEqualTo(idProject);
		List<ProjectUser> selectByExample = projectUserMapper.selectByExample(usere);
		return selectByExample;
	}

	public boolean checkProjectExists(Project p) {
		ProjectExample example = new ProjectExample();
		example.createCriteria().andNameProjectEqualTo(p.getNameProject());
		List<Project> selectByExample = projectMapper.selectByExample(example);
		if (null != selectByExample && !selectByExample.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 删除项目
	 * @param data
	 */
	@Transactional
	public void deleteProject(Project data) {
		projectMapper.deleteByPrimaryKey(data.getIdProject());
		ProjectUserExample usere = new ProjectUserExample();
		usere.createCriteria().andIdProjectEqualTo(data.getIdProject());
		projectUserMapper.deleteByExample(usere);
		
		ProjectGroupExample group = new ProjectGroupExample();
		group.createCriteria().andIdProjectEqualTo(data.getIdProject());
		projectGroupMapper.deleteByExample(group);
		ProjectTagExample tag = new ProjectTagExample();
		tag.createCriteria().andIdProjectEqualTo(data.getIdProject());
		projectTagMapper.deleteByExample(tag);
		
		ProjectTypeExample type = new ProjectTypeExample();
		type.createCriteria().andIdProjectEqualTo(data.getIdProject());
		projectTypeMapper.deleteByExample(type);
		
		TaskExample task = new TaskExample();
		task.createCriteria().andIdProjectEqualTo(data.getIdProject());
		taskMapper.deleteByExample(task);
	}
	
	/** 项目下的任务数（回收站「彻底删除」确认框展示用） */
	public long countTasksOfProject(String idProject) {
		TaskExample e = new TaskExample();
		e.createCriteria().andIdProjectEqualTo(idProject);
		return taskMapper.selectByExample(e).size();
	}

	@AuditLog(bizType = OperationLog.BIZ_PROJECT, action = "删除项目（逻辑删除）")
	public int deleteProjectLogic(Project data) {
		checkProjectDeletePermission(data);
		Project p = new Project();
		p.setIdProject(data.getIdProject());
		p.setFlagDelete("Y");
		return projectMapper.updateByPrimaryKeySelective(p);
	}

	@AuditLog(bizType = OperationLog.BIZ_PROJECT, action = "恢复项目")
	public int recoverProjectLogic(Project data) {
		Project p = new Project();
		p.setIdProject(data.getIdProject());
		p.setFlagDelete("N");
		return projectMapper.updateByPrimaryKeySelective(p);
	}

	/**
	 * 彻底删除项目：项目行 + 所有项目相关表数据一次清干净，不可恢复。
	 * 清理范围：
	 * <ul>
	 *   <li>任务域：task、task_user（执行人）、task_comment（评论）、task_subtask（子任务）、
	 *       task_bugfix（缺陷）、task_dependency（依赖）、project_task_file（附件记录）</li>
	 *   <li>项目域：project_user、project_group、project_tag、project_type、
	 *       project_milestone、notification（项目相关的到期提醒等通知）</li>
	 * </ul>
	 * 磁盘上的上传文件实体不在这一步删（避免误删共享目录），只清数据库记录。
	 * 复用 deleteProject（项目域 + task 主表），再补齐它没覆盖的关联表。
	 */
	@Transactional
	@AuditLog(bizType = OperationLog.BIZ_PROJECT, action = "彻底删除项目")
	public int deleteProjectCompletely(Project data) {
		checkProjectDeletePermission(data);
		String idProject = data.getIdProject();
		// 先收集项目下的任务 ID：task_user / task_comment / task_dependency 以 ID_TASK 关联
		TaskExample taskEx = new TaskExample();
		taskEx.createCriteria().andIdProjectEqualTo(idProject);
		List<com.sl.entity.Task> tasks = taskMapper.selectByExample(taskEx);
		List<String> taskIds = tasks.stream().map(com.sl.entity.Task::getIdTask).toList();

		if (!taskIds.isEmpty()) {
			com.sl.entity.TaskUserExample tuEx = new com.sl.entity.TaskUserExample();
			tuEx.createCriteria().andIdTaskIn(taskIds);
			taskUserMapper.deleteByExample(tuEx);

			com.sl.entity.TaskCommentExample tcEx = new com.sl.entity.TaskCommentExample();
			tcEx.createCriteria().andIdTaskIn(taskIds);
			taskCommentMapper.deleteByExample(tcEx);

			for (String idTask : taskIds) {
				taskDependencyMapper.deleteByTask(idTask);
			}
		}
		// 这三张表自身带 ID_PROJECT，直接按项目删
		com.sl.entity.TaskSubtaskExample tsEx = new com.sl.entity.TaskSubtaskExample();
		tsEx.createCriteria().andIdProjectEqualTo(idProject);
		subtaskMapper.deleteByExample(tsEx);

		com.sl.entity.TaskBugfixExample tbEx = new com.sl.entity.TaskBugfixExample();
		tbEx.createCriteria().andIdProjectEqualTo(idProject);
		taskBugfixMapper.deleteByExample(tbEx);

		com.sl.entity.ProjectTaskFileExample pfEx = new com.sl.entity.ProjectTaskFileExample();
		pfEx.createCriteria().andIdProjectEqualTo(idProject);
		projectTaskFileMapper.deleteByExample(pfEx);

		// 检查清单（S4）自身带 ID_PROJECT，直接按项目删
		taskChecklistMapper.deleteByProject(idProject);

		projectMilestoneMapper.deleteByProject(idProject);
		notificationMapper.deleteByProject(idProject);

		// 项目本体 + project_user/group/tag/type + task 主表
		deleteProject(data);
		return 1;
	}

	/**
	 * 删除项目的权限兜底：全局管理员放行；普通用户只能删自己创建的项目。
	 * 项目列表/回收站两个删除入口（deleteProjectLogic / deleteProjectCompletely）都走这里。
	 * @param data 待删项目（须带 CD_CREATE_USER）
	 */
	private void checkProjectDeletePermission(Project data) {
		User u = ComponentUtil.getCurrentUser();
		String me = (null == u) ? null : u.getUserId();
		if (null == me) {
			throw new IllegalStateException("当前会话已失效，请重新登录后再试");
		}
		boolean admin;
		try {
			admin = SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER);
		} catch (Exception e) {
			admin = false;
		}
		if (!admin && !me.equals(data.getCdCreateUser())) {
			throw new IllegalArgumentException("只能删除自己创建的项目");
		}
	}

	public boolean checkGroupExists(ProjectGroup group) {
		ProjectGroupExample e = new ProjectGroupExample();
		e.createCriteria().andIdProjectEqualTo(group.getIdProject()).andCdGroupEqualTo(group.getCdGroup());
		List<ProjectGroup> selectByExample = projectGroupMapper.selectByExample(e);
		if (null != selectByExample && !selectByExample.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public boolean checkTypeExists(ProjectType group) {
		ProjectTypeExample e = new ProjectTypeExample();
		e.createCriteria().andIdProjectEqualTo(group.getIdProject()).andCdTypeEqualTo(group.getCdType());
		List<ProjectType> selectByExample = projectTypeMapper.selectByExample(e);
		if (null != selectByExample && !selectByExample.isEmpty()) {
			return true;
		}
		return false;
	}
	public boolean checkTagExists(ProjectTag group) {
		ProjectTagExample e = new ProjectTagExample();
		e.createCriteria().andIdProjectEqualTo(group.getIdProject()).andCdTagEqualTo(group.getCdTag());
		List<ProjectTag> selectByExample = projectTagMapper.selectByExample(e);
		if (null != selectByExample && !selectByExample.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public Integer updateProjectUserTime(String idproject) {
		ProjectUser pu = new ProjectUser(idproject, ComponentUtil.getCurrentUser().getUserId());
		pu.setDtUpdate(new Date());
		ProjectUserExample ex = new ProjectUserExample();
		ex.createCriteria().andIdProjectEqualTo(idproject).andIdUserEqualTo(ComponentUtil.getCurrentUser().getUserId());
		return projectUserMapper.updateByExample(pu, ex);
	}
	/**
	 * 根据日期列出各个日期的任务数量
	 * @param idProject
	 * @return
	 */
	public List<Map<String,Object>> getTaskNumOrder(String idProject){
		// 原先 taskMapper.getTaskTrending() 不接参数，idProject 被丢弃，
		// 统计页画的是全库任务趋势而不是当前项目的。
		List<Map<String,Object>> taskTrending = taskMapper.getTaskTrending(idProject);
		return taskTrending;
	}
}
