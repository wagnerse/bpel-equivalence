package de.uni_stuttgart.iaas.bpel.equivalence.model;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;

/**
 * A activity network factory creates a activty network from a given {@link EObject}.
 * 
 * @author Jonas Scheurich
 *
 */
public interface IActivityNetworkFactory {
	
	public Class<?> getSupportedClass();

	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, Object object, PANetwork network);

}
