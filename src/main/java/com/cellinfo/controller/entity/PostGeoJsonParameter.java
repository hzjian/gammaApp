package com.cellinfo.controller.entity;

import java.util.Map;

public class PostGeoJsonParameter {

	private String featype = "POINT";
	private String taskGuid;
	private String feaGuid;
	private String feaClassid;
	private String feasid;
	private Map<String,Object> geoJson;
	/**
	 * @return the featype
	 */
	public String getFeatype() {
		return featype;
	}
	/**
	 * @param featype the featype to set
	 */
	public void setFeatype(String featype) {
		this.featype = featype;
	}
	/**
	 * @return the geoJson
	 */

	/**
	 * @return the taskGuid
	 */
	public String getTaskGuid() {
		return taskGuid;
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
	/**
	 * @param taskGuid the taskGuid to set
	 */
	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}
	/**
	 * @return the feaGuid
	 */
	public String getFeaGuid() {
		return feaGuid;
	}
	/**
	 * @param feaGuid the feaGuid to set
	 */
	public void setFeaGuid(String feaGuid) {
		this.feaGuid = feaGuid;
	}
	
	/**
	 * @return the feaClassid
	 */
	public String getFeaClassid() {
		return feaClassid;
	}
	/**
	 * @param feaClassid the feaClassid to set
	 */
	public void setFeaClassid(String feaClassid) {
		this.feaClassid = feaClassid;
	}
	/**
	 * @return the feasid
	 */
	public String getFeasid() {
		return feasid;
	}
	/**
	 * @param feasid the feasid to set
	 */
	public void setFeasid(String feasid) {
		this.feasid = feasid;
	}
	
	
	
	
}
