package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 站内通知（T2-4 通知中心）。
 * <p>
 * 场景：任务被指派、评论被 @、任务到期前一天、里程碑临近。
 * 只做站内提醒，不接邮件/短信（需要时按 CD_TYPE 扩展一个外发通道即可）。
 */
@TableName("notification")
public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 指派任务 */
	public static final String TYPE_ASSIGN = "ASSIGN";
	/** 任务评论中被 @ */
	public static final String TYPE_COMMENT = "COMMENT";
	/** 任务即将到期 */
	public static final String TYPE_DUE = "DUE";
	/** 里程碑临近 */
	public static final String TYPE_MILESTONE = "MILESTONE";
	/** 前置任务已完成 */
	public static final String TYPE_DEPEND = "DEPEND";
	/** 日程提醒（T2-11） */
	public static final String TYPE_SCHEDULE = "SCHEDULE";

	private String idNotification;

	private String idUser;

	private String cdType;

	private String idBiz;

	private String idProject;

	private String cdTitle;

	private String cdContent;

	private String flagRead;

	private Date dtCreate;

	private Date dtRead;

	public String getIdNotification() {
		return idNotification;
	}

	public void setIdNotification(String idNotification) {
		this.idNotification = idNotification;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getCdType() {
		return cdType;
	}

	public void setCdType(String cdType) {
		this.cdType = cdType;
	}

	public String getIdBiz() {
		return idBiz;
	}

	public void setIdBiz(String idBiz) {
		this.idBiz = idBiz;
	}

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	public String getCdTitle() {
		return cdTitle;
	}

	public void setCdTitle(String cdTitle) {
		this.cdTitle = cdTitle;
	}

	public String getCdContent() {
		return cdContent;
	}

	public void setCdContent(String cdContent) {
		this.cdContent = cdContent;
	}

	public String getFlagRead() {
		return flagRead;
	}

	public void setFlagRead(String flagRead) {
		this.flagRead = flagRead;
	}

	public Date getDtCreate() {
		return dtCreate;
	}

	public void setDtCreate(Date dtCreate) {
		this.dtCreate = dtCreate;
	}

	public Date getDtRead() {
		return dtRead;
	}

	public void setDtRead(Date dtRead) {
		this.dtRead = dtRead;
	}

	/**
	 * 类型中文名，列表里直接展示，避免 UI 里散落 switch。
	 */
	public String getTypeName() {
		if (null == cdType) {
			return "";
		}
		return switch (cdType) {
		case TYPE_ASSIGN -> "任务指派";
		case TYPE_COMMENT -> "评论提及";
		case TYPE_DUE -> "即将到期";
		case TYPE_MILESTONE -> "里程碑";
		case TYPE_DEPEND -> "前置任务";
		case TYPE_SCHEDULE -> "日程提醒";
		default -> cdType;
		};
	}
}
