package com.sl.util;

import java.util.ArrayList;
import java.util.List;

public class Constants {

	/**
	 * 任务状态枚举。
	 * 注意：必须是不可变集合。此处原先是可变 ArrayList，而 MyTaskComponent 是 prototype Bean，
	 * 每次实例化都会往里追加元素，导致任务状态下拉框里的选项越攒越多。
	 * 需要增删元素的地方请自行拷贝一份：new ArrayList<>(STATUS_LIST)。
	 */
	public static final List<String> STATUS_LIST = List.of(
			"未开始", "进行中", "已结束", "已中断", "已废弃", "未分配");

}
