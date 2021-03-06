package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_dict_item database table.
 * 
 */
@Entity
@Table(name="tl_gamma_dict_item")
@NamedQuery(name="TlGammaDictItem.findAll", query="SELECT t FROM TlGammaDictItem t")
public class TlGammaDictItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id 
	@SequenceGenerator(name="seq_dict_item", sequenceName="seq_dict_item",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="seq_dict_item")
	private Long id;

	@Column(name="dict_id")
	private String dictId;

	@Column(name="dict_item", length=64)
	private String dictItem;
	
	@Column(name="update_time")
	private Timestamp updateTime;

	public TlGammaDictItem() {
	}

	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	public String getDictId() {
		return this.dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictItem() {
		return this.dictItem;
	}

	public void setDictItem(String dictItem) {
		this.dictItem = dictItem;
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