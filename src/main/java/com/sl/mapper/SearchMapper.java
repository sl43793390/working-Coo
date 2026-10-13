package com.sl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sl.bo.SearchHit;

/**
 * 全文搜索（T2-8）。
 * <p>
 * 用 MySQL 自带的 ngram 全文索引（见 V9 迁移脚本），不引入 ES：
 * 项目规模下 MATCH...AGAINST 足够，少一个中间件就少一份运维成本。
 */
public interface SearchMapper {

	List<SearchHit> searchTasks(@Param("keyword") String keyword,
			@Param("projectIds") List<String> projectIds,
			@Param("limit") int limit);

	List<SearchHit> searchComments(@Param("keyword") String keyword,
			@Param("projectIds") List<String> projectIds,
			@Param("limit") int limit);

	List<SearchHit> searchDocs(@Param("keyword") String keyword,
			@Param("projectIds") List<String> projectIds,
			@Param("limit") int limit);

	// ---------------- LIKE 降级查询：全文索引缺失 / 单字关键词时使用 ----------------

	List<SearchHit> searchTasksLike(@Param("keyword") String keyword,
			@Param("projectIds") List<String> projectIds,
			@Param("limit") int limit);

	List<SearchHit> searchCommentsLike(@Param("keyword") String keyword,
			@Param("projectIds") List<String> projectIds,
			@Param("limit") int limit);

	List<SearchHit> searchDocsLike(@Param("keyword") String keyword,
			@Param("projectIds") List<String> projectIds,
			@Param("limit") int limit);
}
