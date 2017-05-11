package edu.sammoffat.advert.gsons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invitation {

@SerializedName("name")
@Expose
private String name;
@SerializedName("id")
@Expose
private String id;
public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

@SerializedName("date")
@Expose
private long date;
@SerializedName("Status")
@Expose
private String status;
@SerializedName("comment")
@Expose
private String comment;
@SerializedName("look")
@Expose
private AdvertList look;
@SerializedName("offer")
@Expose
private AdvertList offer;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public long getDate() {
return date;
}

public void setDate(long date) {
this.date = date;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getComment() {
return comment;
}

public void setComment(String comment) {
this.comment = comment;
}

public AdvertList getLook() {
return look;
}

public void setLook(AdvertList look) {
this.look = look;
}

public AdvertList getOffer() {
return offer;
}

public void setOffer(AdvertList offer) {
this.offer = offer;
}

}