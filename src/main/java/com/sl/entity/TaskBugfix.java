package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

public class TaskBugfix implements Serializable {
    private String idTask;//

    private String idProject;//

    private String nameTask;//

    private String cdTaskType;//

    private String cdTaskTag;//

    private String cdStatus;//״̬��δ��ʼ�������С����޸����ѽ���������

    private Date dtCreate;//����ʱ��

    private Date dtEnd;//�ر�ʱ��

    private String idCreateUser;//������

    private String cdDescription;//��ע

    private String nameTester;//������

    private Date dtFixed;//�޸�ʱ��

    private String cdBugReason;//ȱ��ԭ��

    private String cdSeverity;//���س̶�

    private String cdModual;//ģ��

    private String nameSystem;//ȱ��ϵͳ

    private String cdSuspend;//����ԭ��

    private static final long serialVersionUID = 1L;

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask == null ? null : idTask.trim();
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask == null ? null : nameTask.trim();
    }

    public String getCdTaskType() {
        return cdTaskType;
    }

    public void setCdTaskType(String cdTaskType) {
        this.cdTaskType = cdTaskType == null ? null : cdTaskType.trim();
    }

    public String getCdTaskTag() {
        return cdTaskTag;
    }

    public void setCdTaskTag(String cdTaskTag) {
        this.cdTaskTag = cdTaskTag == null ? null : cdTaskTag.trim();
    }

    public String getCdStatus() {
        return cdStatus;
    }

    public void setCdStatus(String cdStatus) {
        this.cdStatus = cdStatus == null ? null : cdStatus.trim();
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

    public String getIdCreateUser() {
        return idCreateUser;
    }

    public void setIdCreateUser(String idCreateUser) {
        this.idCreateUser = idCreateUser == null ? null : idCreateUser.trim();
    }

    public String getCdDescription() {
        return cdDescription;
    }

    public void setCdDescription(String cdDescription) {
        this.cdDescription = cdDescription == null ? null : cdDescription.trim();
    }

    public String getNameTester() {
        return nameTester;
    }

    public void setNameTester(String nameTester) {
        this.nameTester = nameTester == null ? null : nameTester.trim();
    }

    public Date getDtFixed() {
        return dtFixed;
    }

    public void setDtFixed(Date dtFixed) {
        this.dtFixed = dtFixed;
    }

    public String getCdBugReason() {
        return cdBugReason;
    }

    public void setCdBugReason(String cdBugReason) {
        this.cdBugReason = cdBugReason == null ? null : cdBugReason.trim();
    }

    public String getCdSeverity() {
        return cdSeverity;
    }

    public void setCdSeverity(String cdSeverity) {
        this.cdSeverity = cdSeverity == null ? null : cdSeverity.trim();
    }

    public String getCdModual() {
        return cdModual;
    }

    public void setCdModual(String cdModual) {
        this.cdModual = cdModual == null ? null : cdModual.trim();
    }

    public String getNameSystem() {
        return nameSystem;
    }

    public void setNameSystem(String nameSystem) {
        this.nameSystem = nameSystem == null ? null : nameSystem.trim();
    }

    public String getCdSuspend() {
        return cdSuspend;
    }

    public void setCdSuspend(String cdSuspend) {
        this.cdSuspend = cdSuspend == null ? null : cdSuspend.trim();
    }
}