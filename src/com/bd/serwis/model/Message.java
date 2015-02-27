package com.bd.serwis.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer messageId;
	private User sender;
	private User reciepent;
	private String messageContent;
	private Date dateOfSending;

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReciepent() {
		return reciepent;
	}

	public void setReciepent(User reciepent) {
		this.reciepent = reciepent;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public Date getDateOfSending() {
		return dateOfSending;
	}

	public void setDateOfSending(Date dateOfSending) {
		this.dateOfSending = dateOfSending;
	}
}
