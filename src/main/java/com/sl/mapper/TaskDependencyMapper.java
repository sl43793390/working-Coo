package com.sl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sl.entity.TaskDependency;

public interface TaskDependencyMapper {

	int insert(TaskDependency record);

	int deleteByPrimaryKey(String idDependency);

	/** 某个任务的所有前置依赖 */
	List<TaskDependency> selectByTask(@Param("idTask") String idTask);

	/** 以某个任务为前置的所有后继依赖 */
	List<TaskDependency> selectByPreTask(@Param("idPreTask") String idPreTask);

	/** 项目下的全部依赖（甘特图连线 / 环路检测一次性取） */
	List<TaskDependency> selectByProject(@Param("idProject") String idProject);

	long countPair(@Param("idTask") String idTask, @Param("idPreTask") String idPreTask);

	/** 任务删除时连同依赖一起清掉 */
	int deleteByTask(@Param("idTask") String idTask);
}
