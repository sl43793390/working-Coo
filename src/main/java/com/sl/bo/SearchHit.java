package com.sl.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 全文搜索的命中项（T2-8）。
 * 三种来源（任务 / 评论 / 文档）统一成一种结构，页面里一张表展示。
 */
public class SearchHit implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TYPE_TASK = "TASK";
	public static final String TYPE_COMMENT = "COMMENT";
	public static final String TYPE_DOC = "DOC";

	private String cdType;

	private String idBiz;

	private String idProject;

	private String nameTitle;

	private String cdContent;

	private Date dtCreate;

	public String getCdType() {
		return cdType;
	}

	public void setCdType(String cdType) {
		this.cdType = cdType;
	}

	public String getIdBiz() {
		return idBiz;
	}

	public void setIdBiz(String idBiz) {
		this.idBiz = idBiz;
	}

	public String getIdProject() {
		return idProject;
	}

	public void setIdProject(String idProject) {
		this.idProject = idProject;
	}

	public String getNameTitle() {
		return nameTitle;
	}

	public void setNameTitle(String nameTitle) {
		this.nameTitle = nameTitle;
	}

	public String getCdContent() {
		return cdContent;
	}

	public void setCdContent(String cdContent) {
		this.cdContent = cdContent;
	}

	public Date getDtCreate() {
		return dtCreate;
	}

	public void setDtCreate(Date dtCreate) {
		this.dtCreate = dtCreate;
	}

	public String getTypeName() {
		if (TYPE_TASK.equals(cdType)) {
			return "任务";
		}
		if (TYPE_COMMENT.equals(cdType)) {
			return "评论";
		}
		if (TYPE_DOC.equals(cdType)) {
			return "文档";
		}
		return cdType;
	}
}
