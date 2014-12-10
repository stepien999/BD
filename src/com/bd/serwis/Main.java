package com.bd.serwis;

import com.bd.serwis.connection.ConnectionManager;

public class Main {

	public static void main(String[] args) {
		
		ConnectionManager conn = new ConnectionManager();
		conn.connect();
	}

}
