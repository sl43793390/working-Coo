package com.sl.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.ProjectMilestone;
import com.sl.mapper.ProjectMilestoneMapper;

import cn.hutool.core.util.IdUtil;

/**
 * 项目里程碑（T2-6）。
 */
@Service
public class MilestoneService {

	private static final Logger log = LoggerFactory.getLogger(MilestoneService.class);

	@Autowired
	private ProjectMilestoneMapper milestoneMapper;

	public List<ProjectMilestone> listByProject(String idProject) {
		return milestoneMapper.selectByProject(idProject);
	}

	public ProjectMilestone getById(String idMilestone) {
		return milestoneMapper.selectByPrimaryKey(idMilestone);
	}

	public int save(ProjectMilestone m) {
		if (null == m.getIdMilestone() || m.getIdMilestone().isBlank()) {
			m.setIdMilestone(IdUtil.getSnowflakeNextIdStr());
			m.setDtCreate(new Date());
			m.setIdCreateUser(safeCurrentUserId());
			if (null == m.getCdStatus()) {
				m.setCdStatus(ProjectMilestone.STATUS_UNDONE);
			}
			return milestoneMapper.insert(m);
		}
		return milestoneMapper.updateByPrimaryKeySelective(m);
	}

	public void delete(String idMilestone) {
		milestoneMapper.deleteByPrimaryKey(idMilestone);
	}

	public void deleteByProject(String idProject) {
		milestoneMapper.deleteByProject(idProject);
	}

	/** 调度器扫描用：区间内未达成的里程碑 */
	public List<ProjectMilestone> listDueBetween(Date from, Date to) {
		return milestoneMapper.selectDueBetween(from, to);
	}

	private String safeCurrentUserId() {
		try {
			return ComponentUtil.getCurrentUser().getUserId();
		} catch (Exception e) {
			log.debug("取当前用户失败（可能运行在调度线程）", e);
			return null;
		}
	}
}
