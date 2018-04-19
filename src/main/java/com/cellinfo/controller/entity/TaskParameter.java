package com.cellinfo.controller.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskParameter {

	@JsonProperty(value = "taskId")
	private String taskId;
	
	@JsonProperty(value = "taskFields")
	private List<TaskField> taskFields;
	
	@JsonProperty(value = "taskGeoType")
	private String taskGeoType;
	
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the taskFields
	 */
	public List<TaskField> getTaskFields() {
		return taskFields;
	}

	/**
	 * @param taskFields the taskFields to set
	 */
	public void setTaskFields(List<TaskField> taskFields) {
		this.taskFields = taskFields;
	}

	/**
	 * @return the taskGeoType
	 */
	public String getTaskGeoType() {
		return taskGeoType;
	}

	/**
	 * @param taskGeoType the taskGeoType to set
	 */
	public void setTaskGeoType(String taskGeoType) {
		this.taskGeoType = taskGeoType;
	}


	
}
