package com.cellinfo.controller.entity;

import java.util.Map;

public class GeoParameter {

	private String kernelId;
	private String layerId;
	private Map<String,Object> geoJson;
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
	 * @return the layerId
	 */
	public String getLayerId() {
		return layerId;
	}
	/**
	 * @param layerId the layerId to set
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
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
