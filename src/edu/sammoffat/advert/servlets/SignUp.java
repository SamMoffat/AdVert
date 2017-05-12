package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/SignUp")
public class SignUp extends DatabaseConnect {
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String postRequest(@HeaderParam("Authorization") String token,
							  @QueryParam("id")				String id,
							  @QueryParam("name") 			String name,
							  @QueryParam("lo") 			String lo, 
							  @QueryParam("la") 			String la, 
							  @QueryParam("area") 			String area, 
							  @QueryParam("look") 			String look, 
							  @QueryParam("offer") 			String offer,
							  @QueryParam("comm") 			String comm) {
		//return "FAILURE";
		if (lo.equals("undefined") || la.equals("undefined")) {
			return "{\"data\" : \"Please enable geolocation to make an advert!\"}";
		} else if (area.equals("undefined")) {
			return "{\"data\" : \"Please define your area!\"}";
		} 
		if (comm.equals("undefined")) {
			comm = "NULL";
		}
		
		String lookId = addList(look);
		String offerId;
		if (!look.equals("NULL") ) offerId = addList(offer); else offerId = "NULL";
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			String sqlInsAdv = String.format("INSERT INTO advert.adverts VALUES (%d, %s, %s, \"%s\", %s, %s, \"%s\", NULL)", 
					Instant.now().getEpochSecond(), lo, la, comm, offerId, lookId, id);
			st.execute(sqlInsAdv, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			String advertId="NULL";
			if (rs.next()) advertId = String.valueOf(rs.getInt(1));
			
			String sqlInsUsr = String.format("INSERT INTO advert.users VALUES (\"%s\", %s, %s, \"%s\");", name, area, advertId, id);
			int res = st.executeUpdate(sqlInsUsr);
			if (res == 1) {
				return "{\"data\" : \"Success\"}";
			}
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
		return "{\"data\" : \"Failure\"}";
	}

	private String addList(String look) {
		char[] a = look.toCharArray();
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			String sql = String.format("INSERT INTO lists VALUES(%c,%c,%c,%c,%c,%c,%c,%c,%c,%c,%c,%c, NULL);",
										a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10],a[11]
									  );
			st.execute(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				return String.valueOf(rs.getInt(1));
			} else {
				return "NULL";
			}
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
		return "NULL";
	}
}
