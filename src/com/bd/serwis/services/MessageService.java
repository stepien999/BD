package com.bd.serwis.services;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.bd.serwis.connection.ConnectionManager;
import com.bd.serwis.model.Message;

public class MessageService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1052891753717731202L;
	private ConnectionManager conn = new ConnectionManager();
	private Message message;
	private List<Message> messagesList;
	private UserService userService = new UserService();

	private final static String CREATE_MESSAGE_QUERY = "INSERT INTO Message (SenderId,ReciepentId,MessageContent,DateOfSending)"
			+ "VALUES (?,?,?,?)";

	private final static String FIND_ALL_SENT_MESSAGES_QUERY = "SELECT * FROM Message"
			+ " WHERE SenderId=?" + " ORDER BY MessageId DESC";

	private final static String FIND_ALL_RECEIVED_MESSAGES_QUERY = "SELECT * FROM Message"
			+ " WHERE ReciepentId=?" + " ORDER BY MessageId DESC";

	public List<Message> findAllSentMessages(int senderId) {
		messagesList = new ArrayList<Message>();
		PreparedStatement pstmt;
		ResultSet rs;

		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_ALL_SENT_MESSAGES_QUERY);
			pstmt.setInt(1, senderId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				message = new Message();
				message.setMessageId(rs.getInt("MessageId"));
				message.setSender(userService.findUserById(rs
						.getInt("SenderId")));
				message.setReciepent(userService.findUserById(rs
						.getInt("ReciepentId")));
				message.setMessageContent(rs.getString("MessageContent"));
				message.setDateOfSending(rs.getDate("DateOfSending"));
				messagesList.add(message);
			}
			pstmt.close();
			rs.close();
			conn.close();

			return messagesList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messagesList;
	}

	public List<Message> findAllReceivedMessages(int reciepentId) {
		messagesList = new ArrayList<Message>();
		PreparedStatement pstmt;
		ResultSet rs;

		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_ALL_RECEIVED_MESSAGES_QUERY);
			pstmt.setInt(1, reciepentId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				message = new Message();
				message.setMessageId(rs.getInt("MessageId"));
				message.setSender(userService.findUserById(rs
						.getInt("SenderId")));
				message.setReciepent(userService.findUserById(rs
						.getInt("ReciepentId")));
				message.setMessageContent(rs.getString("MessageContent"));
				message.setDateOfSending(rs.getDate("DateOfSending"));
				messagesList.add(message);
			}
			pstmt.close();
			rs.close();
			conn.close();

			return messagesList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messagesList;
	}

	public void createMessage(Message message) {
		if (message.getSender().getUserId()
				.equals(message.getReciepent().getUserId())) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Nie mo¿esz wys³aæ wiadomoœci do siebie"));
			return;
		}
		PreparedStatement pstmt;
		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(CREATE_MESSAGE_QUERY);

			pstmt.setInt(1, message.getSender().getUserId());
			pstmt.setInt(2, message.getReciepent().getUserId());
			pstmt.setString(3, message.getMessageContent());
			pstmt.setDate(4, new java.sql.Date(message.getDateOfSending()
					.getTime()));
			pstmt.execute();

			pstmt.close();
			conn.close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Wiadomoœæ zosta³a wys³ana"));

		} catch (SQLException e) {
			System.out.println("B³¹d sql");
			e.printStackTrace();
		}
	}
}
