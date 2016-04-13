package de.uni_stuttgart.iaas.bpel.equivalence.console;

import java.io.File;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.BpelEquivalence;
import de.uni_stuttgart.iaas.bpel.equivalence.PA2IA.PA2IA;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra.IANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.NetworkUtils;

public class IANetworkConsole {

	public static void main(String[] args) {
		if(!(0 < args.length && args.length  <= 2)) return;

		long start = System.currentTimeMillis();
		System.out.println("Create interval algebra network for \n" + args[0]);
	
		EObject process1 = (EObject) BPELUtils.readProcessFromFile(args[0]);
		
		BpelEquivalence equivalence = new BpelEquivalence();
		
		PANetwork pointNetwork = equivalence.createNetwork(process1, true);
		
		long durationPA = System.currentTimeMillis() - start;
		System.out.println(
				"Network created (" 
				+ pointNetwork.getConstraints().size() + " constraints, " 
				+ pointNetwork.getVariables().size() + " variables"
				+ ").");
		System.out.println("Calculation time (point algebra): " + durationPA + "ms.");
		
		IANetwork intervalNetwork = new PA2IA(pointNetwork).transfrom();
		
		long durationIA = System.currentTimeMillis() - start - durationPA;
		System.out.println("Calculation time (interval algebra): " + durationIA + "ms.");
		
		if (args.length >= 2) {
			System.out.println("Write csv file ...");
			try {
				NetworkUtils.saveAsCSV(intervalNetwork, new File(args[1]));
				System.out.println("File written: " + args[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
