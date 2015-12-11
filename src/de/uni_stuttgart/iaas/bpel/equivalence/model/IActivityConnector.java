package de.uni_stuttgart.iaas.bpel.equivalence.model;


import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;

public interface IActivityConnector {
	
	public ActivityState[] getConnectionStates();

}
