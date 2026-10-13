package com.sl.entity;

public class TaskUser {
    private String idTask;

    private String idExeUser;

    private String idProject;

    private String cdMark;

    public TaskUser(String idTask, String idExeUser, String idProject) {
		this.idTask = idTask;
		this.idExeUser = idExeUser;
		this.idProject = idProject;
	}

	public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask == null ? null : idTask.trim();
    }

    public String getIdExeUser() {
        return idExeUser;
    }

    public void setIdExeUser(String idExeUser) {
        this.idExeUser = idExeUser == null ? null : idExeUser.trim();
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getCdMark() {
        return cdMark;
    }

    public void setCdMark(String cdMark) {
        this.cdMark = cdMark == null ? null : cdMark.trim();
    }
}