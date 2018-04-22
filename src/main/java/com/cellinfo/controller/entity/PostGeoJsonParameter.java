package com.cellinfo.controller.entity;

public class PostGeoJsonParameter {

	private String featype = "POINT";
	private String taskGuid;
	private String geoJson;
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
	public String getGeoJson() {
		return geoJson;
	}
	/**
	 * @param geoJson the geoJson to set
	 */
	public void setGeoJson(String geoJson) {
		this.geoJson = geoJson;
	}
	/**
	 * @return the taskGuid
	 */
	public String getTaskGuid() {
		return taskGuid;
	}
	/**
	 * @param taskGuid the taskGuid to set
	 */
	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}
	
	
	
}
