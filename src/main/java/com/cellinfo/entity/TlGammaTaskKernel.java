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


	public String getKernelGuid() {
		return this.kernelGuid;
	}

	public void setKernelGuid(String kernelGuid) {
		this.kernelGuid = kernelGuid;
	}

	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

}