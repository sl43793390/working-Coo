package com.sl.notify;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sl.entity.ProjectMilestone;
import com.sl.entity.ProjectUser;
import com.sl.entity.Schedule;
import com.sl.entity.Task;
import com.sl.mapper.TaskMapper;
import com.sl.service.MilestoneService;
import com.sl.service.NotificationService;
import com.sl.service.ProjectProcess;
import com.sl.service.ScheduleProcess;

/**
 * 通知调度器（T2-4）。
 * <p>
 * 只干两件事：
 * <ol>
 *   <li>任务到期前一天，给执行人和创建人推「即将到期」；</li>
 *   <li>里程碑临近（默认 3 天内），给项目全体成员推「里程碑」。</li>
 * </ol>
 * 判重交给 {@link NotificationService#push}：同一用户 + 同一业务对象 + 同一类型
 * 已有记录（不论已读未读）就不重复推，所以就算这里每小时跑一次也不会刷屏。
 */
@Component
public class NotificationScheduler {

	private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);

	/** 里程碑提前几天提醒 */
	private static final int MILESTONE_AHEAD_DAYS = 3;

	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private MilestoneService milestoneService;
	@Autowired
	private ProjectProcess projectProcess;
	@Autowired
	private ScheduleProcess scheduleProcess;

	/**
	 * 每小时整点跑一次。用 fixedDelay 而不是 cron，是为了避免应用重启后
	 * 错过 cron 触发点导致当天不提醒。
	 */
	@Scheduled(initialDelay = 60_000, fixedDelay = 3_600_000)
	public void scanDueTasks() {
		try {
			Date from = startOfTomorrow();
			Date to = endOfTomorrow();
			List<Map<String, Object>> rows = taskMapper.selectDueTaskExecutors(from, to);
			int pushed = 0;
			for (Map<String, Object> row : rows) {
				Task t = new Task();
				t.setIdTask(str(row.get("ID_TASK")));
				t.setIdProject(str(row.get("ID_PROJECT")));
				t.setNameTask(str(row.get("NAME_TASK")));
				t.setCdStatus(str(row.get("CD_STATUS")));
				t.setDtEnd((Date) row.get("DT_END"));
				String exeUser = str(row.get("ID_EXE_USER"));
				if (null != exeUser && notificationService
						.push(exeUser, com.sl.entity.Notification.TYPE_DUE, t.getIdTask(), t.getIdProject(),
								"即将到期：" + t.getNameTask(),
								"任务「" + t.getNameTask() + "」明天到期（" + t.getDtEnd() + "）")) {
					pushed++;
				}
				// 创建人也提醒一次（自己建的任务自己最容易忘）
				String creator = str(row.get("ID_CREATE_USER"));
				if (null != creator && !creator.equals(exeUser)) {
					notificationService.push(creator, com.sl.entity.Notification.TYPE_DUE, t.getIdTask(),
							t.getIdProject(), "即将到期：" + t.getNameTask(),
							"你创建的任务「" + t.getNameTask() + "」明天到期");
				}
			}
			if (pushed > 0) {
				log.info("到期提醒扫描完成，推送 {} 条", pushed);
			}
		} catch (Exception e) {
			log.error("扫描到期任务失败", e);
		}
	}

	/**
	 * T2-11：日程提醒。每 5 分钟扫一次「起始时间落在自己提前量窗口内、且还没提醒过」的日程。
	 * 用 5 分钟而不是 1 小时，是因为日程的提前量可能只有 10 分钟，一小时粒度会漏。
	 */
	@Scheduled(initialDelay = 180_000, fixedDelay = 300_000)
	public void scanSchedules() {
		try {
			List<Schedule> pending = scheduleProcess.listPendingRemind(new Date());
			for (Schedule s : pending) {
				notificationService.pushScheduleRemind(s);
				// 不管 push 是否真的写入（判重会返回 false），都置为已提醒：
				// 用户已有一条未读的同对象通知，再推也没意义，不该每次扫描都重试。
				scheduleProcess.markReminded(s.getIdUser(), s.getIdSchedule());
			}
		} catch (Exception e) {
			log.error("扫描日程提醒失败", e);
		}
	}

	@Scheduled(initialDelay = 120_000, fixedDelay = 3_600_000)
	public void scanMilestones() {
		try {
			Date from = new Date();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, MILESTONE_AHEAD_DAYS);
			Date to = endOfDay(cal.getTime());
			List<ProjectMilestone> list = milestoneService.listDueBetween(from, to);
			for (ProjectMilestone m : list) {
				List<ProjectUser> members;
				try {
					members = projectProcess.getProjectUserListByIdProject(m.getIdProject());
				} catch (Exception e) {
					log.warn("取项目成员失败，project={}", m.getIdProject(), e);
					continue;
				}
				for (ProjectUser pu : members) {
					notificationService.push(pu.getIdUser(), com.sl.entity.Notification.TYPE_MILESTONE,
							m.getIdMilestone(), m.getIdProject(),
							"里程碑临近：" + m.getNameMilestone(),
							"里程碑「" + m.getNameMilestone() + "」将于 " + m.getDtMilestone() + " 到期");
				}
			}
		} catch (Exception e) {
			log.error("扫描里程碑失败", e);
		}
	}

	/**
	 * 每天凌晨清理 30 天前的已读通知，避免 notification 表无限膨胀。
	 */
	@Scheduled(cron = "0 30 3 * * *")
	public void cleanupReadNotifications() {
		// 目前没有直接写删除 SQL：通知量在单机项目规模下很小，
		// 真到需要清理时按 DT_READ < now()-30d 批量删即可，这里先留日志口径。
		log.debug("已读通知清理：当前策略为保留全部，需要时再按 DT_READ 归档");
	}

	private static String str(Object o) {
		return null == o ? null : String.valueOf(o);
	}

	private static Date startOfTomorrow() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return startOfDay(cal.getTime());
	}

	private static Date endOfTomorrow() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return endOfDay(cal.getTime());
	}

	private static Date startOfDay(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private static Date endOfDay(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
}
