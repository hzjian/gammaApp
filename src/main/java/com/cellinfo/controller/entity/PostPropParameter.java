package com.cellinfo.controller.entity;

import java.util.List;

public class PostPropParameter {
	
	private String guid;
	private String taskid;
	private String username;
	
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
	 * @return the taskid
	 */
	public String getTaskid() {
		return taskid;
	}

	/**
	 * @param taskid the taskid to set
	 */
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
