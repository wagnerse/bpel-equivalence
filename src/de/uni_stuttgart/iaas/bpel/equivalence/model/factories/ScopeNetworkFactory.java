package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Scope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ScopeNetwork;

public class ScopeNetworkFactory implements IActivityNetworkFactory{

	@Override
	public AbstractActivityNetwork createElementNetwork(EObject eobject, NetworkSolver network) {
		return new ScopeNetwork((Scope) eobject, network);
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getScope();
	}
}
