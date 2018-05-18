package com.cellinfo.controller.entity;

public class TaskParameter implements GammaParameter{

	private String taskName;

	private String taskGuid;
	
	private String taskDesc;
	
	private String startDate;
	
	private String endDate;
	
	private String classId;
	
	private String className;
	
	private String extGuid;
	
	private String extName;
	
	private FieldParameter field;
		
	private String refExtGuid;
	
	private String refExtName;
	
	private UserParameter user;
	
	private String refClassid;
	
	private String refClassName;
	
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
	 * @return the field
	 */
	public FieldParameter getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(FieldParameter field) {
		this.field = field;
	}

	/**
	 * @return the refExtGuid
	 */
	public String getRefExtGuid() {
		return refExtGuid;
	}

	/**
	 * @param refExtGuid the refExtGuid to set
	 */
	public void setRefExtGuid(String refExtGuid) {
		this.refExtGuid = refExtGuid;
	}

	/**
	 * @return the user
	 */
	public UserParameter getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserParameter user) {
		this.user = user;
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

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the extName
	 */
	public String getExtName() {
		return extName;
	}

	/**
	 * @param extName the extName to set
	 */
	public void setExtName(String extName) {
		this.extName = extName;
	}

	/**
	 * @return the refClassName
	 */
	public String getRefClassName() {
		return refClassName;
	}

	/**
	 * @param refClassName the refClassName to set
	 */
	public void setRefClassName(String refClassName) {
		this.refClassName = refClassName;
	}

	/**
	 * @return the refExtName
	 */
	public String getRefExtName() {
		return refExtName;
	}

	/**
	 * @param refExtName the refExtName to set
	 */
	public void setRefExtName(String refExtName) {
		this.refExtName = refExtName;
	}

	
}
