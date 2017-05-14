package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Scope;
import org.eclipse.bpel.model.impl.ScopeImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ScopeNetwork;

/**
 * Creates a activity network for the BPEL activity {@link Scope}
 * 
 * @author Jonas Scheurich
 * 
 */
public class ScopeNetworkFactory implements IActivityNetworkFactory {

	@Override
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, Object object, PANetwork network) {
		return new ScopeNetwork(parentNetwork, (Scope)object, network);
	}

	@Override
	public Class<?> getSupportedClass() {
		return ScopeImpl.class;
	}
}
