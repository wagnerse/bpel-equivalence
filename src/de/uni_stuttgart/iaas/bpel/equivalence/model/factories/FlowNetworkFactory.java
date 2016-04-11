package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Flow;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.FlowNetwork;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Creates a activity network for the BPEL activity {@link Flow}
 *
 */
public class FlowNetworkFactory implements IActivityNetworkFactory {

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getFlow();
	}

	@Override
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, EObject eobject,
			PANetwork network) {
		return new FlowNetwork(parentNetwork, (Flow) eobject, network);
	}

}
