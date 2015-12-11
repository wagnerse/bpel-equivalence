package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.Activity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.BasicActivityNetwork;

public class BasicActivityFactory implements IActivityNetworkFactory{
	
	private EClass support;
	
	public BasicActivityFactory(EClass support) {
		this.support = support;
	}

	@Override
	public EClass getSupportedEClass() {
		return support;
	}

	@Override
	public AbstractActivityNetwork createElementNetwork(EObject eobject, NetworkSolver network) {
		return new BasicActivityNetwork(support,(Activity) eobject, network);
	}

}
