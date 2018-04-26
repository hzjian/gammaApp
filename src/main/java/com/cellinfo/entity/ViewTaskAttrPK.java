package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ViewTaskAttrPK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "task_guid")
	private String taskGuid;

	@Column(name = "attr_guid")
	private String attrGuid;

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
	 * @return the attrGuid
	 */
	public String getAttrGuid() {
		return attrGuid;
	}

	/**
	 * @param attrGuid the attrGuid to set
	 */
	public void setAttrGuid(String attrGuid) {
		this.attrGuid = attrGuid;
	}

	
}
