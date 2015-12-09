package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.metacsp.framework.ConstraintSolver;
import org.metacsp.time.qualitative.SimpleAllenInterval;

public class ActivityState extends SimpleAllenInterval{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2838842176872064496L;
	
	private BepelState stateType;
	
	public ActivityState(ConstraintSolver cs, int id) {
		super(cs, id);
	}
	
	public ActivityState(BepelState stateType, ConstraintSolver cs, int id) {
		super(cs, id);
		this.stateType = stateType;
	}
	
	public void setStateType(BepelState state) {
		this.stateType = state;
	}
	
	public BepelState getStateType() {
		return stateType;
	}

}
