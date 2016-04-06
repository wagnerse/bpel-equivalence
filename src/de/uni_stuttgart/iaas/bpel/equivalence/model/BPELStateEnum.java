package de.uni_stuttgart.iaas.bpel.equivalence.model;

/**
 * 
 * @author Jonas Scheurich
 * 
 * The states of a BPEL element
 * 
 * aborted  terminated
 *   |          |
 * inital - executing - completed
 *   |          |		   fault
 *   |          |        / 
 * dead    fault handling 
 *        				 \ 
 *					       fault caught
 */
public enum BPELStateEnum {
	INITAL, 
	DEAD,
	ABORTED,
	
	EXECUTING,
	COMPLETED,
	TERMINATED,	
	
	FAULT,
	FAULT_CAUGHT,
	
	FAULT_HANDLING
}
