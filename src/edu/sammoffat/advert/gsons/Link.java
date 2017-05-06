package edu.sammoffat.advert.gsons;

public class Link { //Class for HATEOAS implementation with GSON
	
	private String rel;
	private String link;
	
	public String getRel() { return rel; }
	public void setRel(String rel) { this.rel = rel; }
	
	public String getLink() { return link; }
	public void setLink(String link) { this.link = link; }
	
}
