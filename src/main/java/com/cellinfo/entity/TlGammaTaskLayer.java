package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * 任务对应的核心对象及标签子类
 * 任务对应的参考核心对象标签及子类
 * 
 */
@Entity
@Table(name="tl_gamma_task_layer")
@NamedQuery(name="TlGammaTaskLayer.findAll", query="SELECT t FROM TlGammaTaskLayer t")
public class TlGammaTaskLayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name="layer_guid", length=36)
	private String layerGuid;
	
	@Column(name="task_guid", length=36)
	private String taskGuid;
	
	@Column(name="kernel_classid",length=36)
	private String kernelClassid;
	
	@Column(name="ext_guid", length=36)
	private String extGuid;
	
	@Column(name="kernel_num", length=36)
	private Integer kernelNum;
	
	/**
	 * 1 ---- 可编辑
	 * 0 -----参考
	 */
	@Column(name="layer_grade")
	private Integer  layerGrade;

	public TlGammaTaskLayer() {
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
		
}