package com.sl.entity;

import java.util.Date;

public class ProjectTaskFile {
    private String idFile;

    private String idProject;

    private String idTask;

    private String nameFile;

    private String cdFilePath;

    private Date dtUpload;

    private String idUser;

    private String cdComment;

    public String getIdFile() {
        return idFile;
    }

    public void setIdFile(String idFile) {
        this.idFile = idFile == null ? null : idFile.trim();
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask == null ? null : idTask.trim();
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile == null ? null : nameFile.trim();
    }

    public String getCdFilePath() {
        return cdFilePath;
    }

    public void setCdFilePath(String cdFilePath) {
        this.cdFilePath = cdFilePath == null ? null : cdFilePath.trim();
    }

    public Date getDtUpload() {
        return dtUpload;
    }

    public void setDtUpload(Date dtUpload) {
        this.dtUpload = dtUpload;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser == null ? null : idUser.trim();
    }

    public String getCdComment() {
        return cdComment;
    }

    public void setCdComment(String cdComment) {
        this.cdComment = cdComment == null ? null : cdComment.trim();
    }
}