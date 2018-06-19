package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Subselect("select a.task_guid,a.business_password,a.kernel_classid,b.kernel_classname,a.task_name,a.start_time,a.terminal_time ,a.user_name,"
		+ "a.create_time,a.update_time,a.task_desc, (select count(*) from tl_gamma_task_user where task_guid = a.task_guid) user_num "
		+ "from tl_gamma_task a, tl_gamma_kernel b "
		+ "where a.kernel_classid = b.kernel_classid ")
public class ViewTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="task_guid")
	private String taskGuid;

	@Column(name="business_password")
	private String businessPassword;
	
	@Column(name="kernel_classid")
	private String kernelClassid;
	
	@Column(name="kernel_classname")
	private String kernelClassname;

	@Column(name="task_name")
	private String taskName;

	@Column(name="terminal_time")
	private Timestamp terminalTime;

	@Column(name="start_time")
	private Timestamp startTime;

	@Column(name="user_name")
	private String userName;
	
	@Column(name="update_time")
	private Timestamp updateTime;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	@Column(name="task_desc")
	private String taskDesc;
	
	@Column(name="user_num")
	private Integer userNum;

	public ViewTask() {
	}

	public String getKernelClassid() {
		return this.kernelClassid;
	}

	public void setKernelClassid(String kernelClassid) {
		this.kernelClassid = kernelClassid;
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

	/**
	 * @return the kernelClassname
	 */
	public String getKernelClassname() {
		return kernelClassname;
	}

	/**
	 * @param kernelClassname the kernelClassname to set
	 */
	public void setKernelClassname(String kernelClassname) {
		this.kernelClassname = kernelClassname;
	}

	/**
	 * @return the userNum
	 */
	public Integer getUserNum() {
		return userNum;
	}

	/**
	 * @param userNum the userNum to set
	 */
	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
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