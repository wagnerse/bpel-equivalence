package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.ConstraintSolver;
import org.metacsp.time.qualitative.SimpleAllenInterval;

public class ActivityState extends SimpleAllenInterval{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2838842176872064496L;
	
	private BPELStateEnum stateType;
	private EObject bpelElement;
	
	public ActivityState(ConstraintSolver cs, int id) {
		super(cs, id);
	}
	
	public ActivityState(EObject bpelElement, BPELStateEnum stateType, ConstraintSolver cs, int id) {
		super(cs, id);
		this.stateType = stateType;
		this.bpelElement = bpelElement;
	}
	
	public void setStateType(BPELStateEnum state) {
		this.stateType = state;
	}
	
	public BPELStateEnum getStateType() {
		return stateType;
	}
	
	public EObject getBPELElement() {
		return this.bpelElement;
	}
	
	public void setBPELElement(EObject bpelElement){
		this.bpelElement = bpelElement;
	}

}
