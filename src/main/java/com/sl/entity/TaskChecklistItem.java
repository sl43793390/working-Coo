package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 任务检查清单项（S4）。
 * 挂在任务详情页，一项一行，勾选完成；完成进度在详情页展示。
 */
@TableName("task_checklist_item")
public class TaskChecklistItem implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FLAG_DONE_Y = "Y";
	public static final String FLAG_DONE_N = "N";

	private String idItem;

	private String idProject;

	private String idTask;

	private String cdContent;

	private String flagDone;

	private Integer nbrOrder;

	private String idUser;

	private Date dtCreate;

	public String getIdItem() {
		return idItem;
	}

	public void setIdItem(String idItem) {
		this.idItem = idItem;
	}

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	public String getIdTask() {
		return idTask;
	}

	public void setIdTask(String idTask) {
		this.idTask = idTask;
	}

	public String getCdContent() {
		return cdContent;
	}

	public void setCdContent(String cdContent) {
		this.cdContent = cdContent;
	}

	public String getFlagDone() {
		return flagDone;
	}

	public void setFlagDone(String flagDone) {
		this.flagDone = flagDone;
	}

	public Integer getNbrOrder() {
		return nbrOrder;
	}

	public void setNbrOrder(Integer nbrOrder) {
		this.nbrOrder = nbrOrder;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public Date getDtCreate() {
		return dtCreate;
	}

	public void setDtCreate(Date dtCreate) {
		this.dtCreate = dtCreate;
	}

	public boolean isDone() {
		return FLAG_DONE_Y.equals(flagDone);
	}
}
