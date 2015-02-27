package com.bd.serwis.model;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Announcement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer announcementId;
	private Category category;
	private User user;
	private Date dateOfAdding;
	private String title;
	private String content;
	private byte isAccepted = 0;
	private List<Tag> tags;
	private List<Image> images = new ArrayList<Image>();
	private String tagExpression;

	public Integer getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(Integer announcementId) {
		this.announcementId = announcementId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDateOfAdding() {
		return dateOfAdding;
	}

	public void setDateOfAdding(Date dateOfAdding) {
		this.dateOfAdding = dateOfAdding;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public byte isAccepted() {
		return isAccepted;
	}

	public void setAccepted(byte isAccepted) {
		this.isAccepted = isAccepted;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags() {
		Tag fullTag;
		tags = new ArrayList<Tag>();
		String[] tagTable = tagExpression.split(",");
		for(String tag : tagTable){
			tag.replaceAll("\\s+","");
			fullTag = new Tag();
			fullTag.setTagName(tag);
			this.tags.add(fullTag);
		}
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<byte[]> images) {
		Image image;
		this.images = new ArrayList<Image>();
		for(byte[] imageData : images){
			image = new Image();
			image.setImage(imageData);
			this.images.add(image);
		}
	}

	public String getTagExpression() {
		return tagExpression;
	}

	public void setTagExpression(String tagExpression) {
		this.tagExpression = tagExpression;
	}
}
