package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.ConstraintNetwork;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;

/**
 * 
 * @author Jonas Scheurich
 *
 * A Problem contains a point algebra network for BPEL processes
 */
public class Problem extends ConstraintNetwork{

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
	 * @param newConstraint
	 * @return
	 * @throws IllegalStateException if the constraint is a contradiction (empty constraint)
	 */
	public boolean reduceConstraint(PAConstraint newConstraint) {
		
		//check contradiction
		if (newConstraint.getRelations().size() == 0) {
			throw new IllegalStateException("Contradiction in " + newConstraint.toString());
		}
		
		PAConstraint oldConstraint = (PAConstraint) this.getConstraint(newConstraint.getFrom(), newConstraint.getTo());
		
		// return if unavailable
		if (oldConstraint == null) return false;
		
		//check relations
		for (RelationEnum rel: newConstraint.getRelations()) {
			if(!oldConstraint.getRelations().contains(rel)) {
				return false;
			}
		}
		
		//reduce constraint
		oldConstraint.setRelations(newConstraint.getRelations());
		
		return true;
	}
}
