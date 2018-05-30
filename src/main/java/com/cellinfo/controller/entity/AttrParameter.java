package com.cellinfo.controller.entity;

public class AttrParameter {

	private String classId;
	private String attrId;
	private String attrName;
	private String attrAlias;
	/**
	 * STRING
	 * INTEGER
	 * DOUBLE
	 * DATETIME
	 */
	private String attrType;
	private Integer isEdit;
	
	/**
	 * 属性字段众筹等级
	 * TASKGRADE 不同任务保留多份数据
	 * USERGRADE 不同用户保留多份数据
	 */
	private String attrGrade;
	private String dictId;
	
	private String minValue;
	private String maxValue;
	
	private String attrDesc;
	
	private String taskId;
	
	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}
	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}
	/**
	 * @return the attrId
	 */
	public String getAttrId() {
		return attrId;
	}
	/**
	 * @param attrId the attrId to set
	 */
	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}
	/**
	 * @return the attrName
	 */
	public String getAttrName() {
		return attrName;
	}
	/**
	 * @param attrName the attrName to set
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	/**
	 * @return the attrAlias
	 */
	public String getAttrAlias() {
		return attrAlias;
	}
	/**
	 * @param attrAlias the attrAlias to set
	 */
	public void setAttrAlias(String attrAlias) {
		this.attrAlias = attrAlias;
	}
	/**
	 * @return the attrType
	 */
	public String getAttrType() {
		return attrType;
	}
	/**
	 * @param attrType the attrType to set
	 */
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}
	/**
	 * @return the isEdit
	 */
	public Integer getIsEdit() {
		return isEdit;
	}
	/**
	 * @param isEdit the isEdit to set
	 */
	public void setIsEdit(Integer isEdit) {
		this.isEdit = isEdit;
	}
	/**
	 * @return the attrGrade
	 */
	public String getAttrGrade() {
		return attrGrade;
	}
	/**
	 * @param attrGrade the attrGrade to set
	 */
	public void setAttrGrade(String attrGrade) {
		this.attrGrade = attrGrade;
	}
	
	/**
	 * @return the dictId
	 */
	public String getDictId() {
		return dictId;
	}
	/**
	 * @param dictId the dictId to set
	 */
	public void setDictId(String dictId) {
		this.dictId = dictId;
	}
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the minValue
	 */
	public String getMinValue() {
		return minValue;
	}
	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	/**
	 * @return the maxValue
	 */
	public String getMaxValue() {
		return maxValue;
	}
	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	/**
	 * @return the attrDesc
	 */
	public String getAttrDesc() {
		return attrDesc;
	}
	/**
	 * @param attrDesc the attrDesc to set
	 */
	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}
	
	

}
