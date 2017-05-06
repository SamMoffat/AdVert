package edu.sammoffat.advert.servlets;

import javax.ws.rs.core.UriInfo;

public interface HttpRequestServlets {
	
	String getRequest(UriInfo a);
	String postRequest(UriInfo a);
	
}
