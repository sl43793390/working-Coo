package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.TaskSubtask;
import com.sl.entity.TaskSubtaskExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskSubtaskMapper extends BaseMapper<TaskSubtask>{
    int deleteByExample(TaskSubtaskExample example);

    int deleteByPrimaryKey(String idSubtask);

    int insert(TaskSubtask record);

    int insertSelective(TaskSubtask record);

    List<TaskSubtask> selectByExample(TaskSubtaskExample example);

    TaskSubtask selectByPrimaryKey(String idSubtask);

    int updateByExampleSelective(@Param("record") TaskSubtask record, @Param("example") TaskSubtaskExample example);

    int updateByExample(@Param("record") TaskSubtask record, @Param("example") TaskSubtaskExample example);

    int updateByPrimaryKeySelective(TaskSubtask record);

    int updateByPrimaryKey(TaskSubtask record);

    int insertBatch(List<TaskSubtask> records);
}