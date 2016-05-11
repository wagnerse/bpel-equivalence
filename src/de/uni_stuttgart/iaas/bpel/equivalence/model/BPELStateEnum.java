package de.uni_stuttgart.iaas.bpel.equivalence.model;

/**
 * 
 * The states of a BPEL element
 * 
 * aborted terminating - terminated | | inital - executing - completed | | fault
 * | | / dead fault handling \ fault caught
 *
 * @author Jonas Scheurich
 *
 */
public enum BPELStateEnum {
	INITAL, DEAD, ABORTED,

	EXECUTING, COMPLETED, TERMINATED, TERMINATING,

	FAULT, FAULT_CAUGHT,

	FAULT_HANDLING;

	public static BPELStateEnum fromString(String text) {
	    if (text != null) {
	      for (BPELStateEnum b : BPELStateEnum.values()) {
	        if (text.equalsIgnoreCase(b.name())) {
	          return b;
	        }
	      }
	    }
	    return null;
	  }
}
