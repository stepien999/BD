package com.bd.serwis.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;

import org.primefaces.model.DefaultStreamedContent;

public class Image implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer imageId;
	private Announcement announcement;
	private byte[] image;

	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public DefaultStreamedContent byteToImage() throws IOException {
		ByteArrayInputStream img = new ByteArrayInputStream(image);
		return new DefaultStreamedContent(img, "image/jpg");
	}
}
