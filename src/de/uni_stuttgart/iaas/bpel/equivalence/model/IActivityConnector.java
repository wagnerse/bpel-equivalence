package de.uni_stuttgart.iaas.bpel.equivalence.model;


import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;

/**
 * A activity connector contains the variables of a activity network and is used as data transfer object
 * 
 * @author Jonas Scheurich
 *
 */
public interface IActivityConnector {
	
	public PAVariable[] getVariables();

}
