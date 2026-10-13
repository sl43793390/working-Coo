package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.ProjectTaskFile;
import com.sl.entity.ProjectTaskFileExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectTaskFileMapper  extends BaseMapper<ProjectTaskFile>{
    int deleteByExample(ProjectTaskFileExample example);

    int deleteByPrimaryKey(String idFile);

    int insert(ProjectTaskFile record);

    int insertSelective(ProjectTaskFile record);

    List<ProjectTaskFile> selectByExample(ProjectTaskFileExample example);
    
    List<ProjectTaskFile> selectByProjectAndTadkId(@Param("projectId") String projectId,@Param("taskId") String taskId);

    ProjectTaskFile selectByPrimaryKey(String idFile);

    int updateByExampleSelective(@Param("record") ProjectTaskFile record, @Param("example") ProjectTaskFileExample example);

    int updateByExample(@Param("record") ProjectTaskFile record, @Param("example") ProjectTaskFileExample example);

    int updateByPrimaryKeySelective(ProjectTaskFile record);

    int updateByPrimaryKey(ProjectTaskFile record);

    int insertBatch(List<ProjectTaskFile> records);
}