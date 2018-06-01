package com.cellinfo.controller.entity;

import java.util.Map;

public class GAQueryParameter {
	
	private Map<String,Object> queryRange;
	
	private String kernelClassid;
	
	private String extGuid;
	
	private String geoType;

	/**
	 * @return the queryRange
	 */
	public Map<String, Object> getQueryRange() {
		return queryRange;
	}

	/**
	 * @param queryRange the queryRange to set
	 */
	public void setQueryRange(Map<String, Object> queryRange) {
		this.queryRange = queryRange;
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
	
}
