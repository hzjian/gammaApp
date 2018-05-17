package com.cellinfo.controller.entity;

public class FieldParameter {

	
	private String fieldGuid;
	private String fieldName;
	private String fieldAlias;
	/**
	 * STRING
	 * INTEGER
	 * DOUBLE
	 * DATETIME
	 */
	private String fieldType;
	private String isEdit;
	
	/**
	 * 属性字段众筹等级
	 * TASKGRADE 不同任务保留多份数据
	 * USERGRADE 不同用户保留多份数据
	 */
	private String fieldGrade;
	private String fieldEnum;
	
	/**
	 * @return the fieldGuid
	 */
	public String getFieldGuid() {
		return fieldGuid;
	}
	/**
	 * @param fieldGuid the fieldGuid to set
	 */
	public void setFieldGuid(String fieldGuid) {
		this.fieldGuid = fieldGuid;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	/**
	 * @return the isEdit
	 */
	public String getIsEdit() {
		return isEdit;
	}
	/**
	 * @param isEdit the isEdit to set
	 */
	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}
	/**
	 * @return the fieldGrade
	 */
	public String getFieldGrade() {
		return fieldGrade;
	}
	/**
	 * @param fieldGrade the fieldGrade to set
	 */
	public void setFieldGrade(String fieldGrade) {
		this.fieldGrade = fieldGrade;
	}
	/**
	 * @return the fieldEnum
	 */
	public String getFieldEnum() {
		return fieldEnum;
	}
	/**
	 * @param fieldEnum the fieldEnum to set
	 */
	public void setFieldEnum(String fieldEnum) {
		this.fieldEnum = fieldEnum;
	}
	/**
	 * @return the fieldAlias
	 */
	public String getFieldAlias() {
		return fieldAlias;
	}
	/**
	 * @param fieldAlias the fieldAlias to set
	 */
	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}
	
	
	
}
