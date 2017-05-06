package edu.sammoffat.advert.gsons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvertWeights extends Advert {
	
	@SerializedName("weighting")
	@Expose
	private long weighting;
	@SerializedName("distance")
	@Expose
	private double distance;
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public long getWeighting() {
		return weighting;
	}

	public void setWeighting(long weighting) {
		this.weighting = weighting;
	}
	
}
