package de.uni_stuttgart.iaas.bpel.equivalence.examples;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PASolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;

public class TestPointAlgebra {

	public static void main(String[] args) {
		PANetwork problem = new PANetwork(new PASolver());
		
		PAVariable v1 = problem.createVariable(null, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START)); //id 0
		PAVariable v2 = problem.createVariable(null, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START)); //id 1
		PAVariable v3 = problem.createVariable(null, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START)); //id 2

		System.out.println("#var: " + problem.getVariables().size() + " #const: " + problem.getConstraints().size() + "\n");

		PAConstraint c1 = new PAConstraint(v1, v2, RelationEnum.UNRELATED, RelationEnum.EQUALS);
		
		PAConstraint c2 = new PAConstraint(v1, v3, RelationEnum.UNRELATED, RelationEnum.EQUALS);
		
		problem.addConstraints(c1);
		problem.addConstraints(c2);
		
		
		PAConstraint c3 = (PAConstraint) problem.getTwoWayConstraint(v2,  v3);
		
		System.out.println("\nConstraints created");
		System.out.println("c1 " + c1.toString());
		System.out.println("c2 " + c2.toString());
		System.out.println("c3 (probagated) " + c3.toString());

	}

}
