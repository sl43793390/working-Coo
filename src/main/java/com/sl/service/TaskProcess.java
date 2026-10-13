package com.sl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sl.app.component.util.ComponentUtil;
import com.sl.aop.AuditLog;
import com.sl.entity.OperationLog;
import com.sl.entity.Task;
import com.sl.entity.TaskComment;
import com.sl.entity.TaskCommentExample;
import com.sl.entity.TaskExample;
import com.sl.entity.TaskSubtaskExample;
import com.sl.entity.TaskUser;
import com.sl.entity.TaskUserExample;
import com.sl.entity.User;
import com.sl.entity.UserExample;
import com.sl.entity.TaskExample.Criteria;
import com.sl.mapper.TaskCommentMapper;
import com.sl.mapper.TaskMapper;
import com.sl.mapper.TaskSubtaskMapper;
import com.sl.mapper.TaskUserMapper;
import com.sl.mapper.UserMapper;
import com.sl.user.UserManagementEnums;

import cn.hutool.core.collection.CollUtil;

@Service
public class TaskProcess {

	
	private static final Logger log = LoggerFactory.getLogger(TaskProcess.class);

	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private TaskSubtaskMapper taskSubtaskMapper;
	@Autowired
	private TaskCommentMapper taskCommentMapper;
	@Autowired
	private TaskUserMapper taskUserMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private NotificationService notificationService;

	/**
	 * 查询当前项目下创建的所有公开的任务，管理员角色调用的方法，普通用户只能查看分配给自己的任务
	 * @param idProject
	 * @return
	 */
	public List<Task> getTaskByIdProject(String idProject){
		TaskExample example = new TaskExample();
		example.createCriteria().andIdProjectEqualTo(idProject).andFlagPrivateEqualTo("Y");
		List<Task> selectByExample = taskMapper.selectByExample(example);
		return selectByExample;
	}

	/**
	 * 项目下对指定用户可见的全部任务，可见性口径与列表分页查询（Task_Page_Condition）一致：
	 * <ul>
	 *   <li>allVisible=true（管理员）：项目下全部任务，不限公开性；</li>
	 *   <li>否则（普通用户）：公开（FLAG_PRIVATE='Y'）+ 自己创建的 + 分派给自己的
	 *       （task_user 里挂着的私有任务，按「分配即可见」放行）。</li>
	 * </ul>
	 * 统计页等全量取数场景用这个，不要再用 getTaskByIdProject——它只查 'Y'，
	 * 历史数据里大量任务 FLAG_PRIVATE='N'，会表现成「明明有任务却统计为空」。
	 */
	public List<Task> getTasksVisibleTo(String idProject, String viewUserId, boolean allVisible) {
		TaskExample example = new TaskExample();
		Criteria visible = example.createCriteria().andIdProjectEqualTo(idProject);
		if (allVisible) {
			// 管理员：项目下全部任务
			example.setOrderByClause("DT_CREATE desc");
			return taskMapper.selectByExample(example);
		}
		visible.andFlagPrivateEqualTo("Y");
		if (null != viewUserId && !viewUserId.isBlank()) {
			// 自己创建的（含私有）
			example.or().andIdProjectEqualTo(idProject).andIdUserEqualTo(viewUserId);
			// 分派给自己的（含别人创建的私有任务）
			List<String> myTaskIds = getTaskIdByUserId(idProject, viewUserId);
			if (null != myTaskIds && !myTaskIds.isEmpty()) {
				example.or().andIdProjectEqualTo(idProject).andIdTaskIn(myTaskIds);
			}
		}
		example.setOrderByClause("DT_CREATE desc");
		return taskMapper.selectByExample(example);
	}
	
	/**
	 * 分页查询任务（Grid 懒加载用），条件与 getTaskByCondition 一致，另由 flagPrivate/viewUserId
	 * 决定可见性口径：
	 * <ul>
	 *   <li>flagPrivate="Y"（普通用户）：公开任务 + 自己创建的 + 分派给自己的（SQL exists 判断）；</li>
	 *   <li>flagPrivate=null（管理员）：不做可见性过滤，项目下全部任务。</li>
	 * </ul>
	 */
	public List<Task> getTaskByConditionPaged(String idProject, String flagPrivate, Task task, List<String> taskIds, String viewUserId, int offset, int limit) {
		return taskMapper.selectTaskPage(idProject, flagPrivate, viewUserId, taskIds,
				task.getCdTaskType(), task.getCdTag(), task.getCdStatus(), task.getCdTaskGroup(),
				task.getNameTask(), task.getIdUser(), task.getIdExeUser(), offset, limit);
	}

