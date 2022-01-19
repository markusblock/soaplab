package org.soaplab.domain;

import java.util.Comparator;

public class WeightComparator implements Comparator<Weight> {

	@Override
	public int compare(Weight o1, Weight o2) {
		return Comparator.comparing(Weight::getWeight).thenComparing(Weight::getUnit).compare(o1, o2);
	}

}
