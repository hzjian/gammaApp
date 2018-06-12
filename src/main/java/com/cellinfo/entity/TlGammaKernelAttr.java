package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_kernel_attr database table.
 * 
 */
@Entity
@Table(name="tl_gamma_kernel_attr")
@NamedQuery(name="TlGammaKernelAttr.findAll", query="SELECT t FROM TlGammaKernelAttr t")
public class TlGammaKernelAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attr_guid")
	private String attrGuid;
	
	@Column(name="dict_id", length=36)
	private String dictId;

	@Column(name="attr_field", length=64)
	private String attrField;

	@Column(name="attr_name", length=256)
	private String attrName;

	@Column(name="attr_rang", length=128)
	private String attrRang;

	@Column(name="attr_sum", length=128)
	private String attrSum;

	/**
	 * STRING
	 * INTEGER
	 * NUMBER
	 * DATETIME
	 */

	@Column(name="attr_type", length=64)
	private String attrType;

	@Column(name="kernel_classid", nullable=false)
	private String kernelClassid;
	
	/**
	 * 属性字段众筹等级
	 * TASKGRADE 不同任务保留多份数据
	 * USERGRADE 不同用户保留多份数据
	 */
	@Column(name="attr_fgrade", length=32)
	private String attrFgrade;
	
	@Column(name="attr_min", length=32)
	private String attrMin;
	
	@Column(name="attr_max", length=32)
	private String attrMax;
	
	@Column(name="attr_desc", length=256)
	private String attrDesc;

	@Column(name="update_time")
	private Timestamp updateTime;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	@Column(name="user_name", length=64)
	private String userName;
	
	@Column(name="apply_num")
	private Integer applyNum;
	
	/**
	 * TASK 任务内部共享
	 * GROUP 组织内部共享
	 * 
	 */
	@Column(name="share_grade", length=32)
	private String shareGrade;
	
	public TlGammaKernelAttr() {
	}
	
	/**
	 * @return the dictId
	 */
	public String getDictId() {
		return dictId;
	}

	/**
	 * @param dictId the dictId to set
	 */
	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getAttrField() {
		return this.attrField;
	}

	public void setAttrField(String attrField) {
		this.attrField = attrField;
	}

	public String getAttrGuid() {
		return this.attrGuid;
	}

	public void setAttrGuid(String attrGuid) {
		this.attrGuid = attrGuid;
	}

	public String getAttrName() {
		return this.attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrRang() {
		return this.attrRang;
	}

	public void setAttrRang(String attrRang) {
		this.attrRang = attrRang;
	}

	public String getAttrSum() {
		return this.attrSum;
	}

	public void setAttrSum(String attrSum) {
		this.attrSum = attrSum;
	}

	public String getAttrType() {
		return this.attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	public String getKernelClassid() {
		return this.kernelClassid;
	}

	public void setKernelClassid(String kernelClassid) {
		this.kernelClassid = kernelClassid;
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
	 * @return the attrMin
	 */
	public String getAttrMin() {
		return attrMin;
	}

	/**
	 * @param attrMin the attrMin to set
	 */
	public void setAttrMin(String attrMin) {
		this.attrMin = attrMin;
	}

	/**
	 * @return the attrMax
	 */
	public String getAttrMax() {
		return attrMax;
	}

	/**
	 * @param attrMax the attrMax to set
	 */
	public void setAttrMax(String attrMax) {
		this.attrMax = attrMax;
	}

	/**
	 * @return the attrDesc
	 */
	public String getAttrDesc() {
		return attrDesc;
	}

	/**
	 * @param attrDesc the attrDesc to set
	 */
	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
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

	/**
	 * @return the applyNum
	 */
	public Integer getApplyNum() {
		return applyNum;
	}

	/**
	 * @param applyNum the applyNum to set
	 */
	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}

	/**
	 * @return the shareGrade
	 */
	public String getShareGrade() {
		return shareGrade;
	}

	/**
	 * @param shareGrade the shareGrade to set
	 */
	public void setShareGrade(String shareGrade) {
		this.shareGrade = shareGrade;
	}

	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}