package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_layer_attribute database table.
 * 
 */
@Entity
@Table(name="tl_gamma_layer_attribute")
@NamedQuery(name="TlGammaLayerAttribute.findAll", query="SELECT t FROM TlGammaLayerAttribute t")
public class TlGammaLayerAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="attr_double")
	private double attrDouble;

	@Column(name="attr_guid")
	private String attrGuid;

	@Column(name="attr_long")
	private Long attrLong;

	@Column(name="attr_text", length=1024)
	private String attrText;

	@Column(name="attr_time")
	private Timestamp attrTime;

	@Column(name="kernel_guid")
	private String kernelGuid;

	@Column(name="task_guid")
	private String taskGuid;

	@Column(name="user_guid")
	private String userGuid;

	public TlGammaLayerAttribute() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAttrDouble() {
		return this.attrDouble;
	}

	public void setAttrDouble(double attrDouble) {
		this.attrDouble = attrDouble;
	}

	public Object getAttrGuid() {
		return this.attrGuid;
	}

	public void setAttrGuid(String attrGuid) {
		this.attrGuid = attrGuid;
	}

	public Long getAttrLong() {
		return this.attrLong;
	}

	public void setAttrLong(Long attrLong) {
		this.attrLong = attrLong;
	}

	public String getAttrText() {
		return this.attrText;
	}

	public void setAttrText(String attrText) {
		this.attrText = attrText;
	}

	public Timestamp getAttrTime() {
		return this.attrTime;
	}

	public void setAttrTime(Timestamp attrTime) {
		this.attrTime = attrTime;
	}

	public Object getKernelGuid() {
		return this.kernelGuid;
	}

	public void setKernelGuid(String kernelGuid) {
		this.kernelGuid = kernelGuid;
	}

	public Object getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

	public String getUserGuid() {
		return this.userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

}