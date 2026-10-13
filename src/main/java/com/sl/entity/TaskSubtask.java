package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

public class TaskSubtask implements Serializable {
	@TableId
    private String idSubtask;//��ҵID

    private String idProject;//��ĿID

    private String idTaskParent;//��������ҵID

    private String nameTask;//

    private String cdTaskGroup;//�������

    private String cdTaskType;//�������͡����������ԡ�bug�޸�����ά��

    private String cdTag;//��ǩ

    private String cdStatus;//״̬

    private Boolean intStar;//�Ǳ�

    private Date dtCreate;//����ʱ��

    private Date dtEnd;//����ʱ��

    private String idUser;//������

    private Double pctComplete;//�����

    private Double pctOverdue;//������

    private Integer nbrPrior;//

    private String idExeUser;//ִ����

    private String cdDescription;//��ע

    private String flagPrivate;//�Ƿ�˽��

    private static final long serialVersionUID = 1L;

    public String getIdSubtask() {
        return idSubtask;
    }

    public void setIdSubtask(String idSubtask) {
        this.idSubtask = idSubtask == null ? null : idSubtask.trim();
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getIdTaskParent() {
        return idTaskParent;
    }

    public void setIdTaskParent(String idTaskParent) {
        this.idTaskParent = idTaskParent == null ? null : idTaskParent.trim();
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask == null ? null : nameTask.trim();
    }

    public String getCdTaskGroup() {
        return cdTaskGroup;
    }

    public void setCdTaskGroup(String cdTaskGroup) {
        this.cdTaskGroup = cdTaskGroup == null ? null : cdTaskGroup.trim();
    }

    public String getCdTaskType() {
        return cdTaskType;
    }

    public void setCdTaskType(String cdTaskType) {
        this.cdTaskType = cdTaskType == null ? null : cdTaskType.trim();
    }

    public String getCdTag() {
        return cdTag;
    }

    public void setCdTag(String cdTag) {
        this.cdTag = cdTag == null ? null : cdTag.trim();
    }

    public String getCdStatus() {
        return cdStatus;
    }

    public void setCdStatus(String cdStatus) {
        this.cdStatus = cdStatus == null ? null : cdStatus.trim();
    }

    public Boolean getIntStar() {
        return intStar;
    }

    public void setIntStar(Boolean intStar) {
        this.intStar = intStar;
    }

    public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }

    public Date getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(Date dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser == null ? null : idUser.trim();
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

    public Integer getNbrPrior() {
        return nbrPrior;
    }

    public void setNbrPrior(Integer nbrPrior) {
        this.nbrPrior = nbrPrior;
    }

    public String getIdExeUser() {
        return idExeUser;
    }

    public void setIdExeUser(String idExeUser) {
        this.idExeUser = idExeUser == null ? null : idExeUser.trim();
    }

    public String getCdDescription() {
        return cdDescription;
    }

    public void setCdDescription(String cdDescription) {
        this.cdDescription = cdDescription == null ? null : cdDescription.trim();
    }

    public String getFlagPrivate() {
        return flagPrivate;
    }

    public void setFlagPrivate(String flagPrivate) {
        this.flagPrivate = flagPrivate == null ? null : flagPrivate.trim();
    }
}