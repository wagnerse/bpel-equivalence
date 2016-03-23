package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import java.util.LinkedList;

import org.metacsp.framework.Constraint;
import org.metacsp.framework.ConstraintSolver;
import org.metacsp.framework.Variable;

public class PASolver extends ConstraintSolver{

	private static final long serialVersionUID = -1318154361840997199L;
	
	private Problem problem;

	public PASolver() {
		super(new Class[]{PAConstraint.class}, PAVariable.class);
		this.setOptions(OPTIONS.MANUAL_PROPAGATE);
	}

	/**
	 * @return
	 */
	@Override
	public boolean propagate() {
		if (this.getProblem() == null) return false;
		
		LinkedList<Constraint> queue = new LinkedList<Constraint>();
		
		// initialize queue
		for (Constraint c: getProblem().getConstraints()) {
			if (c instanceof PAConstraint) {
				queue.addLast(c);
			}
		}
				
		while(!queue.isEmpty()) {
			PAConstraint c = (PAConstraint) queue.getFirst();
			queue.removeFirst();
			
			PAVariable from = (PAVariable) c.getFrom();
			PAVariable to = (PAVariable) c.getTo();
			for(Variable intermediate: getProblem().getVariables()) {
				// skip to and from
				if (intermediate.equals(from) || intermediate.equals(to)) continue;
				
				// get the 3-K constraints
				PAConstraint c1 = (PAConstraint) getProblem().getTwoWayConstraint(intermediate, from);
				PAConstraint c2 = (PAConstraint) getProblem().getTwoWayConstraint(to, intermediate);
				//System.out.println("\nStart probagation for " + c + " with " + c1 + ", " + c2);
				
				//create a constraint if necessary
				if (c1 == null || c2 == null) return false;
				
				//check direction of the constraints c and c2, to match c1
				PAConstraint a1 = selectConstraintA(c1.getFrom(), c, c2);
				PAConstraint b1 = selectConstraintB(c1.getTo(), c, c2);
				//check v1 -c1- v2
				PAConstraint comp1 = a1.compose(b1);
				//System.out.println("Probagate " + a1 + " with " + b1 + " :: Comp: " + comp1);
				PAConstraint temp1 = c1.cut(comp1);
				//System.out.println("c1: " + c1.toString() + " vs. " + temp1.toString());
				if (!temp1.equals(c1)) {
					queue.addLast(getProblem().reduceTwoWayConstraint(temp1, true));
				}
				
				//check direction of the constraints c1 and c, to match c2
				PAConstraint a2 = selectConstraintA(c2.getFrom(), c1, c);
				PAConstraint b2 = selectConstraintB(c2.getTo(), c1, c);
				//check v2 -c2- v3
				//System.out.println("Probagate " + a2 + " with " + b2);
				PAConstraint comp2 = a2.compose(b2);
				PAConstraint temp2 = c2.cut(comp2);
				//System.out.println("c2: " + c2.toString() + " vs. " + temp2.toString());
				if (!temp2.equals(c2)) {
					queue.addLast(getProblem().reduceTwoWayConstraint(temp2, true));
				}
			}
		}
		return true;
	}
	
	private PAConstraint selectConstraintA(Variable from, PAConstraint c1, PAConstraint c2) {
		if (from.equals(c1.getFrom())) {
			return c1;
		}
		else if (from.equals(c1.getTo())) {
			return c1.revert();
		}
		else if (from.equals(c2.getFrom())) {
			return c2;
		}
		else if (from.equals(c2.getTo())) {
			return c2.revert();
		}
		else {
			throw new IllegalStateException("No selection found");
		}
	}
	
	private PAConstraint selectConstraintB(Variable to, PAConstraint c1, PAConstraint c2) {
		if (to.equals(c1.getTo())) {
			return c1;
		}
		else if (to.equals(c1.getFrom())) {
			return c1.revert();
		}
		else if (to.equals(c2.getTo())) {
			return c2;
		}
		else if (to.equals(c2.getFrom())) {
			return c2.revert();
		}
		else {
			throw new IllegalStateException("No selection found");
		}
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
	public Problem getProblem() {
		return this.problem;
	}

	/**
	 * This procedure runs before some constraints are added
	 * @return true, if the constraints could be added.
	 */
	@Override
	protected boolean addConstraintsSub(org.metacsp.framework.Constraint[] constraints) {
		
		for (Constraint basicConstraint: constraints) {
			if (!(basicConstraint instanceof PAConstraint))return false;			
		}
		
		return true;
	}

	/**
	 * This procedure runs before some constraints are removed
	 */
	@Override
	protected void removeConstraintsSub(org.metacsp.framework.Constraint[] c) {
		// not implemented
	}

	/**
	 * This procedure create some variables
	 * @return array of {@link PAVariable}s
	 */
	@Override
	protected org.metacsp.framework.Variable[] createVariablesSub(int num) {
		PAVariable[] ret = new PAVariable[num];
		for (int i = 0; i < num; i++) {
			ret[i] = new PAVariable(IDs++, this);
		}
		
		return ret;
	}

	/**
	 * This procedure runs before some variables are removed
	 */
	@Override
	protected void removeVariablesSub(org.metacsp.framework.Variable[] v) {
		// not implemented
	}

	@Override
	public void registerValueChoiceFunctions() {
		// not implemented
	}
}
