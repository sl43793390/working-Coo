package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 操作历史（T2-10）。由 {@code @AuditLog} 注解 + AOP 切面统一落库，业务代码不感知。
 */
@TableName("operation_log")
public class OperationLog implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String BIZ_PROJECT = "PROJECT";
	public static final String BIZ_TASK = "TASK";
	public static final String BIZ_DOC = "DOC";
	public static final String BIZ_USER = "USER";
	public static final String BIZ_SCHEDULE = "SCHEDULE";
	public static final String BIZ_MILESTONE = "MILESTONE";

	private String idLog;

	private String idUser;

	private String nameUser;

	private String idProject;

	private String cdBizType;

	private String idBiz;

	private String cdAction;

	private String cdDetail;

	private Date dtOperation;

	public String getIdLog() {
		return idLog;
	}

	public void setIdLog(String idLog) {
		this.idLog = idLog;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	public String getCdBizType() {
		return cdBizType;
	}

	public void setCdBizType(String cdBizType) {
		this.cdBizType = cdBizType;
	}

	public String getIdBiz() {
		return idBiz;
	}

	public void setIdBiz(String idBiz) {
		this.idBiz = idBiz;
	}

	public String getCdAction() {
		return cdAction;
	}

	public void setCdAction(String cdAction) {
		this.cdAction = cdAction;
	}

	public String getCdDetail() {
		return cdDetail;
	}

	public void setCdDetail(String cdDetail) {
		this.cdDetail = cdDetail;
	}

	public Date getDtOperation() {
		return dtOperation;
	}

	public void setDtOperation(Date dtOperation) {
		this.dtOperation = dtOperation;
	}
}
