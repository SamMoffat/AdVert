package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/adverts/myadvert/{userid}/invitations/delete")
public class DeleteInvitation extends DatabaseConnect {
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@POST
	public String putRequest(@HeaderParam("authorization") String token, @PathParam("userid") String id, @QueryParam("inv") String inviter) {
		TokenAuth auth = new TokenAuth();
		Connection conn = null;
		try {
			auth.checkToken(token);
			if (auth.isPassed()) {
				conn = getConnection();
				Statement st = conn.createStatement();
				String sql1 = String.format("SELECT users.user_id FROM users WHERE users.name = \"%s\";", inviter);
				ResultSet rs = st.executeQuery(sql1);
				String invId = "";
				if (rs.next()) {
					invId = rs.getString("user_id");
				}
				String sql = String.format("DELETE FROM matches "
						   + "WHERE matches.receiver_id = %s "
						   + "AND matches.inviter_id = %s",id,invId);
				int res = st.executeUpdate(sql);
				if (res == 1) {
					return "{\"data\" : \"deleted\"}";
				} else {
					return "{\"data\" : \"sql fail\"}";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"data\" : \"auth error\"}";
		} finally {
			try { conn.close(); } catch ( SQLException e ) { e.printStackTrace();}
		}
		return null;
	}
}
