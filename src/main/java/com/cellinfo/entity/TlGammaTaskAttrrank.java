package com.cellinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * 任务属性字段分类，默认产生5条记录
 * 
 */
@Entity
@Table(name="tl_gamma_task_attrrank")
@NamedQuery(name="TlGammaTaskAttrrank.findAll", query="SELECT t FROM TlGammaTaskAttrrank t")
public class TlGammaTaskAttrrank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="rank_guid",length=36)
	private String rankGuid;
	
	@Column(name="rank_name",length=64)
	private String rankName;
	
	@Column(name="task_guid",length=36)
	private String taskGuid;

	public TlGammaTaskAttrrank() {
	}


	/**
	 * @return the rankGuid
	 */
	public String getRankGuid() {
		return rankGuid;
	}


	/**
	 * @param rankGuid the rankGuid to set
	 */
	public void setRankGuid(String rankGuid) {
		this.rankGuid = rankGuid;
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
	 * @return the rankName
	 */
	public String getRankName() {
		return rankName;
	}

	/**
	 * @param rankName the rankName to set
	 */
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	
}