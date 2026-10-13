package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

public class Schedule implements Serializable {
    private String idUser;

    private String idSchedule;

    private Date dtSchedule;

    private Date dtEnd;

    private String cdNature;

    private String nameSchedule;

    private String comment;
    
    private String participatingUser;

    /** 提前多少分钟提醒；null / 0 = 不提醒（T2-11） */
    private Integer nbrRemindMinutes;

    /** 是否已发过提醒 Y/N（T2-11） */
    private String flagReminded;

    private Date timestamp;

    private static final long serialVersionUID = 1L;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser == null ? null : idUser.trim();
    }

    public String getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(String idSchedule) {
        this.idSchedule = idSchedule == null ? null : idSchedule.trim();
    }

    public Date getDtSchedule() {
        return dtSchedule;
    }

    public void setDtSchedule(Date dtSchedule) {
        this.dtSchedule = dtSchedule;
    }

    public Date getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(Date dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getCdNature() {
        return cdNature;
    }

    public void setCdNature(String cdNature) {
        this.cdNature = cdNature == null ? null : cdNature.trim();
    }

    public String getNameSchedule() {
        return nameSchedule;
    }

    public void setNameSchedule(String nameSchedule) {
        this.nameSchedule = nameSchedule == null ? null : nameSchedule.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
    
    public String getParticipatingUser() {
		return participatingUser;
	}

	public void setParticipatingUser(String participatingUser) {
		this.participatingUser = participatingUser;
	}

	public Integer getNbrRemindMinutes() {
		return nbrRemindMinutes;
	}

	public void setNbrRemindMinutes(Integer nbrRemindMinutes) {
		this.nbrRemindMinutes = nbrRemindMinutes;
	}

	public String getFlagReminded() {
		return flagReminded;
	}

	public void setFlagReminded(String flagReminded) {
		this.flagReminded = flagReminded;
	}

	public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}