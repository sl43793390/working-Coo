package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.ProjectGroup;
import com.sl.entity.ProjectGroupExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectGroupMapper extends BaseMapper<ProjectGroup>{
    int deleteByExample(ProjectGroupExample example);

    int deleteByPrimaryKey(@Param("idProject") String idProject, @Param("cdGroup") String cdGroup);

    int insert(ProjectGroup record);

    int insertSelective(ProjectGroup record);

    List<ProjectGroup> selectByExample(ProjectGroupExample example);

    int updateByExampleSelective(@Param("record") ProjectGroup record, @Param("example") ProjectGroupExample example);

    int updateByExample(@Param("record") ProjectGroup record, @Param("example") ProjectGroupExample example);

    int insertBatch(List<ProjectGroup> records);
}