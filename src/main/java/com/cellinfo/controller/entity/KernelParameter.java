package com.cellinfo.controller.entity;

import java.util.List;

public class KernelParameter {

	private String classGuid;
	private String className;
	private String geomType;
	private String descInfo;
	private List<FieldParameter> fieldList;
	private List<FieldParameter> appendList;
	
	/**
	 * @return the classGuid
	 */
	public String getClassGuid() {
		return classGuid;
	}
	/**
	 * @param classGuid the classGuid to set
	 */
	public void setClassGuid(String classGuid) {
		this.classGuid = classGuid;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the descInfo
	 */
	public String getDescInfo() {
		return descInfo;
	}
	/**
	 * @param descInfo the descInfo to set
	 */
	public void setDescInfo(String descInfo) {
		this.descInfo = descInfo;
	}
	/**
	 * @return the fieldList
	 */
	public List<FieldParameter> getFieldList() {
		return fieldList;
	}
	/**
	 * @param fieldList the fieldList to set
	 */
	public void setFieldList(List<FieldParameter> fieldList) {
		this.fieldList = fieldList;
	}
	/**
	 * @return the geomType
	 */
	public String getGeomType() {
		return geomType;
	}
	/**
	 * @param geomType the geomType to set
	 */
	public void setGeomType(String geomType) {
		this.geomType = geomType;
	}
	/**
	 * @return the appendList
	 */
	public List<FieldParameter> getAppendList() {
		return appendList;
	}
	/**
	 * @param appendList the appendList to set
	 */
	public void setAppendList(List<FieldParameter> appendList) {
		this.appendList = appendList;
	}
	
	
	
	
}
