package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.Polygon;


@Entity
@Table(name="tl_gamma_kernel_geofilter")
@NamedQuery(name="TlGammaKernelGeoFilter.findAll", query="SELECT t FROM TlGammaKernelGeoFilter t")
public class TlGammaKernelGeoFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="filter_guid", length=36)
	private String filterGuid;
	
	@Column(name="ext_guid", length=36)
	private String extGuid;
	
	@Column(name="filter_type" , length=64)
	private String filterType;
	
	@Column(name="filter_name" , length=64)
	private String filterName;
	
	@Column(name="filter_geom",columnDefinition = "geometry(Polygon,4326)")
	private Polygon filterGeom;
	
	@Column(name="update_time")
	private Timestamp updateTime;

	public TlGammaKernelGeoFilter() {
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
	 * @return the filterGeom
	 */
	public Polygon getFilterGeom() {
		return filterGeom;
	}

	/**
	 * @param filterGeom the filterGeom to set
	 */
	public void setFilterGeom(Polygon filterGeom) {
		this.filterGeom = filterGeom;
	}

	/**
	 * @return the filterName
	 */
	public String getFilterName() {
		return filterName;
	}

	/**
	 * @param filterName the filterName to set
	 */
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	/**
	 * @return the updateTime
	 */
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}