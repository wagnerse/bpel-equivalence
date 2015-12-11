package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalConstraint;

public class StateConstraint extends FuzzyAllenIntervalConstraint{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1718735839466179419L;
	
	public StateConstraint() {
		super();
	}
	
	public StateConstraint(FuzzyAllenIntervalConstraint.Type... types) {
		super(types);
	}

}
