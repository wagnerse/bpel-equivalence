package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import org.metacsp.framework.Domain;
import org.metacsp.framework.Variable;

public class SimpleTimePoint extends Domain {

	private static final long serialVersionUID = 9077006583085149727L;
	
	private Variable variable;

	protected SimpleTimePoint(Variable v) {
		super(v);
		variable = v;
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof SimpleTimePoint) {
			SimpleTimePoint that = (SimpleTimePoint)o;
			return toString().compareTo(that.toString());
		}
		return 0;
	}

	@Override
	public String toString() {
		return "TimePoint" + variable.getID();
	}

}
