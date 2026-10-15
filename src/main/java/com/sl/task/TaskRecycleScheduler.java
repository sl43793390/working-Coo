package com.sl.task;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sl.service.TaskProcess;

/**
 * 任务回收站自动清理（S1）。
 * <p>
 * 每天凌晨 2 点物理删除「进回收站超过 30 天」的任务及其关联数据。
 * 软删除语义要求保留期有限：回收站只兜"误删"，不兜"永久归档"，
 * 不清理的话数据会无限膨胀。
 */
@Component
public class TaskRecycleScheduler {

	private static final Logger log = LoggerFactory.getLogger(TaskRecycleScheduler.class);

	/** 回收站保留天数 */
	public static final int RETENTION_DAYS = 30;

	private final TaskProcess taskProcess;

	public TaskRecycleScheduler(@Autowired TaskProcess taskProcess) {
		this.taskProcess = taskProcess;
	}

	/** 每天凌晨 2 点执行。生产多实例部署时只应有一个实例开启（单机部署现状下无需处理） */
	@Scheduled(cron = "0 0 2 * * ?")
	public void purgeExpiredTasks() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -RETENTION_DAYS);
		Date before = cal.getTime();
		try {
			int purged = taskProcess.purgeExpiredDeletedTasks(before);
			if (purged > 0) {
				log.info("任务回收站清理完成：本次物理删除 {} 个任务（删除时间早于 {}）", purged, before);
			}
		} catch (Exception e) {
			// 后台清理失败只记日志，不影响次日重试
			log.error("任务回收站自动清理失败", e);
		}
	}
}
