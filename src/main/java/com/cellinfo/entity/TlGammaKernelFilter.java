package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="tl_gamma_kernel_filter")
@NamedQuery(name="TlGammaKernelFilter.findAll", query="SELECT t FROM TlGammaKernelFilter t")
public class TlGammaKernelFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="filter_guid", nullable=false)
	private String filterGuid;
	
	@Column(name="attr_guid")
	private String attrGuid;

	@Column(name="ext_guid")
	private String extGuid;
	
	@Column(name="attr_field", length=64)
	private String attrField;
	
	/**
	 * STRING
	 * INTEGER
	 * NUMBER
	 * DATETIME
	 * GEOMETRY
	 */

	@Column(name="attr_type", length=64)
	private String attrType;
	
	/**
	 * INTEGER
	 * DOUBLE
	 * DATETIME
			 * MORETHAN
			 * LESSTHAN
			 * BETWEEN
	 * 
	 * GEOMETRY
			 * WITHIN
			 * 
     *	STRING
     		 * LIKE
	 */
	@Column(name="filter_type")
	private String filterType;
	
	@Column(name="min_value")
	private String minValue;
	
	@Column(name="max_value")
	private String maxValue;

	public TlGammaKernelFilter() {
	}

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
	 * @return the filterType
	 */
	public String getFilterType() {
		return filterType;
	}

	/**
	 * @param filterType the filterType to set
	 */
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	/**
	 * @return the attrField
	 */
	public String getAttrField() {
		return attrField;
	}

	/**
	 * @param attrField the attrField to set
	 */
	public void setAttrField(String attrField) {
		this.attrField = attrField;
	}

	/**
	 * @return the attrType
	 */
	public String getAttrType() {
		return attrType;
	}

	/**
	 * @param attrType the attrType to set
	 */
	public void setAttrType(String attrType) {
		this.attrType = attrType;
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

	
}