package com.sl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sl.bo.SearchHit;

/**
 * 全局搜索：当前项目内的任务 / 文档，LIKE 模糊匹配。
 */
public interface SearchMapper {

	List<SearchHit> searchTasksLike(@Param("keyword") String keyword,
			@Param("idProject") String idProject,
			@Param("limit") int limit);

	List<SearchHit> searchDocsLike(@Param("keyword") String keyword,
			@Param("idProject") String idProject,
			@Param("limit") int limit);
}
