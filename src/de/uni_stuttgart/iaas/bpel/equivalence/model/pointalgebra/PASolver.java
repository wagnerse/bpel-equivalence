package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import java.util.LinkedList;


public class PASolver {
	
	private PANetwork problem;

	public PASolver() {
		
	}

	/**
	 * @return
	 */
	public boolean propagate() {
		if (this.getProblem() == null) return false;
		
		LinkedList<PAConstraint> queue = new LinkedList<PAConstraint>();
		
		// initialize queue
		for (PAConstraint c: getProblem().getConstraints()) {
			queue.addLast(c);
		}
				
		while(!queue.isEmpty()) {
			PAConstraint c = queue.getFirst();
			queue.removeFirst();
			
			PAVariable from = c.getFrom();
			PAVariable to = c.getTo();
			for(PAVariable intermediate: getProblem().getVariables()) {
				// skip to and from
				if (intermediate.equals(from) || intermediate.equals(to)) continue;
				
				// get the 3-K constraints
				PAConstraint c1 = getProblem().getTwoWayConstraint(intermediate, from);
				PAConstraint c2 = getProblem().getTwoWayConstraint(to, intermediate);
				
				//create a constraint if necessary
				if (c1 == null || c2 == null) return false;
				
				//check direction of the constraints c and c2, to match c1
				PAConstraint a1 = selectConstraintA(c1.getFrom(), c, c2);
				PAConstraint b1 = selectConstraintB(c1.getTo(), c, c2);
				//check v1 -c1- v2
				PAConstraint temp1 = c1.cut(a1.compose(b1));
				if (temp1.getRelations().size() == 0) {
					throw new IllegalStateException("Contradiction with " + c1 + " cut( " + a1 + " O " + b1 + " )");
				}
				if (!temp1.equals(c1)) {
					queue.addLast(getProblem().reduceTwoWayConstraint(temp1, true));
				}
				
				//check direction of the constraints c1 and c, to match c2
				PAConstraint a2 = selectConstraintA(c2.getFrom(), c1, c);
				PAConstraint b2 = selectConstraintB(c2.getTo(), c1, c);
				//check v2 -c2- v3
				PAConstraint temp2 = c2.cut(a2.compose(b2));
				if (temp2.getRelations().size() == 0) {
					throw new IllegalStateException("Contradiction with " + c2 + " cut( " + a2 + " O " + b2 + " )");
				}
				if (!temp2.equals(c2)) {
					queue.addLast(getProblem().reduceTwoWayConstraint(temp2, true));
				}
			}
		}
		return true;
	}
	
	private PAConstraint selectConstraintA(PAVariable from, PAConstraint c1, PAConstraint c2) {
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
	
	private PAConstraint selectConstraintB(PAVariable to, PAConstraint c1, PAConstraint c2) {
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

	public void setProblem(PANetwork problem) {
		this.problem = problem;
	}
	
	public PANetwork getProblem() {
		return this.problem;
	}
}
