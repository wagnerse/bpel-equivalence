package de.uni_stuttgart.iaas.bpel.equivalence.model.factories;

import org.bpel4chor.mergechoreography.ChoreographyPackage;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ChoreographyNetwork;

public class ChoreographyNetworkFactory implements IActivityNetworkFactory {

	@Override
	public Class<?> getSupportedClass() {
		return ChoreographyPackage.class;
	}

	@Override
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, Object object,
			PANetwork network) {
		return new ChoreographyNetwork(parentNetwork, (ChoreographyPackage) object, network);
	}

}
