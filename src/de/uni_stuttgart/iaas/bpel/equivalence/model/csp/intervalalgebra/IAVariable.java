package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class IAVariable extends CSPVariable {

	private EObject bpelElement;

	public IAVariable(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public EObject getBpelElement() {
		return bpelElement;
	}
	
	public void setBpelElement(EObject bpelElement) {
		this.bpelElement = bpelElement;
	}
	

	public String getName() {
		Object nameAttr = EMFUtils.getAttributeByName(bpelElement, "name");
		String bpelName = (nameAttr instanceof String)? (String) nameAttr : "";
		
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

}
