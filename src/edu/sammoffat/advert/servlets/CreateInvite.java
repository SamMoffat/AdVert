package edu.sammoffat.advert.servlets;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/adverts/otherAdverts/{userid}/invite")
public class CreateInvite extends DatabaseConnect {
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
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
														  + "WHERE (matches.inviter_id = \"%s\" OR matches.receiver_id = \"%s\")"
														  + "AND (matches.inviter_id = \"%s\" OR matches.receiver_id = \"%s\");",invId, invId, id, id));
			if (!check.next()) {
				ResultSet rsInv = st.executeQuery(String.format("SELECT users.advert_id FROM users WHERE users.user_id = %s;", invId));
				String invAdId = "", recAdId = "";
				if (rsInv.next()) {	invAdId = rsInv.getString("advert_id"); }
				ResultSet rsRec = st.executeQuery(String.format("SELECT users.advert_id FROM users WHERE users.user_id = %s;", id));
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
}
