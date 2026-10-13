package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 任务前置依赖（T2-3）。
 * <p>
 * Minimal 版：只支持「前置 -> 后继」一种关系（FS）。
 * 一条记录的含义是 ID_PRE_TASK 必须先完成，ID_TASK 才能开始。
 */
@TableName("task_dependency")
public class TaskDependency implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idDependency;

	private String idProject;

	/** 后继任务 */
	private String idTask;

	/** 前置任务 */
	private String idPreTask;

	private Integer nbrLagDay;

	private Date dtCreate;

	private String idCreateUser;

	/** 非数据库列：列表展示用 */
	private String namePreTask;

	/** 非数据库列：列表展示用 */
	private String nameTask;

	public String getIdDependency() {
		return idDependency;
	}

	public void setIdDependency(String idDependency) {
		this.idDependency = idDependency;
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

	public String getIdPreTask() {
		return idPreTask;
	}

	public void setIdPreTask(String idPreTask) {
		this.idPreTask = idPreTask;
	}

	public Integer getNbrLagDay() {
		return nbrLagDay;
	}

	public void setNbrLagDay(Integer nbrLagDay) {
		this.nbrLagDay = nbrLagDay;
	}

	public Date getDtCreate() {
		return dtCreate;
	}

	public void setDtCreate(Date dtCreate) {
		this.dtCreate = dtCreate;
	}

	public String getIdCreateUser() {
		return idCreateUser;
	}

	public void setIdCreateUser(String idCreateUser) {
		this.idCreateUser = idCreateUser;
	}

	public String getNamePreTask() {
		return namePreTask;
	}

	public void setNamePreTask(String namePreTask) {
		this.namePreTask = namePreTask;
	}

	public String getNameTask() {
		return nameTask;
	}

	public void setNameTask(String nameTask) {
		this.nameTask = nameTask;
	}
}
