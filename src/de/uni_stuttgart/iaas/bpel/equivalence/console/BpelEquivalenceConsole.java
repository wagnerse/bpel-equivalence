package de.uni_stuttgart.iaas.bpel.equivalence.console;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.BpelEquivalence;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELResourceUtils;

public class BpelEquivalenceConsole {

	public static void main(String[] args) {
		
		if(args.length != 2) return;
		
		//TODO read models
		EObject process1 = (EObject) BPELResourceUtils.readProcessFromFile(args[0]);
		EObject process2 = (EObject) BPELResourceUtils.readProcessFromFile(args[1]);
		
		BpelEquivalence equivalence = new BpelEquivalence();
		NetworkSolver network1 =equivalence.createNetwork(process1);
		NetworkSolver network2 =equivalence.createNetwork(process2);
		
		boolean equal = equivalence.checkBpelEquivalence(network1, network2);
		
	}

}
