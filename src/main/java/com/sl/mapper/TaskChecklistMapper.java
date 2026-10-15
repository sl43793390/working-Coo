package com.sl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sl.entity.TaskChecklistItem;

/**
 * 任务检查清单（S4）。
 */
public interface TaskChecklistMapper {

	List<TaskChecklistItem> selectByTask(@Param("idTask") String idTask);

	int insert(TaskChecklistItem item);

	/** 勾选/取消勾选 */
	int updateDone(@Param("idItem") String idItem, @Param("flagDone") String flagDone);

	int deleteByPrimaryKey(@Param("idItem") String idItem);

	/** 按任务清理（任务彻底删除 / 回收站清理） */
	int deleteByTask(@Param("idTask") String idTask);

	/** 按项目清理（项目彻底删除） */
	int deleteByProject(@Param("idProject") String idProject);
}
