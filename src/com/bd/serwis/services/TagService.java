package com.bd.serwis.services;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bd.serwis.model.Announcement;
import com.bd.serwis.model.Tag;
import com.bd.serwis.connection.ConnectionManager;

public class TagService implements Serializable {

	private static final long serialVersionUID = 6029384595870911801L;
	ConnectionManager conn = new ConnectionManager();

	private final static String CREATE_TAG_QUERY = "INSERT INTO Tags (AnnouncementId,TagName)"
			+ "VALUES (?,?)";

	private final static String FIND_LAST_ANNOUNCEMENT_ID_QUERY = "SELECT MAX(AnnouncementId) FROM Announcement";

	public void createTag(Tag tag) {
		PreparedStatement pstmt;
		Statement stmt;
		ResultSet rs;
		Integer announcementId = null;

		conn.connect();
		try {
			stmt = conn.getConnection().createStatement();
			rs = stmt.executeQuery(FIND_LAST_ANNOUNCEMENT_ID_QUERY);
			while (rs.next()) {
				announcementId = rs.getInt(1);
			}

			pstmt = conn.getConnection().prepareStatement(CREATE_TAG_QUERY);

			pstmt.setInt(1, announcementId);
			pstmt.setString(2, tag.getTagName());
			pstmt.execute();

			pstmt.close();
			stmt.close();
			rs.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
