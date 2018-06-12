package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;


/**
 * The persistent class for the tl_gamma_layer_line database table.
 * 
 */
@Entity
//@Table(name="view_task_line")
//@NamedQuery(name="ViewTaskLine.findAll", query="SELECT t FROM ViewTaskLine t")
@Immutable
@Subselect("select  a.ext_guid,a.ext_desc,a.ext_name,a.user_name,a.kernel_classid,"
		+ " (select count(0) from tl_gamma_kernel_filter where ext_guid= a.ext_guid )  filter_num,"
		+ " (select count(0) from tl_gamma_kernel_geofilter where ext_guid= a.ext_guid  ) geofilter_num,"
		+ " (select count(0) from tl_gamma_kernel_subset where ext_guid= a.ext_guid  )  kernel_num,"
		+ " a.update_time,a.ext_grade, b.kernel_classname,b.geom_type "
		+  "from tl_gamma_kernel_ext a, tl_gamma_kernel b "
		+  "where a.kernel_classid = b.kernel_classid" )
public class ViewKernelExt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ext_guid", nullable=false)
	private String extGuid;
	
	@Column(name="ext_name")
	private String extName;

	@Column(name="user_name")
	private String userName;
	
	@Column(name="kernel_classid")
	private String kernelClassid;

	@Column(name="ext_desc",length=256)
	private String extDesc;
	//空间过滤条件数
	@Column(name="geofilter_num")
	private Integer geofilterNum;
	
	//条件过滤数
	@Column(name="filter_num")
	private Integer filterNum;
	
	//核心对象数
	@Column(name="kernel_num")
	private Integer kernelNum;
	
	@Column(name="ext_grade")
	private String extGrade;
	
	@Column(name="kernel_classname")
	private String kernelClassname;

	@Column(name="geom_type")
	private String geomType;
	
	@Column(name="update_time")
	private Timestamp updateTime;
	
	public ViewKernelExt() {
	}

	/**
	 * @return the extGuid
	 */
	public String getExtGuid() {
		return extGuid;
	}

	/**
	 * @param extGuid the extGuid to set
	 */
	public void setExtGuid(String extGuid) {
		this.extGuid = extGuid;
	}

	/**
	 * @return the extName
	 */
	public String getExtName() {
		return extName;
	}

	/**
	 * @param extName the extName to set
	 */
	public void setExtName(String extName) {
		this.extName = extName;
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
	 * @return the extDesc
	 */
	public String getExtDesc() {
		return extDesc;
	}

	/**
	 * @param extDesc the extDesc to set
	 */
	public void setExtDesc(String extDesc) {
		this.extDesc = extDesc;
	}

	/**
	 * @return the geofilterNum
	 */
	public Integer getGeofilterNum() {
		return geofilterNum;
	}

	/**
	 * @param geofilterNum the geofilterNum to set
	 */
	public void setGeofilterNum(Integer geofilterNum) {
		this.geofilterNum = geofilterNum;
	}

	/**
	 * @return the filterNum
	 */
	public Integer getFilterNum() {
		return filterNum;
	}

	/**
	 * @param filterNum the filterNum to set
	 */
	public void setFilterNum(Integer filterNum) {
		this.filterNum = filterNum;
	}

	/**
	 * @return the kernelNum
	 */
	public Integer getKernelNum() {
		return kernelNum;
	}

	/**
	 * @param kernelNum the kernelNum to set
	 */
	public void setKernelNum(Integer kernelNum) {
		this.kernelNum = kernelNum;
	}

	/**
	 * @return the extGrade
	 */
	public String getExtGrade() {
		return extGrade;
	}

	/**
	 * @param extGrade the extGrade to set
	 */
	public void setExtGrade(String extGrade) {
		this.extGrade = extGrade;
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