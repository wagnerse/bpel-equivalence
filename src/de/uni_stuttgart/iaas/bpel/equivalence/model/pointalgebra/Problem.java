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

/**
 * 
 * @author Jonas Scheurich
 *
 * A Problem contains a point algebra network for BPEL processes
 */
public class Problem {
	
	private List<Variable> variables = new ArrayList<Variable>();
	private Map<Pair<Variable, Variable>, Constraint> constraints = new HashMap<Pair<Variable, Variable>, Constraint>();
	
	public Problem() {
	}
	
	/**
	 * Create a variable for a BPEL element and a time point
	 * 
	 * @param bpelElement
	 * @param timeState
	 * @param timeType
	 * @return
	 */
	public Variable createVariable(EObject bpelElement, BPELStateEnum timeState, TimeTypeEnum timeType) {
		return this.createVariable(bpelElement, new TimePointDesc(timeState, timeType));
	}
	
	/**
	 * Create a variable for a BPEL element and a time point
	 * 
	 * @param bpelElement
	 * @param timePoint
	 * @return
	 */
	public Variable createVariable(EObject bpelElement, TimePointDesc timePoint) {
		Variable variable = new Variable(bpelElement, timePoint);
		this.variables.add(variable);
		return variable;
	}
	
	/**
	 * Add some constraints to the point algebra network.
	 * Unknown variables will be registered.
	 * 
	 * If a constraint exists the new and the old constraint will be disjuncted
	 * 
	 * @param constraints
	 */
	public void addConstraints(Constraint... constraints) {
		for (Constraint c: constraints) {
			this.addConstraint(c);
		}
	}
	
	/**
	 * Add a constraint to the point algebra network.
	 * Unknown variables will be registered.
	 * 
	 * If a constraint exists the new and the old constraint will be disjuncted
	 * 
	 * @param constraint
	 */
	public void addConstraint(Constraint constraint) {
		
		// if the variables are unknown, add the variables
		if (!this.variables.contains(constraint.getFrom())) {
			this.variables.add(constraint.getFrom());
		}
		if (!this.variables.contains(constraint.getTo())) {
			this.variables.add(constraint.getTo());
		}
		
		Pair<Variable, Variable> key = new ImmutablePair<Variable, Variable>(constraint.getFrom(),
				constraint.getTo());
		// if the constraint exists, add the relatoins to the existing constraint
		if (!this.constraints.containsKey(key)) {
			this.constraints.put(key, constraint);
		}
		else {
			this.constraints.get(key).addRelations(constraint.getRelations());
		}
	}
	
	/**
	 * Get all constraints of the point algebra network
	 * 
	 * @return
	 */
	public Collection<Constraint> getConstraints() {
		return this.constraints.values();
	}

	/**
	 * Get all variables of the point algebra network
	 * 
	 * @return
	 */
	public Collection<Variable> getVariables() {
		return this.variables;
	}

	/**
	 * Get the constraint between two variables.
	 * 
	 * @param vl
	 * @param vr
	 * @return
	 */
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
