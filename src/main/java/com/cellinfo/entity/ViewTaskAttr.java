package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;


/**
 * The persistent class for the VIEW_USERLAYER database table.
 * 
 */
@Entity
//@Table(name="view_task_attr")
//@NamedQuery(name="ViewTaskAttr.findAll", query="SELECT s FROM ViewTaskAttr s")
@Immutable
@Subselect("select a.task_guid,b.attr_guid,b.rank_guid,c.kernel_classid,c.attr_name,c.attr_field,c.attr_type,c.attr_enum ,"
		+ "c.update_time,c.attr_fgrade,b.attr_isedit,b.layer_grade "
		+ "from tl_gamma_task a, tl_gamma_task_attr b, tl_gamma_kernel_attr c "
		+ "where a.task_guid = b.task_guid and b.attr_guid = c.attr_guid")
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
	
	@Column(name="attr_isedit")
	private Integer attrIsedit;
	
	@Column(name="rank_guid")
	private String rankGuid;
	
	@Column(name="attr_fgrade")
	private String attrFgrade;
	
	@Column(name="layer_grade")
	private Integer layerGrade;
	
	@Column(name="update_time")
	private Timestamp updateTime;

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


	/**
	 * @return the attrIsedit
	 */
	public Integer getAttrIsedit() {
		return attrIsedit;
	}


	/**
	 * @param attrIsedit the attrIsedit to set
	 */
	public void setAttrIsedit(Integer attrIsedit) {
		this.attrIsedit = attrIsedit;
	}


	/**
	 * @return the attrFgrade
	 */
	public String getAttrFgrade() {
		return attrFgrade;
	}


	/**
	 * @param attrFgrade the attrFgrade to set
	 */
	public void setAttrFgrade(String attrFgrade) {
		this.attrFgrade = attrFgrade;
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


	/**
	 * @return the rankGuid
	 */
	public String getRankGuid() {
		return rankGuid;
	}


	/**
	 * @param rankGuid the rankGuid to set
	 */
	public void setRankGuid(String rankGuid) {
		this.rankGuid = rankGuid;
	}


	/**
	 * @return the updateTime
	 */
	public Timestamp getUpdateTime() {
		return updateTime;
	}


	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	
	
}