package de.uni_stuttgart.iaas.bpel.equivalence.console;

import java.io.File;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.BpelEquivalence;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.NetworkUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELUtils;

public class PANetworkConsole {

	public static void main(String[] args) {
		if(!(0 < args.length && args.length  <= 2)) return;

		long start = System.currentTimeMillis();
		System.out.println("Create point algebra network for \n" + args[0]);
	
		EObject process1 = (EObject) BPELUtils.readProcessFromFile(args[0]);
		
		BpelEquivalence equivalence = new BpelEquivalence();
		
		
		PANetwork network1 = equivalence.createNetwork(process1, true);
		long duration = System.currentTimeMillis() - start;
		System.out.println(
				"Network created (" 
				+ network1.getConstraints().size() + " constraints, " 
				+ network1.getVariables().size() + " variables"
				+ ").");
		
		System.out.println("Calculation time: " + duration + "ms.");
		
		
		
		if (args.length >= 2) {
			System.out.println("Write csv file ...");
			try {
				NetworkUtils.saveAsCSV(network1, new File(args[1]));
				System.out.println("File written: " + args[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
