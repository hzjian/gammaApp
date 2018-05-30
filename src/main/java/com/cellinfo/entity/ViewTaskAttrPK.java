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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attrGuid == null) ? 0 : attrGuid.hashCode());
		result = prime * result + ((taskGuid == null) ? 0 : taskGuid.hashCode());
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
		ViewTaskAttrPK other = (ViewTaskAttrPK) obj;
		if (attrGuid == null) {
			if (other.attrGuid != null)
				return false;
		} else if (!attrGuid.equals(other.attrGuid))
			return false;
		if (taskGuid == null) {
			if (other.taskGuid != null)
				return false;
		} else if (!taskGuid.equals(other.taskGuid))
			return false;
		return true;
	}

	
	
}
