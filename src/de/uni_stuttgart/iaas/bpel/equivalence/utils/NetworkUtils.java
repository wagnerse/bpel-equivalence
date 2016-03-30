package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PANetwork;

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
	public static void saveAsCSV(PANetwork network, File file) throws IOException {

		FileWriter writer = new FileWriter(file);

		// write file options
		writer.append("sep=,\n");

		// write head line;
		writer.append(",");
		for (PAVariable v : network.getVariables()) {
			writer.append(((PAVariable) v).getName() + ", ");
		}
		writer.append("\n");

		// write data
		for (PAVariable vl : network.getVariables()) {
			// write state name
			
			if (vl instanceof PAVariable) {
				writer.append(((PAVariable) vl).getName() + ", ");
				// write data
				for (PAVariable vr : network.getVariables()) {
					// constraints 
					PAConstraint constraint = network.getConstraint(vl, vr);
					if (constraint == null){
						PAConstraint constraintToRev = network.getConstraint(vr, vl);
						if (constraintToRev != null) {
							constraint = constraintToRev.revert();
						}
					}
					if (constraint != null) {
						writer.append(constraint.relationsToString());
					}
					writer.append(", ");
				} 
			}
			// end of line
			writer.append("\n");
		}

		// finish
		writer.flush();
		writer.close();
	}

}
