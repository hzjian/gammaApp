package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_log database table.
 * 
 */
@Entity
@Table(name="tl_gamma_log")
@NamedQuery(name="TlGammaLog.findAll", query="SELECT t FROM TlGammaLog t")
public class TlGammaLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="ip_str", length=32)
	private String ipStr;

	@Column(name="log_time")
	private Timestamp logTime;

	@Column(name="module_name", length=32)
	private String moduleName;

	@Column(name="oprate_desc", length=64)
	private String oprateDesc;

	@Column(name="oprate_name", length=32)
	private String oprateName;

	@Column(name="url_str", length=32)
	private String urlStr;

	@Column(name="user_name")
	private String userName;

	public TlGammaLog() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpStr() {
		return this.ipStr;
	}

	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}

	public Timestamp getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Timestamp logTime) {
		this.logTime = logTime;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getOprateDesc() {
		return this.oprateDesc;
	}

	public void setOprateDesc(String oprateDesc) {
		this.oprateDesc = oprateDesc;
	}

	public String getOprateName() {
		return this.oprateName;
	}

	public void setOprateName(String oprateName) {
		this.oprateName = oprateName;
	}

	public String getUrlStr() {
		return this.urlStr;
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
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


}