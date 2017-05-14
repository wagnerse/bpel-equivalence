package de.uni_stuttgart.iaas.bpel.equivalence.console;

import java.io.File;

import org.bpel4chor.mergechoreography.ChoreographyPackage;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.BpelEquivalence;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.NetworkUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.ChorUtils;

public class PANetworkConsole {

	public static void main(String[] args) {
		if(!(0 < args.length && args.length  <= 2)) return;
		createPANetworkOfProcess(args[0], args[1]);
	}
	
	public static void createPANetworkOfProcess(String processPath, String csvOutputPath) {
		long start = System.currentTimeMillis();
		System.out.println("Create point algebra network for process \n" + processPath);
	
		EObject process1 = (EObject) BPELUtils.readProcessFromFile(processPath);
		
		BpelEquivalence equivalence = new BpelEquivalence();
		
		
		PANetwork network1 = equivalence.createNetwork(process1, true);
		long duration = System.currentTimeMillis() - start;
		System.out.println(
				"Network created (" 
				+ network1.getConstraints().size() + " constraints, " 
				+ network1.getVariables().size() + " variables"
				+ ").");
		
		System.out.println("Calculation time: " + duration + "ms.");
		
		System.out.println("Write csv file ...");
		try {
			NetworkUtils.saveAsCSV(network1, new File(csvOutputPath));
			System.out.println("File written: " + csvOutputPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createPANetworkOfChoreography(String chorPath, String csvOutputPath) {
		
		long start = System.currentTimeMillis();
		System.out.println("Create point algebra network for choreography \n" + chorPath);
	
		ChoreographyPackage chor = ChorUtils.readChoreographyFromFile(chorPath);
		
		BpelEquivalence equivalence = new BpelEquivalence();
		
		PANetwork network1 = equivalence.createNetwork(chor, true);
		long duration = System.currentTimeMillis() - start;
		System.out.println(
				"Network created (" 
				+ network1.getConstraints().size() + " constraints, " 
				+ network1.getVariables().size() + " variables"
				+ ").");
		
		System.out.println("Calculation time: " + duration + "ms.");
		
		System.out.println("Write csv file ...");
		try {
			NetworkUtils.saveAsExcel(network1, new File(csvOutputPath), true);
			System.out.println("File written: " + csvOutputPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
