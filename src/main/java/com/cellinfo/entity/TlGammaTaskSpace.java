package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_task_space database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task_space")
@NamedQuery(name="TlGammaTaskSpace.findAll", query="SELECT t FROM TlGammaTaskSpace t")
public class TlGammaTaskSpace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="space_guid", nullable=false)
	private String spaceGuid;

	@Column(name="space_path", length=512)
	private String spacePath;

	@Column(name="task_guid")
	private String taskGuid;

	public TlGammaTaskSpace() {
	}

	public String getSpaceGuid() {
		return this.spaceGuid;
	}

	public void setSpaceGuid(String spaceGuid) {
		this.spaceGuid = spaceGuid;
	}

	public String getSpacePath() {
		return this.spacePath;
	}

	public void setSpacePath(String spacePath) {
		this.spacePath = spacePath;
	}

	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

}