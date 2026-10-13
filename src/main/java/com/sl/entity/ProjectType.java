package com.sl.entity;

import java.io.Serializable;

public class ProjectType implements Serializable {
    private String idProject;//

    private String cdType;//

    private static final long serialVersionUID = 1L;

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getCdType() {
        return cdType;
    }

    public void setCdType(String cdType) {
        this.cdType = cdType == null ? null : cdType.trim();
    }
}