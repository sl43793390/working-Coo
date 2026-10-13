package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
@TableName("projects")
public class Project implements Serializable {
	@TableId
    private String idProject;

    private String idInstitution;

    private String nameProject;

    private String cdStatus;

    private String cdPrior;

    private Double pctComplete;

    private Double pctOverdue;

    private Date dtStart;
    
    private Date dtEnd;

    private Date dtCreate;

    private String cdCreateUser;

    private String cdComment;

    private String flagPrivate;

    private String flagDelete;

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(String idInstitution) {
        this.idInstitution = idInstitution == null ? null : idInstitution.trim();
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject == null ? null : nameProject.trim();
    }

    public String getCdStatus() {
        return cdStatus;
    }

    public void setCdStatus(String cdStatus) {
        this.cdStatus = cdStatus == null ? null : cdStatus.trim();
    }

    public String getCdPrior() {
        return cdPrior;
    }

    public void setCdPrior(String cdPrior) {
        this.cdPrior = cdPrior == null ? null : cdPrior.trim();
    }

    public Double getPctComplete() {
        return pctComplete;
    }

    public void setPctComplete(Double pctComplete) {
        this.pctComplete = pctComplete;
    }

    public Double getPctOverdue() {
        return pctOverdue;
    }

    public void setPctOverdue(Double pctOverdue) {
        this.pctOverdue = pctOverdue;
    }

    public Date getDtStart() {
        return dtStart;
    }

    public void setDtStart(Date dtStart) {
        this.dtStart = dtStart;
    }

    public Date getDtEnd() {
		return dtEnd;
	}

	public void setDtEnd(Date dtEnd) {
		this.dtEnd = dtEnd;
	}

	public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }

    public String getCdCreateUser() {
        return cdCreateUser;
    }

    public void setCdCreateUser(String cdCreateUser) {
        this.cdCreateUser = cdCreateUser == null ? null : cdCreateUser.trim();
    }

    public String getCdComment() {
        return cdComment;
    }

    public void setCdComment(String cdComment) {
        this.cdComment = cdComment == null ? null : cdComment.trim();
    }

    public String getFlagPrivate() {
        return flagPrivate;
    }

    public void setFlagPrivate(String flagPrivate) {
        this.flagPrivate = flagPrivate == null ? null : flagPrivate.trim();
    }

    public String getFlagDelete() {
        return flagDelete;
    }

    public void setFlagDelete(String flagDelete) {
        this.flagDelete = flagDelete == null ? null : flagDelete.trim();
    }

	@Override
	public String toString() {
		return "Project [idProject=" + idProject + ", idInstitution=" + idInstitution + ", nameProject=" + nameProject
				+ ", cdStatus=" + cdStatus + ", cdPrior=" + cdPrior + ", pctComplete=" + pctComplete + ", pctOverdue="
				+ pctOverdue + ", dtStart=" + dtStart + ", dtCreate=" + dtCreate + ", cdCreateUser=" + cdCreateUser
				+ ", cdComment=" + cdComment + ", flagPrivate=" + flagPrivate + ", flagDelete=" + flagDelete + "]";
	}
    
}