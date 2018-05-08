package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_task_kernel database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task_kernel")
@NamedQuery(name="TlGammaTaskKernel.findAll", query="SELECT t FROM TlGammaTaskKernel t")
public class TlGammaTaskKernel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Column(name="kernel_guid")
	private String kernelGuid;

	@Column(name="task_guid", nullable=false)
	private String taskGuid;
	
	@Column(name="kernel_classid")
	private String kernelClassid;

	@Column(name="kernel_status")
	private String kernelStatus;
	
	public TlGammaTaskKernel() {
	}

	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the kernelGuid
	 */
	public String getKernelGuid() {
		return kernelGuid;
	}


	/**
	 * @param kernelGuid the kernelGuid to set
	 */
	public void setKernelGuid(String kernelGuid) {
		this.kernelGuid = kernelGuid;
	}


	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
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
	 * @return the kernelStatus
	 */
	public String getKernelStatus() {
		return kernelStatus;
	}


	/**
	 * @param kernelStatus the kernelStatus to set
	 */
	public void setKernelStatus(String kernelStatus) {
		this.kernelStatus = kernelStatus;
	}
	
	
	
}