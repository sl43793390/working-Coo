package com.sl.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.Task;
import com.sl.entity.TaskDependency;
import com.sl.mapper.TaskDependencyMapper;

import cn.hutool.core.util.IdUtil;

/**
 * 任务依赖（T2-3）。
 * <p>
 * 只支持「前置 -> 后继」一种关系。新增一条依赖前必须做环路检测：
 * 记一条依赖为边 {@code 前置 -> 后继}，那么新增 {@code P -> T} 会成环，
 * 当且仅当当前图中已经存在 {@code T -> ... -> P} 的路径。
 */
@Service
public class TaskDependencyService {

	private static final Logger log = LoggerFactory.getLogger(TaskDependencyService.class);

	@Autowired
	private TaskDependencyMapper taskDependencyMapper;
	@Autowired
	private TaskProcess taskProcess;

	/** 自己不能是自己的前置 */
	public static final String ERR_SELF = "不能把任务自己设为前置任务";
	public static final String ERR_DUPLICATE = "该前置关系已经存在";
	public static final String ERR_CYCLE = "会形成循环依赖，请检查依赖链";

	/**
	 * 新增一条前置依赖。
	 *
	 * @return 成功返回 null，失败返回可读的错误原因（交给 UI 直接提示）
	 */
	@Transactional
	public String addDependency(String idProject, String idTask, String idPreTask) {
		if (null == idTask || null == idPreTask || idTask.isBlank() || idPreTask.isBlank()) {
			return "请先选择任务与前置任务";
		}
		if (idTask.equals(idPreTask)) {
			return ERR_SELF;
		}
		if (taskDependencyMapper.countPair(idTask, idPreTask) > 0) {
			return ERR_DUPLICATE;
		}
		if (reachable(idProject, idTask, idPreTask)) {
			return ERR_CYCLE;
		}
		TaskDependency d = new TaskDependency();
		d.setIdDependency(IdUtil.getSnowflakeNextIdStr());
		d.setIdProject(idProject);
		d.setIdTask(idTask);
		d.setIdPreTask(idPreTask);
		d.setNbrLagDay(0);
		d.setDtCreate(new Date());
		d.setIdCreateUser(currentUserId());
		taskDependencyMapper.insert(d);
		return null;
	}

	public void deleteDependency(String idDependency) {
		taskDependencyMapper.deleteByPrimaryKey(idDependency);
	}

	/** 任务删除时清掉与它相关的依赖 */
	public void deleteByTask(String idTask) {
		taskDependencyMapper.deleteByTask(idTask);
	}

	public List<TaskDependency> getPreDependencies(String idTask) {
		return taskDependencyMapper.selectByTask(idTask);
	}

	public List<TaskDependency> getSuccessorDependencies(String idTask) {
		return taskDependencyMapper.selectByPreTask(idTask);
	}

	/**
	 * 前置任务链（含间接前置，按层级展开），详情页展示用。
	 */
	public List<Task> getPreTasks(String idTask) {
		List<TaskDependency> deps = getPreDependencies(idTask);
		List<Task> result = new ArrayList<>();
		for (TaskDependency d : deps) {
			Task t = taskProcess.getTaskById(d.getIdPreTask());
			if (null != t) {
				result.add(t);
			}
		}
		return result;
	}

	public List<Task> getSuccessorTasks(String idTask) {
		List<TaskDependency> deps = getSuccessorDependencies(idTask);
		List<Task> result = new ArrayList<>();
		for (TaskDependency d : deps) {
			Task t = taskProcess.getTaskById(d.getIdTask());
			if (null != t) {
				result.add(t);
			}
		}
		return result;
	}

	public List<TaskDependency> listByProject(String idProject) {
		return taskDependencyMapper.selectByProject(idProject);
	}

	/**
	 * 是否被阻塞：存在未完成的前置任务。
	 * 前置任务状态为「已结束 / 已废弃」视为已完成。
	 */
	public boolean isBlocked(String idTask) {
		List<Task> pres = getPreTasks(idTask);
		for (Task p : pres) {
			String st = p.getCdStatus();
			if (null == st || !("已结束".equals(st) || "已废弃".equals(st))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 广度优先判断 {@code from} 能否沿「前置 -> 后继」的边走到 {@code to}。
	 * 只取当前项目的依赖集，一次 SQL，不在循环里查库。
	 */
	public boolean reachable(String idProject, String from, String to) {
		if (null == from || null == to) {
			return false;
		}
		if (from.equals(to)) {
			return true;
		}
		List<TaskDependency> all = taskDependencyMapper.selectByProject(idProject);
		// 前置 -> 后继集合
		Map<String, List<String>> edges = new HashMap<>();
		for (TaskDependency d : all) {
			edges.computeIfAbsent(d.getIdPreTask(), k -> new ArrayList<>()).add(d.getIdTask());
		}
		Set<String> visited = new HashSet<>();
		Deque<String> queue = new ArrayDeque<>();
		queue.add(from);
		visited.add(from);
		while (!queue.isEmpty()) {
			String cur = queue.poll();
			List<String> next = edges.get(cur);
			if (null == next) {
				continue;
			}
			for (String n : next) {
				if (to.equals(n)) {
					return true;
				}
				if (visited.add(n)) {
					queue.add(n);
				}
			}
		}
		return false;
	}

	/**
	 * 独立的环路检测：给外部（导入、批量保存）判断当前项目依赖图是否已成环。
	 * 返回构成环的任务 ID 列表，无环时返回空列表。
	 */
	public List<String> findCycle(String idProject) {
		List<TaskDependency> all = taskDependencyMapper.selectByProject(idProject);
		Map<String, List<String>> edges = new HashMap<>();
		Set<String> nodes = new HashSet<>();
		for (TaskDependency d : all) {
			edges.computeIfAbsent(d.getIdPreTask(), k -> new ArrayList<>()).add(d.getIdTask());
			nodes.add(d.getIdPreTask());
			nodes.add(d.getIdTask());
		}
		// 0=未访问 1=访问中 2=已完成
		Map<String, Integer> state = new HashMap<>();
		List<String> stack = new ArrayList<>();
		for (String node : nodes) {
			if (dfsCycle(node, edges, state, stack)) {
				return stack;
			}
		}
		return new ArrayList<>();
	}

	private boolean dfsCycle(String node, Map<String, List<String>> edges,
			Map<String, Integer> state, List<String> stack) {
		Integer st = state.get(node);
		if (Integer.valueOf(1).equals(st)) {
			// 回到访问中的节点 —— 成环，把环上这一段切出来返回
			int idx = stack.indexOf(node);
			if (idx >= 0) {
				stack.add(node);
				return true;
			}
			return true;
		}
		if (Integer.valueOf(2).equals(st)) {
			return false;
		}
		state.put(node, 1);
		stack.add(node);
		List<String> next = edges.get(node);
		if (null != next) {
			for (String n : next) {
				if (dfsCycle(n, edges, state, stack)) {
					return true;
				}
			}
		}
		state.put(node, 2);
		stack.remove(stack.size() - 1);
		return false;
	}

	private String currentUserId() {
		try {
			return ComponentUtil.getCurrentUser().getUserId();
		} catch (Exception e) {
			log.debug("取当前用户失败（可能不在 UI 线程）", e);
			return null;
		}
	}
}
