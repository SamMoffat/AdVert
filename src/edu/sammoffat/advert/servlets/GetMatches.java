package edu.sammoffat.advert.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.sammoffat.advert.gsons.Advert;
import edu.sammoffat.advert.gsons.AdvertList;
import edu.sammoffat.advert.gsons.AdvertWeights;
import edu.sammoffat.advert.comparitors.*;

@Path("/adverts/myadvert/{userid}/matches/")
public class GetMatches extends DatabaseConnect {

	private static final int AGE_WEIGHTING = 5;			//x points are taken off of weight each day advert is up
	private static final int DISTANCE_WEIGHTING = 10;	//x points are taken off per mile 
	private static final double MATCHES_WEIGHTING = 15;	//x points denotes the difference in x and y in an exponent
	public static final double R_M = 3959.8731088; //Miles
	public static final double R_K = 6372.8;	   //Kilometres
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@GET
	public String getRequest(@HeaderParam("authorization") String token, 
							 @PathParam("userid") String id, 
							 @QueryParam("dis") int displace,
							 @QueryParam("lmt") int limit, 
							 @QueryParam("sort") String sort) {
		
		Connection conn = null;
		Advert userAd = new Advert();
		List<AdvertWeights> ads = new ArrayList<>();
		try {
			conn = getConnection();
			String sql = String.format("SELECT users.name, users.user_id, users.area_id, adverts.date, adverts.longitude, adverts.latitude, lists.*, adverts.advert_id "
									 + "FROM lists, adverts, users "
									 + "WHERE users.user_id =\"%s\" "
									 + "AND users.advert_id = adverts.advert_id "
									 + "AND adverts.looking_list_id = lists.list_id;", id);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			userAd = getAd(rs);
			
			String sqlAds = String.format("SELECT users.name, users.user_id, adverts.date, adverts.longitude, adverts.latitude, lists.*, adverts.advert_id "
										+ "FROM lists, adverts, users "
										+ "WHERE users.area_id = %s "
										+ "AND users.advert_id = adverts.advert_id "
										+ "AND adverts.looking_list_id = lists.list_id ",
										//+ "AND users.name <> \"%s\";",
										rs.getString("area_id"));// rs.getString("name"));
			ResultSet rsAds = st.executeQuery(sqlAds);
			while (rsAds.next()) {
				ads.add(getAd(rsAds.getString("user_id"),   rsAds.getString("name"), rsAds.getInt("date"), rsAds.getDouble("longitude"),
							  rsAds.getDouble("latitude"), rsAds.getBoolean("diy"), rsAds.getBoolean("drums"),
							  rsAds.getBoolean("flute"), rsAds.getBoolean("garden_flowers"), rsAds.getBoolean("garden_grass"),
							  rsAds.getBoolean("garden_trees"), rsAds.getBoolean("paint_exterior"), rsAds.getBoolean("paint_interior"),
							  rsAds.getBoolean("piano"), rsAds.getBoolean("sax"), rsAds.getBoolean("trumpet")));
			}
			
			for (AdvertWeights i : ads) {
				long age =Instant.now().getEpochSecond() - i.getDate();
				i.setWeighting(i.getWeighting() - age/(TimeUnit.DAYS.toSeconds(1)/AGE_WEIGHTING));
				long lookMatch = calcMatches(userAd.getLook(), i.getOffer());
				long offrMatch = calcMatches(userAd.getOffer(), i.getLook());
				i.setWeighting(i.getWeighting() + lookMatch + offrMatch);
				i.setDistance(calcMiles(userAd.getLongitude(), userAd.getLatitude(), i.getLongitude(), i.getLatitude()));
				long distWeight = ((int) i.getDistance())*DISTANCE_WEIGHTING;
				i.setWeighting(i.getWeighting() + distWeight);
			}
			
			Gson ret = new Gson();
			if (sort == null) {
				Collections.sort(ads, new WeightingComparitor());
			} else if (sort.equals("Date")) {
				Collections.sort(ads, new DateComparitor());
			} else if (sort.equals("Proximity")) {
				Collections.sort(ads, new DistanceComparitor());
			} else {
				Collections.sort(ads, new WeightingComparitor());
			}
			
			limit += displace;
			if (limit == 0 && ads.size()>=2) 	{ limit = 2; } 
			else if ( limit == 0 || limit > ads.size()) 	{ limit = ads.size(); }
			if (displace > ads.size()) 			{ displace = ads.size(); }
			String fnlRet = "[";
			for (AdvertWeights i : ads.subList(displace, limit)) {
				fnlRet += ret.toJson(i, AdvertWeights.class) + ",";
			}
			fnlRet = fnlRet.substring(0, fnlRet.length()-1);
			fnlRet +="]";
			return fnlRet;
			
		} catch (Exception e) { e.printStackTrace(); }
		finally {
			try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return "{\"error\" : \"The query was unsuccessful, is the request correct?\"}";
	}
	private double calcMiles(double lon1, double lat1, double lon2, double lat2) {
		
	        double dLat = Math.toRadians(lat2 - lat1);
	        double dLon = Math.toRadians(lon2 - lon1);
	        lat1 = Math.toRadians(lat1);
	        lat2 = Math.toRadians(lat2);
	 
	        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
	        double c = 2 * Math.asin(Math.sqrt(a));
	        return R_M * c;
	   
	}
	public double testMiles(double a, double b, double c, double d) {
		return calcMiles(a,b,c,d);
	}

	private long calcMatches(AdvertList usrLook, AdvertList OthOffr) {
		int exp=0;
		if (usrLook.isDiy() && OthOffr.isDiy()) 					{ exp++; }
		if (usrLook.isDrums() && OthOffr.isDrums()) 				{ exp++; }
		if (usrLook.isFlute() && OthOffr.isFlute()) 				{ exp++; }
		if (usrLook.isGardenFlowers() && OthOffr.isGardenFlowers()) { exp++; }
		if (usrLook.isGardenGrass() && OthOffr.isGardenGrass()) 	{ exp++; }
		if (usrLook.isGardenTrees() && OthOffr.isGardenTrees()) 	{ exp++; }
		if (usrLook.isGuitar() && OthOffr.isGuitar()) 				{ exp++; }
		if (usrLook.isPaintExterior() && OthOffr.isPaintExterior()) { exp++; }
		if (usrLook.isPaintInterior() && OthOffr.isPaintInterior()) { exp++; }
		if (usrLook.isPiano() && OthOffr.isPiano()) 				{ exp++; }
		if (usrLook.isSax() && OthOffr.isSax()) 					{ exp++; }
		if (usrLook.isTrumpet() && OthOffr.isTrumpet()) 			{ exp++; }
		
		if (exp>0) {
			return (long) ((MATCHES_WEIGHTING/2) * (Math.pow(2, ((double) exp))));
		}
		return 0;
	}

	private Advert getAd(ResultSet rs) {
		Advert adRet = new Advert();
		Connection conn = null;
		try {
			conn = getConnection();
			if (rs.next()) {
				AdvertList lookLs = new AdvertList();
				
				adRet.setName		(rs.getString("name"));
				adRet.setId			(rs.getString("user_id"));
				adRet.setDate		(rs.getInt("date"));
				adRet.setLongitude	(rs.getDouble("longitude"));
				adRet.setLatitude	(rs.getDouble("latitude"));
				
				lookLs.setDiy			(rs.getBoolean("diy"));
				lookLs.setDrums			(rs.getBoolean("drums"));
				lookLs.setFlute			(rs.getBoolean("flute"));
				lookLs.setGardenFlowers	(rs.getBoolean("garden_flowers"));
				lookLs.setGardenGrass	(rs.getBoolean("garden_grass"));
				lookLs.setGardenTrees	(rs.getBoolean("garden_trees"));
				lookLs.setPaintExterior	(rs.getBoolean("paint_exterior"));
				lookLs.setPaintInterior	(rs.getBoolean("paint_interior"));
				lookLs.setPiano			(rs.getBoolean("piano"));
				lookLs.setSax			(rs.getBoolean("sax"));
				lookLs.setTrumpet		(rs.getBoolean("trumpet"));
				adRet.setLook(lookLs);
			}
			Statement st = conn.createStatement();
			String sql = String.format("SELECT lists.* FROM lists, adverts, users "
					+ "WHERE users.user_id = \"%s\""
					+ "AND users.advert_id = adverts.advert_id "
					+ "AND adverts.offering_list_id = lists.list_id;", rs.getString("user_id"));
			st.executeQuery(sql);
			ResultSet rsOffr = st.getResultSet();
			if  (rsOffr.next()) {
				AdvertList offrLs = new AdvertList();
				
				offrLs.setDiy			(rsOffr.getBoolean("diy"));
				offrLs.setDrums			(rsOffr.getBoolean("drums"));
				offrLs.setFlute			(rsOffr.getBoolean("flute"));
				offrLs.setGardenFlowers	(rsOffr.getBoolean("garden_flowers"));
				offrLs.setGardenGrass	(rsOffr.getBoolean("garden_grass"));
				offrLs.setGardenTrees	(rsOffr.getBoolean("garden_trees"));
				offrLs.setPaintExterior	(rsOffr.getBoolean("paint_exterior"));
				offrLs.setPaintInterior	(rsOffr.getBoolean("paint_interior"));
				offrLs.setPiano			(rsOffr.getBoolean("piano"));
				offrLs.setSax			(rsOffr.getBoolean("sax"));
				offrLs.setTrumpet		(rsOffr.getBoolean("trumpet"));
				adRet.setOffer(offrLs);
			}
		} catch (SQLException e) {e.printStackTrace();}
		finally {
			try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return adRet;
	}
	private AdvertWeights getAd(String id, String name, int date, double longitude, double latitude, 
			boolean diy, boolean drums, boolean flute, boolean gFlowers, 
			boolean gGrass, boolean gTrees, boolean pExt, boolean pInt, 
			boolean piano, boolean sax, boolean trumpet) {
		
		AdvertWeights adRet = new AdvertWeights();
		Connection conn = null;
		try {
			conn = getConnection();
				AdvertList lookLs = new AdvertList();
			
			adRet.setName		(name);
			adRet.setId			(id);
			adRet.setDate		(date);
			adRet.setLongitude	(longitude);
			adRet.setLatitude	(latitude);
			adRet.setWeighting	(100);
			
			lookLs.setDiy			(diy);
			lookLs.setDrums			(drums);
			lookLs.setFlute			(flute);
			lookLs.setGardenFlowers	(gFlowers);
			lookLs.setGardenGrass	(gGrass);
			lookLs.setGardenTrees	(gTrees);
			lookLs.setPaintExterior	(pExt);
			lookLs.setPaintInterior	(pInt);
			lookLs.setPiano			(piano);
			lookLs.setSax			(sax);
			lookLs.setTrumpet		(trumpet);
			adRet.setLook(lookLs);
			
			Statement st = conn.createStatement();
			String sql = String.format("SELECT lists.* FROM lists, adverts, users "
					+ "WHERE users.user_id = \"%s\""
					+ "AND users.advert_id = adverts.advert_id "
					+ "AND adverts.offering_list_id = lists.list_id;", id);
			st.executeQuery(sql);
			ResultSet rsOffr = st.getResultSet();
			if  (rsOffr.next()) {
				AdvertList offrLs = new AdvertList();
				
				offrLs.setDiy			(rsOffr.getBoolean("diy"));
				offrLs.setDrums			(rsOffr.getBoolean("drums"));
				offrLs.setFlute			(rsOffr.getBoolean("flute"));
				offrLs.setGardenFlowers	(rsOffr.getBoolean("garden_flowers"));
				offrLs.setGardenGrass	(rsOffr.getBoolean("garden_grass"));
				offrLs.setGardenTrees	(rsOffr.getBoolean("garden_trees"));
				offrLs.setPaintExterior	(rsOffr.getBoolean("paint_exterior"));
				offrLs.setPaintInterior	(rsOffr.getBoolean("paint_interior"));
				offrLs.setPiano			(rsOffr.getBoolean("piano"));
				offrLs.setSax			(rsOffr.getBoolean("sax"));
				offrLs.setTrumpet		(rsOffr.getBoolean("trumpet"));
				adRet.setOffer(offrLs);
			}
		} catch (SQLException e) {e.printStackTrace();}
		finally {
			try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return adRet;
	}
	
	@POST
	public String postRequest(UriInfo a) {
		// TODO Auto-generated method stub
		return null;
	}

}
