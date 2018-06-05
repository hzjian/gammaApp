package com.cellinfo.entity;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the SYS_RELATE_FILE database table.
 * 
 */
@Entity
@Table(name="tl_gamma_relate_file")
@NamedQuery(name="TlGammaFile.findAll", query="SELECT s FROM TlGammaFile s")
public class TlGammaFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	private String fileid;

	private String fname;
	
	private String ftype;
	
	private String ptype;
	
	private String relateid;
	
	private String fdesc;
	
	@Column(name="update_time")
	private Timestamp updateTime;

	
	/**
	 * @return the fileid
	 */
	public String getFileid() {
		return fileid;
	}

	/**
	 * @param fileid the fileid to set
	 */
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname the fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}

	/**
	 * @return the ftype
	 */
	public String getFtype() {
		return ftype;
	}

	/**
	 * @param ftype the ftype to set
	 */
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
	
	
	/**
	 * @return the ptype
	 */
	public String getPtype() {
		return ptype;
	}

	/**
	 * @param ptype the ptype to set
	 */
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	/**
	 * @return the relateid
	 */
	public String getRelateid() {
		return relateid;
	}

	/**
	 * @param relateid the relateid to set
	 */
	public void setRelateid(String relateid) {
		this.relateid = relateid;
	}

	/**
	 * @return the fdesc
	 */
	public String getFdesc() {
		return fdesc;
	}

	/**
	 * @param fdesc the fdesc to set
	 */
	public void setFdesc(String fdesc) {
		this.fdesc = fdesc;
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