package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class Variable {
	
	private TimePointDesc timePoint;
	private EObject bpelElement;
	
	public Variable(EObject bpelElement, TimePointDesc timePoint) {
		this.bpelElement = bpelElement;
		this.timePoint = timePoint;
	}
	
	public EObject getBpelElement() {
		return bpelElement;
	}
	
	public TimePointDesc getTimePoint() {
		return timePoint;
	}
	
	public String getName() {
		Object nameAttr = EMFUtils.getAttributeByName(bpelElement, "name");
		String bpelName = (nameAttr instanceof String)? (String) nameAttr : "";
		
		return bpelName + timePoint.getState().name() + timePoint.getTimeType().name();
	}

}
