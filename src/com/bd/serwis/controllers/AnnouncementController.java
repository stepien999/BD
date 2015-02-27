package com.bd.serwis.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.bd.serwis.model.Announcement;
import com.bd.serwis.model.User;
import com.bd.serwis.model.Category;
import com.bd.serwis.services.AnnouncementService;

@ManagedBean(name = "announcementController")
@SessionScoped
public class AnnouncementController implements Serializable {

	private static final long serialVersionUID = 1L;
	private AnnouncementService announcementService = new AnnouncementService();
	private Announcement announcement;
	private Category category;
	private Integer categoryId;
	private List<Announcement> announcementsList;
	private List<byte[]> uploadAttachments = new ArrayList<byte[]>();
	private String tagExpression = "";

	public String getTagExpression() {
		return tagExpression;
	}

	public void setTagExpression(String tagExpression) {
		this.tagExpression = tagExpression;
	}

	public AnnouncementController() {

	}

	public void fileUploadAction(FileUploadEvent event) {
		UploadedFile file = event.getFile();

		byte[] foto;
		try {
			foto = IOUtils.toByteArray(file.getInputstream());
			uploadAttachments.add(foto);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String createAnnouncement() {
		announcement = new Announcement();
		return "redirect_create_announcement";
	}

	public String seeAnnouncement(Announcement announcement) {
		this.announcement = announcement;
		return "redirect_see_announcement_page";
	}

	public String seeAnnouncementByCityAndCategory(Category category) {
		return "redirect_see_announcement_city_category";
	}

	public String saveAnnouncement(User user) {
		setAnnouncementImages(announcement);
		announcement.setUser(user);
		announcement.setDateOfAdding(new Date());
		announcement.setTags();
		category = new Category();
		category.setCategoryId(categoryId);
		announcement.setCategory(category);
		announcementService.createAnnouncement(announcement);
		return "announcement_save_success";
	}

	public String updateAnnouncement() {
		announcementService.updateAnnouncement(announcement);
		return "edit_announcement_success";
	}

	public void acceptAnnoucement(Announcement announcement) {
		announcement.setAccepted((byte) 1);
		announcementService.updateAnnouncement(announcement);
	}

	public String deleteAnnouncement(Announcement announcement) {
		announcementService.deleteAnnouncement(announcement);
		return "delete_announcement_success";
	}

	public String seeAnnouncementsByCategory(Category category) {
		this.category = category;
		return "redirect_announcements_by_category";
	}

	public String seeAnnouncementsByCityAndCategory(Category category) {
		this.category = category;
		return "redirect_announcements_by_city_and_category";
	}

	public List<Announcement> getAllAnnouncements() {
		return announcementService.findAllAnnouncements();
	}

	public List<Announcement> findAnnouncementsByCity(User user) {
		if (user == null) {
			return new ArrayList<Announcement>();
		}
		return announcementService.findAnnouncementsByCity(user.getCity());
	}

	public List<Announcement> findAnnouncementsByCityAndCategory(User user) {
		return announcementService.findAnnouncementsByCityAndCategory(
				user.getCity(), category);
	}

	public List<Announcement> findUnacceptedAnnouncements() {
		return announcementService.findNotAcceptedAnnouncements();
	}

	public List<Announcement> findAnnouncementsByCategory() {
		return announcementService.findAnnouncementsByCategory(category);
	}

	public List<Announcement> findAnnouncementsByUser(User user) {
		return announcementService.findAnnouncementsByUser(user);
	}

	public String searchByTags() {
		return "redirect_search_results";
	}

	public List<Announcement> findAnnouncementsByTags() {
		String[] tagTable = tagExpression.split(",");
		for (String tag : tagTable) {
			tag.replaceAll("\\s+", "");
		}
		return announcementService.findAnnouncementsByTags(tagTable);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String seeUserAnnouncements() {
		return "redirect_see_user_announcements";
	}

	public String seeUnacceptedAnnouncements() {
		return "redirect_see_unaccepted_announcements";
	}

	public List<Announcement> getAnnouncementsList() {
		return announcementsList;
	}

	public void setAnnouncementsList(List<Announcement> announcementsList) {
		this.announcementsList = announcementsList;
	}

	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	public void setAnnouncementImages(Announcement announcement) {
		announcement.setImages(uploadAttachments);
	}

	public String giveCategoryToAnnouncement(Category category) {
		this.announcement.setCategory(category);
		return "success_give_category";
	}

	public void acceptAnnouncement(Announcement announcement) {
		announcement.setAccepted((byte) 1);
		announcementService.updateAnnouncement(announcement);
	}

	public String editAnnouncement(Announcement announcement) {
		this.announcement = announcement;
		return "redirect_edit_announcement";
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/*
	 * public List<DefaultStreamedContent> jsfImages() {
	 * List<DefaultStreamedContent> images = new
	 * ArrayList<DefaultStreamedContent>(); List<Image> imageList =
	 * announcementService .findAllImagesByAnnouncement(announcement); for
	 * (Image image : imageList) { try { images.add(image.byteToImage()); }
	 * catch (IOException e) { e.printStackTrace(); } } return images; }
	 * 
	 * public DefaultStreamedContent findFirstImage(Announcement announcement)
	 * throws FileNotFoundException { DefaultStreamedContent image2;
	 * List<DefaultStreamedContent> images = new
	 * ArrayList<DefaultStreamedContent>(); List<Image> imageList =
	 * announcementService .findAllImagesByAnnouncement(announcement);
	 * 
	 * for (Image image : imageList) { try { images.add(image.byteToImage());
	 * image2 = images.get(0); if (image2 == null) return new
	 * DefaultStreamedContent(); } catch (IOException e) { e.printStackTrace();
	 * } } return new DefaultStreamedContent(); }
	 */
}
