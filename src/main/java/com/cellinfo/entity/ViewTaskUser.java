package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the VIEW_USERLAYER database table.
 * 
 */
@Entity
@Table(name="view_task_user")
@NamedQuery(name="ViewTaskUser.findAll", query="SELECT s FROM ViewTaskUser s")
public class ViewTaskUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ViewTaskUserPK id;

	@Column(name="user_cnname", length=32)
	private String userCnname;

	
	@Column(name="user_email", length=64)
	private String userEmail;
	
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



	
}