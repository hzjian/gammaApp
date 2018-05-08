package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_group_task database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task")
@NamedQuery(name="TlGammaTask.findAll", query="SELECT t FROM TlGammaTask t")
public class TlGammaTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="task_guid", nullable=false)
	private String taskGuid;
	
	@Column(name="business_password", length=256)
	private String businessPassword;

	@Column(name="geom_layername", length=128)
	private String geomLayername;

	@Column(name="group_guid")
	private String groupGuid;

	@Column(name="kernel_classid", nullable=false)
	private String kernelClassid;

	@Column(name="kernel_isadd")
	private Integer kernelIsadd;

	@Column(name="task_name", length=256)
	private String taskName;

	@Column(name="task_timeend")
	private Timestamp taskTimeend;

	@Column(name="task_timestart")
	private Timestamp taskTimestart;

	@Column(name="user_guid")
	private String userGuid;

	@Column(name="user_name", length=64)
	private String userName;

	public TlGammaTask() {
	}

	public String getBusinessPassword() {
		return this.businessPassword;
	}

	public void setBusinessPassword(String businessPassword) {
		this.businessPassword = businessPassword;
	}

	public String getGeomLayername() {
		return this.geomLayername;
	}

	public void setGeomLayername(String geomLayername) {
		this.geomLayername = geomLayername;
	}

	public String getGroupGuid() {
		return this.groupGuid;
	}

	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}

	public String getKernelClassid() {
		return this.kernelClassid;
	}

	public void setKernelClassid(String kernelClassid) {
		this.kernelClassid = kernelClassid;
	}

	public Integer getKernelIsadd() {
		return this.kernelIsadd;
	}

	public void setKernelIsadd(Integer kernelIsadd) {
		this.kernelIsadd = kernelIsadd;
	}

	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Timestamp getTaskTimeend() {
		return this.taskTimeend;
	}

	public void setTaskTimeend(Timestamp taskTimeend) {
		this.taskTimeend = taskTimeend;
	}

	public Timestamp getTaskTimestart() {
		return this.taskTimestart;
	}

	public void setTaskTimestart(Timestamp taskTimestart) {
		this.taskTimestart = taskTimestart;
	}

	public String getUserGuid() {
		return this.userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}