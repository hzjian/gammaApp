package com.cellinfo.controller.entity;

import java.util.Map;

public class SpatialQueryParameter {

	private Map<String,Object> queryRange;
	
	private String layerId;

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
}
