package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 项目里程碑（T2-6）。关键节点，到期临近时由通知调度器推送提醒。
 */
@TableName("project_milestone")
public class ProjectMilestone implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String STATUS_UNDONE = "未达成";
	public static final String STATUS_DONE = "已达成";

	private String idMilestone;

	private String idProject;

	private String nameMilestone;

	private Date dtMilestone;

	private String cdStatus;

	private String cdDescription;

	private String idCreateUser;

	private Date dtCreate;

	public String getIdMilestone() {
		return idMilestone;
	}

	public void setIdMilestone(String idMilestone) {
		this.idMilestone = idMilestone;
	}

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	public String getNameMilestone() {
		return nameMilestone;
	}

	public void setNameMilestone(String nameMilestone) {
		this.nameMilestone = nameMilestone;
	}

	public Date getDtMilestone() {
		return dtMilestone;
	}

	public void setDtMilestone(Date dtMilestone) {
		this.dtMilestone = dtMilestone;
	}

	public String getCdStatus() {
		return cdStatus;
	}

	public void setCdStatus(String cdStatus) {
		this.cdStatus = cdStatus;
	}

	public String getCdDescription() {
		return cdDescription;
	}

	public void setCdDescription(String cdDescription) {
		this.cdDescription = cdDescription;
	}

	public String getIdCreateUser() {
		return idCreateUser;
	}

	public void setIdCreateUser(String idCreateUser) {
		this.idCreateUser = idCreateUser;
	}

	public Date getDtCreate() {
		return dtCreate;
	}

	public void setDtCreate(Date dtCreate) {
		this.dtCreate = dtCreate;
	}
}
