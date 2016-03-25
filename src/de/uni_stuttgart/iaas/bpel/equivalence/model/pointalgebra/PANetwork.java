package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;

/**
 * 
 * @author Jonas Scheurich
 *
 *         A Problem contains a point algebra network for BPEL processes
 */
public class PANetwork {

	private PASolver solver;
	private int idCount = 0;
	
	private List<PAVariable> variables = new LinkedList<PAVariable>();
	private Map<Pair<PAVariable, PAVariable>, PAConstraint> constraints = new HashMap<Pair<PAVariable, PAVariable>, PAConstraint>();

	public PANetwork(PASolver solver) {
		this.solver = solver;
		this.solver.setProblem(this);
	}
	
	public boolean probagate() {
		return this.solver.propagate();
	}

	/**
	 * Create a variable for a BPEL element and a time point
	 * 
	 * @param bpelElement
	 * @param timeState
	 * @param timeType
	 * @return
	 */
	public PAVariable createVariable(EObject bpelElement, BPELStateEnum timeState, TimeTypeEnum timeType) {
		PAVariable newVariable = this.createVariable(bpelElement, new TimePointDesc(timeState, timeType));
		createTConstraints(newVariable);		
		return newVariable;
	}

	/**
	 * Create a variable for a BPEL element and a time point
	 * 
	 * @param bpelElement
	 * @param timePoint
	 * @return
	 */
	public PAVariable createVariable(EObject bpelElement, TimePointDesc timePoint) {
		PAVariable newVariable = new PAVariable(idCount++);
		newVariable.setBpelElement(bpelElement);
		newVariable.setTimePoint(timePoint);
		
		this.variables.add(newVariable);
		createTConstraints(newVariable);
		return newVariable;
	}
	
	/**
	 * Create T constraints (containing all relations) between a given variable
	 * an all other variables of the problem network.
	 * @param variable
	 */
	private void createTConstraints(PAVariable variable) {
		for (PAVariable v: this.getVariables()) {
			if (!v.equals(variable) && this.getTwoWayConstraint(variable, v) == null) {
				PAConstraint newConstraint = PAConstraint.newTConstraint(variable, v);
				this.addConstraint(newConstraint);
			}
		}
	}

	public void addConstraint(PAConstraint c) {
		if (containsToWayConstraint(c.getFrom(), c.getTo())) {
			reduceTwoWayConstraint(c, true);
		}
		else {
			Pair<PAVariable, PAVariable> key = new MutablePair<PAVariable, PAVariable>(c.getFrom(), c.getTo());
			this.constraints.put(key, c);
		}
				
	}
	
	public void addConstraints(PAConstraint...constraints) {
		for (PAConstraint c: constraints) {
			addConstraint(c);
		}
	}

	/**
	 * Reduce a constraint
	 * 
	 * @param newConstraint
	 * @param create,
	 *            creates the constraint if unavailable
	 * @return
	 * @throws IllegalStateException
	 *             if the constraint is a contradiction (empty constraint)
	 * @throws IllegalStateException
	 *             if the constraint is multidirected (two constraints)
	 */
	public PAConstraint reduceTwoWayConstraint(PAConstraint newConstraint, boolean create) {

		// check contradiction
		if (newConstraint.getRelations().size() == 0) {
			throw new IllegalStateException("Contradiction in " + newConstraint.getName());
		}

		// get the old constraint in original and reversed direction
		// only one should be found.
		PAConstraint oldConstraint = this.getConstraint(
				newConstraint.getFrom(), 
				newConstraint.getTo());
		PAConstraint oldConstraintRev = this.getConstraint(
				newConstraint.getTo(),
				newConstraint.getFrom());

		// return if the constraint is unavailable
		if (oldConstraint == null && oldConstraintRev == null) {
			if (create) {
				// if create mode is selected, create a T transition
				oldConstraint = PAConstraint.newTConstraint(
						newConstraint.getFrom(), 
						newConstraint.getTo());
			}
			else {
				return null;
			}
		}

		// select original or reverted constraint
		// throw exception, if a original and a reversed constraint exists.
		PAConstraint opOldConstraint;
		PAConstraint opNewConstraint;
		if (oldConstraint != null && oldConstraintRev == null) {
			opOldConstraint = oldConstraint;
			opNewConstraint = newConstraint;
		} else if (oldConstraintRev != null && oldConstraint == null) {
			opOldConstraint = oldConstraintRev;
			opNewConstraint = newConstraint.revert();
		} else {
			throw new IllegalStateException(
					"Bidirected edge found " + oldConstraint.getName() + ", " + oldConstraintRev.getName());
		}

		// reduce constraint
		opOldConstraint.cutAdd(opNewConstraint);
		
		// return the reduced constraint.
		return opOldConstraint;
	}
	

	public boolean containsConstraint(PAVariable from, PAVariable to) {
		Pair<PAVariable, PAVariable> key = new MutablePair<PAVariable, PAVariable>(from, to);
		return this.constraints.containsKey(key);
	}
	
	public boolean containsToWayConstraint(PAVariable from, PAVariable to) {
		return this.containsConstraint(from, to) || this.containsConstraint(to, from);
	}
	
	public Collection<PAConstraint> getConstraints() {
		return this.constraints.values();
	}
	
	public boolean removeConstraint(PAConstraint c) {
		if (this.containsConstraint(c.getFrom(), c.getTo())) {
			Pair<PAVariable, PAVariable> key = new MutablePair<PAVariable, PAVariable>(c.getFrom(), c.getTo());
			this.constraints.remove(key);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean removeTwoWayConstraint(PAConstraint c) {
		return removeConstraint(c) || removeConstraint(c.revert());
	}

	public Collection<PAVariable> getVariables() {
		 return this.variables;
	}

	public PAConstraint getConstraint(PAVariable from, PAVariable to) {
		Pair<PAVariable, PAVariable> key = new MutablePair<PAVariable, PAVariable>(from, to);
		return this.constraints.get(key);

	}
	
	public PAConstraint getTwoWayConstraint(PAVariable from, PAVariable to) {
		PAConstraint c = (PAConstraint) this.getConstraint(from, to);
		PAConstraint cRev = (PAConstraint) this.getConstraint(to, from);
		
		if (c != null && cRev == null) return c;
		else if (cRev != null && c == null) return cRev.revert();
		else if (c == null && cRev == null) return null;
		else throw new IllegalStateException("Multiple constraints.");
	}
	
}
