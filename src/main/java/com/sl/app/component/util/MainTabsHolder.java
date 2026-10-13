package com.sl.app.component.util;

import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.UI;

/**
 * 把「当前 UI 的主 TabSheet」挂在 UI 实例上，供 TabsUtil 取用。
 *
 * 背景：主视图 AppLayoutAccordionView 原先同时标了 @Route + @SpringComponent + @UIScope，
 * TabsUtil 通过 applicationContext.getBean(AppLayoutAccordionView.class).getMainTabsheet()
 * 拿主 TabSheet —— 这样路由实例被 Spring 容器托管，多个浏览器会话会拿到同一个实例，
 * 存在跨 UI 共享的风险。改为把 TabSheet 注册到各自的 UI 上，天然按会话隔离。
 */
public class MainTabsHolder {

	private final TabSheet tabSheet;

	public MainTabsHolder(TabSheet tabSheet) {
		this.tabSheet = tabSheet;
	}

	public TabSheet getTabSheet() {
		return tabSheet;
	}

	/**
	 * 把主 TabSheet 绑定到当前 UI。
	 */
	public static void bind(UI ui, TabSheet tabSheet) {
		if (null == ui || null == tabSheet) {
			return;
		}
		com.vaadin.flow.component.ComponentUtil.setData(ui, MainTabsHolder.class, new MainTabsHolder(tabSheet));
	}

	/**
	 * 从当前 UI 取出主 TabSheet；UI 未就绪时返回 null，调用方需做空判断。
	 */
	public static TabSheet current() {
		UI ui = UI.getCurrent();
		if (null == ui) {
			return null;
		}
		MainTabsHolder holder = com.vaadin.flow.component.ComponentUtil.getData(ui, MainTabsHolder.class);
		return null == holder ? null : holder.getTabSheet();
	}
}
