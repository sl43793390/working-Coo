package com.sl.entity;

import java.io.Serializable;

public class ProjectTag implements Serializable {
    private String idProject;//

    private String cdTag;//

    private static final long serialVersionUID = 1L;

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getCdTag() {
        return cdTag;
    }

    public void setCdTag(String cdTag) {
        this.cdTag = cdTag == null ? null : cdTag.trim();
    }
}