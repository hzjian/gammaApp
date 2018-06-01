package com.cellinfo.controller.entity;

import java.util.Map;

public class GAGeoParameter {
	private String geoType;
	private String taskId;
	private String kernelId;
	private String classId;
	private String featureId;
	private Map<String,Object> geoJson;
	/**
	 * @return the geoType
	 */
	public String getGeoType() {
		return geoType;
	}
	/**
	 * @param geoType the geoType to set
	 */
	public void setGeoType(String geoType) {
		this.geoType = geoType;
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
	 * @return the kernelId
	 */
	public String getKernelId() {
		return kernelId;
	}
	/**
	 * @param kernelId the kernelId to set
	 */
	public void setKernelId(String kernelId) {
		this.kernelId = kernelId;
	}
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
	 * @return the featureId
	 */
	public String getFeatureId() {
		return featureId;
	}
	/**
	 * @param featureId the featureId to set
	 */
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	/**
	 * @return the geoJson
	 */
	public Map<String, Object> getGeoJson() {
		return geoJson;
	}
	/**
	 * @param geoJson the geoJson to set
	 */
	public void setGeoJson(Map<String, Object> geoJson) {
		this.geoJson = geoJson;
	}

	
	
	
}
