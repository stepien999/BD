package com.bd.serwis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager {
	
	private final static String CONNECTION_URL = "jdbc:mysql://localhost/ogloszenia";
	private static Connection connection = null;
	
	public ConnectionManager(){
		try {
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		} catch (InstantiationException e) {
			System.out.println("Nie mo�na utworzy� instancji tej klasy");
		} catch (IllegalAccessException e) {
			System.out.println("Brak dost�pu");
		} catch (ClassNotFoundException e) {
			System.out.println("Nie znaleziono klasy");
		}
	}
	
	public void connect(){
		try {
			connection = DriverManager.getConnection (CONNECTION_URL, "serwisogloszen", "test1234");
			System.out.println("Po��czono");
		} catch (SQLException e) {
			System.out.println("Nie mo�na utworzy� po��czenia z baz� danych");
		}
	}
}
