package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalNetworkSolver;

public class NetworkSolver extends FuzzyAllenIntervalNetworkSolver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7693972019372145973L;
	
	public ActivityState createActivityState(BPELStateEnum stateType) {
		
		ActivityState state = (ActivityState) super.createVariable();
		state.setStateType(stateType);
		return state;
	}
	
	public boolean addConstraint(StateConstraint constraint) {
		return super.addConstraint(constraint);
	}
	
	public boolean addConstraints(StateConstraint... constraints) {
		return super.addConstraints(constraints);
	}
	
}
