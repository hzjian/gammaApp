package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;


/**
 * The persistent class for the VIEW_USERLAYER database table.
 * 
 */
@Entity
//@Table(name="view_task_user")
//@NamedQuery(name="ViewTaskUser.findAll", query="SELECT s FROM ViewTaskUser s")
@Immutable
@Subselect( "SELECT a.task_guid, a.user_name, b.user_cnname, b.user_email, c.task_name, c.business_password, c.terminal_time, c.start_time " + 
			"FROM tl_gamma_task_user a,tl_gamma_user b,tl_gamma_task c " + 
			"WHERE a.user_name = b.user_name AND a.task_guid = c.task_guid")
public class ViewTaskUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ViewTaskUserPK id;

	@Column(name="user_cnname", length=32)
	private String userCnname;

	
	@Column(name="user_email", length=64)
	private String userEmail;
	
	@Column(name="task_name")
	private String taskName;

	@Column(name="terminal_time")
	private Timestamp terminalTime;

	@Column(name="start_time")
	private Timestamp startTime;
	
	@Column(name="business_password")
	private String businessPassword;
	
	public ViewTaskUser() {
	}

	/**
	 * @return the id
	 */
	public ViewTaskUserPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(ViewTaskUserPK id) {
		this.id = id;
	}

	/**
	 * @return the userCnname
	 */
	public String getUserCnname() {
		return userCnname;
	}

	/**
	 * @param userCnname the userCnname to set
	 */
	public void setUserCnname(String userCnname) {
		this.userCnname = userCnname;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	/**
	 * @return the terminalTime
	 */
	public Timestamp getTerminalTime() {
		return terminalTime;
	}

	/**
	 * @param terminalTime the terminalTime to set
	 */
	public void setTerminalTime(Timestamp terminalTime) {
		this.terminalTime = terminalTime;
	}

	/**
	 * @return the startTime
	 */
	public Timestamp getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the businessPassword
	 */
	public String getBusinessPassword() {
		return businessPassword;
	}

	/**
	 * @param businessPassword the businessPassword to set
	 */
	public void setBusinessPassword(String businessPassword) {
		this.businessPassword = businessPassword;
	}
	
	
	
}