	/**
	 * 与 getTaskByConditionPaged 配套的总数查询，口径一致。
	 */
	public long countTaskByCondition(String idProject, String flagPrivate, Task task, List<String> taskIds, String viewUserId) {
		return taskMapper.countTask(idProject, flagPrivate, viewUserId, taskIds,
				task.getCdTaskType(), task.getCdTag(), task.getCdStatus(), task.getCdTaskGroup(),
				task.getNameTask(), task.getIdUser(), task.getIdExeUser());
	}

	public List<Task> getTaskByCondition(String idProject, Task task,List<String> taskIds){
		TaskExample example = new TaskExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andIdProjectEqualTo(idProject).andFlagPrivateEqualTo("Y");
		if (taskIds != null && !taskIds.isEmpty()) {
			createCriteria.andIdTaskIn(taskIds);
		}
		if (task.getCdTaskType() != null) {
			createCriteria.andCdTaskTypeEqualTo(task.getCdTaskType());
		}
		if (null != task.getCdTag()) {
			createCriteria.andCdTagEqualTo(task.getCdTag());
		}
		if (null != task.getCdStatus()) {
			createCriteria.andCdStatusEqualTo(task.getCdStatus());
		}
		if (null != task.getCdTaskGroup()) {
			createCriteria.andCdTaskGroupEqualTo(task.getCdTaskGroup());
		}
		if (null != task.getNameTask()) {
			createCriteria.andNameTaskLike("%" + task.getNameTask() + "%");
		}
		if (null != task.getIdUser()) {
			createCriteria.andIdUserEqualTo(task.getIdUser());
		}
		List<Task> selectByExample = taskMapper.selectByExample(example);
		return selectByExample;
	}
	
	public Task getTaskById(String idTask) {
		if (null == idTask || idTask.isBlank()) {
			return null;
		}
		return taskMapper.selectByPrimaryKey(idTask);
	}

	/**
	 * 列表页展示执行人姓名。
	 * task.ID_EXE_USER 是历史遗留的单值列，多人任务只存了其中一个；执行人的权威数据在 task_user。
	 * 这里按整页任务一次性把 task_user 捞出来做映射（2 条 SQL），
	 * 避免每一行回调里各查一次造成 N+1。
	 */
	public void fillExecutorNames(List<Task> tasks) {
		if (null == tasks || tasks.isEmpty()) {
			return;
		}
		List<String> taskIds = tasks.stream().map(Task::getIdTask).distinct().collect(Collectors.toList());
		TaskUserExample ex = new TaskUserExample();
		ex.createCriteria().andIdTaskIn(taskIds);
		List<TaskUser> taskUsers = taskUserMapper.selectByExample(ex);
		if (null == taskUsers || taskUsers.isEmpty()) {
			return;
		}
		// taskId -> userId 列表
		Map<String, List<String>> userIdsByTask = new HashMap<>();
		Set<String> allUserIds = new HashSet<>();
		for (TaskUser tu : taskUsers) {
			userIdsByTask.computeIfAbsent(tu.getIdTask(), k -> new ArrayList<>()).add(tu.getIdExeUser());
			allUserIds.add(tu.getIdExeUser());
		}
		UserExample ue = new UserExample();
		ue.createCriteria().andUserIdIn(new ArrayList<>(allUserIds));
		Map<String, String> nameById = new HashMap<>();
		for (User u : userMapper.selectByExample(ue)) {
			nameById.put(u.getUserId(), u.getUserName());
		}
		for (Task t : tasks) {
			List<String> ids = userIdsByTask.get(t.getIdTask());
			if (null == ids || ids.isEmpty()) {
				continue;
			}
			t.setNameExeUsers(ids.stream()
					.map(id -> nameById.getOrDefault(id, id))
					.collect(Collectors.joining("，")));
		}
	}

	/**
	 * 看板拖拽改状态：只更新 CD_STATUS，不碰其它字段。
	 */
	@AuditLog(bizType = OperationLog.BIZ_TASK, action = "拖拽变更任务状态")
	public int updateTaskStatus(String idTask, String cdStatus) {
		if (null == idTask || idTask.isBlank() || null == cdStatus) {
			return 0;
		}
		Task t = new Task();
		t.setIdTask(idTask);
		t.setCdStatus(cdStatus);
		// 拖到「已结束」时顺手把完成率补成 100，避免看板和统计口径不一致
		if ("已结束".equals(cdStatus)) {
			t.setPctComplete(100d);
		}
		return taskMapper.updateByPrimaryKeySelective(t);
	}

	/**
	 * 甘特图 / 燃尽图取数：只取有排期的任务，按开始时间排序。
	 */
	public List<Task> getTasksWithSchedule(String idProject) {
		TaskExample ex = new TaskExample();
		ex.createCriteria().andIdProjectEqualTo(idProject);
		ex.setOrderByClause("DT_START ASC, DT_END ASC");
		List<Task> list = taskMapper.selectByExample(ex);
		fillExecutorNames(list);
		return list;
	}

