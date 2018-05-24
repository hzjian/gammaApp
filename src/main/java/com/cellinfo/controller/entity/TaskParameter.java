package com.cellinfo.controller.entity;

public class TaskParameter implements GammaParameter{

	private String taskName;

	private String taskGuid;
	
	private String taskDesc;
	
	private String startDate;
	
	private String endDate;
	
	private String classId;
	
	private String extGuid;
	
	private String refClassid;
	
	private String fileId;
	
	private String busPassword;
	
	private Integer kernelAdd;

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
	 * @return the taskGuid
	 */
	public String getTaskGuid() {
		return taskGuid;
	}

	/**
	 * @param taskGuid the taskGuid to set
	 */
	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
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
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}

	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}

	/**
	 * @return the extGuid
	 */
	public String getExtGuid() {
		return extGuid;
	}

	/**
	 * @param extGuid the extGuid to set
	 */
	public void setExtGuid(String extGuid) {
		this.extGuid = extGuid;
	}

	/**
	 * @return the refClassid
	 */
	public String getRefClassid() {
		return refClassid;
	}

	/**
	 * @param refClassid the refClassid to set
	 */
	public void setRefClassid(String refClassid) {
		this.refClassid = refClassid;
	}

	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the busPassword
	 */
	public String getBusPassword() {
		return busPassword;
	}

	/**
	 * @param busPassword the busPassword to set
	 */
	public void setBusPassword(String busPassword) {
		this.busPassword = busPassword;
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
}
