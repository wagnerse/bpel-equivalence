package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Catch;
import org.eclipse.bpel.model.impl.CatchImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.FHCatchNetwork;

/**
 * Creates a activity network for the BPEL activity {@link Catch} (of a fault handler)
 * 
 * @author Jonas Scheurich
 *
 */
public class FHCatchNetworkFactory implements IActivityNetworkFactory{

	@Override
	public Class<?> getSupportedClass() {
		return CatchImpl.class;
	}

	@Override
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, Object object, PANetwork network) {
		return new FHCatchNetwork(parentNetwork, (Catch) object, network);
	}

}
