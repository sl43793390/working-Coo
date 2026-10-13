package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.Menu;
import com.sl.entity.MenuExample;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MenuMapper extends BaseMapper<Menu>{
    int deleteByExample(MenuExample example);

    int deleteByPrimaryKey(String idMenu);

    int insert(Menu record);

    int insertSelective(Menu record);

    List<Menu> selectByExample(MenuExample example);

    Menu selectByPrimaryKey(String idMenu);

    int updateByExampleSelective(@Param("record") Menu record, @Param("example") MenuExample example);

    int updateByExample(@Param("record") Menu record, @Param("example") MenuExample example);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    int insertBatch(List<Menu> records);
    
    List<Menu> getMenuByRoleId(String roleId);
    
    ArrayList<Menu> selectAllMenusByUserId(String userId);
}