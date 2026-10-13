package com.sl.mapper;

import com.sl.entity.Project;
import com.sl.entity.ProjectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ProjectMapper extends BaseMapper<Project>{
    int deleteByExample(ProjectExample example);

    int deleteByPrimaryKey(String idProject);

    int insert(Project record);

    int insertSelective(Project record);

    List<Project> selectByExample(ProjectExample example);

    Project selectByPrimaryKey(String idProject);

    int updateByExampleSelective(@Param("record") Project record, @Param("example") ProjectExample example);

    int updateByExample(@Param("record") Project record, @Param("example") ProjectExample example);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);

    int insertBatch(List<Project> records);
    
    List<Project> selectByConditionForadmin(@Param("institution")String institution,@Param("pName") String pName,@Param("prior") String prior);
    List<Project> selectByCondition(@Param("institution")String institution,@Param("idUser")String idUser,@Param("pName") String pName,@Param("prior") String prior);

    /** 分页版本，供列表懒加载使用（offset/limit） */
    List<Project> selectByConditionForadminPage(@Param("institution")String institution,@Param("pName") String pName,@Param("prior") String prior,
                                                @Param("offset") int offset, @Param("limit") int limit);
    long countByConditionForadmin(@Param("institution")String institution,@Param("pName") String pName,@Param("prior") String prior);
    List<Project> selectByConditionPage(@Param("institution")String institution,@Param("idUser")String idUser,@Param("pName") String pName,@Param("prior") String prior,
                                        @Param("offset") int offset, @Param("limit") int limit);
    long countByCondition(@Param("institution")String institution,@Param("idUser")String idUser,@Param("pName") String pName,@Param("prior") String prior);
}