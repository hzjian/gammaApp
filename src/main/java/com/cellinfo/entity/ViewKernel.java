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
@Subselect("select a.kernel_classid,a.group_guid,a.kernel_classdesc,a.kernel_classname,a.geom_type,a.update_time,a.server_path,a.kernel_num,"
		+ " (select count(*) from tl_gamma_task where kernel_classid = a.kernel_classid) task_num "
		+ " from tl_gamma_kernel a ")
public class ViewKernel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="kernel_classid")
	private String kernelClassid;
	
	@Column(name="group_guid")
	private String groupGuid;

	@Column(name="kernel_classdesc")
	private String kernelClassdesc;

	@Column(name="kernel_classname")
	private String kernelClassname;

	@Column(name="geom_type")
	private String geomType;
	
	@Column(name="update_time")
	private Timestamp updateTime;
	
	@Column(name="server_path")
	private String serverPath;
	
	@Column(name="kernel_num")
	private Long kernelNum;
	
	@Column(name="task_num")
	private Long taskNum;

	public ViewKernel() {
	}

	public String getGroupGuid() {
		return this.groupGuid;
	}

	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}

	public String getKernelClassdesc() {
		return this.kernelClassdesc;
	}

	public void setKernelClassdesc(String kernelClassdesc) {
		this.kernelClassdesc = kernelClassdesc;
	}

	public String getKernelClassid() {
		return this.kernelClassid;
	}

	public void setKernelClassid(String kernelClassid) {
		this.kernelClassid = kernelClassid;
	}

	public String getKernelClassname() {
		return this.kernelClassname;
	}

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
	 * @return the serverPath
	 */
	public String getServerPath() {
		return serverPath;
	}

	/**
	 * @param serverPath the serverPath to set
	 */
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
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
	 * @return the kernelNum
	 */
	public Long getKernelNum() {
		return kernelNum;
	}

	/**
	 * @param kernelNum the kernelNum to set
	 */
	public void setKernelNum(Long kernelNum) {
		this.kernelNum = kernelNum;
	}

	/**
	 * @return the taskNum
	 */
	public Long getTaskNum() {
		return taskNum;
	}

	/**
	 * @param taskNum the taskNum to set
	 */
	public void setTaskNum(Long taskNum) {
		this.taskNum = taskNum;
	}

}