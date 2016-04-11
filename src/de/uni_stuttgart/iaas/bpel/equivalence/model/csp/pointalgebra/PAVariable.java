package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Describe the time point in a point algebra.
 *
 */
public class PAVariable extends CSPVariable {
	
	private TimePointDesc timePoint;
	private EObject bpelElement;
	
	public PAVariable(int id) {
		super(id);
	}
		
	public PAVariable(int id, EObject bpelElement, TimePointDesc timePoint) {
		super(id);
		this.bpelElement = bpelElement;
		this.timePoint = timePoint;
	}
	
	public void setBpelElement(EObject bpelElement) {
		this.bpelElement = bpelElement;
	}
	
	public EObject getBpelElement() {
		return bpelElement;
	}
	
	public void setTimePoint(TimePointDesc timePoint) {
		this.timePoint = timePoint;
	}
	
	public TimePointDesc getTimePoint() {
		return timePoint;
	}
	
	public String getName() {
		Object nameAttr = EMFUtils.getAttributeByName(bpelElement, "name");
		String bpelName = (nameAttr instanceof String)? (String) nameAttr : "";
		
		return bpelName + timePoint.getState().name() + "_" + timePoint.getTimeType().name();
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
		if (!(obj instanceof PAVariable))
			return false;
		if (obj == this)
			return true;

		PAVariable rhs = (PAVariable) obj;
		return new EqualsBuilder()
				.append(timePoint, rhs.timePoint)
				.append(bpelElement, rhs.bpelElement)
				.isEquals();
	}

	/*@Override
	public int compareTo(org.metacsp.framework.Variable arg0) {
		if (!(arg0 instanceof PAVariable)) throw new IllegalArgumentException();
		int compareAct = this.getName().compareTo(((PAVariable) arg0).getName());
		if (compareAct == 0) {
			return this.timePoint.compareTo(((PAVariable) arg0).getTimePoint());
		}
		else {
			return compareAct;
		}
	}*/
}
