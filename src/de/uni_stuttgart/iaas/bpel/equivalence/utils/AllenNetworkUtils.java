package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.metacsp.framework.Constraint;
import org.metacsp.framework.Variable;
import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalConstraint;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;

public class AllenNetworkUtils {

	public static void saveAsCSV(NetworkSolver network, File file) throws IOException {

		FileWriter writer = new FileWriter(file);

		// write file options
		writer.append("sep=,\n");

		// write head line;
		writer.append(",");
		for (Variable v : network.getVariables()) {
			if (v instanceof ActivityState) {
				writer.append(((ActivityState) v).getName() + ", ");
			}
		}
		writer.append("\n");

		// TODO write data
		for (Variable vl : network.getVariables()) {
			if (vl instanceof ActivityState) {
				// write state name
				writer.append(((ActivityState) vl).getName() + ", ");

				// write data
				for (Variable vr : network.getVariables()) {
					if (vr instanceof ActivityState) {

						// constraints list
						for (Constraint constr : network.getConstraints(vl, vr)) {
							if (constr instanceof StateConstraint) {
								for (FuzzyAllenIntervalConstraint.Type type : ((StateConstraint) constr).getTypes()) {
									writer.append(type.name() + " ");
								}
							}
						}
						writer.append(", ");
					}
				}

				// end of line
				writer.append("\n");
			}
		}

		// finsih
		writer.flush();
		writer.close();
	}

}
