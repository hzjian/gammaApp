package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_task_attr database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task_attr")
@NamedQuery(name="TlGammaTaskAttr.findAll", query="SELECT t FROM TlGammaTaskAttr t")
public class TlGammaTaskAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name="seq_task_attr", sequenceName="seq_task_attr")
	@Id 
	@GeneratedValue(generator="seq_task_attr")
	private Long id;
	
	@Column(name="attr_guid")
	private String attrGuid;

	@Column(name="attr_isedit")
	private Integer attrIsedit;

	@Column(name="task_guid")
	private String taskGuid;

	public TlGammaTaskAttr() {
	}

	public String getAttrGuid() {
		return this.attrGuid;
	}

	public void setAttrGuid(String attrGuid) {
		this.attrGuid = attrGuid;
	}

	public Integer getAttrIsedit() {
		return this.attrIsedit;
	}

	public void setAttrIsedit(Integer attrIsedit) {
		this.attrIsedit = attrIsedit;
	}

	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

}