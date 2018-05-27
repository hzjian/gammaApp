package com.cellinfo.controller.entity;

import java.util.Map;

public class GeoFilterParameter {
	
	private String filterId;
	
	private String extId;
	
	//geojson
	private Map<String,Object> filterGeom;

	/**
	 * @return the filterId
	 */
	public String getFilterId() {
		return filterId;
	}

	/**
	 * @param filterId the filterId to set
	 */
	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}

	/**
	 * @return the extId
	 */
	public String getExtId() {
		return extId;
	}

	/**
	 * @param extId the extId to set
	 */
	public void setExtId(String extId) {
		this.extId = extId;
	}

	/**
	 * @return the filterGeom
	 */
	public Map<String, Object> getFilterGeom() {
		return filterGeom;
	}

	/**
	 * @param filterGeom the filterGeom to set
	 */
	public void setFilterGeom(Map<String, Object> filterGeom) {
		this.filterGeom = filterGeom;
	}
	
	
	
}