	/**
	 * 根据任务ID查询该任务的所有执行用户
	 * @param idTask
	 * @return
	 */
	public List<User> getTaskUserByIdTask(String idTask){
		TaskUserExample example = new TaskUserExample();
		example.createCriteria().andIdTaskEqualTo(idTask);
		List<TaskUser> selectByExample = taskUserMapper.selectByExample(example);
		List<String> ids = selectByExample.stream().map(e -> e.getIdExeUser()).collect(Collectors.toList());
		if (null == ids || ids.isEmpty()) {
			return new ArrayList<User>();
		}
		UserExample ue = new UserExample();
		ue.createCriteria().andUserIdIn(ids);
		List<User> selectByExample2 = userMapper.selectByExample(ue);
		return selectByExample2;
	}
	/**
	 * 根据任务ID查询该任务的所有执行用户
	 * @param idTask
	 * @return
	 */
	public List<String> getTaskUserIdsByIdTask(String idTask){
		TaskUserExample example = new TaskUserExample();
		example.createCriteria().andIdTaskEqualTo(idTask);
		List<TaskUser> selectByExample = taskUserMapper.selectByExample(example);
		List<String> ids = selectByExample.stream().map(e -> e.getIdExeUser()).collect(Collectors.toList());
		return ids;
	}
	
	/**
	 * 查询用户在当前项目下参与的任务,以及当前项目公开的任务
	 * @param userId
	 * @param idProject
	 * @return
	 */
	public List<Task> getTaskByUserId(String idProject, String userId) {
		//首先查询分派给当前用户的任务
		TaskUserExample example = new TaskUserExample();
		example.createCriteria().andIdProjectEqualTo(idProject).andIdExeUserEqualTo(userId);
		List<TaskUser> selectByExample = taskUserMapper.selectByExample(example);
		List<String> resultsIds = new ArrayList<>();
		if (null != selectByExample) {
			List<String> taskIds = selectByExample.stream().map(e -> e.getIdTask()).collect(Collectors.toList());
			resultsIds.addAll(taskIds);
		}
		//然后查询当前项目公开的任务
//		TaskExample ex = new TaskExample();
//		ex.createCriteria().andIdProjectEqualTo(idProject).andIdUserEqualTo(userId);
//		List<Task> selectByExample3 = taskMapper.selectByExample(ex);
//		if (null != selectByExample3) {
//			List<String> collect = selectByExample3.stream().map(e ->e.getIdTask()).collect(Collectors.toList());
//			resultsIds.addAll(collect);
//		}
		
		if (null == resultsIds || resultsIds.isEmpty()) {
			return new ArrayList<Task>();
		}
		TaskExample taske = new TaskExample();
		taske.createCriteria().andIdTaskIn(resultsIds);
		List<Task> selectByExample2 = taskMapper.selectByExample(taske);
		return selectByExample2;
	}
	
	public List<String> getTaskIdByUserId(String idProject, String userId) {
		//首先查询分派给当前用户的任务
		TaskUserExample example = new TaskUserExample();
		example.createCriteria().andIdProjectEqualTo(idProject).andIdExeUserEqualTo(userId);
		List<TaskUser> selectByExample = taskUserMapper.selectByExample(example);
		List<String> resultsIds = new ArrayList<>();
		if (null != selectByExample) {
			List<String> taskIds = selectByExample.stream().map(e -> e.getIdTask()).collect(Collectors.toList());
			resultsIds.addAll(taskIds);
		}
		return resultsIds;
	}
	
	
	public List<TaskComment> getTaskCommentByTaskId(String taskId){
		TaskCommentExample ex = new TaskCommentExample();
		ex.createCriteria().andIdTaskEqualTo(taskId);
		List<TaskComment> selectByExample = taskCommentMapper.selectByExample(ex);
		return selectByExample;
	}
	
	@AuditLog(bizType = OperationLog.BIZ_TASK, action = "新建任务")
	public int saveTask(Task t) {
		try {
			fillLegacyExeUser(t);
			taskMapper.insertSelective(t);
			if (null != t.getIdExeUsers()) {
				for (User u : t.getIdExeUsers()) {
					taskUserMapper.insert(new TaskUser(t.getIdTask(),u.getUserId(),t.getIdProject()));
					notificationService.pushTaskAssigned(t, u.getUserId());
				}
			}
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return taskMapper.updateByPrimaryKey(t);
		}
		return 1;
	}
	
