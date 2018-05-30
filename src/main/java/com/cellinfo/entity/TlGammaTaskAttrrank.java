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
 * 任务属性字段分类，默认产生5条记录
 * 
 */
@Entity
@Table(name="tl_gamma_task_attrrank")
@NamedQuery(name="TlGammaTaskAttrrank.findAll", query="SELECT t FROM TlGammaTaskAttrrank t")
public class TlGammaTaskAttrrank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seq_task_attr_rank", sequenceName="seq_task_attr_rank",allocationSize=1)
	@GeneratedValue(generator="seq_task_attr_rank")
	private Long id;
	
	@Column(name="task_guid",length=36)
	private String taskGuid;
	
	@Column(name="rank_name",length=64)
	private String rankName;

	public TlGammaTaskAttrrank() {
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