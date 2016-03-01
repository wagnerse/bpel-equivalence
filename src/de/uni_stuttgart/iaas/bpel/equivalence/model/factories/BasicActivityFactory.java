package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.Activity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.BasicActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;

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
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, EObject eobject, Problem network) {
		return new BasicActivityNetwork(parentNetwork, support,(Activity) eobject, network);
	}

}
