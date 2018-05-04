package com.cellinfo.controller.entity;

import java.util.List;

public class TaskParameter {

	private String taskname;
	//@JsonProperty(value = "taskGuid")
	private String taskGuid;
	
	private String taskdesc;
	
	private String startdatestr;
	
	private String enddatestr;
	
	private List<FieldParameter> fieldlist;
	
	private List<KernelParameter> kernellist;
	
	private List<UserParameter> userlist;

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
	 * @return the startdatestr
	 */
	public String getStartdatestr() {
		return startdatestr;
	}

	/**
	 * @param startdatestr the startdatestr to set
	 */
	public void setStartdatestr(String startdatestr) {
		this.startdatestr = startdatestr;
	}

	/**
	 * @return the enddatestr
	 */
	public String getEnddatestr() {
		return enddatestr;
	}

	/**
	 * @param enddatestr the enddatestr to set
	 */
	public void setEnddatestr(String enddatestr) {
		this.enddatestr = enddatestr;
	}

	/**
	 * @return the fieldlist
	 */
	public List<FieldParameter> getFieldlist() {
		return fieldlist;
	}

	/**
	 * @param fieldlist the fieldlist to set
	 */
	public void setFieldlist(List<FieldParameter> fieldlist) {
		this.fieldlist = fieldlist;
	}

	/**
	 * @return the kernellist
	 */
	public List<KernelParameter> getKernellist() {
		return kernellist;
	}

	/**
	 * @param kernellist the kernellist to set
	 */
	public void setKernellist(List<KernelParameter> kernellist) {
		this.kernellist = kernellist;
	}

	/**
	 * @return the userlist
	 */
	public List<UserParameter> getUserlist() {
		return userlist;
	}

	/**
	 * @param userlist the userlist to set
	 */
	public void setUserlist(List<UserParameter> userlist) {
		this.userlist = userlist;
	}

	/**
	 * @return the taskname
	 */
	public String getTaskname() {
		return taskname;
	}

	/**
	 * @param taskname the taskname to set
	 */
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	/**
	 * @return the taskdesc
	 */
	public String getTaskdesc() {
		return taskdesc;
	}

	/**
	 * @param taskdesc the taskdesc to set
	 */
	public void setTaskdesc(String taskdesc) {
		this.taskdesc = taskdesc;
	}
	
	
	
	
}
