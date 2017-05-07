package edu.sammoffat.advert.comparitors;

import java.util.Comparator;

import edu.sammoffat.advert.gsons.AdvertWeights;

public class WeightingComparitor implements Comparator<AdvertWeights> {

	@Override
	public int compare(AdvertWeights arg0, AdvertWeights arg1) {
		int ret = Long.compare(arg1.getWeighting(), arg0.getWeighting());
		return ret;
	}
	
}
