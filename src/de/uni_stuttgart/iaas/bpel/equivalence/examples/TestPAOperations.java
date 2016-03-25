package de.uni_stuttgart.iaas.bpel.equivalence.examples;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PASolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;

public class TestPAOperations {

	public static void main(String[] args) {
		PANetwork problem = new PANetwork(new PASolver());

		PAVariable v1 = problem.createVariable(null, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START)); // id
		PAVariable v2 = problem.createVariable(null, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START)); // id
		PAVariable v3 = problem.createVariable(null, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START)); // id
																													// 2

		PAConstraint c1a = new PAConstraint(v1, v2, RelationEnum.UNRELATED, RelationEnum.EQUALS);
		PAConstraint c1b = PAConstraint.newTConstraint(v1, v2);
		PAConstraint c1c = new PAConstraint(v1, v2, RelationEnum.LESS);
		PAConstraint c1d = new PAConstraint(v1, v2, RelationEnum.UNRELATED);
		
		PAConstraint c2a = new PAConstraint(v2, v3, RelationEnum.UNRELATED, RelationEnum.EQUALS);
		PAConstraint c2b = PAConstraint.newTConstraint(v2, v3);
		PAConstraint c2c = new PAConstraint(v2, v3, RelationEnum.LESS);
		
		System.out.println("Test cut");
		System.out.println("Cut c1a with c1b" + c1a.cut(c1b));
		
		System.out.println("\nTest compose 1");
		System.out.println("Compose ||,= with ||,=: " + c1a.compose(c2a));
		System.out.println("Compose ||,= with T: " + c1a.compose(c2b));
		System.out.println("Compose T with ||,=: " + c1b.compose(c2a));
		System.out.println("Compose T with T: " + c1b.compose(c2b));
		System.out.println("Compose < with ||,=: " + c1c.compose(c2a));
		System.out.println("Compose ||,= with <: " + c1a.compose(c2c));
		System.out.println("Compose < with <: " + c1c.compose(c2c));
		System.out.println("Compose || with ||,=: " + c1d.compose(c2a));
	}
}
