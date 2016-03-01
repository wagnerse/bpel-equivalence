package de.uni_stuttgart.iaas.bpel.equivalence.model;

import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;

public abstract class AbstractStubActivityNetwork extends AbstractActivityNetwork{

	public AbstractStubActivityNetwork(AbstractActivityNetwork parentNetwork, Problem network) {
		super(parentNetwork, network);
	}
	
	@Override
	public Problem linkActivityNetworkLayer() {
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
	public Constraint[] getLocalLinks() {
		Constraint[] noConstraint = {};
		return noConstraint;
	}


}
