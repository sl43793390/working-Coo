package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.ProjectUser;
import com.sl.entity.ProjectUserExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectUserMapper extends BaseMapper<ProjectUser>{
    int deleteByExample(ProjectUserExample example);

    int deleteByPrimaryKey(@Param("idProject") String idProject, @Param("idUser") String idUser);

    int insert(ProjectUser record);

    int insertSelective(ProjectUser record);

    List<ProjectUser> selectByExample(ProjectUserExample example);

    int updateByExampleSelective(@Param("record") ProjectUser record, @Param("example") ProjectUserExample example);

    int updateByExample(@Param("record") ProjectUser record, @Param("example") ProjectUserExample example);

    /**
     * 按主键更新非空字段。XML 里一直有这两条语句，但接口没声明，
     * 改项目成员角色（T2-5）时才发现调不到，这里补上。
     */
    int updateByPrimaryKeySelective(ProjectUser record);

    int updateByPrimaryKey(ProjectUser record);

    int insertBatch(List<ProjectUser> records);
}