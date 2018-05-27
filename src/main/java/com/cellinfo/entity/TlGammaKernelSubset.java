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
 * 标签核心对象关系
 * 
 */
@Entity
@Table(name="tl_gamma_kernel_subset")
@NamedQuery(name="TlGammaKernelSubset.findAll", query="SELECT t FROM TlGammaKernelSubset t")
public class TlGammaKernelSubset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seq_kernel_subset", sequenceName="seq_kernel_subset",allocationSize=1)
	@GeneratedValue(generator="seq_kernel_subset")
	private Long id;
	
	@Column(name="kernel_guid",length=36)
	private String kernelGuid;

	@Column(name="ext_guid", length=36)
	private String extGuid;

	public TlGammaKernelSubset() {
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