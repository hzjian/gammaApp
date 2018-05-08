package com.cellinfo.controller.entity;

import java.util.Map;

public class SpatialQueryParameter {

	private Map<String,Object> queryRange;
	
	private String classId;
	
	private String taskId;
	/**
	 * @return the queryRange
	 */
	public Map<String, Object> getQueryRange() {
		return queryRange;
	}

	/**
	 * @param queryRange the queryRange to set
	 */
	public void setQueryRange(Map<String, Object> queryRange) {
		this.queryRange = queryRange;
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
}
