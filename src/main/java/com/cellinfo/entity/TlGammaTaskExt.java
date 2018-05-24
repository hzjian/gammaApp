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
 * 任务对应的核心对象及标签子类
 * 任务对应的参考核心对象标签及子类
 * 
 */
@Entity
@Table(name="tl_gamma_task_ext")
@NamedQuery(name="TlGammaTaskExt.findAll", query="SELECT t FROM TlGammaTaskExt t")
public class TlGammaTaskExt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name="seq_task_ext", sequenceName="seq_task_ext",allocationSize=1)
	@GeneratedValue(generator="seq_task_ext")
	private Long id;
	
	@Column(name="task_guid", length=36)
	private String taskGuid;
	
	
	@Column(name="kernel_classid",length=36)
	private String kernelClassid;
	
	@Column(name="kernel_classname", length=256)
	private String kernelClassname;

	@Column(name="ext_guid", length=36)
	private String extGuid;

	@Column(name="ext_name",length=128)
	private String extName;
	
	@Column(name="kernel_num", length=36)
	private Integer kernelNum;
	
	/**
	 * 1 ---- 可编辑
	 * 0 -----参考
	 */
	@Column(name="kernel_grade")
	private Integer  kernelGrade;

	public TlGammaTaskExt() {
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
	 * @return the kernelGrade
	 */
	public Integer getKernelGrade() {
		return kernelGrade;
	}

	/**
	 * @param kernelGrade the kernelGrade to set
	 */
	public void setKernelGrade(Integer kernelGrade) {
		this.kernelGrade = kernelGrade;
	}

	
	
	
}