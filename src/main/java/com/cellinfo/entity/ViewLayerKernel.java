package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;


/**
 * The persistent class for the tl_gamma_layer_line database table.
 * 
 */
@Entity
//@Table(name="view_layer_kernel")
//@NamedQuery(name="ViewLayerKernel.findAll", query="SELECT t FROM ViewLayerKernel t")
@Immutable
@Subselect("select kernel_guid,kernel_classid from tl_gamma_layer_line " + 
		"union all " + 
		"select kernel_guid,kernel_classid from tl_gamma_layer_point " + 
		"union all " + 
		"select kernel_guid,kernel_classid from tl_gamma_layer_polygon")
public class ViewLayerKernel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="kernel_guid", nullable=false)
	private String kernelGuid;
	
	@Column(name="kernel_classid")
	private String kernelClassid;
	
	
	public ViewLayerKernel() {
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
	
}