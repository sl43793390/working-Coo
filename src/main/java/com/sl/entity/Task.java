package com.sl.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableName;
@TableName("task")
public class Task implements Serializable {
    private String idTask;

    private String idProject;

    private String nameTask;//

    private String cdTaskGroup;

    private String cdTaskType;

    private String cdTag;

    private String cdStatus;
    
    private Boolean intStar;

    private Date dtCreate;

    /**
     * 计划开始时间（T2-1 新增）。
     * 原先只有 DT_END，甘特图没有起点排不了期、燃尽图也算不出「应该还剩多少」。
     */
    private Date dtStart;

    private Date dtEnd;

    private String idUser;

    private Double pctComplete;

    private Double pctOverdue;

    /** 预计工时（小时），燃尽图的基准线 */
    private Double nbrEstimateHours;

    /** 实际投入工时（小时），燃尽图的实际线 */
    private Double nbrActualHours;

    private String cdPrior;//

    private String idExeUser;//
    
    private Set<User> idExeUsers;

    private String cdDescription;

    private String flagPrivate;//

    /**
     * 删除标记（S1 任务回收站）：'Y' 表示已进入回收站，'N'/空 为正常任务。
     * 各查询用 FLAG_DELETE <> 'Y' 过滤，软删除后 30 天由调度器物理清理。
     */
    private String flagDelete;

    /** 删除时间（回收站排序与 30 天清理依据），软删除时写入 */
    private Date dtDelete;

    /**
     * 执行人姓名串，逗号分隔。
     * 这不是数据库列（resultMap 里没有映射），由 TaskProcess#fillExecutorNames 批量回填，
     * 避免列表页每一行都去查一次 task_user 造成的 N+1。
     */
    private String nameExeUsers;

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

    public Double getNbrEstimateHours() {
        return nbrEstimateHours;
    }

    public void setNbrEstimateHours(Double nbrEstimateHours) {
        this.nbrEstimateHours = nbrEstimateHours;
    }

    public Double getNbrActualHours() {
        return nbrActualHours;
    }

    public void setNbrActualHours(Double nbrActualHours) {
        this.nbrActualHours = nbrActualHours;
    }

    public String getCdPrior() {
        return cdPrior;
    }

    public void setCdPrior(String cdPrior) {
        this.cdPrior = cdPrior == null ? null : cdPrior.trim();
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

    public String getFlagDelete() {
        return flagDelete;
    }

    public void setFlagDelete(String flagDelete) {
        this.flagDelete = flagDelete == null ? null : flagDelete.trim();
    }

    public Date getDtDelete() {
        return dtDelete;
    }

    public void setDtDelete(Date dtDelete) {
        this.dtDelete = dtDelete;
    }

	public Set<User> getIdExeUsers() {
		return idExeUsers;
	}

	public void setIdExeUsers(Set<User> idExeUsers) {
		this.idExeUsers = idExeUsers;
	}

	public String getNameExeUsers() {
		return nameExeUsers;
	}

	public void setNameExeUsers(String nameExeUsers) {
		this.nameExeUsers = nameExeUsers;
	}

	/**
	 * 剩余工时 = 预计工时 * (1 - 完成率)。
	 * 燃尽图的「实际剩余」用这个；预计工时或完成率缺失时返回 null，由调用方决定兜底。
	 */
	public Double getRemainingHours() {
		if (null == nbrEstimateHours) {
			return null;
		}
		double pct = (null == pctComplete) ? 0d : pctComplete.doubleValue();
		// 库里的 PCT_COMPLETE 既有 0~1 也有 0~100 两种写法，统一按「>1 视为百分比」处理
		if (pct > 1) {
			pct = pct / 100d;
		}
		if (pct < 0) {
			pct = 0d;
		}
		if (pct > 1) {
			pct = 1d;
		}
		return nbrEstimateHours * (1 - pct);
	}
    
    
}