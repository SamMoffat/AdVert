package edu.sammoffat.advert.gsons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NameGeo {

@SerializedName("name")
@Expose
private String name;
@SerializedName("longitude")
@Expose
private Double longitude;
@SerializedName("latitude")
@Expose
private Double latitude;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public Double getLongitude() {
return longitude;
}

public void setLongitude(Double longitude) {
this.longitude = longitude;
}

public Double getLatitude() {
return latitude;
}

public void setLatitude(Double latitude) {
this.latitude = latitude;
}

}