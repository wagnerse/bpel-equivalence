package de.uni_stuttgart.iaas.bpel.equivalence.examples;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra.complexity.AllenComplexity;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PASolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;

public class TestPAOperations {

	public static void main(String[] args) {

		try {
			AllenComplexity comp = new AllenComplexity();
			FileWriter writer = new FileWriter(new File(args[0]));

			// write file options
			writer.append("sep=,\n");
			
			// write first line
			writer.append(",");
			for (HashSet<RelationEnum> rel : comp.getGammaX()) {
				List<RelationEnum> relList = new ArrayList<RelationEnum>(rel);
				PAConstraint c = new PAConstraint(relList);
				writer.append(c.relationsToString() + ", ");
			}
			writer.append("\n");

			// create content
			PANetwork problem = new PANetwork(new PASolver());

			PAVariable v1 = problem.createVariable(null, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START)); // id
			PAVariable v2 = problem.createVariable(null,
					new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START)); // id
			PAVariable v3 = problem.createVariable(null, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START)); // id
																														// 2
			for (HashSet<RelationEnum> rel1 : comp.getGammaX()) {
				List<RelationEnum> rel1List = new ArrayList<RelationEnum>(rel1);
				PAConstraint c1 = new PAConstraint(rel1List);
				c1.setFrom(v1);
				c1.setTo(v2);
				
				writer.append(c1.relationsToString() + ", ");

				for (HashSet<RelationEnum> rel2 : comp.getGammaX()) {
					List<RelationEnum> rel2List = new ArrayList<RelationEnum>(rel2);
					PAConstraint c2 = new PAConstraint(rel2List);
					c2.setFrom(v2);
					c2.setTo(v3);

					// compose constraints
					PAConstraint composition = c1.compose(c2);
					writer.append(composition.relationsToString() + ", ");
				}
				writer.append("\n");
			}

			// finish
			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
