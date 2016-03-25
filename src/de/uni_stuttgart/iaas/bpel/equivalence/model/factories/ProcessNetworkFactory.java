package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ProcessNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PANetwork;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Creates a activity network for the BPEL activity {@link Process}
 *
 */
public class ProcessNetworkFactory implements IActivityNetworkFactory{

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getProcess();
	}

	@Override
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, EObject eobject, PANetwork network) {
		return new ProcessNetwork(parentNetwork, eobject, network);
	}

}
