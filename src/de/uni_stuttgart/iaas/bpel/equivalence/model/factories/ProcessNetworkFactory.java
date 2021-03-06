package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.impl.ProcessImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ProcessNetwork;

/**
 * Creates a activity network for the BPEL activity {@link Process}
 * 
 * @author Jonas Scheurich
 *
 */
public class ProcessNetworkFactory implements IActivityNetworkFactory{

	@Override
	public Class<?> getSupportedClass() {
		return ProcessImpl.class;
	}

	@Override
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, Object object, PANetwork network) {
		return new ProcessNetwork(parentNetwork, (Process) object, network);
	}

}
