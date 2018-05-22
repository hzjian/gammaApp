package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_dict database table.
 * 
 */
@Entity
@Table(name="tl_gamma_dict")
@NamedQuery(name="TlGammaDict.findAll", query="SELECT t FROM TlGammaDict t")
public class TlGammaDict implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="dict_id", length=36,nullable=false)
	private String dictId;

	@Column(name="dict_name", length=64)
	private String dictName;
	
	@Column(name="dict_desc", length=512)
	private String dictDesc;

	@Column(name="group_guid", length=36)
	private String groupGuid;

	
	public TlGammaDict() {
	}

	public String getDictDesc() {
		return this.dictDesc;
	}

	public void setDictDesc(String dictDesc) {
		this.dictDesc = dictDesc;
	}

	public String getDictId() {
		return this.dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return this.dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	/**
	 * @return the groupGuid
	 */
	public String getGroupGuid() {
		return groupGuid;
	}

	/**
	 * @param groupGuid the groupGuid to set
	 */
	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}
	

}