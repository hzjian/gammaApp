package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_task_user database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task_user")
@NamedQuery(name="TlGammaTaskUser.findAll", query="SELECT t FROM TlGammaTaskUser t")
public class TlGammaTaskUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Column(name="task_guid")
	private String taskGuid;

	@Column(name="user_guid")
	private String userGuid;

	public TlGammaTaskUser() {
	}

	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

	public String getUserGuid() {
		return this.userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

}