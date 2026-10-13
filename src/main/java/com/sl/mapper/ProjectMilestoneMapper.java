package com.sl.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sl.entity.ProjectMilestone;

public interface ProjectMilestoneMapper {

	int insert(ProjectMilestone record);

	int updateByPrimaryKeySelective(ProjectMilestone record);

	ProjectMilestone selectByPrimaryKey(String idMilestone);

	List<ProjectMilestone> selectByProject(@Param("idProject") String idProject);

	/** 到期提醒用：扫描落在 [from, to] 区间内且未达成的里程碑 */
	List<ProjectMilestone> selectDueBetween(@Param("from") Date from, @Param("to") Date to);

	int deleteByPrimaryKey(String idMilestone);

	int deleteByProject(@Param("idProject") String idProject);
}
