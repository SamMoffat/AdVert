package edu.sammoffat.advert.servlets;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/api/")
public class TestServlet implements HttpRequestServlets {
	//Test servlet to retrieve data from UriInfo type so an interface can be implemented
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getRequest(@Context UriInfo r) {
        return "{\"data\": \"" + r.getQueryParameters().getFirst("bar") + "\" }";
    }

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public String postRequest(UriInfo a) {
		return getRequest(a);
	}
	
}
