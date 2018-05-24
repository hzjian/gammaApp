package com.cellinfo.controller.entity;

import java.util.Map;

public class GeoFilterParameter {
	
	private String filterId;
	
	private String extGuid;
	
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
	 * @return the extGuid
	 */
	public String getExtGuid() {
		return extGuid;
	}

	/**
	 * @param extGuid the extGuid to set
	 */
	public void setExtGuid(String extGuid) {
		this.extGuid = extGuid;
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
