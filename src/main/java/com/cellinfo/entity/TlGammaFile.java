package com.cellinfo.entity;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the SYS_RELATE_FILE database table.
 * 
 */
@Entity
@Table(name="tl_gamma_file")
@NamedQuery(name="TlGammaFile.findAll", query="SELECT s FROM TlGammaFile s")
public class TlGammaFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="file_guid", length=36, nullable=false)
	private String fileGuid;

	@Column(name="file_name", length=128)
	private String fileName;
	
	@Column(name="file_type", length=128)
	private String fileType;
	
	@Column(name="relate_type", length=36)
	private String relateType;
	
	@Column(name="relate_id", length=36)
	private String relateId;
	
	@Column(name="file_desc", length=256)
	private String fileDesc;
	
	@Column(name="create_time")
	private Timestamp createTime;

	/**
	 * @return the fileGuid
	 */
	public String getFileGuid() {
		return fileGuid;
	}

	/**
	 * @param fileGuid the fileGuid to set
	 */
	public void setFileGuid(String fileGuid) {
		this.fileGuid = fileGuid;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the relateType
	 */
	public String getRelateType() {
		return relateType;
	}

	/**
	 * @param relateType the relateType to set
	 */
	public void setRelateType(String relateType) {
		this.relateType = relateType;
	}

	/**
	 * @return the relateId
	 */
	public String getRelateId() {
		return relateId;
	}

	/**
	 * @param relateId the relateId to set
	 */
	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	/**
	 * @return the fileDesc
	 */
	public String getFileDesc() {
		return fileDesc;
	}

	/**
	 * @param fileDesc the fileDesc to set
	 */
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
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