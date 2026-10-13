package com.sl.bo;

import java.io.Serializable;

import cn.idev.excel.annotation.ExcelProperty;

/**
 * 任务导入导出的行模型（T2-9）。
 * <p>
 * 日期/数字统一用字符串承载：Excel 里日期常被存成数字或文本，
 * 交给 EasyExcel 自动转换容易出现「1900-01-00」这类脏值，
 * 这里自己按 yyyy-MM-dd 解析，失败就在导入结果里报出具体行号。
 */
public class TaskExcelRow implements Serializable {

	private static final long serialVersionUID = 1L;

	@ExcelProperty("任务名称")
	private String nameTask;

	@ExcelProperty("任务分组")
	private String cdTaskGroup;

	@ExcelProperty("任务类型")
	private String cdTaskType;

	@ExcelProperty("任务标签")
	private String cdTag;

	@ExcelProperty("任务状态")
	private String cdStatus;

	@ExcelProperty("优先级")
	private String cdPrior;

	@ExcelProperty("完成比例")
	private String pctComplete;

	@ExcelProperty("计划开始日期")
	private String dtStart;

	@ExcelProperty("结束日期")
	private String dtEnd;

	@ExcelProperty("预计工时")
	private String estimateHours;

	@ExcelProperty("实际工时")
	private String actualHours;

	@ExcelProperty("执行人ID")
	private String exeUserIds;

	@ExcelProperty("是否私有")
	private String flagPrivate;

	@ExcelProperty("任务描述")
	private String cdDescription;

	public String getNameTask() {
		return nameTask;
	}

	public void setNameTask(String nameTask) {
		this.nameTask = nameTask;
	}

	public String getCdTaskGroup() {
		return cdTaskGroup;
	}

	public void setCdTaskGroup(String cdTaskGroup) {
		this.cdTaskGroup = cdTaskGroup;
	}

	public String getCdTaskType() {
		return cdTaskType;
	}

	public void setCdTaskType(String cdTaskType) {
		this.cdTaskType = cdTaskType;
	}

	public String getCdTag() {
		return cdTag;
	}

	public void setCdTag(String cdTag) {
		this.cdTag = cdTag;
	}

	public String getCdStatus() {
		return cdStatus;
	}

	public void setCdStatus(String cdStatus) {
		this.cdStatus = cdStatus;
	}

	public String getCdPrior() {
		return cdPrior;
	}

	public void setCdPrior(String cdPrior) {
		this.cdPrior = cdPrior;
	}

	public String getPctComplete() {
		return pctComplete;
	}

	public void setPctComplete(String pctComplete) {
		this.pctComplete = pctComplete;
	}

	public String getDtStart() {
		return dtStart;
	}

	public void setDtStart(String dtStart) {
		this.dtStart = dtStart;
	}

	public String getDtEnd() {
		return dtEnd;
	}

	public void setDtEnd(String dtEnd) {
		this.dtEnd = dtEnd;
	}

	public String getEstimateHours() {
		return estimateHours;
	}

	public void setEstimateHours(String estimateHours) {
		this.estimateHours = estimateHours;
	}

	public String getActualHours() {
		return actualHours;
	}

	public void setActualHours(String actualHours) {
		this.actualHours = actualHours;
	}

	public String getExeUserIds() {
		return exeUserIds;
	}

	public void setExeUserIds(String exeUserIds) {
		this.exeUserIds = exeUserIds;
	}

	public String getFlagPrivate() {
		return flagPrivate;
	}

	public void setFlagPrivate(String flagPrivate) {
		this.flagPrivate = flagPrivate;
	}

	public String getCdDescription() {
		return cdDescription;
	}

	public void setCdDescription(String cdDescription) {
		this.cdDescription = cdDescription;
	}
}
