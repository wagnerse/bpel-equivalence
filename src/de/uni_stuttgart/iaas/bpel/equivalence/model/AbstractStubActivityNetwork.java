package de.uni_stuttgart.iaas.bpel.equivalence.model;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;

public abstract class AbstractStubActivityNetwork extends AbstractActivityNetwork{

	public AbstractStubActivityNetwork(AbstractActivityNetwork parentNetwork, NetworkSolver network) {
		super(parentNetwork, network);
	}
	
	@Override
	public NetworkSolver linkActivityNetworkLayer() {
		super.childNetworks = createChildNetworks();
		
		// perfrom pre processing
		doPreProcessing();

		// perform post processing
		doPostProcessing();

		// perform child processing
		for (AbstractActivityNetwork cn : getChildNetworks()) {
			cn.linkActivityNetworkLayer();
		}
		
		return getNetwork();
	}
	
	@Override
	public IActivityConnector getActivityConnector() {
		// return connector of the parent e.g. scope network
		return getParentActivityNetwork().getActivityConnector();
	}

	@Override
	public StateConstraint[] getLocalLinks() {
		StateConstraint[] noConstraint = {};
		return noConstraint;
	}


}