	/**
	 * 领取任务（幂等）：task_user 主键是 (ID_TASK, ID_EXE_USER)，重复领取撞主键。
	 * 返回值约定：1=新领取成功；2=已领取过（幂等放行）；0=失败。
	 * 原实现直接 insert，撞主键后 catch 掉静默返回 0——用户看不到任何反馈，
	 * 还留下整屏 Duplicate entry 错误日志。
	 */
	public int saveTaskUser(String idproject,String idTask,String idUser) {
		// 先查后插：同一任务同一人只允许一条关联记录
		TaskUserExample existEx = new TaskUserExample();
		existEx.createCriteria().andIdTaskEqualTo(idTask).andIdExeUserEqualTo(idUser);
		if (!taskUserMapper.selectByExample(existEx).isEmpty()) {
			return 2;
		}
		try {
			return taskUserMapper.insert(new TaskUser(idTask, idUser, idproject));
		} catch (Exception e) {
			// 并发兜底：查询和插入之间被同一用户抢先领取，同样视为已领取
			log.info("领取任务撞主键，按已领取处理, idTask={}, idUser={}", idTask, idUser);
			return 2;
		}
	}
	
	/**
	 * task.ID_EXE_USER 是历史遗留的单值列，多人任务存不下。
	 * 执行人的权威数据已经统一到 task_user（见 V2 迁移注释），
	 * 这里把第一个执行人回写进去，只是为了让旧报表 / 旧 SQL 不至于查不到人，
	 * 待确认无引用后可直接删除该列。
	 */
	private void fillLegacyExeUser(Task t) {
		if (null == t.getIdExeUsers() || t.getIdExeUsers().isEmpty()) {
			return;
		}
		for (User u : t.getIdExeUsers()) {
			if (null != u && null != u.getUserId()) {
				t.setIdExeUser(u.getUserId());
				return;
			}
		}
	}

	@AuditLog(bizType = OperationLog.BIZ_TASK, action = "修改任务")
	public int updateTask(Task t) {
		try {
			fillLegacyExeUser(t);
			taskMapper.updateByPrimaryKeySelective(t);
			if (null != t.getIdExeUsers()) {
				List<String> before = getTaskUserIdsByIdTask(t.getIdTask());
				TaskUserExample example = new TaskUserExample();
				example.createCriteria().andIdTaskEqualTo(t.getIdTask());
				taskUserMapper.deleteByExample(example);
				for (User u : t.getIdExeUsers()) {
					taskUserMapper.insert(new TaskUser(t.getIdTask(),u.getUserId(),t.getIdProject()));
				}
				// 只给「这次新增的执行人」发通知，避免每次保存都骚扰一遍老执行人
				for (User u : t.getIdExeUsers()) {
					if (null != u && !before.contains(u.getUserId())) {
						notificationService.pushTaskAssigned(t, u.getUserId());
					}
				}
			}
			return 1;
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	public boolean checkTaskExists(Task saveTask) {
		TaskExample example = new TaskExample();
		example.createCriteria().andNameTaskEqualTo(saveTask.getNameTask());
		List<Task> selectByExample = taskMapper.selectByExample(example);
		if (CollUtil.isNotEmpty(selectByExample)) {
			return true;
		}
		return false;
	}

	@Transactional
	@AuditLog(bizType = OperationLog.BIZ_TASK, action = "删除任务")
	public void deleteTask(Task task) {
		checkDeletePermission(task.getIdUser(), "任务");
		taskMapper.deleteByPrimaryKey(task.getIdTask());
		TaskSubtaskExample example = new TaskSubtaskExample();
		example.createCriteria().andIdTaskParentEqualTo(task.getIdTask());
		taskSubtaskMapper.deleteByExample(example);
		
		TaskUserExample usere = new TaskUserExample();
		usere.createCriteria().andIdTaskEqualTo(task.getIdTask());
		taskUserMapper.deleteByExample(usere);
		
		TaskCommentExample commexample = new TaskCommentExample();
		commexample.createCriteria().andIdTaskEqualTo(task.getIdTask());
		taskCommentMapper.deleteByExample(commexample);
	}

	/**
	 * 删除权限的服务端兜底：全局管理员放行；普通用户只能删自己创建的。
	 * UI 按钮显隐只是第一道门，服务端必须再校验一次。
	 * @param creatorId 业务对象创建人（task.ID_USER / project.CD_CREATE_USER）
	 * @param bizName 业务名，用于拼错误文案
	 */
	private void checkDeletePermission(String creatorId, String bizName) {
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
		if (!admin && !me.equals(creatorId)) {
			throw new IllegalArgumentException("只能删除自己创建的" + bizName);
		}
	}

	@AuditLog(bizType = OperationLog.BIZ_TASK, action = "发布任务评论")
	public Integer saveTaskComment(TaskComment c) {
		int insert = taskCommentMapper.insert(c);
		return insert;
	}
	
}
