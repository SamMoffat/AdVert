package edu.sammoffat.advert.comparitors;

import java.util.Comparator;

import edu.sammoffat.advert.gsons.AdvertWeights;

public class DistanceComparitor implements Comparator<AdvertWeights> {

	@Override
	public int compare(AdvertWeights arg0, AdvertWeights arg1) {
		return Double.compare(arg0.getDistance(), arg1.getDistance());
	}
	
}
