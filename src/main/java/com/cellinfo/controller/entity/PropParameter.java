package com.cellinfo.controller.entity;

import java.util.List;

public class PropParameter {
	
	private String kernelId;
	private String layerId;
	
	private List<PropItem> props;

	/**
	 * @return the kernelId
	 */
	public String getKernelId() {
		return kernelId;
	}

	/**
	 * @param kernelId the kernelId to set
	 */
	public void setKernelId(String kernelId) {
		this.kernelId = kernelId;
	}

	/**
	 * @return the layerId
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * @param layerId the layerId to set
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

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

	
	
}
