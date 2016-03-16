package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Provide functionalities for activity networks
 *
 */
public class NetworkUtils {

	/**
	 * Save the activity network as csv representation.
	 * @param network
	 * @param file
	 * @throws IOException
	 */
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

		// write data
		for (Variable vl : network.getVariables()) {
			// write state name
			writer.append(vl.getName() + ", ");

			// write data
			for (Variable vr : network.getVariables()) {
				if (vr instanceof Variable) {
					// constraints 
					Constraint constraint =  network.getConstraints(vl, vr);
					if (constraint != null) {
						writer.append(constraint.relationsToString());
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
