package de.uni_stuttgart.iaas.bpel.equivalence.model;


import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;

/**
 * 
 * @author Jonas Scheurich
 *
 * A activity connector contains the variables of a activity network and is used as data transfer object
 *
 */
public interface IActivityConnector {
	
	public PAVariable[] getVariables();

}
