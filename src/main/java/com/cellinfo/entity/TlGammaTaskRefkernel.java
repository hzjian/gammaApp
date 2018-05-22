package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_task_kernel database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task_refkernel")
@NamedQuery(name="TlGammaTaskRefkernel.findAll", query="SELECT t FROM TlGammaTaskRefkernel t")
public class TlGammaTaskRefkernel implements Serializable {
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name="seq_task_refkernel", sequenceName="seq_task_refkernel",allocationSize=1)
	@Id 
	@GeneratedValue(generator="seq_task_refkernel")
	private Long id;
	
	@Column(name="kernel_guid",length=36)
	private String kernelGuid;

	@Column(name="task_guid", length=36)
	private String taskGuid;

	
	public TlGammaTaskRefkernel() {
	}

	public TlGammaTaskRefkernel(String taskGuid,String kernelGuid) {
		this.taskGuid = taskGuid;
		this.kernelGuid= kernelGuid;
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
}