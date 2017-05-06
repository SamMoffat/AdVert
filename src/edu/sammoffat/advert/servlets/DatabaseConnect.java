package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseConnect {
    
	private DataSource datasource;   
	
	protected void initialise() {
		 try {
			 Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
			 datasource = (DataSource) envCtx.lookup("jdbc/db");
		 }
		    catch (NamingException e) { e.printStackTrace(); }
	}
	
	protected Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}
	
}