package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;

@Path("/adverts/myadvert/{userid}")
public class GetUserAdvert extends DatabaseConnect {

	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@GET
	@Produces("application/json")
	public String getRequest(@PathParam("userid") String id) {
		Connection conn = null;
		try {
			conn = getConnection();
			String offr = "offering";
			String look = "looking";
			String sql = "SELECT lists.* FROM lists, adverts, users "
						+ "WHERE users.user_id = adverts.user_id "
						+ "AND adverts.%s_list_id = lists.list_id "
						+ "AND users.user_id = \""+id+"\";";
			
			Statement st = conn.createStatement();
			ResultSet rsOffr = st.executeQuery(String.format(sql, offr));
			String[] retOffr = getList(rsOffr);
			ResultSet rsLook = st.executeQuery(String.format(sql, look));
			String[] retLook = getList(rsLook);
			Gson gson = new Gson();
			String finalRet = String.format("{\"offer\" : %s, \"look\" : %s}", gson.toJson(retLook), gson.toJson(retOffr)); 
			return finalRet;
		} catch (Exception e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch (SQLException e) {}
		}
		return null;
	}

	private String[] getList(ResultSet rs) {
		try {
			ResultSetMetaData meta = rs.getMetaData();
			final int colLmt = meta.getColumnCount();
			List<String> toRet = new ArrayList<String>();
			if (rs.next()) {
				for (int i=1; i<colLmt-1; i++) {
					if (rs.getBoolean(i)) {
						toRet.add(meta.getColumnName(i));
					}
				}
			}
			return toRet.toArray(new String[toRet.size()]);
		}catch (SQLException e) { e.printStackTrace(); }
		return null;
	}

	@POST
	public String postRequest(UriInfo a) {
		// TODO Auto-generated method stub
		return null;
	}

}
