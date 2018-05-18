package com.cellinfo.controller.entity;

import java.util.List;
import java.util.Map;

public class ExtTypeParameter {

	private String extGuid;

	private String extName;

	private String kernelClassid;

	private String extDesc;
	
	List<FilterParameter> filterList;
	
	//geojson
	private Map<String,Object> filterGeom;
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
	 * @return the extName
	 */
	public String getExtName() {
		return extName;
	}

	/**
	 * @param extName the extName to set
	 */
	public void setExtName(String extName) {
		this.extName = extName;
	}

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
	 * @return the extDesc
	 */
	public String getExtDesc() {
		return extDesc;
	}

	/**
	 * @param extDesc the extDesc to set
	 */
	public void setExtDesc(String extDesc) {
		this.extDesc = extDesc;
	}

	/**
	 * @return the filterList
	 */
	public List<FilterParameter> getFilterList() {
		return filterList;
	}

	/**
	 * @param filterList the filterList to set
	 */
	public void setFilterList(List<FilterParameter> filterList) {
		this.filterList = filterList;
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
