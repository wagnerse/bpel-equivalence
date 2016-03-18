package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.Domain;

import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Describe the time point in a point algebra.
 *
 */
public class PAVariable extends org.metacsp.framework.Variable {

	private static final long serialVersionUID = -3160371602789326199L;
	
	private TimePointDesc timePoint;
	private EObject bpelElement;
	private Domain domain;
	
	public PAVariable(int id, PASolver solver) {
		super(solver, id);
		setDomain(new SimpleTimePoint(this));
	}
	
	public PAVariable(int id, PASolver solver, EObject bpelElement, TimePointDesc timePoint) {
		super(solver, id);
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
		
		return bpelName + timePoint.getState().name() + timePoint.getTimeType().name();
	}
	
	@Override
	public String toString() {
		return Integer.toString(this.getID());
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

	@Override
	public int compareTo(org.metacsp.framework.Variable arg0) {
		if (!(arg0 instanceof PAVariable)) throw new IllegalArgumentException();
		int compareAct = this.getName().compareTo(((PAVariable) arg0).getName());
		if (compareAct == 0) {
			return this.timePoint.compareTo(((PAVariable) arg0).getTimePoint());
		}
		else {
			return compareAct;
		}
	}

	@Override
	public Domain getDomain() {
		return domain;
	}

	@Override
	public void setDomain(Domain d) {
		this.domain = d;
	}
}
