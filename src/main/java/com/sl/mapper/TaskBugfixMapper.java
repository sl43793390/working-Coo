package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.TaskBugfix;
import com.sl.entity.TaskBugfixExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskBugfixMapper extends BaseMapper<TaskBugfix>{
    int deleteByExample(TaskBugfixExample example);

    int deleteByPrimaryKey(String idTask);

    int insert(TaskBugfix record);

    int insertSelective(TaskBugfix record);

    List<TaskBugfix> selectByExample(TaskBugfixExample example);

    TaskBugfix selectByPrimaryKey(String idTask);

    int updateByExampleSelective(@Param("record") TaskBugfix record, @Param("example") TaskBugfixExample example);

    int updateByExample(@Param("record") TaskBugfix record, @Param("example") TaskBugfixExample example);

    int updateByPrimaryKeySelective(TaskBugfix record);

    int updateByPrimaryKey(TaskBugfix record);

    int insertBatch(List<TaskBugfix> records);
}