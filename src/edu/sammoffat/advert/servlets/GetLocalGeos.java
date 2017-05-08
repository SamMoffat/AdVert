package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;

import com.google.gson.Gson;

import edu.sammoffat.advert.gsons.NameGeo;

@Path("/user/{userid}/adverts/otheradverts/local")
public class GetLocalGeos extends DatabaseConnect {
	//Test servlet to retrieve data from UriInfo type so an interface can be implemented
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@GET
    public String getRequest(@PathParam("userid") String id) {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			String sqlArea = "SELECT users.area_id FROM users WHERE users.user_id = " + id + ";";
			st.executeQuery(sqlArea);
			ResultSet rsId = st.getResultSet();
			String areaId = "";
			if (rsId.next()) {
				areaId = rsId.getString("area_id");
			}
			String sqlLngLat = "SELECT users.name, adverts.latitude, adverts.longitude "
					+ "FROM users, adverts "
					+ "WHERE users.advert_id = adverts.advert_id "
					+ "AND users.area_id = " + areaId + " "
					+ "AND users.user_id <> " + id + ";";
			st.executeQuery(sqlLngLat);
			ResultSet rsLngLat = st.getResultSet();
			
			List<NameGeo> nameGeos = new ArrayList<NameGeo>();
			while (rsLngLat.next()) {
				NameGeo toAdd = new NameGeo();
				toAdd.setLatitude(rsLngLat.getDouble("latitude"));
				toAdd.setLongitude(rsLngLat.getDouble("longitude"));
				toAdd.setName(rsLngLat.getString("name"));
				nameGeos.add(toAdd);
			}
			
			String retStr = "[";
			Gson gson = new Gson();
			for (NameGeo a : nameGeos) {
				retStr += gson.toJson(a, NameGeo.class) + ",";
			}
			retStr = retStr.substring(0, retStr.length()-1);
			retStr +="]";
			return retStr;
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
        return null;
    }
}