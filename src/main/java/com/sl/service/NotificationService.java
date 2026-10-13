package com.sl.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.entity.Notification;
import com.sl.entity.Schedule;
import com.sl.entity.Task;
import com.sl.mapper.NotificationMapper;

import cn.hutool.core.util.IdUtil;

/**
 * 通知中心（T2-4）。
 * <p>
 * 写入侧统一走 {@link #push}：它负责判重，避免定时任务每小时扫一次就堆一条重复通知。
 * 读取侧给站内铃铛用：{@link #countUnread} 决定红点数字，{@link #page} 翻列表。
 */
@Service
public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
	private NotificationMapper notificationMapper;

	/**
	 * 推送一条通知。同一用户 + 同一业务对象 + 同一类型下已有记录（不论已读未读）就不重复推。
	 * <p>
	 * 判重不能带 FLAG_READ='N'：到期/里程碑提醒是每小时扫同一批记录，用户点掉红点后
	 * 下次扫描若只查未读会判重为 0，又插一条，红点被反复点亮。
	 *
	 * @param idUser   接收人
	 * @param cdType   {@link Notification} 里的 TYPE_* 常量
	 * @param idBiz    业务对象 ID（任务 ID / 里程碑 ID），用于判重与跳转
	 * @param idProject 项目 ID，可为空
	 * @param title    标题
	 * @param content  正文
	 * @return 是否真的写入了一条
	 */
	public boolean push(String idUser, String cdType, String idBiz, String idProject,
			String title, String content) {
		if (null == idUser || idUser.isBlank() || null == cdType) {
			return false;
		}
		try {
			if (notificationMapper.countUnreadBiz(idUser, cdType, idBiz) > 0) {
				return false;
			}
			Notification n = new Notification();
			n.setIdNotification(IdUtil.getSnowflakeNextIdStr());
			n.setIdUser(idUser);
			n.setCdType(cdType);
			n.setIdBiz(idBiz);
			n.setIdProject(idProject);
			n.setCdTitle(title);
			n.setCdContent(content);
			n.setFlagRead("N");
			n.setDtCreate(new Date());
			return notificationMapper.insert(n) > 0;
		} catch (Exception e) {
			// 通知失败不能阻断主流程（任务保存、评论发布都依赖它）
			log.error("推送通知失败，user={}, type={}, biz={}", idUser, cdType, idBiz, e);
			return false;
		}
	}

	/**
	 * 任务被指派给某个执行人。
	 */
	public void pushTaskAssigned(Task task, String idExeUser) {
		if (null == task || null == idExeUser) {
			return;
		}
		push(idExeUser, Notification.TYPE_ASSIGN, task.getIdTask(), task.getIdProject(),
				"任务指派：" + task.getNameTask(),
				"你被指派为任务「" + task.getNameTask() + "」的执行人，截止 " + task.getDtEnd());
	}

	/**
	 * 评论里被 @ 到的人。
	 */
	public void pushCommentMention(String idUser, Task task, String comment) {
		if (null == idUser || null == task) {
			return;
		}
		push(idUser, Notification.TYPE_COMMENT, task.getIdTask(), task.getIdProject(),
				"评论提及：" + task.getNameTask(),
				null == comment ? "" : comment);
	}

	/**
	 * 日程即将开始（T2-11，由调度器扫描后调用）。
	 */
	public void pushScheduleRemind(Schedule schedule) {
		if (null == schedule) {
			return;
		}
		push(schedule.getIdUser(), Notification.TYPE_SCHEDULE, schedule.getIdSchedule(), null,
				"日程提醒：" + schedule.getNameSchedule(),
				"日程「" + schedule.getNameSchedule() + "」将于 " + schedule.getDtSchedule() + " 开始");
	}

	/**
	 * 任务即将到期（由调度器扫描后调用）。
	 */
	public void pushTaskDue(Task task, String idExeUser) {
		if (null == task || null == idExeUser) {
			return;
		}
		push(idExeUser, Notification.TYPE_DUE, task.getIdTask(), task.getIdProject(),
				"即将到期：" + task.getNameTask(),
				"任务「" + task.getNameTask() + "」将于 " + task.getDtEnd() + " 到期");
	}

	public long countUnread(String idUser) {
		if (null == idUser) {
			return 0;
		}
		try {
			return notificationMapper.countUnread(idUser);
		} catch (Exception e) {
			log.error("查询未读通知数失败，user={}", idUser, e);
			return 0;
		}
	}

	public List<Notification> page(String idUser, String flagRead, int offset, int limit) {
		return notificationMapper.selectByUser(idUser, flagRead, offset, limit);
	}

	public long count(String idUser, String flagRead) {
		return notificationMapper.countByUser(idUser, flagRead);
	}

	/**
	 * 单条标记已读。idUser 必须传当前登录用户：SQL 带 ID_USER 条件，
	 * 只按通知 ID 更新的话，任何登录用户都能标记别人的通知已读。
	 */
	public void markRead(String idNotification, String idUser) {
		if (null == idUser) {
			return;
		}
		notificationMapper.markRead(idNotification, idUser);
	}

	public int markAllRead(String idUser) {
		return notificationMapper.markAllRead(idUser);
	}
}
