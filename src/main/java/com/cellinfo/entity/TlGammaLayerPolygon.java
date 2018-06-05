package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.MultiPolygon;


/**
 * The persistent class for the tl_gamma_layer_polygon database table.
 * 
 */
@JsonIgnoreProperties({ "kernelGeom" })
@Entity
@Table(name="tl_gamma_layer_polygon",indexes = {
        @Index(columnList = "kernel_classid", name = "idx_polygon_kernel_classid")
        })
@NamedQuery(name="TlGammaLayerPolygon.findAll", query="SELECT t FROM TlGammaLayerPolygon t")
public class TlGammaLayerPolygon implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@Column(name="kernel_guid", nullable=false)
	private String kernelGuid;
	
	@Column(name="geom_style", length=256)
	private String geomStyle;

	@Column(name="kernel_geom",columnDefinition = "geometry(MultiPolygon,4326)")
	private MultiPolygon kernelGeom;

	@Column(name="kernel_id")
	private String kernelId;
	
	@Column(name="kernel_classid")
	private String kernelClassid;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="task_guid")
	private String taskGuid;
	
	@Column(name="kernel_anno", length=256)
	private String kernelAnno;
	
	@Column(name="update_time")
	private Timestamp updateTime;


	public TlGammaLayerPolygon() {
	}

	public String getGeomStyle() {
		return this.geomStyle;
	}

	public void setGeomStyle(String geomStyle) {
		this.geomStyle = geomStyle;
	}

	public MultiPolygon getKernelGeom() {
		return this.kernelGeom;
	}

	public void setKernelGeom(MultiPolygon kernelGeom) {
		this.kernelGeom = kernelGeom;
	}

	public String getKernelGuid() {
		return this.kernelGuid;
	}

	public void setKernelGuid(String kernelGuid) {
		this.kernelGuid = kernelGuid;
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

	/**
	 * @return the kernelId
	 */
	public String getKernelId() {
		return kernelId;
	}

	/**
	 * @param kernelId the kernelId to set
	 */
	public void setKernelId(String kernelId) {
		this.kernelId = kernelId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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