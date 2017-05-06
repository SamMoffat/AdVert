package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.UriInfo;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/area_list/")
public class GetAreaList extends DatabaseConnect implements HttpRequestServlets {

	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@GET
	public String getRequest(UriInfo a) {
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "SELECT * FROM areas;";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			JSONArray arr = new JSONArray();
			while (rs.next()) {
				JSONObject row = new JSONObject();
				row.put("location", rs.getString("area_name"));
				row.put("index", rs.getInt("area_id"));	
				arr.put(row);
			}
			return arr.toString();
		} catch (Exception e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch (SQLException e) {}
		}
		return null;
	}

	@POST
	public String postRequest(UriInfo a) {
		// TODO Auto-generated method stub
		return null;
	}

}
