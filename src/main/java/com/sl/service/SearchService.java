package com.sl.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.bo.SearchHit;
import com.sl.mapper.SearchMapper;

/**
 * 全局搜索（S5）。
 * <p>
 * 范围：当前机构全部项目（跨项目），搜任务（名称+描述）、任务评论、
 * 项目文档（文件名+备注）、项目（名称+备注）。
 * <p>
 * 匹配策略：任务优先走 ngram 全文索引（自然语言模式），命中为空或关键词
 * 是单字（ngram 最小分词 2 字符，单字必然搜不到）时自动降级 LIKE；
 * 评论/文档/项目直接 LIKE——模糊匹配更符合「输入片段即可搜」的直觉，
 * 也不依赖索引脚本是否执行过。合并时按命中类型去重保序。
 */
@Service
public class SearchService {

	private static final Logger log = LoggerFactory.getLogger(SearchService.class);

	@Autowired
	private SearchMapper searchMapper;

	/** 每类最多返回多少条 */
	private static final int LIMIT_PER_TYPE = 50;

	/**
	 * @param keyword       搜索关键词（模糊匹配）
	 * @param cdType        SearchHit.TYPE_*，null 表示全部
	 * @param idInstitution 当前机构 ID，空则返回空结果
	 */
	public List<SearchHit> search(String keyword, String cdType, String idInstitution) {
		List<SearchHit> hits = new ArrayList<>();
		if (null == keyword || keyword.isBlank() || null == idInstitution || idInstitution.isBlank()) {
			return hits;
		}
		String kw = keyword.trim();
		boolean all = null == cdType || cdType.isBlank();

		if (all || SearchHit.TYPE_PROJECT.equals(cdType)) {
			hits.addAll(safe(() -> searchMapper.searchProjectsGlobal(kw, idInstitution, LIMIT_PER_TYPE), "项目"));
		}
		if (all || SearchHit.TYPE_TASK.equals(cdType)) {
			hits.addAll(searchTasks(kw, idInstitution));
		}
		if (all || SearchHit.TYPE_COMMENT.equals(cdType)) {
			hits.addAll(safe(() -> searchMapper.searchCommentsGlobal(kw, idInstitution, LIMIT_PER_TYPE), "评论"));
		}
		if (all || SearchHit.TYPE_DOC.equals(cdType)) {
			hits.addAll(safe(() -> searchMapper.searchDocsGlobal(kw, idInstitution, LIMIT_PER_TYPE), "文档"));
		}
		return hits;
	}

	/**
	 * 任务搜索：全文索引优先，LIKE 兜底，两类结果按任务 ID 去重。
	 * ngram 全文索引对单字关键词必然空命中，直接跳过省一次查询。
	 */
	private List<SearchHit> searchTasks(String kw, String idInstitution) {
		Map<String, SearchHit> merged = new LinkedHashMap<>();
		if (kw.length() >= 2) {
			for (SearchHit h : safe(() -> searchMapper.searchTasksFt(kw, idInstitution, LIMIT_PER_TYPE), "任务(全文索引)")) {
				merged.put(h.getIdBiz(), h);
			}
		}
		for (SearchHit h : safe(() -> searchMapper.searchTasksLikeGlobal(kw, idInstitution, LIMIT_PER_TYPE), "任务(LIKE)")) {
			merged.putIfAbsent(h.getIdBiz(), h);
		}
		return new ArrayList<>(merged.values());
	}

	/** 单类查询失败只记日志返回空，不让整页搜索白屏 */
	private interface Query {
		List<SearchHit> run();
	}

	private List<SearchHit> safe(Query q, String what) {
		try {
			return q.run();
		} catch (Exception e) {
			log.error("{}搜索失败，可能缺全文索引（执行 institutions.sql 尾部 ALTER）", what, e);
			return new ArrayList<>();
		}
	}
}
