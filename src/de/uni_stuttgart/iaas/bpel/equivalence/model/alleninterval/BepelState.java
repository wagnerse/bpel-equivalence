package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

public enum BepelState {
	INITAL, 
	DEAD, 
	
	EXECUTING,
	COMPLETED,
	TERMINATED,	
	
	FAULT,
	
	FAULT_HANDLING, 
	FAULT_UNCOUGHT, 
	FAULT_COUGHT,
}
