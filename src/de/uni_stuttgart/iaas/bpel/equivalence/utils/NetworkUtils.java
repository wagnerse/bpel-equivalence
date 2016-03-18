package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.metacsp.framework.Variable;

import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;

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
			if (v instanceof PAVariable) {
				writer.append(((PAVariable) v).getName() + ", ");
			}
		}
		writer.append("\n");

		// write data
		for (Variable vl : network.getVariables()) {
			// write state name
			
			if (vl instanceof PAVariable) {
				writer.append(((PAVariable) vl).getName() + ", ");
				// write data
				for (Variable vr : network.getVariables()) {
					if (vr instanceof PAVariable) {
						// constraints 
						PAConstraint constraint = (PAConstraint) network.getConstraint(vl, vr);
						if (constraint != null) {
							writer.append(constraint.relationsToString());
						}
						writer.append(", ");
					}
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
