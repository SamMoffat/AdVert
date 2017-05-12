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

import edu.sammoffat.advert.gsons.AdvertList;
import edu.sammoffat.advert.gsons.Invitation;
import edu.sammoffat.advert.gsons.InvitationSent;

@Path("/adverts/myadvert/{userid}/invitations/")
public class GetInvitations extends DatabaseConnect {
	
	@PostConstruct
	protected void intialise() {
		super.initialise();
	}
	
	@Path("/myinvites")
	@GET
	public String getRequest2(@PathParam("userid") String id) {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			String sql = String.format("SELECT matches.date, matches.status, matches.inviter_comment, matches.receiver_comment, matches.invite_advert_id, matches.receive_advert_id "
					 				 + "FROM users, matches "
					 				 + "WHERE users.user_id = matches.inviter_id "
					 				 + "AND users.user_id = %s", id);
			ResultSet rs = st.executeQuery(sql);
			List<InvitationSent> inviteList = new ArrayList<InvitationSent>();
			while (rs.next()) {
				Statement sst = conn.createStatement();
				String recAdId = rs.getString("receive_advert_id");
				InvitationSent toAdd = new InvitationSent();
				AdvertList look = new AdvertList();
				AdvertList offr = new AdvertList();
				toAdd.setComment(rs.getString("inviter_comment"));
				toAdd.setTheirComment(rs.getString("receiver_comment"));
				toAdd.setDate(rs.getLong("date"));
				toAdd.setStatus(rs.getString("status"));
				
				String sqlName = String.format("SELECT users.name, users.user_id FROM users, adverts "
											 + "WHERE users.advert_id = adverts.advert_id "
											 + "AND adverts.advert_id = %s;", recAdId);
				ResultSet rsName = sst.executeQuery(sqlName);
				if (rsName.next()) {
					toAdd.setName(rsName.getString("name"));
					toAdd.setId(rsName.getString("user_id"));
				}
				String sqlLook = String.format("SELECT lists.* "
							   	  + "FROM lists, adverts "
							   	  + "WHERE lists.list_id = adverts.looking_list_id "
							   	  + "AND adverts.advert_id = %s", recAdId);
				ResultSet rsLook = sst.executeQuery(String.format(sqlLook));
				look = populateAdList(rsLook);
				ResultSet rsOffr = sst.executeQuery(String.format("SELECT lists.* "
																+ "FROM lists, adverts "
																+ "WHERE lists.list_id = adverts.offering_list_id "
																+ "AND adverts.advert_id = %s", recAdId));
				offr = populateAdList(rsOffr);
				toAdd.setLook(look);
				toAdd.setOffer(offr);
				inviteList.add(toAdd);
				
			}
			Gson gson = new Gson();
			String retStr = "[";
			for (Invitation i : inviteList) {
				System.out.println(gson.toJson(i, InvitationSent.class));
				retStr += gson.toJson(i, InvitationSent.class) + ",";
			}
			retStr = retStr.substring(0, retStr.length()-1);
			retStr += "]";
			System.out.println(retStr);
			return retStr;
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } 
		}
		return "";
	}
	@Path("")
	@GET
	public String getRequest(@PathParam("userid") String id) {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			String sql = String.format("SELECT matches.date, matches.status, matches.inviter_comment, matches.invite_advert_id, matches.receive_advert_id "
									 + "FROM users, matches "
									 + "WHERE users.user_id = matches.receiver_id "
									 + "AND users.user_id = %s", id);
			
			ResultSet rs = st.executeQuery(sql);
			
			List<Invitation> inviteList = new ArrayList<Invitation>();
			while (rs.next()) {
				Statement sst = conn.createStatement();
				String invAdId = rs.getString("invite_advert_id");
				Invitation toAdd = new Invitation();
				AdvertList look = new AdvertList();
				AdvertList offr = new AdvertList();
				toAdd.setComment(rs.getString("inviter_comment"));
				toAdd.setDate(rs.getLong("date"));
				toAdd.setStatus(rs.getString("status"));
				
				String sqlName = String.format("SELECT users.name, users.user_id FROM users, adverts "
											 + "WHERE users.advert_id = adverts.advert_id "
											 + "AND adverts.advert_id = %s;", invAdId);
				ResultSet rsName = sst.executeQuery(sqlName);
				if (rsName.next()) {
					toAdd.setName(rsName.getString("name"));
					toAdd.setId(rsName.getString("user_id"));
				}
				String sqlLook = String.format("SELECT lists.* "
							   	  + "FROM lists, adverts "
							   	  + "WHERE lists.list_id = adverts.looking_list_id "
							   	  + "AND adverts.advert_id = %s", invAdId);
				ResultSet rsLook = sst.executeQuery(String.format(sqlLook));
				look = populateAdList(rsLook);
				ResultSet rsOffr = sst.executeQuery(String.format("SELECT lists.* "
														       + "FROM lists, adverts "
														       + "WHERE lists.list_id = adverts.offering_list_id "
														       + "AND adverts.advert_id = %s", invAdId));
				offr = populateAdList(rsOffr);
				toAdd.setLook(look);
				toAdd.setOffer(offr);
				inviteList.add(toAdd);
			}
			
			Gson gson = new Gson();
			String retStr = "[";
			for (Invitation i : inviteList) {
				retStr += gson.toJson(i, Invitation.class) + ",";
			}
			retStr = retStr.substring(0, retStr.length()-1);
			retStr += "]";
			return retStr;
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	@Path("/complete")
	@GET
	public String getRequest3(@PathParam("userid") String id) {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			String sql = String.format("SELECT matches.date, matches.status, matches.inviter_comment, matches.receiver_comment, matches.invite_advert_id, matches.receive_advert_id "
					 				 + "FROM users, matches "
					 				 + "WHERE users.user_id = matches.inviter_id "
					 				 + "AND matches.status = 'A' "
					 				 + "AND users.user_id = %s", id);
			ResultSet rs = st.executeQuery(sql);
			List<InvitationSent> inviteList = new ArrayList<InvitationSent>();
			while (rs.next()) {
				Statement sst = conn.createStatement();
				String recAdId = rs.getString("receive_advert_id");
				InvitationSent toAdd = new InvitationSent();
				AdvertList look = new AdvertList();
				AdvertList offr = new AdvertList();
				toAdd.setComment(rs.getString("inviter_comment"));
				toAdd.setTheirComment(rs.getString("receiver_comment"));
				toAdd.setDate(rs.getLong("date"));
				toAdd.setStatus(rs.getString("status"));
				
				String sqlName = String.format("SELECT users.name, users.user_id FROM users, adverts, matches "
											 + "WHERE users.advert_id = adverts.advert_id "
											 + "AND adverts.advert_id = %s;", recAdId);
				ResultSet rsName = sst.executeQuery(sqlName);
				if (rsName.next()) {
					toAdd.setName(rsName.getString("name"));
					toAdd.setId(rsName.getString("user_id"));
				}
				String sqlLook = String.format("SELECT lists.* "
							   	  + "FROM lists, adverts "
							   	  + "WHERE lists.list_id = adverts.looking_list_id "
							   	  + "AND adverts.advert_id = %s", recAdId);
				ResultSet rsLook = sst.executeQuery(String.format(sqlLook));
				look = populateAdList(rsLook);
				ResultSet rsOffr = sst.executeQuery(String.format("SELECT lists.* "
														       + "FROM lists, adverts "
														       + "WHERE lists.list_id = adverts.offering_list_id "
														       + "AND adverts.advert_id = %s", recAdId));
				offr = populateAdList(rsOffr);
				toAdd.setLook(look);
				toAdd.setOffer(offr);
				inviteList.add(toAdd);
				
			}
			Gson gson = new Gson();
			String retStr = "[";
			for (Invitation i : inviteList) {
				System.out.println(gson.toJson(i, InvitationSent.class));
				retStr += gson.toJson(i, InvitationSent.class) + ",";
			}
			retStr = retStr.substring(0, retStr.length()-1);
			retStr += "]";
			System.out.println(retStr);
			return retStr;
		} catch (SQLException e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } 
		}
		return "";
	}
	
	private AdvertList populateAdList(ResultSet rs) {
		AdvertList toRet = new AdvertList();
		try {
			if (rs.next()) {
				toRet.setDiy(rs.getBoolean("diy"));
				toRet.setDrums(rs.getBoolean("drums"));
				toRet.setFlute(rs.getBoolean("flute"));
				toRet.setGardenFlowers(rs.getBoolean("garden_flowers"));
				toRet.setGardenGrass(rs.getBoolean("garden_grass"));
				toRet.setGardenTrees(rs.getBoolean("garden_trees"));
				toRet.setGuitar(rs.getBoolean("guitar"));
				toRet.setPaintExterior(rs.getBoolean("paint_exterior"));
				toRet.setPaintInterior(rs.getBoolean("paint_interior"));
				toRet.setPiano(rs.getBoolean("piano"));
				toRet.setSax(rs.getBoolean("sax"));
				toRet.setTrumpet(rs.getBoolean("trumpet"));	
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return toRet;
	}
	
}
