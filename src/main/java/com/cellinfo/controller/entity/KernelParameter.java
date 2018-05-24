package com.cellinfo.controller.entity;

public class KernelParameter {

	private String classId;
	private String className;
	private String geomType;
	private String descInfo;
	
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
	
}
