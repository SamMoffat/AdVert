package edu.sammoffat.advert.servlets;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.json.JSONObject;
import com.google.gson.Gson;
import edu.sammoffat.advert.gsons.AppToken;

public class TokenAuth { //
	
	private boolean passed = false;
	
    public void checkToken(String userToken) throws Exception {
    	String appToken = getAppToken();
    	String query = String.format("debug_token?input_token=%s&access_token=" + appToken,
    			URLEncoder.encode(userToken.toString(), "UTF-8"));
    	System.out.println("SHROOP " + query);
        URL fAuth = new URL("https://graph.facebook.com/" + query);
        URLConnection fConn = fAuth.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                fConn.getInputStream()));
        String input;
        while ((input = in.readLine()) != null) {
        	org.json.JSONObject jsonObj = new JSONObject(input);
        	if (jsonObj.has("data")) {
        		jsonObj = jsonObj.getJSONObject("data");
        		if (jsonObj.has("is_valid")) {
        			passed = jsonObj.getBoolean("is_valid");
        		}
        	}
        }
    }
    
    public String getAppToken() throws Exception {
    	URL appReq = new URL("https://graph.facebook.com/oauth/access_token?client_id=" + ServerInfo.getAppID() + "&client_secret="+ ServerInfo.getAppSec() + "&grant_type=client_credentials");
    	URLConnection fConn = appReq.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                fConn.getInputStream()));
        String input, output = "";
        while ((input = in.readLine()) != null) {
        	 output += input;
        }
        
        System.out.println("PLAP    " + output);
        Gson gson = new Gson();
        AppToken appToken = gson.fromJson(output, AppToken.class);
        System.out.println(appToken.getAccessToken());
    	return appToken.getAccessToken();
    }

	public boolean isPassed() {
		return passed;
	}

}

