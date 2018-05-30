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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kernelGuid == null) ? 0 : kernelGuid.hashCode());
		result = prime * result + ((tmpGuid == null) ? 0 : tmpGuid.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TlGammaTaskTmpPK other = (TlGammaTaskTmpPK) obj;
		if (kernelGuid == null) {
			if (other.kernelGuid != null)
				return false;
		} else if (!kernelGuid.equals(other.kernelGuid))
			return false;
		if (tmpGuid == null) {
			if (other.tmpGuid != null)
				return false;
		} else if (!tmpGuid.equals(other.tmpGuid))
			return false;
		return true;
	}
	
	
}
