package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.Geometry;


/**
 * The persistent class for the tl_gamma_layer_point database table.
 * 
 */
@Entity
@Table(name="tl_gamma_layer_point")
@NamedQuery(name="TlGammaLayerPoint.findAll", query="SELECT t FROM TlGammaLayerPoint t")
public class TlGammaLayerPoint implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="kernel_guid", nullable=false)
	private String kernelGuid;
	
	@Column(name="kernel_id")
	private String kernelId;

	@Column(name="geom_style", length=256)
	private String geomStyle;

	@Column(name="kernel_geom")
	private Geometry kernelGeom;

	@Column(name="group_guid")
	private String groupGuid;
	
	@Column(name="kernel_classid")
	private String kernelClassid;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="task_guid")
	private String taskGuid;
	
	@Column(name="kernel_anno")
	private String kernelAnno;
	
	public TlGammaLayerPoint() {
	}

	public String getGeomStyle() {
		return this.geomStyle;
	}

	public void setGeomStyle(String geomStyle) {
		this.geomStyle = geomStyle;
	}

	public Geometry getKernelGeom() {
		return this.kernelGeom;
	}

	public void setKernelGeom(Geometry kernelGeom) {
		this.kernelGeom = kernelGeom;
	}

	public String getKernelGuid() {
		return this.kernelGuid;
	}

	public void setKernelGuid(String kernelGuid) {
		this.kernelGuid = kernelGuid;
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