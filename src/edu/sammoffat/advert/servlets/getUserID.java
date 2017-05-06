package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/LogIn")
public class getUserID extends DatabaseConnect {
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String getRequest(@Context UriInfo a, @HeaderParam("Authorization") String token) {
		
		String userID = a.getQueryParameters().getFirst("userID");
	    Connection connection = null;
	    TokenAuth checkToken = new TokenAuth();
		try {
			checkToken.checkToken(token);
			if (checkToken.isPassed()) {
				connection = getConnection();
				String sql = "SELECT * FROM advert.users WHERE user_id = \"" + userID + "\";";
		    	ResultSet rs;
		    	Statement st = connection.createStatement();
		    	rs = st.executeQuery(sql);
		    	
				if (!rs.next() ) { return "{\"data\": \"Failure\"}"; }
							else { return "{\"data\": \"Success\"}"; }
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally {
		if (connection != null) 
			try {connection.close();} catch (SQLException e) {}
		}
        return "{\"data\": \"Unauthorised\"}";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String postRequest(@Context UriInfo a, @HeaderParam("Authorization") String token) {
		return getRequest(a, token);
	}
}
