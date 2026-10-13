package com.sl.app.component.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.Element;

import cn.hutool.core.collection.CollectionUtil;

public class TabsUtil {

	
	private static final Logger log = LoggerFactory.getLogger(TabsUtil.class);

	/**
	 * Tab -> 内容组件 的映射，仅用于「切换项目时遍历已打开的视图并刷新数据」。
	 * 用 WeakHashMap：Tab 被关闭后条目会被自动回收，不会造成内存泄漏。
	 */
	private static final Map<TabSheet, Map<Tab, Component>> TAB_CONTENT =
			Collections.synchronizedMap(new WeakHashMap<>());

	private static void rememberContent(TabSheet tabs, Tab tab, Component com) {
		TAB_CONTENT.computeIfAbsent(tabs, k -> Collections.synchronizedMap(new WeakHashMap<>())).put(tab, com);
	}

	private static void forgetContent(TabSheet tabs, Tab tab) {
		Map<Tab, Component> m = TAB_CONTENT.get(tabs);
		if (null != m) {
			m.remove(tab);
		}
	}

	/**
	 * 取出当前 TabSheet 上所有仍打开的内容组件（按 tab 顺序）。
	 */
	public static List<Component> getTabContents(TabSheet tabs) {
		if (null == tabs) {
			return Collections.emptyList();
		}
		Map<Tab, Component> m = TAB_CONTENT.get(tabs);
		if (null == m || m.isEmpty()) {
			return Collections.emptyList();
		}
		List<Component> result = new ArrayList<>();
		for (int i = 0; i < tabs.getTabCount(); i++) {
			Component c = m.get(tabs.getTabAt(i));
			if (null != c) {
				result.add(c);
			}
		}
		return result;
	}

	/**
	 * 刷新当前 TabSheet 上所有已打开且实现了 Refreshable 的视图。
	 * 用于保存/修改数据后让列表页（我的任务、看板、甘特图等）立即反映最新数据，
	 * 不用再手动关掉 tab 重开。单个视图刷新失败不影响其它视图。
	 * @return 成功刷新的视图数
	 */
	public static int refreshRefreshableTabs() {
		int n = 0;
		for (Component c : getTabContents(getTabsheet())) {
			if (c instanceof Refreshable refreshable) {
				try {
					refreshable.refresh();
					n++;
				} catch (Exception e) {
					// 刷新失败不阻断其它视图，也不打断调用方流程
				}
			}
		}
		return n;
	}

	/**
	 * 关闭指定 key 的标签页（会自动清理内容映射）。
	 */
	public static boolean removeTab(TabSheet tabs, String key) {
		if (null == tabs) {
			return false;
		}
		Tab tab = getTabById(tabs, sanitizeId(key));
		if (null == tab) {
			return false;
		}
		forgetContent(tabs, tab);
		tabs.remove(tab);
		return true;
	}

	public static final String CAPTION_ADD_ROLE = "新增角色";
	public static final String CAPTION_ADD_USER = "新增用户";
	public static final String CAPTION_MODIFY_USER = "修改用户";

	public static final String CAPTION_USER_MANAGEMENT = "用户管理";

	public static final String CAPTION_ROLE_MANAGEMENT = "角色管理";

	/**
	 * 移除 tabsheet 上的所有 tab。
	 * 注意：TabSheet.getChildren() 只返回内部的 Tabs 头组件，
	 * 遍历 children 再 remove 是无效操作（一个 tab 都删不掉），
	 * 必须通过 getTabAt(i) 收集 Tab 快照后逐个 remove。
	 * @param tabsheet
	 */
	public static void removeAllTabs(TabSheet tabsheet) {
		if (null == tabsheet) {
			return;
		}
		List<Tab> tabs = new ArrayList<>();
		for (int i = 0; i < tabsheet.getTabCount(); i++) {
			tabs.add(tabsheet.getTabAt(i));
		}
		tabs.forEach(t -> forgetContent(tabsheet, t));
		tabs.forEach(tabsheet::remove);
	}
	public static Component getComponentByName(TabSheet tabsheet,String name) {
		Stream<Component> children = tabsheet.getChildren();
		List<Component> filter = children.filter(e ->{
			if (!(e instanceof Tabs)) {
				if(e.getId().get().equals(name)){
					return true;
				}
				return false;
			}
			return false;
		}).collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(filter)) {
			return filter.get(0);
		}
		return null;
	}
	public static Component getComponentByName(String name) {
		TabSheet tabsheet = getTabsheet();
		Stream<Component> children = tabsheet.getChildren();
		List<Component> filter = children.filter(e ->{
			if (!(e instanceof Tabs)) {
				if(e.getId().get().equals(name)){
					return true;
				}
				return false;
			}
			return false;
		}).collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(filter)) {
			return filter.get(0);
		}
		return null;
	}
	public static boolean checkTabExists(TabSheet tabsheet,String name) {
		if (null == tabsheet) {
			return false;
		}
		return null != getTabById(tabsheet, sanitizeId(name));
	}
	public static void setSelectedTab(String name) {
		Tab tabByName = getTabByName(name);
		getTabsheet().setSelectedTab(tabByName);
	}
	public static boolean checkTabExists(String name) {
		return checkTabExists(getTabsheet(), name);
	}
	public static void removeAllTabs() {
		removeAllTabs(getTabsheet());
	}
	public static Tab getTabByName(String name) {
		return getTabById(getTabsheet(), sanitizeId(name));
	}
