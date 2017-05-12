package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/adverts/otherAdverts/{userid}/invite")
public class CreateInvite extends DatabaseConnect {
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@Path("")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String getRequest(@PathParam("userid") String invId, @QueryParam("m") String id, @QueryParam("com") String comment) {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			ResultSet check = st.executeQuery(String.format("SELECT matches.match_id "
														  + "FROM matches "
														  + "WHERE matches.inviter_id = \"s\" "
														  + "AND matches.receiver_id = \"s\";", id, invId));
			if (!check.next()) {
				ResultSet rsInv = st.executeQuery(String.format("SELECT users.advert_id FROM users WHERE users.user_id = %s;", id));
				String invAdId = "", recAdId = "";
				if (rsInv.next()) {	invAdId = rsInv.getString("advert_id"); }
				ResultSet rsRec = st.executeQuery(String.format("SELECT users.advert_id FROM users WHERE users.user_id = %s;", invId));
				if (rsRec.next()) {	recAdId = rsRec.getString("advert_id"); }
				String sql = String.format("INSERT INTO matches "
										 + "VALUES(%s, 'P', \"%s\", NULL, %s, %s, %s, %s, NULL);",
										   Instant.now().getEpochSecond(), comment, id, invId, invAdId, recAdId
										  );
				int res = st.executeUpdate(sql);
				if (res == 1) {
					return "{\"data\" : \"Success\"}";
				} else {
					return "{\"data\" : \"Failure\"}";
				}
			} else {
				return "{\"data\" : \"Database\"}";
			}
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	@Path("/accept")
	@POST
	public String postRequest(@QueryParam("inv") String otherId, @QueryParam("comm") String comment, @PathParam("userid") String id, @HeaderParam("Authorization") String token) {
		TokenAuth auth = new TokenAuth();
		Connection conn = null;
		try {
			auth.checkToken(token);
			conn = getConnection();
			Statement st = conn.createStatement();
			if (auth.isPassed()) {
				
				String sql = String.format("UPDATE matches "
										 + "SET status = 'A', receiver_comment = \"%s\" "
										 + "WHERE inviter_id = %s "
										 + "AND receiver_id = %s", comment, id, otherId);
				int res = st.executeUpdate(sql);
				if (res == 1) {
					return "{\"data\" : \"Success\"}";
				} else {
					return "{\"data\" : \"Failure\"}";
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		return "{\"data\" : \"auth error\"}";
	}
}
