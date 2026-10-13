package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

public class TaskComment implements Serializable {
    private String idTask;//

    private Date dtComment;//

    private String commentUser;//

    private String cdComment;//

    private static final long serialVersionUID = 1L;

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask == null ? null : idTask.trim();
    }

    public Date getDtComment() {
        return dtComment;
    }

    public void setDtComment(Date dtComment) {
        this.dtComment = dtComment;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser == null ? null : commentUser.trim();
    }

    public String getCdComment() {
        return cdComment;
    }

    public void setCdComment(String cdComment) {
        this.cdComment = cdComment == null ? null : cdComment.trim();
    }
}