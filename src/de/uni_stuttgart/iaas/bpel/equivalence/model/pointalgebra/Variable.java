package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class Variable {
	
	public enum StateTime {START, END};
	
	private BPELStateEnum stateType;
	private EObject bpelElement;
	private StateTime stateTime;
	
	public Variable(EObject bpelElement, BPELStateEnum stateType, StateTime stateTime) {
		this.bpelElement = bpelElement;
		this.stateType = stateType;
		this.stateTime = stateTime;
	}
	
	public EObject getBpelElement() {
		return bpelElement;
	}
	
	public StateTime getStateTime() {
		return stateTime;
	}
	
	public BPELStateEnum getStateType() {
		return stateType;
	}
	
	public String getName() {
		Object nameAttr = EMFUtils.getAttributeByName(bpelElement, "name");
		String bpelName = (nameAttr instanceof String)? (String) nameAttr : "";
		
		return bpelName + stateType.toString();
	}

}
