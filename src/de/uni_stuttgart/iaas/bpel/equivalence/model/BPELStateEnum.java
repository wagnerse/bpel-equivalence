package de.uni_stuttgart.iaas.bpel.equivalence.model;

/**
 * 
 * @author Jonas Scheurich
 * 
 * The states of a BPEL element
 * 
 *  dead   terminated
 *   |    /     |
 * inital - executing - completed
 *              |		   fault
 *              |        / 
 *        fault handling 
 *        				 \ 
 *					       fault caught
 */
public enum BPELStateEnum {
	INITAL, 
	DEAD, 
	
	EXECUTING,
	COMPLETED,
	TERMINATED,	
	
	FAULT,
	FAULT_CAUGHT,
	
	FAULT_HANDLING
}
