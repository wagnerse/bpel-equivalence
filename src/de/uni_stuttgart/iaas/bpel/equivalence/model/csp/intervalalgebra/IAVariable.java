package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * Describes an interval in a interval network.
 * 
 * @author Jonas Scheurich
 *
 */
public class IAVariable extends CSPVariable {

	private EObject bpelElement;
	private BPELStateEnum state;

	public IAVariable(int id) {
		super(id);
	}
	
	public IAVariable(int id, EObject bpelElement, BPELStateEnum state) {
		super(id);
		this.bpelElement = bpelElement;
		this.state = state;
	}
	
	public EObject getBpelElement() {
		return bpelElement;
	}
	
	public void setBpelElement(EObject bpelElement) {
		this.bpelElement = bpelElement;
	}
	
	public void setState(BPELStateEnum state) {
		this.state = state;
	}
	
	public BPELStateEnum getState() {
		return state;
	}

	@Override
	public String getName() {
		Object nameAttr = EMFUtils.getAttributeByName(bpelElement, "name");
		String bpelName = (nameAttr instanceof String)? (String) nameAttr + this.getState().name(): "";
		
		return bpelName;
	}
	
	@Override
	public String toString() {
		if (bpelElement != null) {
			return this.getName();
		}
		else {
			return Integer.toString(this.getID());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IAVariable))
			return false;
		if (obj == this)
			return true;

		IAVariable rhs = (IAVariable) obj;
		return new EqualsBuilder()
				.append(bpelElement, rhs.bpelElement)
				.append(state, rhs.state)
				.isEquals();
	}

}
