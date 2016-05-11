package de.uni_stuttgart.iaas.bpel.equivalence.model;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * A BPEL state instance assigns the start and the end time point of a BPEL activity state 
 * to a state object and a BPEL activity object.
 * 
 * @author Jonas Scheurich
 *
 */
public class BPELStateInstance {
	
	private EObject activity;
	private BPELStateEnum state;
	private PAVariable start;
	private PAVariable end;
	
	public BPELStateInstance(EObject activity, BPELStateEnum state, PAVariable start, PAVariable end) {
		this.activity = activity;
		this.state = state;
		this.start = start;
		this.end = end;
	}
	
	public EObject getBpelElement() {
		return activity;
	}
	
	public String getBpelName() {
		Object nameAttr = EMFUtils.getAttributeByName(activity, "name");
		String bpelName = (nameAttr instanceof String)? (String) nameAttr : "";
		return bpelName;
	}
	
	public BPELStateEnum getState() {
		return state;
	}
	
	public PAVariable getStart() {
		return start;
	}
	
	public PAVariable getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return this.getBpelName();
	}
}
