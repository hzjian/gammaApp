package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_group database table.
 * 
 */
@Entity
@Table(name="tl_gamma_group")
@NamedQuery(name="TlGammaGroup.findAll", query="SELECT t FROM TlGammaGroup t")
public class TlGammaGroup implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="group_guid", nullable=false)
	private String groupGuid;

	@Column(name="group_address", length=512)
	private String groupAddress;

	@Column(name="group_code", length=256)
	private String groupCode;

	@Column(name="group_email", length=128)
	private String groupEmail;

	@Column(name="group_name", length=256)
	private String groupName;

	@Column(name="group_phone", length=128)
	private String groupPhone;

	@Column(name="group_pic", length=128)
	private String groupPic;

	@Column(name="group_status")
	private Integer groupStatus;
	
	@Column(name="group_service", length=256)
	private String groupService;

	public TlGammaGroup() {
	}

	public String getGroupAddress() {
		return this.groupAddress;
	}

	public void setGroupAddress(String groupAddress) {
		this.groupAddress = groupAddress;
	}

	public String getGroupCode() {
		return this.groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupEmail() {
		return this.groupEmail;
	}

	public void setGroupEmail(String groupEmail) {
		this.groupEmail = groupEmail;
	}

	public String getGroupGuid() {
		return this.groupGuid;
	}

	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupPhone() {
		return this.groupPhone;
	}

	public void setGroupPhone(String groupPhone) {
		this.groupPhone = groupPhone;
	}

	public String getGroupPic() {
		return this.groupPic;
	}

	public void setGroupPic(String groupPic) {
		this.groupPic = groupPic;
	}

	public Integer getGroupStatus() {
		return this.groupStatus;
	}

	public void setGroupStatus(Integer groupStatus) {
		this.groupStatus = groupStatus;
	}

	/**
	 * @return the groupService
	 */
	public String getGroupService() {
		return groupService;
	}

	/**
	 * @param groupService the groupService to set
	 */
	public void setGroupService(String groupService) {
		this.groupService = groupService;
	}

}