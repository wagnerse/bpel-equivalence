package de.uni_stuttgart.iaas.bpel.equivalence.examples;

import de.uni_stuttgart.iaas.bpel.equivalence.console.PANetworkConsole;

public class TestPANetwork {

	public static void main(String[] args) {
		//String input = "/Users/wagnerse/Documents/Diss/Prototype/bpel-equivalence/BpelFiles/EmptyActivity/EmptyActivity.bpel";
		String csvOutput = "/Users/wagnerse/Documents/temp/pa.xls";
		String input = "/Users/wagnerse/Documents/Diss/Prototype/bpel-equivalence/BpelFiles/AsynInvokeReceive.zip";
		
	
		PANetworkConsole.createPANetworkOfChoreography(input, csvOutput);

	}

}
