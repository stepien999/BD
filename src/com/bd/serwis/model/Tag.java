package com.bd.serwis.model;

import java.io.Serializable;

public class Tag implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer tagsId;
	private Announcement announcement;
	private String tagName;

	public Integer getTagsId() {
		return tagsId;
	}

	public void setTagsId(Integer tagsId) {
		this.tagsId = tagsId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}
}
