package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the VIEW_USERLAYER database table.
 * 
 */
@Entity
@Table(name="view_task_kernel")
@NamedQuery(name="ViewTaskKernel.findAll", query="SELECT s FROM ViewTaskKernel s")
public class ViewTaskKernel implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ViewTaskKernelPK id;

	@Column(name="kernel_classdesc", length=512)
	private String kernelClassdesc;

	@Column(name="kernel_classname", length=256)
	private String kernelClassname;
	
	@Column(name="service_name", length=64)
	private String serviceName;
	
	public ViewTaskKernel() {
	}

	/**
	 * @return the id
	 */
	public ViewTaskKernelPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(ViewTaskKernelPK id) {
		this.id = id;
	}

	/**
	 * @return the kernelClassdesc
	 */
	public String getKernelClassdesc() {
		return kernelClassdesc;
	}

	/**
	 * @param kernelClassdesc the kernelClassdesc to set
	 */
	public void setKernelClassdesc(String kernelClassdesc) {
		this.kernelClassdesc = kernelClassdesc;
	}

	/**
	 * @return the kernelClassname
	 */
	public String getKernelClassname() {
		return kernelClassname;
	}

	/**
	 * @param kernelClassname the kernelClassname to set
	 */
	public void setKernelClassname(String kernelClassname) {
		this.kernelClassname = kernelClassname;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}