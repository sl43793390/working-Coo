package com.sl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sl.entity.Notification;

/**
 * 站内通知。这里没有沿用其它表那套 MBG 生成的 Example，
 * 直接用 @Param 声明入参：通知的查询维度很固定（接收人 + 已读标记），
 * 引入 Example 只会多一堆用不上的代码。
 */
public interface NotificationMapper {

	int insert(Notification record);

	Notification selectByPrimaryKey(String idNotification);

	/**
	 * 分页取某个用户的通知（未读在前）。
	 */
	List<Notification> selectByUser(@Param("idUser") String idUser,
			@Param("flagRead") String flagRead,
			@Param("offset") int offset,
			@Param("limit") int limit);

	long countByUser(@Param("idUser") String idUser, @Param("flagRead") String flagRead);

	long countUnread(@Param("idUser") String idUser);

	/**
	 * 单条标记已读。必须带 ID_USER：防止越权标记他人的通知。
	 */
	int markRead(@Param("idNotification") String idNotification, @Param("idUser") String idUser);

	/**
	 * 全部标记已读。
	 */
	int markAllRead(@Param("idUser") String idUser);

	int deleteByPrimaryKey(String idNotification);

	/**
	 * 项目被彻底删除时，清掉该项目相关的所有通知（到期提醒等）。
	 */
	int deleteByProject(@Param("idProject") String idProject);

	/**
	 * 防重复推送：查同一用户 + 同一业务对象 + 同一类型是否已有通知。
	 * 不看 FLAG_READ——已读之后再扫到同一条记录时不能重新插一条，
	 * 否则用户点掉红点后，下一次定时扫描又把红点亮起来。
	 * 到期提醒是定时任务，不判重的话每小时跑一次就会堆一条。
	 */
	long countUnreadBiz(@Param("idUser") String idUser,
			@Param("cdType") String cdType,
			@Param("idBiz") String idBiz);
}
