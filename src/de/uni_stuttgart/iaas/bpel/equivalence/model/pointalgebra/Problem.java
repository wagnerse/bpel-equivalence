package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;

public class Problem {
	
	private List<Variable> variables = new ArrayList<Variable>();
	private Map<Pair<Variable, Variable>, Constraint> constraints = new HashMap<Pair<Variable, Variable>, Constraint>();
	
	public Problem() {
	}
	
	public Variable createVariable(EObject bpelElement, BPELStateEnum timeState, TimeTypeEnum timeType) {
		return this.createVariable(bpelElement, new TimePointDesc(timeState, timeType));
	}
	
	public Variable createVariable(EObject bpelElement, TimePointDesc timePoint) {
		Variable variable = new Variable(bpelElement, timePoint);
		this.variables.add(variable);
		return variable;
	}
	
	public void addConstraints(Constraint... constraints) {
		for (Constraint c: constraints) {
			this.addConstraint(c);
		}
	}
	
	public void addConstraint(Constraint constraint) {
		Pair<Variable, Variable> key = new ImmutablePair<Variable, Variable>(constraint.getFrom(),
				constraint.getTo());
		if (!this.constraints.containsKey(key)) {
			this.constraints.put(key, constraint);
		}
		else {
			this.constraints.get(key).addRelations(constraint.getRelations());
		}
	}
	
	public Collection<Constraint> getConstraints() {
		return this.constraints.values();
	}

	public Collection<Variable> getVariables() {
		return this.variables;
	}

	public Constraint getConstraints(Variable vl, Variable vr) {
		Pair<Variable, Variable> key = new ImmutablePair<Variable, Variable>(vl, vr);
		if (this.constraints.containsKey(key)) {
			return this.constraints.get(key);
		}
		else {
			return null;
		}
	}

}
