package de.uni_stuttgart.iaas.bpel.equivalence.model;


import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityStateLink;

public interface IActivityConnector {
	
	public ActivityState[] getConnectionStates();

}
