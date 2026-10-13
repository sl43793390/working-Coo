package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.ProjectType;
import com.sl.entity.ProjectTypeExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectTypeMapper extends BaseMapper<ProjectType>{
    int deleteByExample(ProjectTypeExample example);

    int deleteByPrimaryKey(@Param("idProject") String idProject, @Param("cdType") String cdType);

    int insert(ProjectType record);

    int insertSelective(ProjectType record);

    List<ProjectType> selectByExample(ProjectTypeExample example);

    int updateByExampleSelective(@Param("record") ProjectType record, @Param("example") ProjectTypeExample example);

    int updateByExample(@Param("record") ProjectType record, @Param("example") ProjectTypeExample example);

    int insertBatch(List<ProjectType> records);
}