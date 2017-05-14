package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * Describes the time point in a point algebra.
 * 
 * @author Jonas Scheurich
 *
 */
public class PAVariable extends CSPVariable {
	
	private TimePointDesc timePoint;
	public PAVariable(int id) {
		super(id);
	}
		
	public PAVariable(int id, Object bpelElement, TimePointDesc timePoint) {
		super(id, bpelElement);
		this.timePoint = timePoint;
	}
	
	public void setTimePoint(TimePointDesc timePoint) {
		this.timePoint = timePoint;
	}
	
	public TimePointDesc getTimePoint() {
		return timePoint;
	}
	
	
	@Override
	public String getStateName() {
		return timePoint.getState().name();
	}
	
	
	@Override
	public String getName() {		
		return this.getBpelName() + timePoint.getState().name() + "_" + timePoint.getTimeType().name();
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

}
