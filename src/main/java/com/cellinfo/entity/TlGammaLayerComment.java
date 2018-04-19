package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_layer_comment database table.
 * 
 */
@Entity
@Table(name="tl_gamma_layer_comment")
@NamedQuery(name="TlGammaLayerComment.findAll", query="SELECT t FROM TlGammaLayerComment t")
public class TlGammaLayerComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="comment_id", nullable=false)
	private Long commentId;

	@Column(name="comment_text", length=512)
	private String commentText;

	@Column(name="comment_time")
	private Timestamp commentTime;

	@Column(name="kernel_guid")
	private String kernelGuid;

	@Column(name="user_guid")
	private String userGuid;

	@Column(name="user_name", length=128)
	private String userName;

	public TlGammaLayerComment() {
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

	public String getKernelGuid() {
		return this.kernelGuid;
	}

	public void setKernelGuid(String kernelGuid) {
		this.kernelGuid = kernelGuid;
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