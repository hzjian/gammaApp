package com.cellinfo.controller.entity;

import java.util.List;

public class PostPropParameter {
	
	private String guid;
	private String taskGuid;
	private String userName;
	
	private List<PropItem> props;

	/**
	 * @return the props
	 */
	public List<PropItem> getProps() {
		return props;
	}

	/**
	 * @param props the props to set
	 */
	public void setProps(List<PropItem> props) {
		this.props = props;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
