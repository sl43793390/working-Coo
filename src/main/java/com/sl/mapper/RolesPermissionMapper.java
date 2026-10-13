package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.RolesPermission;
import com.sl.entity.RolesPermissionExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RolesPermissionMapper  extends BaseMapper<RolesPermission>{
    int deleteByExample(RolesPermissionExample example);

    int deleteByPrimaryKey(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    int insert(RolesPermission record);

    int insertSelective(RolesPermission record);

    List<RolesPermission> selectByExample(RolesPermissionExample example);

    RolesPermission selectByPrimaryKey(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

    int updateByExampleSelective(@Param("record") RolesPermission record, @Param("example") RolesPermissionExample example);

    int updateByExample(@Param("record") RolesPermission record, @Param("example") RolesPermissionExample example);

    int updateByPrimaryKeySelective(RolesPermission record);

    int updateByPrimaryKey(RolesPermission record);

    int insertBatch(List<RolesPermission> records);
}