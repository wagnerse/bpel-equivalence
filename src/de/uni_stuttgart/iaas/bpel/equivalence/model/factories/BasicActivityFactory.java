package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.Activity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.BasicActivityNetwork;

/**
 * Creates a activity network for the BPEL basic activities
 * receive, reply, invoke, assign, throw, exit, wait, empty, opaque
 * 
 * @author Jonas Scheurich
 *
 */
public class BasicActivityFactory implements IActivityNetworkFactory{
	
	private Class<?> support;
	
	public BasicActivityFactory(Class<?> support) {
		this.support = support;
	}

	@Override
	public Class<?> getSupportedClass() {
		return support;
	}

	@Override
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, Object object, PANetwork network) {
		return new BasicActivityNetwork(parentNetwork, support,(Activity) object, network);
	}

}
