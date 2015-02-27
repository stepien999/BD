package com.bd.serwis.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

import com.bd.serwis.connection.ConnectionManager;
import com.bd.serwis.model.Announcement;
import com.bd.serwis.model.Image;

public class ImageService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2996563757630408182L;
	private ConnectionManager conn = new ConnectionManager();

	private final static String CREATE_IMAGE_QUERY = "INSERT INTO Images (AnnouncementId,Image) VALUES(?,?)";
	private final static String FIND_LAST_ANNOUNCEMENT_ID_QUERY = "SELECT MAX(AnnouncementId) FROM Announcement";

	public void createImage(Image image) {
		PreparedStatement pstmt;
		ResultSet rs;
		Statement stmt;
		Integer announcementId = null;
		try {
			conn.connect();
			stmt = conn.getConnection().createStatement();
			rs = stmt.executeQuery(FIND_LAST_ANNOUNCEMENT_ID_QUERY);

			while (rs.next()) {
				announcementId = rs.getInt(1);
			}

			pstmt = conn.getConnection().prepareStatement(CREATE_IMAGE_QUERY);

			pstmt.setInt(1, announcementId);
			pstmt.setBinaryStream(2, new ByteArrayInputStream(image.getImage()));

			pstmt.execute();

			pstmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("B³¹d sql");
			e.printStackTrace();
		}
	}
}
