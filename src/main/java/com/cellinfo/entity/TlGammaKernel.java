package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_group_kernel database table.
 * 
 */
@Entity
@Table(name="tl_gamma_kernel")
@NamedQuery(name="TlGammaKernel.findAll", query="SELECT t FROM TlGammaKernel t")
public class TlGammaKernel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="kernel_classid", length=36)
	private String kernelClassid;
	
	@Column(name="group_guid", length=36)
	private String groupGuid;

	@Column(name="kernel_classdesc", length=512)
	private String kernelClassdesc;

	@Column(name="kernel_classname", length=256)
	private String kernelClassname;
	
	@Column(name="kernel_num")
	private Long kernelNum;

	@Column(name="geom_type", length=16)
	private String geomType;
	
	@Column(name="update_time")
	private Timestamp updateTime;
	
	@Column(name="server_path", length=128)
	private String serverPath;

	public TlGammaKernel() {
	}

	public String getGroupGuid() {
		return this.groupGuid;
	}

	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}

	public String getKernelClassdesc() {
		return this.kernelClassdesc;
	}

	public void setKernelClassdesc(String kernelClassdesc) {
		this.kernelClassdesc = kernelClassdesc;
	}

	public String getKernelClassid() {
		return this.kernelClassid;
	}

	public void setKernelClassid(String kernelClassid) {
		this.kernelClassid = kernelClassid;
	}

	public String getKernelClassname() {
		return this.kernelClassname;
	}

	public void setKernelClassname(String kernelClassname) {
		this.kernelClassname = kernelClassname;
	}

	/**
	 * @return the geomType
	 */
	public String getGeomType() {
		return geomType;
	}

	/**
	 * @param geomType the geomType to set
	 */
	public void setGeomType(String geomType) {
		this.geomType = geomType;
	}

	/**
	 * @return the serverPath
	 */
	public String getServerPath() {
		return serverPath;
	}

	/**
	 * @param serverPath the serverPath to set
	 */
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
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

	/**
	 * @return the kernelNum
	 */
	public Long getKernelNum() {
		return kernelNum;
	}

	/**
	 * @param kernelNum the kernelNum to set
	 */
	public void setKernelNum(Long kernelNum) {
		this.kernelNum = kernelNum;
	}
	
	
}