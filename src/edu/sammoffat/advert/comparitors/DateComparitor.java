package edu.sammoffat.advert.comparitors;

import java.util.Comparator;

import edu.sammoffat.advert.gsons.AdvertWeights;

public class DateComparitor implements Comparator<AdvertWeights> {

	@Override
	public int compare(AdvertWeights arg0, AdvertWeights arg1) {
		return Long.compare(arg0.getDate(), arg1.getDate());
	}
	
}
