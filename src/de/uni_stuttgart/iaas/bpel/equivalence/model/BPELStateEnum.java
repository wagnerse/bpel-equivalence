package de.uni_stuttgart.iaas.bpel.equivalence.model;

public enum BPELStateEnum {
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
