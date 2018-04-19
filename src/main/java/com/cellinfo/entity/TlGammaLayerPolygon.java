package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Polygon;


/**
 * The persistent class for the tl_gamma_layer_polygon database table.
 * 
 */
@JsonIgnoreProperties({ "kernelGeom" })
@Entity
@Table(name="tl_gamma_layer_polygon")
@NamedQuery(name="TlGammaLayerPolygon.findAll", query="SELECT t FROM TlGammaLayerPolygon t")
public class TlGammaLayerPolygon implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@Column(name="kernel_guid", nullable=false)
	private String kernelGuid;
	
	@Column(name="geom_style", length=256)
	private String geomStyle;

	@Column(name="kernel_geom",columnDefinition = "geometry(Polygon,3857)")
	private Polygon kernelGeom;

	@Column(name="kernel_id")
	private Long kernelId;
	
	@Column(name="group_guid")
	private String groupGuid;
	
	@Column(name="kernel_classid")
	private String kernelClassid;
	
	@Column(name="user_guid")
	private String userGuid;
	
	@Column(name="task_guid")
	private String taskGuid;
	
	@Column(name="kernel_anno")
	private String kernelAnno;

	public TlGammaLayerPolygon() {
	}

	public String getGeomStyle() {
		return this.geomStyle;
	}

	public void setGeomStyle(String geomStyle) {
		this.geomStyle = geomStyle;
	}

	public Polygon getKernelGeom() {
		return this.kernelGeom;
	}

	public void setKernelGeom(Polygon kernelGeom) {
		this.kernelGeom = kernelGeom;
	}

	public String getKernelGuid() {
		return this.kernelGuid;
	}

	public void setKernelGuid(String kernelGuid) {
		this.kernelGuid = kernelGuid;
	}

	public Long getKernelId() {
		return this.kernelId;
	}

	public void setKernelId(Long kernelId) {
		this.kernelId = kernelId;
	}

	/**
	 * @return the groupGuid
	 */
	public String getGroupGuid() {
		return groupGuid;
	}

	/**
	 * @param groupGuid the groupGuid to set
	 */
	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
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
	 * @return the userGuid
	 */
	public String getUserGuid() {
		return userGuid;
	}

	/**
	 * @param userGuid the userGuid to set
	 */
	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	/**
	 * @return the taskGuid
	 */
	public String getTaskGuid() {
		return taskGuid;
	}

	/**
	 * @param taskGuid the taskGuid to set
	 */
	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

	/**
	 * @return the kernelAnno
	 */
	public String getKernelAnno() {
		return kernelAnno;
	}

	/**
	 * @param kernelAnno the kernelAnno to set
	 */
	public void setKernelAnno(String kernelAnno) {
		this.kernelAnno = kernelAnno;
	}

	
}