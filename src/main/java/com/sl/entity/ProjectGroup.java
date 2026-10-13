package com.sl.entity;

import java.io.Serializable;

public class ProjectGroup implements Serializable {
    private String idProject;//

    private String cdGroup;//

    private static final long serialVersionUID = 1L;

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getCdGroup() {
        return cdGroup;
    }

    public void setCdGroup(String cdGroup) {
        this.cdGroup = cdGroup == null ? null : cdGroup.trim();
    }
}