package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import java.util.Arrays;
import java.util.LinkedList;

import org.metacsp.framework.Constraint;
import org.metacsp.framework.ConstraintSolver;
import org.metacsp.framework.Variable;

public class PASolver extends ConstraintSolver{

	private static final long serialVersionUID = -1318154361840997199L;
	
	private Problem problem;

	public PASolver() {
		super(new Class[]{PAConstraint.class}, PAVariable.class);
		this.setOptions(OPTIONS.AUTO_PROPAGATE);
	}

	/**
	 * @return
	 */
	@Override
	public boolean propagate() {
		if (this.problem == null) return false;
		
		LinkedList<Constraint> queue = new LinkedList<Constraint>();
		
		// initialize queue
		for (Constraint c: problem.getConstraints()) {
			if (c instanceof PAConstraint) {
				queue.addLast(c);
			}
		}
		if (queue.size() == 0) return true;
		//FIXME problem contains no constraints: theNetwork and problem are different?
		while(queue.isEmpty()) {
			PAConstraint c = (PAConstraint) queue.getFirst();
			queue.removeFirst();
			
			PAVariable from = (PAVariable) c.getFrom();
			PAVariable to = (PAVariable) c.getTo();
			for(Variable intermediate: problem.getVariables()) {
				PAConstraint c1 = (PAConstraint) problem.getConstraint(from, intermediate);
				PAConstraint c2 = (PAConstraint) problem.getConstraint(intermediate, to);
				
				//create a constraint if necessary
				if (c1 == null) {
					c1 = PAConstraint.newTConstraint(from, (PAVariable) intermediate);
				}
				if (c2 == null) {
					c2 = PAConstraint.newTConstraint((PAVariable) intermediate, to);
				}
				
				//check direction of the constraints
				if (!c1.getFrom().equals(from)) {
					c1 = c1.invert();
				}
				if (!c2.getTo().equals(to)) {
					c2 = c2.invert();
				}
				
				//check v1 -c1- v2
				PAConstraint temp1 = c1.cut(c.compose(c2));
				if (!temp1.equals(c1)) {
					problem.reduceConstraint(temp1);
					queue.addLast(temp1);
				}
				
				//check v2 -c2- v3
				PAConstraint temp2 = c2.cut(c1.compose(c));
				if (!temp2.equals(c2)) {
					problem.reduceConstraint(temp2);
					queue.addLast(temp2);
				}
			}
		}
		return true;
	}
	

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@Override
	protected boolean addConstraintsSub(org.metacsp.framework.Constraint[] c) {
		// not implemented
		return true;
	}

	@Override
	protected void removeConstraintsSub(org.metacsp.framework.Constraint[] c) {
		// not implemented
	}

	@Override
	protected org.metacsp.framework.Variable[] createVariablesSub(int num) {
		PAVariable[] ret = new PAVariable[num];
		for (int i = 0; i < num; i++) {
			ret[i] = new PAVariable(IDs++, this);
		}
		
		return ret;
	}

	@Override
	protected void removeVariablesSub(org.metacsp.framework.Variable[] v) {
		// not implemented
	}

	@Override
	public void registerValueChoiceFunctions() {
		// not implemented
	}
}
