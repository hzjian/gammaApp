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
	@Column(name="task_guid", length=36, nullable=false)
	private String taskGuid;
	
	@Column(name="business_password", length=256)
	private String businessPassword;

	@Column(name="group_guid", length=36)
	private String groupGuid;

	@Column(name="kernel_classid", length=36, nullable=false)
	private String kernelClassid;

	/**
	 * 该任务能否新建核心对象
	 */
	@Column(name="kernel_add")
	private Integer kernelAdd;

	@Column(name="task_name", length=256)
	private String taskName;

	@Column(name="terminal_time")
	private Timestamp terminalTime;

	@Column(name="start_time")
	private Timestamp startTime;

	@Column(name="user_name", length=64)
	private String userName;
	
	@Column(name="update_time")
	private Timestamp updateTime;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	@Column(name="task_desc", length=512)
	private String taskDesc;

	public TlGammaTask() {
	}

	public String getBusinessPassword() {
		return this.businessPassword;
	}

	public void setBusinessPassword(String businessPassword) {
		this.businessPassword = businessPassword;
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
	
	/**
	 * @return the kernelAdd
	 */
	public Integer getKernelAdd() {
		return kernelAdd;
	}

	/**
	 * @param kernelAdd the kernelAdd to set
	 */
	public void setKernelAdd(Integer kernelAdd) {
		this.kernelAdd = kernelAdd;
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

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the updateTime
	 */
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the taskDesc
	 */
	public String getTaskDesc() {
		return taskDesc;
	}

	/**
	 * @param taskDesc the taskDesc to set
	 */
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	
	
	
}