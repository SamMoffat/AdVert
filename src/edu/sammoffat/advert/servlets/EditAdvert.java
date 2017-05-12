package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import javax.annotation.PostConstruct;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/adverts/myadvert/{userid}/edit/")
public class EditAdvert extends DatabaseConnect {
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@POST
	public String getRequest(@HeaderParam("Authorization") String token,
			  				 @PathParam("userid") String id,
			  				 @QueryParam("name") String name,
			  				 @QueryParam("lo") String lo, 
			  				 @QueryParam("la") String la, 
			  				 @QueryParam("area") String area, 
			  				 @QueryParam("look") String look, 
			  				 @QueryParam("offer") String offer,
			  				 @QueryParam("comm") String comm) {
		TokenAuth auth = new TokenAuth();
		try {
			auth.checkToken(token);
			
			String addLoLa="";
			String addArea="";
			String addComm="";
			if (auth.isPassed()) {
				if (lo != "NULL" && la != "NULL" ) {
					addLoLa = "longitude = "+lo+", latitude = "+la+",";
				}
				if (!area.equals("undefined")) {
					addArea = "area = "+area+",";
				}
				if (!comm.equals("")) {
					addArea = "comment = \""+comm+"\",";
				}
				
				edtList(look, id, "look");
				edtList(offer, id, "offer");
				
				Connection conn = getConnection();
				Statement st = conn.createStatement();
				
				String sqlAdId = "SELECT advert_id FROM users WHERE user_id = "+ id;
				ResultSet rs = st.executeQuery(sqlAdId);
				String advertId="";
				if (rs.next()) advertId = rs.getString("advert_id");
				
				long date = Instant.now().getEpochSecond();
				String sql = "UPDATE adverts SET date = "+date+","+addLoLa+addArea+addComm;
				if (sql.charAt(sql.length()-1) == ',') sql = sql.substring(0, sql.length()-1);
				sql += " WHERE advert_id = "+advertId;
				int res = st.executeUpdate(sql);
				if (res == 1) {
					return "{\"data\" : \"Success\"}";
				} else {
					return "{\"data\" : \"Failure\"}";
				}
				
			}
			return "{\"data\" : \"auth error\"}";
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	private String edtList(String list, String id, String type) {
		char[] a = list.toCharArray();
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			
			if (type.equals("look")) type = "adverts.looking_list_id";
							    else type = "adverts.offering_list_id";
			String sqllstId = String.format("SELECT lists.list_id FROM lists, adverts, users "
					+ "WHERE users.user_id = adverts.user_id "
					+ "AND %s = lists.list_id "
					+ "AND users.user_id = \"%s\"", type, id);
			ResultSet rs = st.executeQuery(sqllstId);
			String lstId="";
			if (rs.next()) lstId = rs.getString("list_id");
			String sql = String.format("UPDATE lists SET "
									 + "paint_interior = %s, "
									 + "paint_exterior = %s, "
									 + "garden_grass = %s, "
									 + "garden_flowers = %s, "
									 + "garden_trees = %s, "
									 + "guitar = %s, "
									 + "piano = %s, "
									 + "drums = %s, "
									 + "sax = %s, "
									 + "trumpet = %s, "
									 + "flute = %s, "
									 + "diy = %s "
									 + "WHERE list_id = \"%s\"",
									 a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10],a[11], lstId);
			int res = st.executeUpdate(sql);
			if (res == 1) {
				return "PASS";
			} else {
				return "FAIL";
			}
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
		return "NULL";
	}
}
