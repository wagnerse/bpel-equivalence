package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalNetworkSolver;

public class NetworkSolver extends FuzzyAllenIntervalNetworkSolver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7693972019372145973L;
	
	public ActivityState createActivityState(BepelState stateType) {
		
		ActivityState state = (ActivityState) super.createVariable();
		state.setStateType(stateType);
		return state;
	}
	
	public boolean addLink(ActivityStateLink link) {
		return super.addConstraint(link);
	}
	
	public boolean addLinks(ActivityStateLink... links) {
		return super.addConstraints(links);
	}
	
}
