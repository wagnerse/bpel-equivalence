package de.uni_stuttgart.iaas.bpel.equivalence.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;

/**
 * A activity network factory creates a activty network from a given {@link EObject}.
 * 
 * @author Jonas Scheurich
 *
 */
public interface IActivityNetworkFactory {
	
	public EClass getSupportedEClass();

	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, EObject eobject, PANetwork network);

}