//	public static boolean checkTabExists(String name) {
//		MyTabsheet tabsheet = getTabsheet();
//		Component tabByName = getComponentByName(tabsheet,name);
//		if (null != tabByName) {
//			return true;
//		}
//		return false;
//	}
	
	public static TabSheet getTabsheet() {
		return MainTabsHolder.current();
	}
	
	/**
	 * add tab
	 * @param tabs
	 * @param text
	 */
	public static void addTab(TabSheet tabs, String text){
		Tab t = new Tab(new TabHeader(tabs, text));
		tabs.add(t,new Span("无内容"));
		tabs.setSelectedTab(t);
		t.setId(text);
	}
	/**
	 * 打开（或激活）一个标签页。
	 * key 用于去重和组件 id，title 用于显示。同一个 key 已打开时只激活、不新增，
	 * 避免重复点击菜单开出一大堆同名 tab。
	 *
	 * @return true=新建了标签页；false=已存在仅激活，或已达上限被拒绝
	 */
	public static boolean addComponentTab(TabSheet tabs, String key, String title, Component com) {
		if (null == tabs || null == com) {
			return false;
		}
		String id = sanitizeId(key);
		// 已存在同名标签页：直接激活，不再新建一个
		Tab existing = getTabById(tabs, id);
		if (null != existing) {
			tabs.setSelectedTab(existing);
			return false;
		}
		if (tabs.getTabCount() >= MAX_TABS) {
			ComFactory.notificationError("标签页最多同时打开 " + MAX_TABS + " 个，请先关闭不用的标签页");
			return false;
		}
		Tab t = new Tab(new TabHeader(tabs, title));
		t.setId(id);//必须放在tabsheet的add方法之前，否则会有奇怪的bug；
		com.setId(id);//必须放在tabsheet的add方法之前，否则会有奇怪的bug；
		tabs.add(t,com);
		rememberContent(tabs, t, com);
		tabs.setSelectedTab(t);
		return true;
	}

	public static boolean addComponentTab(TabSheet tabs, String text,Component com){
		return addComponentTab(tabs, text, text, com);
	}

	public static boolean addComponentTab(String text,Component com){
		return addComponentTab(getTabsheet(), text, text, com);
	}

	/**
	 * 在当前主 TabSheet 上打开标签页，key 与 title 分离。
	 * 适合「同一个功能、不同业务对象」的场景（例如编辑不同项目）。
	 */
	public static boolean addComponentTab(String key, String title, Component com){
		return addComponentTab(getTabsheet(), key, title, com);
	}

	public static boolean addComponentTab(TabSheet tabs, String text,Component com,boolean closeable){
		if (null == tabs || null == com) {
			return false;
		}
		String id = sanitizeId(text);
		Tab existing = getTabById(tabs, id);
		if (null != existing) {
			tabs.setSelectedTab(existing);
			return false;
		}
		Tab t = new Tab(new TabHeader(tabs, text,closeable));
		t.setId(id);//必须放在tabsheet的add方法之前，否则会有奇怪的bug；
		com.setId(id);//必须放在tabsheet的add方法之前，否则会有奇怪的bug；
		tabs.add(t,com);
		rememberContent(tabs, t, com);
		tabs.setSelectedTab(t);
		return true;
	}

	/**
	 * 同时打开的标签页上限，超出后拒绝新建并提示，防止无限堆叠拖垮浏览器。
	 */
	public static final int MAX_TABS = 15;

	/**
	 * Tab / Component 的 id 会写到 DOM 上，需过滤掉空格、斜杠等非法字符。
	 */
	private static String sanitizeId(String raw) {
		if (null == raw || raw.isBlank()) {
			return "tab-" + System.nanoTime();
		}
		String s = raw.replaceAll("[^\\w\\u4e00-\\u9fa5-]", "_");
		if (s.isEmpty()) {
			return "tab-" + System.nanoTime();
		}
		if (Character.isDigit(s.charAt(0))) {
			s = "t" + s;
		}
		return s;
	}

	/**
	 * 按 id 精确查找标签页（checkTabExists 只判断存在性，拿不到 Tab 实例）。
	 */
	public static Tab getTabById(TabSheet tabsheet, String id) {
		if (null == tabsheet) {
			return null;
		}
		for (int i = 0; i < tabsheet.getTabCount(); i++) {
			Tab tab = tabsheet.getTabAt(i);
			if (id.equals(tab.getId().orElse(null))) {
				return tab;
			}
		}
		return null;
	}

	/**
	 * 关闭除指定 key 之外的所有标签页（右键「关闭其他」用）。
	 */
	public static int closeOtherTabs(TabSheet tabsheet, String keepKey) {
		if (null == tabsheet) {
			return 0;
		}
		String keepId = sanitizeId(keepKey);
		List<Tab> toRemove = new ArrayList<>();
		for (int i = 0; i < tabsheet.getTabCount(); i++) {
			Tab tab = tabsheet.getTabAt(i);
			if (!keepId.equals(tab.getId().orElse(null))) {
				toRemove.add(tab);
			}
		}
		toRemove.forEach(tabsheet::remove);
		return toRemove.size();
	}

	/**
	 * 关闭所有标签页；传入 keepKey 时保留该标签页。
	 */
	public static int closeAllTabs(TabSheet tabsheet, String keepKey) {
		if (null == tabsheet) {
			return 0;
		}
		if (null == keepKey) {
			int count = tabsheet.getTabCount();
			removeAllTabs(tabsheet);
			return count;
		}
		return closeOtherTabs(tabsheet, keepKey);
	}
	public static boolean closeTargetTab(String title) {
		try {
			Tab tabByName = getTabByName(title);
			getTabsheet().remove(tabByName);
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public static void closeCurrentTab() {
		TabSheet tab = getTabsheet();
		tab.remove(tab.getSelectedTab());
	}
	
}
