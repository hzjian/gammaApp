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

	@Id
	@SequenceGenerator(name="seq_task_attr", sequenceName="seq_task_attr",allocationSize=1)
	@GeneratedValue(generator="seq_task_attr")
	private Long id;
	
	@Column(name="task_guid",length=36)
	private String taskGuid;
	
	
	@Column(name="attr_guid",length=36)
	private String attrGuid;

	@Column(name="attr_isedit")
	private Integer attrIsedit;
	
	/**
	 * 1----main layer
	 * 0----ref layer
	 */
	@Column(name="layer_grade")
	private Integer layerGrade;

	public TlGammaTaskAttr() {
	}

	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

	public String getAttrGuid() {
		return this.attrGuid;
	}

	public void setAttrGuid(String attrGuid) {
		this.attrGuid = attrGuid;
	}
	
	/**
	 * @return the layerGrade
	 */
	public Integer getLayerGrade() {
		return layerGrade;
	}


	/**
	 * @param layerGrade the layerGrade to set
	 */
	public void setLayerGrade(Integer layerGrade) {
		this.layerGrade = layerGrade;
	}


	public Integer getAttrIsedit() {
		return this.attrIsedit;
	}

	public void setAttrIsedit(Integer attrIsedit) {
		this.attrIsedit = attrIsedit;
	}
}