package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.ProjectTag;
import com.sl.entity.ProjectTagExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectTagMapper extends BaseMapper<ProjectTag>{
    int deleteByExample(ProjectTagExample example);

    int deleteByPrimaryKey(@Param("idProject") String idProject, @Param("cdTag") String cdTag);

    int insert(ProjectTag record);

    int insertSelective(ProjectTag record);

    List<ProjectTag> selectByExample(ProjectTagExample example);

    int updateByExampleSelective(@Param("record") ProjectTag record, @Param("example") ProjectTagExample example);

    int updateByExample(@Param("record") ProjectTag record, @Param("example") ProjectTagExample example);

    int insertBatch(List<ProjectTag> records);
}