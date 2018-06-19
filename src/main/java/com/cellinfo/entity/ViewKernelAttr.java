package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Subselect("select a.attr_guid,a.dict_id,a.attr_field,a.attr_name,a.attr_rang,a.attr_sum,a.attr_type,a.kernel_classid,a.attr_fgrade, "
		+ "a.attr_min,a.attr_max,a.attr_desc,a.create_time,a.user_name,a.apply_num,a.share_grade,b.kernel_classname,b.geom_type "
		+ "from tl_gamma_kernel_attr a,tl_gamma_kernel b "
		+ "where a.kernel_classid= b.kernel_classid" )
public class ViewKernelAttr implements Serializable{

	@Id
	@Column(name="attr_guid")
	private String attrGuid;

	@Column(name="dict_id")
	private String dictId;
	
	@Column(name="attr_field")
	private String attrField;

	@Column(name="attr_name")
	private String attrName;

	@Column(name="attr_rang")
	private String attrRang;

	@Column(name="attr_sum")
	private String attrSum;

	@Column(name="attr_type")
	private String attrType;

	@Column(name="kernel_classid")
	private String kernelClassid;
	
	@Column(name="attr_fgrade")
	private String attrFgrade;
	
	@Column(name="attr_min")
	private String attrMin;
	
	@Column(name="attr_max")
	private String attrMax;
	
	@Column(name="attr_desc")
	private String attrDesc;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="apply_num")
	private Integer applyNum;
	
	@Column(name="share_grade")
	private String shareGrade;
	
	@Column(name="kernel_classname")
	private String kernelClassname;

	@Column(name="geom_type")
	private String geomType;

	/**
	 * @return the attrGuid
	 */
	public String getAttrGuid() {
		return attrGuid;
	}

	/**
	 * @param attrGuid the attrGuid to set
	 */
	public void setAttrGuid(String attrGuid) {
		this.attrGuid = attrGuid;
	}

	/**
	 * @return the attrField
	 */
	public String getAttrField() {
		return attrField;
	}

	/**
	 * @param attrField the attrField to set
	 */
	public void setAttrField(String attrField) {
		this.attrField = attrField;
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
	 * @return the attrRang
	 */
	public String getAttrRang() {
		return attrRang;
	}

	/**
	 * @param attrRang the attrRang to set
	 */
	public void setAttrRang(String attrRang) {
		this.attrRang = attrRang;
	}

	/**
	 * @return the attrSum
	 */
	public String getAttrSum() {
		return attrSum;
	}

	/**
	 * @param attrSum the attrSum to set
	 */
	public void setAttrSum(String attrSum) {
		this.attrSum = attrSum;
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
	 * @return the kernelClassname
	 */
	public String getKernelClassname() {
		return kernelClassname;
	}

	/**
	 * @param kernelClassname the kernelClassname to set
	 */
	public void setKernelClassname(String kernelClassname) {
		this.kernelClassname = kernelClassname;
	}

	/**
	 * @return the geomType
	 */
	public String getGeomType() {
		return geomType;
	}

	/**
	 * @param geomType the geomType to set
	 */
	public void setGeomType(String geomType) {
		this.geomType = geomType;
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
	
}
