package com.bd.serwis.services;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;

import com.bd.serwis.connection.ConnectionManager;
import com.bd.serwis.model.Announcement;
import com.bd.serwis.model.Category;
import com.bd.serwis.model.Image;
import com.bd.serwis.model.Tag;
import com.bd.serwis.model.User;

public class AnnouncementService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6298003058691673322L;
	private ConnectionManager conn = new ConnectionManager();
	private Announcement announcement;
	private List<Announcement> announcementsList;
	private UserService userService = new UserService();
	private CategoryService categoryService = new CategoryService();
	private TagService tagService = new TagService();
	private ImageService imageService = new ImageService();

	private final static String CREATE_ANNOUNCEMENT_QUERY = "INSERT INTO Announcement (CategoryId,UserId,DateOfAdding,Title,Content,isAccepted)"
			+ "VALUES (?,?,?,?,?,?)";

	private final static String FIND_ALL_ANNOUNCEMENTS_QUERY = "SELECT * FROM Announcement ORDER BY AnnouncementId DESC;";

	private final static String FIND_ANNOUNCEMENTS_BY_CITY_QUERY = "SELECT * FROM Announcement a,User u "
			+ "WHERE u.City = ? AND a.IsAccepted = 1"
			+ " AND a.UserId = u.UserId " + "ORDER BY a.AnnouncementId  DESC;";

	private final static String FIND_ANNOUNCEMENTS_BY_CITY_AND_CATEGORY_QUERY = "SELECT * FROM Announcement a,User u "
			+ "WHERE u.City = ? AND a.CategoryId= ? AND a.isAccepted = 1 "
			+ "AND a.UserId=u.UserId " + "ORDER BY a.AnnouncementId  DESC;";

	private final static String FIND_ANNOUNCEMENTS_BY_USER_QUERY = "SELECT * FROM Announcement "
			+ "WHERE UserId=?" + " ORDER BY AnnouncementId  DESC";

	private final static String FIND_ANNOUNCEMENTS_BY_CATEGORY_QUERY = "SELECT * FROM Announcement "
			+ "WHERE CategoryId=? AND IsAccepted = 1"
			+ " ORDER BY AnnouncementId  DESC";

	private final static String FIND_ANNOUNCEMENTS_BY_TAGS_QUERY = "SELECT * FROM Announcement a,Tags t "
			+ "WHERE t.TagName = ? AND a.IsAccepted = 1 "
			+ "AND a.AnnouncementId=t.AnnouncementId "
			+ "ORDER BY a.AnnouncementId DESC";

	private final static String FIND_ACCEPTED_ANNOUNCEMENTS_QUERY = "SELECT * FROM Announcement "
			+ "WHERE isAccepted=1" + " ORDER BY AnnouncementId DESC";

	private final static String FIND_NOT_ACCEPTED_ANNOUNCEMENTS_QUERY = "SELECT * FROM Announcement "
			+ "WHERE isAccepted=0" + " ORDER BY AnnouncementId DESC";

	private final static String FIND_ANNOUNCEMENT_BY_ID_QUERY = "SELECT * FROM Annoucement WHERE AnnouncementId = ?";

	private final static String DELETE_ANNOUNCEMENT_QUERY = "DELETE FROM Announcement WHERE AnnouncementId = ?";

	private final static String DELETE_IMAGES_FROM_ANNOUNCEMENT_QUERY = "DELETE FROM Images WHERE AnnouncementId = ?";

	private final static String UPDATE_ANNOUNCEMENT_QUERY = "UPDATE Announcement SET Title=?, Content=?, IsAccepted=?"
			+ " WHERE AnnouncementId=?";

	private final static String FIND_IMAGES_BY_ANNOUNCEMENT_QUERY = "SELECT * FROM Images WHERE AnnouncementId=?";

	public List<Image> findAllImagesByAnnouncement(Announcement announcement) {
		List<Image> imagesList = new ArrayList<Image>();
		Image image = new Image();
		PreparedStatement pstmt;
		ResultSet rs;
		conn.connect();

		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_IMAGES_BY_ANNOUNCEMENT_QUERY);
			pstmt.setInt(1, announcement.getAnnouncementId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				image.setImageId(rs.getInt("ImagesId"));
				image.setAnnouncement(announcement);
				try {
					image.setImage(IOUtils.toByteArray(rs
							.getBinaryStream("Image")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				imagesList.add(image);
			}
			pstmt.close();
			rs.close();
			conn.close();

			return imagesList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return imagesList;
	}

	public List<Announcement> findAllAnnouncements() {

		announcementsList = new ArrayList<Announcement>();
		PreparedStatement pstmt;
		Statement stmt;
		ResultSet rs;

		conn.connect();
		try {
			stmt = conn.getConnection().createStatement();

			rs = stmt.executeQuery(FIND_ALL_ANNOUNCEMENTS_QUERY);

			while (rs.next()) {
				announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
				announcement.setCategory(categoryService.findCategoryById(rs
						.getInt("CategoryId")));
				announcement.setUser(userService.findUserById(rs
						.getInt("UserId")));
				announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
				announcement.setTitle(rs.getString("Title"));
				announcement.setContent(rs.getString("Content"));
				announcement.setAccepted(rs.getByte("isAccepted"));
				announcementsList.add(announcement);
			}
			stmt.close();
			rs.close();
			conn.close();

			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcementsList;
	}

	public List<Announcement> findAnnouncementById(int announcementId) {
		PreparedStatement pstmt;
		ResultSet rs;
		announcementsList = new ArrayList<Announcement>();
		conn.connect();

		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_ANNOUNCEMENT_BY_ID_QUERY);
			pstmt.setInt(1, announcementId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
				announcement.setCategory(categoryService.findCategoryById(rs
						.getInt("CategoryId")));
				announcement.setUser(userService.findUserById(rs
						.getInt("UserId")));
				announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
				announcement.setTitle(rs.getString("Title"));
				announcement.setContent(rs.getString("Content"));
				announcement.setAccepted(rs.getByte("isAccepted"));
				announcementsList.add(announcement);
			}
			pstmt.close();
			rs.close();

			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return announcementsList;
	}

	public List<Announcement> findNotAcceptedAnnouncements() {

		announcementsList = new ArrayList<Announcement>();
		Statement stmt;
		ResultSet rs;
		conn.connect();
		try {
			stmt = conn.getConnection().createStatement();

			rs = stmt.executeQuery(FIND_NOT_ACCEPTED_ANNOUNCEMENTS_QUERY);

			while (rs.next()) {
				announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
				announcement.setCategory(categoryService.findCategoryById(rs
						.getInt("CategoryId")));
				announcement.setUser(userService.findUserById(rs
						.getInt("UserId")));
				announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
				announcement.setTitle(rs.getString("Title"));
				announcement.setContent(rs.getString("Content"));
				announcement.setAccepted(rs.getByte("isAccepted"));
				announcementsList.add(announcement);
			}
			stmt.close();
			rs.close();
			conn.close();

			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcementsList;
	}

	public List<Announcement> findAcceptedAnnouncements() {

		announcementsList = new ArrayList<Announcement>();
		Statement stmt;
		ResultSet rs;

		conn.connect();
		try {
			stmt = conn.getConnection().createStatement();

			rs = stmt.executeQuery(FIND_ACCEPTED_ANNOUNCEMENTS_QUERY);

			while (rs.next()) {
				announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
				announcement.setCategory(categoryService.findCategoryById(rs
						.getInt("CategoryId")));
				announcement.setUser(userService.findUserById(rs
						.getInt("UserId")));
				announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
				announcement.setTitle(rs.getString("Title"));
				announcement.setContent(rs.getString("Content"));
				announcement.setAccepted(rs.getByte("isAccepted"));
				announcementsList.add(announcement);
			}
			stmt.close();
			rs.close();
			conn.close();

			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcementsList;
	}

	public List<Announcement> findAnnouncementsByCity(String city) {
		announcementsList = new ArrayList<Announcement>();
		PreparedStatement pstmt;
		ResultSet rs;
		if (conn.getConnection() == null)
			conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_ANNOUNCEMENTS_BY_CITY_QUERY);
			pstmt.setString(1, city);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
				announcement.setCategory(categoryService.findCategoryById(rs
						.getInt("CategoryId")));
				announcement.setUser(userService.findUserById(rs
						.getInt("UserId")));
				announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
				announcement.setTitle(rs.getString("Title"));
				announcement.setContent(rs.getString("Content"));
				announcement.setAccepted(rs.getByte("isAccepted"));
				announcementsList.add(announcement);
			}
			pstmt.close();
			rs.close();
			conn.close();

			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcementsList;
	}

	public List<Announcement> findAnnouncementsByCityAndCategory(String city,
			Category category) {
		announcementsList = new ArrayList<Announcement>();
		PreparedStatement pstmt;
		ResultSet rs;

		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_ANNOUNCEMENTS_BY_CITY_AND_CATEGORY_QUERY);
			pstmt.setString(1, city);
			pstmt.setInt(2, category.getCategoryId());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
				announcement.setCategory(categoryService.findCategoryById(rs
						.getInt("CategoryId")));
				announcement.setUser(userService.findUserById(rs
						.getInt("UserId")));
				announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
				announcement.setTitle(rs.getString("Title"));
				announcement.setContent(rs.getString("Content"));
				announcement.setAccepted(rs.getByte("isAccepted"));
				announcementsList.add(announcement);
			}
			pstmt.close();
			rs.close();
			conn.close();

			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcementsList;
	}

	public List<Announcement> findAnnouncementsByCategory(Category category) {
		announcementsList = new ArrayList<Announcement>();
		PreparedStatement pstmt;
		ResultSet rs;

		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_ANNOUNCEMENTS_BY_CATEGORY_QUERY);
			pstmt.setInt(1, category.getCategoryId());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
				announcement.setCategory(categoryService.findCategoryById(rs
						.getInt("CategoryId")));
				announcement.setUser(userService.findUserById(rs
						.getInt("UserId")));
				announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
				announcement.setTitle(rs.getString("Title"));
				announcement.setContent(rs.getString("Content"));
				announcement.setAccepted(rs.getByte("isAccepted"));
				announcementsList.add(announcement);
			}
			pstmt.close();
			rs.close();
			conn.close();

			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcementsList;
	}

	public List<Announcement> findAnnouncementsByUser(User user) {
		announcementsList = new ArrayList<Announcement>();
		PreparedStatement pstmt;
		ResultSet rs;

		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_ANNOUNCEMENTS_BY_USER_QUERY);
			pstmt.setInt(1, user.getUserId());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
				announcement.setCategory(categoryService.findCategoryById(rs
						.getInt("CategoryId")));
				announcement.setUser(userService.findUserById(rs
						.getInt("UserId")));
				announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
				announcement.setTitle(rs.getString("Title"));
				announcement.setContent(rs.getString("Content"));
				announcement.setAccepted(rs.getByte("isAccepted"));
				announcementsList.add(announcement);
			}
			pstmt.close();
			rs.close();
			conn.close();

			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcementsList;
	}

	public List<Announcement> findAnnouncementsByTags(String[] tags) {
		Set<Announcement> announcementSet = new HashSet<Announcement>();
		announcementsList = new ArrayList<Announcement>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		conn.connect();
		try {

			for (String tag : tags) {
				pstmt = conn.getConnection().prepareStatement(
						FIND_ANNOUNCEMENTS_BY_TAGS_QUERY);
				pstmt.setString(1, tag);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					announcement = new Announcement();
					announcement.setAnnouncementId(rs.getInt("AnnouncementId"));
					announcement.setCategory(categoryService
							.findCategoryById(rs.getInt("CategoryId")));
					announcement.setUser(userService.findUserById(rs
							.getInt("UserId")));
					announcement.setDateOfAdding(rs.getDate("DateOfAdding"));
					announcement.setTitle(rs.getString("Title"));
					announcement.setContent(rs.getString("Content"));
					announcement.setAccepted(rs.getByte("isAccepted"));
					announcementsList.add(announcement);
				}
			}
			pstmt.close();
			rs.close();
			conn.close();
			for (Announcement announcement1 : announcementsList) {
				announcementSet.add(announcement1);
			}

			announcementsList.clear();
			announcementsList = new ArrayList<Announcement>(announcementSet);
			return announcementsList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcementsList;
	}

	private void createTags(List<Tag> tagList) {
		for (Tag tag : tagList)
			tagService.createTag(tag);
	}

	private void createImages(List<Image> imageList) {
		for (Image image : imageList)
			imageService.createImage(image);
	}

	public void createAnnouncement(Announcement announcement) {
		PreparedStatement pstmt;

		try {
			conn.connect();
			pstmt = conn.getConnection().prepareStatement(
					CREATE_ANNOUNCEMENT_QUERY);

			pstmt.setInt(1,

			announcement.getCategory().getCategoryId());
			pstmt.setInt(2, announcement.getUser().getUserId());
			pstmt.setDate(3, new java.sql.Date(announcement.getDateOfAdding()
					.getTime()));
			pstmt.setString(4, announcement.getTitle());
			pstmt.setString(5, announcement.getContent());
			pstmt.setByte(6, announcement.isAccepted());
			pstmt.execute();
			createTags(announcement.getTags());
			createImages(announcement.getImages());
			pstmt.close();
			conn.close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Og這szenie zosta這 dodane "));

		} catch (SQLException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Og這szenie nie zosta這 dodane "));
			e.printStackTrace();
		}
	}

	public void updateAnnouncement(Announcement announcement) {
		PreparedStatement pstmt;
		try {
			conn.connect();

			pstmt = conn.getConnection().prepareStatement(
					UPDATE_ANNOUNCEMENT_QUERY);
			pstmt.setString(1, announcement.getTitle());
			pstmt.setString(2, announcement.getContent());
			pstmt.setByte(3, announcement.isAccepted());
			pstmt.setInt(4, announcement.getAnnouncementId());
			pstmt.execute();

			pstmt.close();
			conn.close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Og這szenie zosta這 zaktualizowane "));

		} catch (SQLException e) {
			System.out.println("B章d sql");
			e.printStackTrace();
		}
	}

	public void deleteAnnouncement(Announcement announcement) {
		PreparedStatement pstmt;
		try {
			conn.connect();
			pstmt = conn.getConnection().prepareStatement(
					DELETE_IMAGES_FROM_ANNOUNCEMENT_QUERY);

			pstmt.setInt(1, announcement.getAnnouncementId());
			pstmt.execute();

			pstmt = conn.getConnection().prepareStatement(
					DELETE_ANNOUNCEMENT_QUERY);

			pstmt.setInt(1, announcement.getAnnouncementId());
			pstmt.execute();

			pstmt.close();
			conn.close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Og這szenie zosta這 usuni皻e "));

		} catch (SQLException e) {
			System.out.println("B章d sql");
			e.printStackTrace();
		}
	}
}
