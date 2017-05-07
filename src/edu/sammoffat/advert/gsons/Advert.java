package edu.sammoffat.advert.gsons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Advert {

@SerializedName("name")
@Expose
private String name;
@SerializedName("id")
@Expose
private String id;
@SerializedName("date")
@Expose
private long date;
@SerializedName("longitude")
@Expose
private double longitude;
@SerializedName("latitude")
@Expose
private double latitude;
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

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public long getDate() {
return date;
}

public void setDate(long date) {
this.date = date;
}

public double getLongitude() {
return longitude;
}

public void setLongitude(double longitude) {
this.longitude = longitude;
}

public double getLatitude() {
return latitude;
}

public void setLatitude(double latitude) {
this.latitude = latitude;
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

