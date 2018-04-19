package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_role database table.
 * 
 */
@Entity
@Table(name="tl_gamma_role")
@NamedQuery(name="TlGammaRole.findAll", query="SELECT t FROM TlGammaRole t")
public class TlGammaRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="role_desc", length=128)
	private String roleDesc;

	@Id
	@Column(name="role_id", nullable=false, length=32)
	private String roleId;

	public TlGammaRole() {
	}

	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}