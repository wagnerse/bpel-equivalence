package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalConstraint;

public class ActivityStateLink extends FuzzyAllenIntervalConstraint{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1718735839466179419L;
	
	public ActivityStateLink() {
		super();
	}
	
	public ActivityStateLink(FuzzyAllenIntervalConstraint.Type... types) {
		super(types);
	}

}
