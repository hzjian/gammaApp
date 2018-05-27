package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.MultiPolygon;


/**
 * The persistent class for the tl_gamma_layer_polygon database table.
 * 
 */
@JsonIgnoreProperties({ "kernelGeom" })
@Entity
//@Table(name="view_task_polygon")
//@NamedQuery(name="ViewTaskPolygon.findAll", query="SELECT t FROM ViewTaskPolygon t")
@Immutable
@Subselect("select  a.kernel_guid,a.geom_style,a.kernel_anno,a.kernel_classid,a.kernel_geom,a.kernel_id,a.user_name,b.ext_guid  "
		+ "from tl_gamma_layer_polygon a, tl_gamma_kernel_subset b "
		+ "where a.kernel_guid = b.kernel_guid" )
public class ViewTaskPolygon implements Serializable {
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
	
	@Column(name="kernel_anno")
	private String kernelAnno;
	
	@Column(name="ext_guid")
	private String extGuid;

	public ViewTaskPolygon() {
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