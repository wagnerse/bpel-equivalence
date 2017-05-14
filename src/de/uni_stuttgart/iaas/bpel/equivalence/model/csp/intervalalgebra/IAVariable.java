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
	
	protected BPELStateEnum state;

	public IAVariable(int id) {
		super(id);
	}
	
	public IAVariable(int id, Object bpelElement, BPELStateEnum state) {
		super(id, bpelElement);
		this.state = state;

	}
	
	@Override
	public String getStateName() {
		return this.state.name();
	}
	
	@Override
	public String getName() {
		
		Object nameAttr = null;
		if (bpelElement instanceof EObject) {
			nameAttr = EMFUtils.getAttributeByName((EObject)bpelElement, "name");
		}
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
				.append(bpelElement, rhs.getBpelElement())
				.append(state, rhs.getState())
				.isEquals();
	}
	
	public void setState(BPELStateEnum state) {
		this.state = state;
	}

	public BPELStateEnum getState() {
		return state;
	}

}
