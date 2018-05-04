package com.cellinfo.entity;

import java.io.Serializable;

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
@Table(name="tl_gamma_kernel_ext")
@NamedQuery(name="TlGammaKernelExt.findAll", query="SELECT t FROM TlGammaKernelExt t")
public class TlGammaKernelExt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ext_guid", nullable=false)
	private String extGuid;
	
	@Column(name="ext_name")
	private String extName;

	@Column(name="kernel_classid")
	private String kernelClassid;

	@Column(name="ext_desc")
	private String extDesc;
	

	public TlGammaKernelExt() {
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
	 * @return the extName
	 */
	public String getExtName() {
		return extName;
	}

	/**
	 * @param extName the extName to set
	 */
	public void setExtName(String extName) {
		this.extName = extName;
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
	 * @return the extDesc
	 */
	public String getExtDesc() {
		return extDesc;
	}

	/**
	 * @param extDesc the extDesc to set
	 */
	public void setExtDesc(String extDesc) {
		this.extDesc = extDesc;
	}
	
}