package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_task_comment database table.
 * 
 */
@Entity
@Table(name="tl_gamma_task_comment")
@NamedQuery(name="TlGammaTaskComment.findAll", query="SELECT t FROM TlGammaTaskComment t")
public class TlGammaTaskComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="comment_id", nullable=false)
	private Long commentId;

	@Column(name="comment_text", length=512)
	private String commentText;

	@Column(name="comment_time")
	private Timestamp commentTime;

	@Column(name="task_guid")
	private String taskGuid;

	@Column(name="user_guid")
	private String userGuid;

	@Column(name="user_name", length=128)
	private String userName;

	public TlGammaTaskComment() {
	}

	public Long getCommentId() {
		return this.commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getCommentText() {
		return this.commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public Timestamp getCommentTime() {
		return this.commentTime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}

	public String getTaskGuid() {
		return this.taskGuid;
	}

	public void setTaskGuid(String taskGuid) {
		this.taskGuid = taskGuid;
	}

	public String getUserGuid() {
		return this.userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}