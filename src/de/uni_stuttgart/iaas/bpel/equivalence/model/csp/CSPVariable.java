package de.uni_stuttgart.iaas.bpel.equivalence.model.csp;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;



/**
 * Describes a variable in a csp network.
 * 
 * @author Jonas Scheurich
 *
 */
public abstract class CSPVariable {
	private int id;
	protected Object bpelElement;


	public CSPVariable(int id) {
		this.id = id;
	}
	
	public CSPVariable(int id, Object bpelElement) {
		this.id = id;
		this.bpelElement = bpelElement;

	}
	
	public int getID() {
		return id;
	} 
	
	public String getBpelName() {
		Object nameAttr = null;
		if (bpelElement instanceof EObject) {
			nameAttr = EMFUtils.getAttributeByName((EObject)bpelElement, "name");
		}
		
		
		String bpelName = (nameAttr instanceof String)? (String) nameAttr : "";
		
		return bpelName;
	}
	
	public abstract String getName();
	
	public abstract String getStateName();
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(61, 79)
				.append(id)
				.toHashCode();
	}

	public Object getBpelElement() {
		return bpelElement;
	}

	public void setBpelElement(Object bpelElement) {
		this.bpelElement = bpelElement;
	}

	
	
}
