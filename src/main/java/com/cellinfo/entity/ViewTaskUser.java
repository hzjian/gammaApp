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
@Subselect( "SELECT a.task_guid, a.user_name, b.user_cnname, b.user_email, c.task_name, c.business_password, "
		  + "c.terminal_time, c.start_time,c.update_time,c.create_time,c.task_desc,c.kernel_classid,d.kernel_classname, "
		  + " (select count(*) from tl_gamma_task_user where task_guid = a.task_guid) user_num " 
		  +	"FROM tl_gamma_task_user a,tl_gamma_user b,tl_gamma_task c,tl_gamma_kernel d " 
		  + "WHERE a.user_name = b.user_name AND a.task_guid = c.task_guid and c.kernel_classid = d.kernel_classid ")
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
	
	@Column(name="update_time")
	private Timestamp updateTime;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	@Column(name="task_desc")
	private String taskDesc;
	
	@Column(name="kernel_classid")
	private String kernelClassid;
	
	@Column(name="kernel_classname")
	private String kernelClassname;
	
	@Column(name="user_num")
	private Integer userNum;

	
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
	 * @return the kernelClassid
	 */
	public String getKernelClassid() {
		return kernelClassid;
	}

	/**
	 * @param kernelClassid the kernelClassid to set
	 */
	public void setKernelClassid(String kernelClassid) {
		this.kernelClassid = kernelClassid;
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
	
	
}
