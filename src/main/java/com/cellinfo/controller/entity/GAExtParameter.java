package com.cellinfo.controller.entity;

import java.util.List;

public class GAExtParameter {

	private String kernelClassid;
	
	private String extGuid;
	
	private String geoType;
	
	private List<GAFilter> filterList;

	private List<GAGeoFilter> geofilterList;
	
	/**
	 * @return the kernelClassid
	 */
	public String getKernelClassid() {
		return kernelClassid;
	}

	/**
	 * @param kernelClassid the kernelClassid to set
	 */
	public void setKernelClassid(String kernelClassid) {
		this.kernelClassid = kernelClassid;
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
	 * @return the filterList
	 */
	public List<GAFilter> getFilterList() {
		return filterList;
	}

	/**
	 * @param filterList the filterList to set
	 */
	public void setFilterList(List<GAFilter> filterList) {
		this.filterList = filterList;
	}

	/**
	 * @return the geofilterList
	 */
	public List<GAGeoFilter> getGeofilterList() {
		return geofilterList;
	}

	/**
	 * @param geofilterList the geofilterList to set
	 */
	public void setGeofilterList(List<GAGeoFilter> geofilterList) {
		this.geofilterList = geofilterList;
	}
	
}
