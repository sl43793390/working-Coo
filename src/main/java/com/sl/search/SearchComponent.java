package com.sl.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.sl.app.component.util.ComFactory;
import com.sl.app.component.util.TabsUtil;
import com.sl.bo.SearchHit;
import com.sl.service.SearchService;
import com.sl.task.TaskDetailComponent;
import com.sl.app.component.util.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * 全局搜索（T2-8）：跨「我参与的项目」搜任务 / 评论 / 文档。
 */
@SpringComponent
@Scope("prototype")
public class SearchComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(SearchComponent.class);

	private final SearchService searchService;

	private TextField keywordField;
	private ComboBox<String> typeBox;
	private Grid<SearchHit> grid;
	private Span emptyHint;
	private Span tip;

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
		keywordField.setPlaceholder("输入关键词，回车搜索");
		keywordField.setWidth("320px");
		keywordField.addKeyDownListener(com.vaadin.flow.component.Key.ENTER, e -> doSearch());
		typeBox = ComFactory.getCombobox();
		typeBox.setItems("全部", "任务", "评论", "文档");
		typeBox.setValue("全部");
		Button searchBtn = ComFactory.getPrimaryBtn("搜索", VaadinIcon.SEARCH);
		searchBtn.addClickListener(e -> doSearch());
		toolbar.add(ComFactory.getLabel("关键词："), keywordField, ComFactory.getLabel("类型："),
				typeBox, searchBtn);

		tip = new Span("搜索范围为你参与的所有项目，可搜任务、评论、文档。建议输入 2 个字以上；单个字也能搜（走模糊匹配，稍慢）。");
		tip.addClassName("search-tip");
		add(tip);

		grid = ComFactory.getGrid(SearchHit.class);
		grid.setHeight("520px");
		grid.addColumn(SearchHit::getTypeName).setHeader("类型").setWidth("90px");
		grid.addColumn(SearchHit::getNameTitle).setHeader("标题").setResizable(true);
		grid.addColumn(h -> null == h.getCdContent() ? "" : h.getCdContent()).setHeader("内容摘要").setResizable(true);
		grid.addColumn(SearchHit::getIdProject).setHeader("项目ID").setWidth("200px");
		grid.addComponentColumn(h -> {
			Button b = ComFactory.getTertriayBtn("查看", VaadinIcon.LIST);
			b.setVisible(SearchHit.TYPE_TASK.equals(h.getCdType()));
			b.addClickListener(e -> openTask(h.getIdBiz()));
			return b;
		}).setHeader("操作").setWidth("90px");
		add(grid);

		emptyHint = ComFactory.getEmptyHint("输入关键词后点「搜索」");
		add(emptyHint);
	}

	private void doSearch() {
		String kw = keywordField.getValue();
		if (null == kw || kw.isBlank()) {
			ComFactory.notification("请输入搜索关键词");
			return;
		}
		String type = null;
		String label = typeBox.getValue();
		if ("任务".equals(label)) {
			type = SearchHit.TYPE_TASK;
		} else if ("评论".equals(label)) {
			type = SearchHit.TYPE_COMMENT;
		} else if ("文档".equals(label)) {
			type = SearchHit.TYPE_DOC;
		}
		List<SearchHit> hits;
		try {
			hits = searchService.search(kw, type);
		} catch (Exception e) {
			log.error("搜索失败，keyword={}", kw, e);
			ComFactory.notificationError("搜索失败", e);
			return;
		}
		grid.setItems(hits);
		ComFactory.toggleEmptyHint(emptyHint, hits.size());
		if (hits.isEmpty() && kw.trim().length() < 2) {
			ComFactory.notification("关键词太短，请至少输入 2 个字符");
		} else if (hits.isEmpty()) {
			ComFactory.notification("没有找到匹配的内容");
		}
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
