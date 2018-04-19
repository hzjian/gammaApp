package com.cellinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tl_gamma_message database table.
 * 
 */
@Entity
@Table(name="tl_gamma_message")
@NamedQuery(name="TlGammaMessage.findAll", query="SELECT t FROM TlGammaMessage t")
public class TlGammaMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="message_id", nullable=false)
	private Integer messageId;
	
	@Column(name="message_comment", length=256)
	private String messageComment;
	
	@Column(name="message_status", length=10)
	private String messageStatus;

	@Column(name="message_time")
	private Timestamp messageTime;

	@Column(name="user_guid", nullable=false)
	private String userGuid;

	public TlGammaMessage() {
	}

	public String getMessageComment() {
		return this.messageComment;
	}

	public void setMessageComment(String messageComment) {
		this.messageComment = messageComment;
	}

	public Integer getMessageId() {
		return this.messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getMessageStatus() {
		return this.messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public Timestamp getMessageTime() {
		return this.messageTime;
	}

	public void setMessageTime(Timestamp messageTime) {
		this.messageTime = messageTime;
	}

	public String getUserGuid() {
		return this.userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

}