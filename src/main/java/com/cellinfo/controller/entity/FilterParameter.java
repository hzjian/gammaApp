package com.cellinfo.controller.entity;

public class FilterParameter {
	
	private String filterId;
	
	private String extGuid;
	
	private String attrGuid;
	
	/**
	 * INTEGER
	 * DOUBLE
	 * DATETIME
			 * BETWEEN
	 * 
	 * GEOMETRY
			 * WITHIN
			 * 
     *	STRING
     		 * LIKE
	 */
	private String type;
	
	private String minValue;
	
	private String maxValue;

	/**
	 * @return the attrGuid
	 */
	public String getAttrGuid() {
		return attrGuid;
	}

	/**
	 * @param attrGuid the attrGuid to set
	 */
	public void setAttrGuid(String attrGuid) {
		this.attrGuid = attrGuid;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the minValue
	 */
	public String getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public String getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

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
	
}
