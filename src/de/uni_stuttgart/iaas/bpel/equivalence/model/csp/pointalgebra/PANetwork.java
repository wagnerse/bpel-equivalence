package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateInstance;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;

/**
 * A point algebra network contains constraints and time points
 * and holds a point algebra solver
 * 
 * @author Jonas Scheurich
 *
 */
public class PANetwork extends CSPNetwork{
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private PASolver solver;
	private int idCount = 0;
	
	
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
		
		super.addVariable(newVariable);
		createFullConstraints(newVariable);
		return newVariable;
	}
	
	/**
	 * Return the variable for a given {@link EObject} and a given {@link TimePointDesc}.
	 * @param bpelElement
	 * @param timeState
	 * @param timeType
	 * @return null if no variable is available.
	 */
	public PAVariable getVariable(EObject bpelElement, BPELStateEnum timeState, TimeTypeEnum timeType) {
		TimePointDesc desc = new TimePointDesc(timeState, timeType);
		for (CSPVariable v: this.getVariables()) {
			if (((PAVariable) v).getBpelElement().equals(bpelElement) && ((PAVariable) v).getTimePoint().equals(desc)) {
				return (PAVariable) v;
			}
		}
		return null;
	}
		
	/**
	 * Create a collection of {@link PAVariable} pairs.
	 * A pair contains the start and end point of a BPEL state (see {@link BPELStateEnum}).
	 * left value: start point
	 * right value: end point
	 * @return
	 */
	public Collection<BPELStateInstance> getVariablePairs() {
		List<BPELStateInstance> list = new LinkedList<BPELStateInstance>();
		
		for(CSPVariable v_start: this.getVariables()) {
			if (((PAVariable) v_start).getTimePoint().getTimeType() == TimeTypeEnum.END) continue;
			
			PAVariable v_end = this.getVariable(
					((PAVariable) v_start).getBpelElement(), 
					((PAVariable) v_start).getTimePoint().getState(), 
					TimeTypeEnum.END);
			if (v_end != null) {
				BPELStateInstance state = new BPELStateInstance(
						((PAVariable) v_start).getBpelElement(),
						((PAVariable) v_start).getTimePoint().getState(),
						(PAVariable)  v_start, 
						(PAVariable) v_end);
				list.add(state);
			}
			else {
				LOGGER.log(Level.WARNING, "Could not found variable for end time point of " + ((PAVariable) v_start).getName());
			}
		}
		
		return list;
	}
	
	/**
	 * Create T constraints (containing all relations) between a given variable
	 * an all other variables of the problem network.
	 * @param variable
	 */
	private void createFullConstraints(CSPVariable variable) {
		for (CSPVariable v: this.getVariables()) {
			if (!v.equals(variable) && this.getTwoWayConstraint(variable, (PAVariable) v) == null) {
				PAConstraint newConstraint = this.newTConstraint(variable, v);
				this.addConstraint(newConstraint);
			}
			else if (v.equals(variable)) {
				// create self constraint: v = v
				this.addConstraint(new PAConstraint((PAVariable) variable, (PAVariable) variable, 
						RelationEnum.EQUALS));
			}
		}
	}

	/**
	 * Calculate the cut of this constraint with a second This constraint will
	 * be changed.
	 * 
	 * @param c
	 * @return
	 */
	@Override
	public PAConstraint newTConstraint(CSPVariable from, CSPVariable to) {
		if (!(from instanceof PAVariable) || !(to instanceof PAVariable)) return null;
		
		PAConstraint tConstraint = new PAConstraint((PAVariable) from, (PAVariable) to, 
				RelationEnum.LESS, 
				RelationEnum.EQUALS, 
				RelationEnum.UNRELATED,
				RelationEnum.GREATER);
		
		return tConstraint;
	}

	
}
