package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;

public class NetworkUtils {

	public static void saveAsCSV(Problem network, File file) throws IOException {

		FileWriter writer = new FileWriter(file);

		// write file options
		writer.append("sep=,\n");

		// write head line;
		writer.append(",");
		for (Variable v : network.getVariables()) {

			writer.append(v.getName() + ", ");

		}
		writer.append("\n");

		// TODO write data
		for (Variable vl : network.getVariables()) {
			// write state name
			writer.append(vl.getName() + ", ");

			// write data
			for (Variable vr : network.getVariables()) {
				if (vr instanceof Variable) {
					// constraints list
					for (Constraint constr : network.getConstraints(vl, vr)) {
						for (RelationEnum type : constr.getRelations()) {
							writer.append(type.name() + " ");
						}
					}
					writer.append(", ");
				}
			}
			
			// end of line
			writer.append("\n");
		}

		// finsih
		writer.flush();
		writer.close();
	}

}
