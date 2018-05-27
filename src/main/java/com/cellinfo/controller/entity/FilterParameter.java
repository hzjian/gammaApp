package com.cellinfo.controller.entity;

public class FilterParameter {
	
	private String filterId;
	
	private String extId;
	
	private String attrId;
	
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
	 * @return the attrId
	 */
	public String getAttrId() {
		return attrId;
	}

	/**
	 * @param attrId the attrId to set
	 */
	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}
	
	
}
