package edu.sammoffat.advert.gsons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvitationSent extends Invitation {
	@SerializedName("theirComment")
	@Expose
	private String theirComment;

	public String getTheirComment() {
		return theirComment;
	}

	public void setTheirComment(String theirComment) {
		this.theirComment = theirComment;
	}
	
}
