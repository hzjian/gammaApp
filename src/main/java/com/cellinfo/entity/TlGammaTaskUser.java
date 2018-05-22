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
 * The persistent class for the tl_gamma_task_user database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task_user")
@NamedQuery(name="TlGammaTaskUser.findAll", query="SELECT t FROM TlGammaTaskUser t")
public class TlGammaTaskUser implements Serializable {
	private static final long serialVersionUID = 1L;


	@SequenceGenerator(name="seq_task_user", sequenceName="seq_task_user",allocationSize=1)
	@Id 
	@GeneratedValue(generator="seq_task_user")
	private Long id;
	
	@Column(name="task_guid")
	private String taskGuid;

	@Column(name="user_name")
	private String userName;

	public TlGammaTaskUser() {
	}

	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}