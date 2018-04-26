package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewTaskKernelPK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "task_guid")
	private String taskGuid;

	@Column(name = "kernel_classid")
	private String kernelClassid;

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
