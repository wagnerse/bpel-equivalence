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
 *					       fault uncaught
 */
public enum BPELStateEnum {
	INITAL, 
	DEAD, 
	
	EXECUTING,
	COMPLETED,
	TERMINATED,	
	
	FAULT,
	FAULT_UNCOUGHT,
	
	FAULT_HANDLING	
}
