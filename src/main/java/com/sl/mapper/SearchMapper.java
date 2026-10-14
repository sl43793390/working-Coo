package com.sl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sl.bo.SearchHit;

/**
 * 全局搜索（S5：跨项目、按机构过滤）。
 * <p>
 * 任务用 ngram 全文索引（NAME_TASK/CD_DESCRIPTION、CD_COMMENT 已建索引），
 * 中文短词（单字）全文索引搜不到时由 Service 层自动降级 LIKE。
 * 项目/文档量小且字段杂，直接 LIKE。
 */
public interface SearchMapper {

	List<SearchHit> searchTasksLike(@Param("keyword") String keyword,
			@Param("idProject") String idProject,
			@Param("limit") int limit);

	List<SearchHit> searchDocsLike(@Param("keyword") String keyword,
			@Param("idProject") String idProject,
			@Param("limit") int limit);

	// ==================== S5 跨项目（按机构） ====================

	/** 任务：ngram 全文索引（机构内、排除回收站任务） */
	List<SearchHit> searchTasksFt(@Param("keyword") String keyword,
			@Param("idInstitution") String idInstitution,
			@Param("limit") int limit);

	/** 任务：LIKE 兜底（全文索引搜不到 / 单字关键词） */
	List<SearchHit> searchTasksLikeGlobal(@Param("keyword") String keyword,
			@Param("idInstitution") String idInstitution,
			@Param("limit") int limit);

	/** 评论：搜评论内容，标题带所属任务名 */
	List<SearchHit> searchCommentsGlobal(@Param("keyword") String keyword,
			@Param("idInstitution") String idInstitution,
			@Param("limit") int limit);

	/** 文档：文件名 + 备注 */
	List<SearchHit> searchDocsGlobal(@Param("keyword") String keyword,
			@Param("idInstitution") String idInstitution,
			@Param("limit") int limit);

	/** 项目：项目名 + 备注 */
	List<SearchHit> searchProjectsGlobal(@Param("keyword") String keyword,
			@Param("idInstitution") String idInstitution,
			@Param("limit") int limit);
}
