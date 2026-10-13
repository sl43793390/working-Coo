package com.sl.app.component.util;

/**
 * 会随「当前项目」变化而需要重新取数的视图组件。
 *
 * 背景：顶栏切换项目时，早期实现是 removeAllTabs 清空所有标签页再重建一个「项目列表」，
 * 用户正在编辑的任务、文档等未保存内容会全部丢失。改为只刷新数据、保留标签页，
 * 由实现了本接口的组件自行重新查询。
 */
public interface Refreshable {

	/**
	 * 重新加载本组件的数据。实现方必须保证可重复调用且不改变用户的输入状态。
	 */
	void refresh();
}
