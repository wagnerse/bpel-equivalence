package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;

/**
 * Provide functionalities for {@link CSPNetwork} objects.
 * 
 * @author Jonas Scheurich
 */
public class NetworkUtils {

	/**
	 * Write a {@link CSPNetwork} into a csv file.
	 * 
	 * @param network
	 * @param file
	 * @throws IOException
	 */
	public static void saveAsCSV(CSPNetwork network, File file) throws IOException {

		FileWriter writer = new FileWriter(file);

		// write file options
		writer.append("sep=,\n");

		// write head line;
		writer.append(",");
		for (CSPVariable v : network.getVariables()) {
			writer.append(v.getName() + ", ");
		}
		writer.append("\n");

		// write data
		for (CSPVariable vl : network.getVariables()) {
			// write state name
			
			writer.append(vl.getName() + ", ");
			// write data
			for (CSPVariable vr : network.getVariables()) {
				// constraints 
				CSPConstraint constraint = network.getConstraint(vl, vr);
				if (constraint == null){
					CSPConstraint constraintToRev = network.getConstraint(vr, vl);
					if (constraintToRev != null) {
						constraint = constraintToRev.revert();
					}
				}
				if (constraint != null) {
					writer.append(constraint.valueToString());
				}
				writer.append(", ");
			} 
			
			// end of line
			writer.append("\n");
		}

		// finish
		writer.flush();
		writer.close();
	}

}
