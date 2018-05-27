package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;


/**
 * The persistent class for the VIEW_USERLAYER database table.
 * 
 */
@Entity
@Immutable
@Subselect("select a.layer_guid,a.task_guid,a.kernel_classid,b.kernel_classname,a.kernel_num,a.layer_grade,b.geom_type,a.ext_guid, "
		+ "(select e.ext_name from tl_gamma_kernel_ext e where a.ext_guid= e.ext_guid) ext_name "
		+ "from tl_gamma_task_layer a, tl_gamma_kernel b "
		+ "where a.kernel_classid = b.kernel_classid")
public class ViewTaskExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name="layer_guid")
	private String layerGuid;
	
	@Column(name="task_guid")
	private String taskGuid;
	
	@Column(name="kernel_classid")
	private String kernelClassid;
	
	@Column(name="ext_guid")
	private String extGuid;
	
	@Column(name="ext_name")
	private String extName;
	
	@Column(name="kernel_num")
	private Integer kernelNum;
	
	/**
	 * 1 ---- 可编辑
	 * 0 -----参考
	 */
	@Column(name="layer_grade")
	private Integer  layerGrade;
	
	@Column(name="kernel_classname")
	private String kernelClassname;

	@Column(name="geom_type")
	private String geomType;

	public ViewTaskExt() {
	}
	
	/**
	 * @return the layerGuid
	 */
	public String getLayerGuid() {
		return layerGuid;
	}

	/**
	 * @param layerGuid the layerGuid to set
	 */
	public void setLayerGuid(String layerGuid) {
		this.layerGuid = layerGuid;
	}

	/**
	 * @return the taskGuid
	 */
	public String getTaskGuid() {
		return taskGuid;
	}

	/**
	 * @param taskGuid the taskGuid to set
	 */
	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
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
	
	
}