package com.sl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.ProjectUser;
import com.sl.entity.Schedule;
import com.sl.entity.ScheduleExample;
import com.sl.entity.User;
import com.sl.mapper.ScheduleMapper;

@Service
public class ScheduleProcess {

	@Autowired
	private ScheduleMapper scheduleMapper;
	@Autowired
	private ProjectProcess projectProcess;
	@Autowired
	private UserManageMentProcess manageMentProcess;
	
	public List<Schedule> getScheduleByUserId(String userId){
		ScheduleExample example = new ScheduleExample();
		example.createCriteria().andIdUserEqualTo(userId);
		example.setOrderByClause(" DT_SCHEDULE ASC");
		List<Schedule> selectByExample = scheduleMapper.selectByExample(example);
		if (null == selectByExample || selectByExample.isEmpty()) {
			return new ArrayList<Schedule>();
		}
		return selectByExample;
	}
	
	public Integer deleteScheduleById(String idUser,String idSchedule) {
		return scheduleMapper.deleteByPrimaryKey(idUser, idSchedule);
	}

	public Integer saveSchedule(Schedule sc) {
		if (null == sc.getFlagReminded()) {
			// 新建时统一置为未提醒，否则调度器会因为 FLAG_REMINDED 为 NULL 一直扫不到
			sc.setFlagReminded("N");
		}
		return scheduleMapper.insertSelective(sc);
	}

	/**
	 * 编辑日程用全字段更新：null 字段也会写入（清空 DT_END / CD_NATURE / NBR_REMIND_MINUTES）。
	 * 不能用 updateByPrimaryKeySelective —— selective 会跳过 null 字段，
	 * 「全天→非全天」「提醒→不提醒」这类置空修改落不到库，回显就会错乱。
	 */
	public int updateSchedule(Schedule sc) {
		return scheduleMapper.updateScheduleEditable(sc);
	}

	/**
	 * T2-11：拖拽改期。只改起止时间，时长保持不变。
	 */
	public int reschedule(Schedule sc, Date newStart) {
		if (null == sc || null == newStart) {
			return 0;
		}
		Date oldStart = sc.getDtSchedule();
		long duration = (null == sc.getDtEnd() || null == oldStart)
				? 0L
				: sc.getDtEnd().getTime() - oldStart.getTime();
		sc.setDtSchedule(newStart);
		sc.setDtEnd(duration > 0 ? new Date(newStart.getTime() + duration) : null);
		// 改期后提醒要重新发，否则用户会被按旧时间提醒
		sc.setFlagReminded("N");
		return scheduleMapper.updateByPrimaryKeySelective(sc);
	}

	public List<Schedule> listPendingRemind(Date now) {
		return scheduleMapper.selectPendingRemind(now);
	}

	/**
	 * 按主键取单条日程（最新值）。拖拽 resize 等只改部分字段的操作，
	 * 必须基于最新行更新，不能用页面加载时的快照覆盖 FLAG_REMINDED 等字段。
	 */
	public Schedule getScheduleById(String idUser, String idSchedule) {
		return scheduleMapper.selectByPrimaryKey(idUser, idSchedule);
	}

	public int markReminded(String idUser, String idSchedule) {
		return scheduleMapper.markReminded(idUser, idSchedule);
	}
	
	public List<User> getProjectUserList(){
		List<ProjectUser> projectUserListByIdProject = projectProcess.getProjectUserListByIdProject(ComponentUtil.getCurrentProject().getIdProject());
		Set<String> userIds = projectUserListByIdProject.stream().map(e ->e.getIdUser()).collect(Collectors.toSet());
		List<User> userByIds = manageMentProcess.getUserByIds(userIds);
		return userByIds;
	}
}
