package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Flow;
import org.eclipse.bpel.model.impl.FlowImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.FlowNetwork;

/**
 * Creates a activity network for the BPEL activity {@link Flow}
 * 
 * @author Jonas Scheurich
 *
 */
public class FlowNetworkFactory implements IActivityNetworkFactory {

	@Override
	public Class<?> getSupportedClass() {
		return FlowImpl.class;
	}

	@Override
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, Object object,
			PANetwork network) {
		return new FlowNetwork(parentNetwork, (Flow) object, network);
	}

}
