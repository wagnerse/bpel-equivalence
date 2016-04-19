package de.uni_stuttgart.iaas.bpel.equivalence.model;

/**
 * 
 * The states of a BPEL element
 * 
 * aborted  terminating - terminated
 *   |          |
 * inital - executing - completed
 *   |          |		   fault
 *   |          |        / 
 * dead    fault handling 
 *        				 \ 
 *					       fault caught
 *
 * @author Jonas Scheurich
 *
 */
public enum BPELStateEnum {
	INITAL, 
	DEAD,
	ABORTED,
	
	EXECUTING,
	COMPLETED,
	TERMINATED,
	TERMINATING,
	
	FAULT,
	FAULT_CAUGHT,
	
	FAULT_HANDLING
}
