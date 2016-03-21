package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.Constraint;
import org.metacsp.framework.ConstraintNetwork;
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
public class Problem extends ConstraintNetwork {

	private static final long serialVersionUID = -474348422217169204L;

	private PASolver solver;

	public Problem(PASolver solver) {
		super(solver);
		this.solver = solver;
		this.solver.setProblem(this);
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
		return this.createVariable(bpelElement, new TimePointDesc(timeState, timeType));
	}

	/**
	 * Create a variable for a BPEL element and a time point
	 * 
	 * @param bpelElement
	 * @param timePoint
	 * @return
	 */
	public PAVariable createVariable(EObject bpelElement, TimePointDesc timePoint) {
		PAVariable variable = (PAVariable) solver.createVariable();
		variable.setBpelElement(bpelElement);
		variable.setTimePoint(timePoint);

		return variable;
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
	public boolean reduceConstraint(PAConstraint newConstraint, boolean create) {

		// check contradiction
		if (newConstraint.getRelations().size() == 0) {
			throw new IllegalStateException("Contradiction in " + newConstraint.toString());
		}

		PAConstraint oldConstraint = (PAConstraint) this.getConstraint(
				newConstraint.getFrom(), 
				newConstraint.getTo());
		PAConstraint oldConstraintRev = (PAConstraint) this.getConstraint(
				newConstraint.getTo(),
				newConstraint.getFrom());

		// return if unavailable
		if (!create && oldConstraint == null && oldConstraintRev == null) {
			return false;
		}
		else {
			oldConstraint = PAConstraint.newTConstraint(
					(PAVariable) newConstraint.getFrom(), 
					(PAVariable) newConstraint.getTo());
		}

		// select constraint
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
					"Bidirected edge found " + oldConstraint.toString() + ", " + oldConstraintRev.toString());
		}

		// check relations
		for (RelationEnum rel : opNewConstraint.getRelations()) {
			if (!opOldConstraint.getRelations().contains(rel)) {
				return false;
			}
		}

		// reduce constraint
		opOldConstraint.setRelations(opNewConstraint.getRelations());

		return true;
	}

	public void mergeConstraints() {

		for (Variable v1 : this.getVariables()) {
			for (Variable v2 : this.getVariables()) {
				Constraint[] c = this.getConstraints(v1, v2);
				Constraint[] cRev = this.getConstraints(v2, v1);

				// TODO
			}
		}
	}
}
