package com.sl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sl.entity.OperationLog;

public interface OperationLogMapper {

	int insert(OperationLog record);

	/** 某个业务对象的变更时间线 */
	List<OperationLog> selectByBiz(@Param("cdBizType") String cdBizType,
			@Param("idBiz") String idBiz,
			@Param("limit") int limit);

	/** 项目内最近的操作 */
	List<OperationLog> selectByProject(@Param("idProject") String idProject,
			@Param("limit") int limit);
}
