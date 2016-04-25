package de.uni_stuttgart.iaas.bpel.equivalence.console;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.BpelEquivalence;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence.ProcessDifference;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELUtils;

public class BpelEquivalenceConsole {

	public static void main(String[] args) {
		
		if(args.length != 2) return;
		
		// read models
		EObject process1 = (EObject) BPELUtils.readProcessFromFile(args[0]);
		EObject process2 = (EObject) BPELUtils.readProcessFromFile(args[1]);
		List<String> activities = new ArrayList<String>();
		for (int i = 2; i < args.length; i++) {
			activities.add(args[i]);
		}
		
		System.out.printf("Check execution equivalence:\n%s\n%s\n\n", args[0], args[1]);
		
		BpelEquivalence equivalence = new BpelEquivalence();
		PANetwork network1 = equivalence.createNetwork(process1);
		PANetwork network2 = equivalence.createNetwork(process2);
		
		ProcessDifference difference = equivalence.checkBpelEquivalence(network1, network2, activities);
		
		if (difference.isEquals()) {
			System.out.println("The processes are equals");
		}
		else {
			System.out.println("The processes are not equals");
			System.out.println("Unequal activites: " + difference.getUnexpectedStates());
			System.out.println("Unequal execution: " + difference.getUnequalConstraints());
		}
	}

}
