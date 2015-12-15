package de.uni_stuttgart.iaas.bpel.equivalence;

import org.eclipse.emf.ecore.EObject;

import java.io.File;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.AllenNetworkUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELResourceUtils;

public class BpelAllenNetworkConsole {

	public static void main(String[] args) {
		if(!(0 < args.length && args.length  <= 2)) return;

		long start = System.currentTimeMillis();
		System.out.println("Create interval network for \n" + args[0]);
	
		EObject process1 = (EObject) BPELResourceUtils.readProcessFromFile(args[0]);
		
		BpelEquivalence equivalence = new BpelEquivalence();
		equivalence.init();
		
		NetworkSolver network1 =equivalence.createNetwork(process1);
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("Network created (" + network1.getConstraints().length + " constraints).");
		
		System.out.println("Calculation time: " + duration + "ms.");
		
		
		
		if (args.length >= 2) {
			System.out.println("Write csv file ...");
			try {
				AllenNetworkUtils.saveAsCSV(network1, new File(args[1]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("File written: " + args[1]);
		}
		
	}
}
