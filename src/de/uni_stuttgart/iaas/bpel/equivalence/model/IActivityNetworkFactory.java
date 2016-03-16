package de.uni_stuttgart.iaas.bpel.equivalence.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;

/**
 * 
 * @author Jonas Scheurich
 * 
 * A activity network factory creates a activty network from a given {@link EObject}.
 *
 */
public interface IActivityNetworkFactory {
	
	public EClass getSupportedEClass();

	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, EObject eobject, Problem network);

}
