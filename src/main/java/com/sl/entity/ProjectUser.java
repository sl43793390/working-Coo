package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
@TableName("project_user")
public class ProjectUser implements Serializable {
    private String idProject;//

    private String idUser;//

    private Date dtUpdate = new Date();//

    /**
     * 项目内角色（T2-5）：OWNER 项目负责人 / MEMBER 成员 / VIEWER 只读访客。
     * 原先只有全局 RBAC，表达不了「项目负责人可以恢复自己的项目」这类中间态。
     */
    private String cdProjectRole = ROLE_MEMBER;

    /** 项目负责人 */
    public static final String ROLE_OWNER = "OWNER";
    /** 项目成员 */
    public static final String ROLE_MEMBER = "MEMBER";
    /** 只读访客 */
    public static final String ROLE_VIEWER = "VIEWER";

    private static final long serialVersionUID = 1L;


    public ProjectUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProjectUser(String idProject, String idUser) {
		super();
		this.idProject = idProject;
		this.idUser = idUser;
	}

	public ProjectUser(String idProject, String idUser, Date dtUpdate) {
		super();
		this.idProject = idProject;
		this.idUser = idUser;
		this.dtUpdate = dtUpdate;
	}

	public ProjectUser(String idProject, String idUser, String cdProjectRole) {
		this(idProject, idUser);
		this.cdProjectRole = cdProjectRole;
	}

	public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject == null ? null : idProject.trim();
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser == null ? null : idUser.trim();
    }

    public Date getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(Date dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public String getCdProjectRole() {
        return cdProjectRole;
    }

    public void setCdProjectRole(String cdProjectRole) {
        this.cdProjectRole = cdProjectRole;
    }

    /** 角色中文名，列表展示用 */
    public String getProjectRoleName() {
        if (ROLE_OWNER.equals(cdProjectRole)) {
            return "项目负责人";
        }
        if (ROLE_VIEWER.equals(cdProjectRole)) {
            return "只读访客";
        }
        return "项目成员";
    }
}