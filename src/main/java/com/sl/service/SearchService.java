package com.sl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.app.component.util.ComponentUtil;
import com.sl.bo.SearchHit;
import com.sl.entity.Project;
import com.sl.mapper.SearchMapper;

/**
 * 全文搜索（T2-8）。
 * <p>
 * 首选 MySQL ngram 全文索引（见 institutions.sql 的 FULLTEXT 建索引段落），
 * 两条兜底路径保证「一定能搜出东西」：
 * <ol>
 *   <li>关键词不足 2 个字：ngram 按 2 字分词，单字 MATCH 永远空结果，直接走 LIKE；</li>
 *   <li>库里没建 FULLTEXT 索引（脚本没执行）：MATCH 会抛 SQL 异常，捕获后整体降级 LIKE。</li>
 * </ol>
 * 搜索范围恒定为「当前用户参与的项目」，不接受任意项目 ID，避免越权看到别人的数据。
 */
@Service
public class SearchService {

	private static final Logger log = LoggerFactory.getLogger(SearchService.class);

	@Autowired
	private SearchMapper searchMapper;
	@Autowired
	private ProjectProcess projectProcess;

	/** 每类最多返回多少条 */
	private static final int LIMIT_PER_TYPE = 30;

	public List<SearchHit> search(String keyword, String cdType) {
		if (null == keyword || keyword.isBlank()) {
			return new ArrayList<>();
		}
		List<String> projectIds = myProjectIds();
		if (projectIds.isEmpty()) {
			return new ArrayList<>();
		}
		String kw = keyword.trim();
		// ngram_token_size=2：单字在全文索引下永远不命中，直接走 LIKE（LIKE 支持单字）
		if (kw.length() < 2) {
			return fallbackLike(kw, cdType, projectIds);
		}
		List<SearchHit> result = new ArrayList<>();
		try {
			if (wantType(cdType, SearchHit.TYPE_TASK)) {
				result.addAll(searchMapper.searchTasks(kw, projectIds, LIMIT_PER_TYPE));
			}
			if (wantType(cdType, SearchHit.TYPE_COMMENT)) {
				result.addAll(searchMapper.searchComments(kw, projectIds, LIMIT_PER_TYPE));
			}
			if (wantType(cdType, SearchHit.TYPE_DOC)) {
				result.addAll(searchMapper.searchDocs(kw, projectIds, LIMIT_PER_TYPE));
			}
			return result;
		} catch (Exception e) {
			// 索引还没建（institutions.sql 的 FULLTEXT 段落未执行）时 MATCH 直接报 SQL 异常，
			// 降级成 LIKE 查询保证功能可用。此前降级只搜任务名，评论/文档永远搜不到——已补齐。
			log.warn("全文检索失败，降级为 LIKE 查询，keyword={}", kw, e);
		}
		return fallbackLike(kw, cdType, projectIds);
	}

	/** cdType 为空表示「全部」 */
	private boolean wantType(String cdType, String type) {
		return null == cdType || cdType.isBlank() || type.equals(cdType);
	}

	/**
	 * LIKE 降级：任务（名称+描述）、评论、文档（文件名+备注）三类全查。
	 * 三条查询任何一条失败都不影响其它类别。
	 */
	private List<SearchHit> fallbackLike(String kw, String cdType, List<String> projectIds) {
		List<SearchHit> hits = new ArrayList<>();
		try {
			if (wantType(cdType, SearchHit.TYPE_TASK)) {
				hits.addAll(searchMapper.searchTasksLike(kw, projectIds, LIMIT_PER_TYPE));
			}
		} catch (Exception e) {
			log.error("任务 LIKE 搜索失败，keyword={}", kw, e);
		}
		try {
			if (wantType(cdType, SearchHit.TYPE_COMMENT)) {
				hits.addAll(searchMapper.searchCommentsLike(kw, projectIds, LIMIT_PER_TYPE));
			}
		} catch (Exception e) {
			log.error("评论 LIKE 搜索失败，keyword={}", kw, e);
		}
		try {
			if (wantType(cdType, SearchHit.TYPE_DOC)) {
				hits.addAll(searchMapper.searchDocsLike(kw, projectIds, LIMIT_PER_TYPE));
			}
		} catch (Exception e) {
			log.error("文档 LIKE 搜索失败，keyword={}", kw, e);
		}
		return hits;
	}

	/** 当前用户参与的项目 ID */
	private List<String> myProjectIds() {
		try {
			if (null == ComponentUtil.getCurrentUser()) {
				return new ArrayList<>();
			}
			List<Project> projects = projectProcess.getCurrentUserProjects();
			return projects.stream().map(Project::getIdProject).distinct().collect(Collectors.toList());
		} catch (Exception e) {
			log.warn("取当前用户项目失败", e);
			return new ArrayList<>();
		}
	}
}
