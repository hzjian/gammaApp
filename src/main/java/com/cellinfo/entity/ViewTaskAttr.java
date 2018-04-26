package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the VIEW_USERLAYER database table.
 * 
 */
@Entity
@Table(name="view_task_attr")
@NamedQuery(name="ViewTaskAttr.findAll", query="SELECT s FROM ViewTaskAttr s")
public class ViewTaskAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ViewTaskAttrPK id;

	@Column(name="kernel_classid")
	private String kernelClassid;

	@Column(name="attr_name")
	private String attrName;
	
	@Column(name="attr_type")
	private String attrType;
	
	@Column(name="attr_enum")
	private String attrEnum;
	
	
	public ViewTaskAttr() {
	}


	/**
	 * @return the id
	 */
	public ViewTaskAttrPK getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(ViewTaskAttrPK id) {
		this.id = id;
	}


	/**
	 * @return the kernelClassid
	 */
	public String getKernelClassid() {
		return kernelClassid;
	}


	/**
	 * @param kernelClassid the kernelClassid to set
	 */
	public void setKernelClassid(String kernelClassid) {
		this.kernelClassid = kernelClassid;
	}


	/**
	 * @return the attrName
	 */
	public String getAttrName() {
		return attrName;
	}


	/**
	 * @param attrName the attrName to set
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}


	/**
	 * @return the attrType
	 */
	public String getAttrType() {
		return attrType;
	}


	/**
	 * @param attrType the attrType to set
	 */
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}


	/**
	 * @return the attrEnum
	 */
	public String getAttrEnum() {
		return attrEnum;
	}


	/**
	 * @param attrEnum the attrEnum to set
	 */
	public void setAttrEnum(String attrEnum) {
		this.attrEnum = attrEnum;
	}

}