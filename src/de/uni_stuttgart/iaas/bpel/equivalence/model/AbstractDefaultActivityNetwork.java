package de.uni_stuttgart.iaas.bpel.equivalence.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;

public abstract class AbstractDefaultActivityNetwork extends AbstractActivityNetwork{

	public AbstractDefaultActivityNetwork(NetworkSolver network) {
		super(network);
	}

	@Override
	public Map<Pair<BPELStateEnum, BPELStateEnum>, Type[]> getConnectionConstraints() {
		return new HashMap<Pair<BPELStateEnum, BPELStateEnum>, Type[]>();
	}

	@Override
	public IActivityConnector getActivityConnector() {
		return new DefaultConnector();
	}

	@Override
	protected StateConstraint[] getLocalLinks() {
		StateConstraint[] stateLinks = {};
		return stateLinks;
	}
	
	public class DefaultConnector implements IActivityConnector {

		private ActivityState[] states = {};

		public DefaultConnector() {
		}

		@Override
		public ActivityState[] getConnectionStates() {
			return states;
		}

	}

}
