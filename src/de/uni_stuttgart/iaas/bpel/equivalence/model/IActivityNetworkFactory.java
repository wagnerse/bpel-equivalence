package de.uni_stuttgart.iaas.bpel.equivalence.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;

public interface IActivityNetworkFactory {
	
	public EClass getSupportedEClass();

	public AbstractActivityNetwork createElementNetwork(EObject eobject, NetworkSolver network);

}
