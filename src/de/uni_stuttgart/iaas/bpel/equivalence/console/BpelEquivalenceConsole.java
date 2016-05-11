package de.uni_stuttgart.iaas.bpel.equivalence.console;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.BpelEquivalence;
import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence.EqualsConfiguration;
import de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence.ProcessDifference;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELUtils;

public class BpelEquivalenceConsole {

	/**
	 * Parameter
	 * 
	 * <path process 1> <path process 2> [-s]{<state>} [-a]{<activity name>}
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length <= 2) return;
		
		// read models
		EObject process1 = (EObject) BPELUtils.readProcessFromFile(args[0]);
		EObject process2 = (EObject) BPELUtils.readProcessFromFile(args[1]);
		List<BPELStateEnum> states = new ArrayList<BPELStateEnum>();
		List<String> activities = new ArrayList<String>();
		boolean readState = false;
		boolean readActivity = false;
		for (int i = 2; i < args.length; i++) {
			if (args[i].equals("-s")) {
				readState = true;
			}
			else if (args[i].equals("-a")) {
				readState = false;
				readActivity = true;
			}
			else if (readState && BPELStateEnum.fromString(args[i]) != null) {
				states.add(BPELStateEnum.fromString(args[i]));
			}
			else if (readActivity) {
				activities.add(args[i]);
			}
		}
		
		System.out.printf("Check execution equivalence:\n%s\n%s\n\n", args[0], args[1]);
		
		// create point algebra networks
		BpelEquivalence equivalence = new BpelEquivalence();
		PANetwork network1 = equivalence.createNetwork(process1);
		PANetwork network2 = equivalence.createNetwork(process2);
		
		// check equivalence
		System.out.println("Start equivalence checking");
		EqualsConfiguration config = new EqualsConfiguration();
		if (states.size() != 0) {
			config.getStates().addAll(states);
		}
		else {
			config.addState(BPELStateEnum.EXECUTING);
		}
		config.getActivities().addAll(activities);
		ProcessDifference difference = equivalence.checkBpelEquivalence(network1, network2, config);
		
		// print result;
		System.out.println();
		
		if (difference.isEquals()) {
			System.out.println("The processes are equals");
		}
		else {
			System.out.println("The processes are not equals");
			if (difference.getUnexpectedStates().size() > 0) {
				System.out.println("Unequal activites: " + difference.getUnexpectedStates());
			}
			if (difference.getUnequalConstraints().size() > 0) {
				System.out.println("Unequal execution: " + difference.getUnequalConstraints());
			}
		}
	}

}
