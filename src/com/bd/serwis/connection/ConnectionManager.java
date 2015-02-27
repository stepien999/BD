package com.bd.serwis.connection;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ConnectionManager implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4467716087660311685L;
	private final static String CONNECTION_URL = "jdbc:mysql://localhost/ogloszenia";
	private Connection connection;

	public ConnectionManager(){
		try {
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		} catch (InstantiationException e) {
			System.out.println("Nie mo¿na utworzyæ instancji tej klasy");
		} catch (IllegalAccessException e) {
			System.out.println("Brak dostêpu");
		} catch (ClassNotFoundException e) {
			System.out.println("Nie znaleziono klasy");
		}
	}
	
	public void connect(){
		try {
			connection = DriverManager.getConnection (CONNECTION_URL, "serwisogloszen", "test1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected(){
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void close() throws SQLException{
		connection.close();
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
