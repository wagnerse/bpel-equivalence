package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable.StateTime;

public class Problem {
	
	public Problem() {
	}
	
	public Variable createVariable(EObject bpelElement, BPELStateEnum stateType, StateTime stateTime) {
		Variable variable = new Variable(bpelElement, stateType, stateTime);
		//TODO store in internal representation
		return variable;
	}
	
	public void addConstraints(Constraint... constraints) {
		//TODO store in internal representation
	}
	
	public void addConstraint(Constraint constraint) {
		//TODO store in internal representation
	}
	
	public Constraint[] getConstraints() {
		//TODO implement
		return null;
	}

	public Variable[] getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	public Constraint[] getConstraints(Variable vl, Variable vr) {
		// TODO Auto-generated method stub
		return null;
	}

}
