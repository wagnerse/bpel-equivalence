package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.metacsp.framework.ConstraintSolver;
import org.metacsp.time.qualitative.SimpleAllenInterval;

public class ActivityState extends SimpleAllenInterval{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2838842176872064496L;
	
	private BPELStateEnum stateType;
	
	public ActivityState(ConstraintSolver cs, int id) {
		super(cs, id);
	}
	
	public ActivityState(BPELStateEnum stateType, ConstraintSolver cs, int id) {
		super(cs, id);
		this.stateType = stateType;
	}
	
	public void setStateType(BPELStateEnum state) {
		this.stateType = state;
	}
	
	public BPELStateEnum getStateType() {
		return stateType;
	}

}
