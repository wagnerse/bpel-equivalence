package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.Variable;
import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalNetworkSolver;
import org.metacsp.time.qualitative.SimpleAllenInterval;

public class NetworkSolver extends FuzzyAllenIntervalNetworkSolver{

	private static final long serialVersionUID = 7693972019372145973L;
	
	private int IDs = 0;
	
	public ActivityState createActivityState(EObject bpelElement, BPELStateEnum stateType) {
		Variable[] states = this.createVariablesSub(1);
		
		if (states.length != 1 && !(states[0] instanceof ActivityState)) {
			throw new IllegalStateException("Created variable is not an ActivityState");
		}
		ActivityState state = (ActivityState) states[0];		
		state.setStateType(stateType);
		state.setBPELElement(bpelElement);
		return state;
	}
	
	/**
	 * Overrides the create method of {@link FuzzyAllenIntervalNetworkSolver} to create an ActivityState
	 */
	@Override
	protected Variable[] createVariablesSub(int num) {
		SimpleAllenInterval[] ret = new ActivityState[num];
		for (int i = 0; i < num; i++) {
			ret[i] = new ActivityState(this, IDs++);
		}
		return ret;
	}
	
	public boolean addConstraint(StateConstraint constraint) {
		return super.addConstraint(constraint);
	}
	
	public boolean addConstraints(StateConstraint... constraints) {
		return super.addConstraints(constraints);
	}
	
}
