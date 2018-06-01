package com.cellinfo.controller.entity;

import java.util.Map;

public class GAGeoFilter {

	private String filterGuid;
	
	private Map<String,Object> filterGeom;

	/**
	 * @return the filterGuid
	 */
	public String getFilterGuid() {
		return filterGuid;
	}

	/**
	 * @param filterGuid the filterGuid to set
	 */
	public void setFilterGuid(String filterGuid) {
		this.filterGuid = filterGuid;
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
