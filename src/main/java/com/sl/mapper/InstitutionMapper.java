package com.sl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.Institution;
import com.sl.entity.InstitutionExample;

public interface InstitutionMapper extends BaseMapper<Institution>{
    int deleteByExample(InstitutionExample example);

    int deleteByPrimaryKey(String idInstitution);

    int insert(Institution record);

    int insertSelective(Institution record);

    List<Institution> selectByExample(InstitutionExample example);

    Institution selectByPrimaryKey(String idInstitution);

    int updateByExampleSelective(@Param("record") Institution record, @Param("example") InstitutionExample example);

    int updateByExample(@Param("record") Institution record, @Param("example") InstitutionExample example);

    int updateByPrimaryKeySelective(Institution record);

    int updateByPrimaryKey(Institution record);

    int insertBatch(List<Institution> records);
}