package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.TaskComment;
import com.sl.entity.TaskCommentExample;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskCommentMapper extends BaseMapper<TaskComment>{
    int deleteByExample(TaskCommentExample example);

    int deleteByPrimaryKey(@Param("idTask") String idTask, @Param("dtComment") Date dtComment);

    int insert(TaskComment record);

    int insertSelective(TaskComment record);

    List<TaskComment> selectByExample(TaskCommentExample example);

    TaskComment selectByPrimaryKey(@Param("idTask") String idTask, @Param("dtComment") Date dtComment);

    int updateByExampleSelective(@Param("record") TaskComment record, @Param("example") TaskCommentExample example);

    int updateByExample(@Param("record") TaskComment record, @Param("example") TaskCommentExample example);

    int updateByPrimaryKeySelective(TaskComment record);

    int updateByPrimaryKey(TaskComment record);

    int insertBatch(List<TaskComment> records);
}