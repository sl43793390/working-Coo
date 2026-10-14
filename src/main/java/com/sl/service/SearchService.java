package com.sl.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.bo.SearchHit;
import com.sl.mapper.SearchMapper;

/**
 * 全局搜索。
 * <p>
 * 范围：仅当前项目（顶部下拉选中的项目），搜任务（名称+描述）和项目文档（文件名+备注）。
 * 匹配方式：LIKE 模糊匹配——任意长度的关键词（含单字）都能搜，中文分词场景下
 * 比 ngram 全文索引更符合「模糊搜索」的直觉，也不依赖建索引脚本是否执行过。
 */
@Service
public class SearchService {

	private static final Logger log = LoggerFactory.getLogger(SearchService.class);

	@Autowired
	private SearchMapper searchMapper;

	/** 每类最多返回多少条 */
	private static final int LIMIT_PER_TYPE = 50;

	/**
	 * @param keyword 搜索关键词（模糊匹配）
	 * @param cdType  SearchHit.TYPE_TASK / TYPE_DOC，null 表示全部
	 * @param idProject 当前项目 ID，空则返回空结果
	 */
	public List<SearchHit> search(String keyword, String cdType, String idProject) {
		List<SearchHit> hits = new ArrayList<>();
		if (null == keyword || keyword.isBlank() || null == idProject || idProject.isBlank()) {
			return hits;
		}
		String kw = keyword.trim();
		if (null == cdType || cdType.isBlank() || SearchHit.TYPE_TASK.equals(cdType)) {
			try {
				hits.addAll(searchMapper.searchTasksLike(kw, idProject, LIMIT_PER_TYPE));
			} catch (Exception e) {
				log.error("任务搜索失败，keyword={}", kw, e);
			}
		}
		if (null == cdType || cdType.isBlank() || SearchHit.TYPE_DOC.equals(cdType)) {
			try {
				hits.addAll(searchMapper.searchDocsLike(kw, idProject, LIMIT_PER_TYPE));
			} catch (Exception e) {
				log.error("文档搜索失败，keyword={}", kw, e);
			}
		}
		return hits;
	}
}
