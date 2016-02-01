package com.gcit.training.lms.service;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/library", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}
