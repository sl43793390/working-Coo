package com.sl.search;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.ComponentUtil;
import com.sl.app.component.util.Refreshable;
import com.sl.app.component.util.TabsUtil;
import com.sl.bo.SearchHit;
import com.sl.entity.Project;
import com.sl.service.SearchService;
import com.sl.task.TaskDetailComponent;
import com.sl.app.component.editor.QuillEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 全局搜索：搜「当前项目」下的任务（名称+描述）和项目文档（文件名+备注），LIKE 模糊匹配。
 */
@SpringComponent
@Scope("prototype")
public class SearchComponent extends VerticalLayout implements Refreshable {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(SearchComponent.class);

	/** 内容摘要列最多显示多少字符，防止超长描述把列撑出横向滚动条 */
	private static final int SUMMARY_MAX_LEN = 80;

	private final SearchService searchService;

	private TextField keywordField;
	private ComboBox<String> typeBox;
	private Grid<SearchHit> grid;
	private Span emptyHint;
	private Span tip;
	/** 最近一次搜索的关键词，切项目后按它重查 */
	private String lastKeyword;

	public SearchComponent(@Autowired SearchService searchService) {
		this.searchService = searchService;
		setWidthFull();
		setPadding(true);
		add(ComFactory.getTitle("全局搜索"));
		add(ComFactory.getHorizontalLine());

		HorizontalLayout toolbar = ComFactory.getResponsiveHorizontalLayoutLeft();
		toolbar.setAlignItems(Alignment.CENTER);
		add(toolbar);
		keywordField = ComFactory.getTextField();
		keywordField.setPlaceholder("输入任务/文档关键词，回车搜索");
		keywordField.setWidth("320px");
		keywordField.addKeyDownListener(com.vaadin.flow.component.Key.ENTER, e -> doSearch());
		typeBox = ComFactory.getCombobox();
		typeBox.setItems("全部", "任务", "文档");
		typeBox.setValue("全部");
		Button searchBtn = ComFactory.getPrimaryBtn("搜索", VaadinIcon.SEARCH);
		searchBtn.addClickListener(e -> doSearch());
		toolbar.add(ComFactory.getLabel("关键词："), keywordField, ComFactory.getLabel("类型："),
				typeBox, searchBtn);

		tip = new Span();
		tip.addClassName("search-tip");
		add(tip);

		grid = ComFactory.getGrid(SearchHit.class);
		grid.setWidthFull();
		grid.setHeight("520px");
		grid.setSelectionMode(SelectionMode.NONE);
		grid.addColumn(SearchHit::getTypeName).setHeader("类型").setAutoWidth(true).setFlexGrow(0);
		// autoWidth + flexGrow：列宽随容器伸缩，内容不会把表格撑出横向滚动条
		grid.addColumn(this::plainTitle).setHeader("标题").setAutoWidth(true).setFlexGrow(1).setResizable(true);
		grid.addColumn(h -> plainSummary(h.getCdContent())).setHeader("内容摘要").setAutoWidth(true)
				.setFlexGrow(2).setResizable(true);
		grid.addComponentColumn(h -> {
			Button b = ComFactory.getTertriayBtn("查看", VaadinIcon.LIST);
			b.setVisible(SearchHit.TYPE_TASK.equals(h.getCdType()));
			b.addClickListener(e -> openTask(h.getIdBiz()));
			return b;
		}).setHeader("操作").setAutoWidth(true).setFlexGrow(0);
		add(grid);

		emptyHint = ComFactory.getEmptyHint("输入关键词后点「搜索」");
		add(emptyHint);

		updateTip();
	}

	@Override
	public void refresh() {
		// 顶栏切换项目：清空旧结果，提示语跟着新项目走
		lastKeyword = null;
		grid.setItems(new ArrayList<>());
		ComFactory.toggleEmptyHint(emptyHint, -1);
		updateTip();
	}

	private void updateTip() {
		Project p = ComponentUtil.getCurrentProject();
		String projectName = null == p ? "（未选择）" : p.getNameProject();
		tip.setText("搜索范围为当前项目「" + projectName + "」下的任务（名称、描述）和项目文档（文件名、备注），"
				+ "支持模糊匹配：输入任意片段即可，不必输全。");
	}

	private void doSearch() {
		Project p = ComponentUtil.getCurrentProject();
		if (null == p) {
			ComFactory.notification("请先在顶部选择项目");
			return;
		}
		String kw = keywordField.getValue();
		if (null == kw || kw.isBlank()) {
			ComFactory.notification("请输入搜索关键词");
			return;
		}
		String type = null;
		String label = typeBox.getValue();
		if ("任务".equals(label)) {
			type = SearchHit.TYPE_TASK;
		} else if ("文档".equals(label)) {
			type = SearchHit.TYPE_DOC;
		}
		List<SearchHit> hits;
		try {
			hits = searchService.search(kw, type, p.getIdProject());
		} catch (Exception e) {
			log.error("搜索失败，keyword={}", kw, e);
			ComFactory.notificationError("搜索失败", e);
			return;
		}
		lastKeyword = kw;
		grid.setItems(hits);
		ComFactory.toggleEmptyHint(emptyHint, hits.size());
		if (hits.isEmpty()) {
			ComFactory.notification("在当前项目下没有找到匹配的内容");
		}
	}

	/** 标题（任务名/文件名）纯文本化并截断 */
	private String plainTitle(SearchHit h) {
		return ellipsis(plain(h.getNameTitle()), 50);
	}

	/** 描述/备注可能是富文本 HTML，转纯文本再截断，避免撑宽列 */
	private String plainSummary(String content) {
		return ellipsis(plain(content), SUMMARY_MAX_LEN);
	}

	private String plain(String s) {
		if (null == s || s.isBlank()) {
			return "";
		}
		return QuillEditor.htmlToPlainText(s);
	}

	private String ellipsis(String s, int max) {
		if (null == s || s.length() <= max) {
			return null == s ? "" : s;
		}
		return s.substring(0, max) + "…";
	}

	private void openTask(String idTask) {
		com.sl.entity.Task t = ComponentUtil.applicationContext
				.getBean(com.sl.service.TaskProcess.class).getTaskById(idTask);
		if (null == t) {
			ComFactory.notification("任务不存在或已被删除");
			return;
		}
		TaskDetailComponent c = ComponentUtil.applicationContext.getBean(TaskDetailComponent.class);
		c.setTask(t);
		c.initContent();
		TabsUtil.addComponentTab("任务详情-" + idTask, "任务详情", c);
	}
}
