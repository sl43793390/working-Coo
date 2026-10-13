package com.sl.mapper;

import com.sl.entity.TaskUser;
import com.sl.entity.TaskUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskUserMapper {
    int deleteByExample(TaskUserExample example);

    int deleteByPrimaryKey(@Param("idTask") String idTask, @Param("idExeUser") String idExeUser);

    int insert(TaskUser record);

    int insertSelective(TaskUser record);

    List<TaskUser> selectByExample(TaskUserExample example);

    TaskUser selectByPrimaryKey(@Param("idTask") String idTask, @Param("idExeUser") String idExeUser);

    int updateByExampleSelective(@Param("record") TaskUser record, @Param("example") TaskUserExample example);

    int updateByExample(@Param("record") TaskUser record, @Param("example") TaskUserExample example);

    int updateByPrimaryKeySelective(TaskUser record);

    int updateByPrimaryKey(TaskUser record);
}