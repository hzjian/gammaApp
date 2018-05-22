package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TlGammaTaskTmpPK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "tmp_guid",length=36)
	private String tmpGuid;

	@Column(name = "kernel_guid",length=36)
	private String kernelGuid;

	/**
	 * @return the tmpGuid
	 */
	public String getTmpGuid() {
		return tmpGuid;
	}

	/**
	 * @param tmpGuid the tmpGuid to set
	 */
	public void setTmpGuid(String tmpGuid) {
		this.tmpGuid = tmpGuid;
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

	
	
}
