package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.Constraint;
import org.metacsp.framework.Variable;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;

/**
 * 
 * @author Jonas Scheurich
 *
 *         A Problem contains a point algebra network for BPEL processes
 */
public class Problem {

	private PASolver solver;

	public Problem(PASolver solver) {
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
		PAVariable newVariable = (PAVariable) solver.createVariable();
		newVariable.setBpelElement(bpelElement);
		newVariable.setTimePoint(timePoint);
		createTConstraints(newVariable);
		return newVariable;
	}
	
	/**
	 * Create T constraints (containing all relations) between a given variable
	 * an all other variables of the problem network.
	 * @param variable
	 */
	private void createTConstraints(PAVariable variable) {
		for (Variable v: this.getVariables()) {
			if (!v.equals(variable) && this.getTwoWayConstraint(variable, v) == null) {
				PAConstraint newConstraint = PAConstraint.newTConstraint(variable, (PAVariable) v);
				this.addConstraint(newConstraint);
			}
		}
	}
	
	public boolean containsConstraint(Variable from, Variable to) {
		Constraint constraints = this.getConstraint(from, to);
		if (constraints != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean containsToWayConstraint(Variable from, Variable to) {
		Constraint constraints = this.getTwoWayConstraint(from, to);
		if (constraints != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean addConstraint(PAConstraint c) {
		if (containsToWayConstraint(c.getFrom(), c.getTo())) {
			reduceTwoWayConstraint(c, true);
			return true; //return this.solver.propagate();
		}
		else {
			return this.solver.addConstraint(c);
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
		PAConstraint oldConstraint = (PAConstraint) this.getConstraint(
				newConstraint.getFrom(), 
				newConstraint.getTo());
		PAConstraint oldConstraintRev = (PAConstraint) this.getConstraint(
				newConstraint.getTo(),
				newConstraint.getFrom());

		// return if the constraint is unavailable
		if (oldConstraint == null && oldConstraintRev == null) {
			if (create) {
				// if create mode is selected, create a T transition
				oldConstraint = PAConstraint.newTConstraint(
						(PAVariable) newConstraint.getFrom(), 
						(PAVariable) newConstraint.getTo());
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
	
	public Constraint[] getConstraints() {
		return this.solver.getConstraints();
	}
	
	public void removeConstraint(Constraint c) {
		this.solver.removeConstraint(c);
	}

	public Variable[] getVariables() {
		 return this.solver.getVariables();
	}

	public Constraint getConstraint(Variable from, Variable to) {
		Constraint[] constraints = this.solver.getConstraints(from, to);
		if (constraints.length != 0) {
			return constraints[0];
		}
		else {
			return null;
		}
	}
	
	public PAConstraint getTwoWayConstraint(Variable from, Variable to) {
		PAConstraint c = (PAConstraint) this.getConstraint(from, to);
		PAConstraint cRev = (PAConstraint) this.getConstraint(to, from);
		
		if (c != null && cRev == null) return c;
		else if (cRev != null && c == null) return cRev.revert();
		else if (c == null && cRev == null) return null;
		else throw new IllegalStateException("Multiple constraints.");
	}
	
}
