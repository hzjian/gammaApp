package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_role database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task_tmp")
@NamedQuery(name="TlGammaTaskTmp.findAll", query="SELECT t FROM TlGammaTaskTmp t")
public class TlGammaTaskTmp implements Serializable {
	private static final long serialVersionUID = 1L;

	//tmp_guid,kernel_guid,f_num
	
	@EmbeddedId
	private TlGammaTaskTmpPK id;
	
	@Column(name = "f_num")
	private Integer fNum;

	/**
	 * @return the id
	 */
	public TlGammaTaskTmpPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(TlGammaTaskTmpPK id) {
		this.id = id;
	}

	/**
	 * @return the fNum
	 */
	public Integer getfNum() {
		return fNum;
	}

	/**
	 * @param fNum the fNum to set
	 */
	public void setfNum(Integer fNum) {
		this.fNum = fNum;
	}

}