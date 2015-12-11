package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.bpel.model.Activity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;

public class BasicActivityNetwork extends AbstractActivityNetwork {

	private EClass support;
	private Activity activity;
	private ActivityState[] activityStates;
	private StateConstraint[] activityStateLinks;

	public BasicActivityNetwork(EClass support, Activity subject, NetworkSolver network) {
		super(network);
		this.support = support;
		this.activity = subject;
		initNetwork();
	}
	
	/*public BasicActivityNetwork(EClass support, EObject subject, NetworkSolver network) {
		super(network);
		this.support = support;
		this.activity = (Activity) subject;
		initNetwork();
	}*/

	@Override
	public EClass getSupportedEClass() {
		return support;
	}

	@Override
	public Map<Pair<BPELStateEnum, BPELStateEnum>, Type[]> getConnectionTable() {
		return new HashMap<Pair<BPELStateEnum, BPELStateEnum>, Type[]>();
	}

	@Override
	public IActivityConnector getActivityConnector() {
		return new BasicActivityConnector(activityStates);
	}

	@Override
	protected StateConstraint[] getLocalLinks() {
		return activityStateLinks;
	}

	@Override
	protected AbstractActivityNetwork[] getChildNetworks() {
		AbstractActivityNetwork[] childs = {};
		return childs;
	}

	private void initNetwork() {
		// states
		ActivityState init = getNetwork().createActivityState(BPELStateEnum.INITAL);
		ActivityState dead = getNetwork().createActivityState(BPELStateEnum.DEAD);
		ActivityState executing = getNetwork().createActivityState(BPELStateEnum.EXECUTING);
		ActivityState completed = getNetwork().createActivityState(BPELStateEnum.COMPLETED);
		ActivityState terminated = getNetwork().createActivityState(BPELStateEnum.TERMINATED);
		ActivityState fault = getNetwork().createActivityState(BPELStateEnum.FAULT);

		ActivityState[] stateList = { init, dead, executing, completed, terminated, fault };
		activityStates = stateList;

		// links
		List<StateConstraint> activityStateLinksList = new ArrayList<StateConstraint>();
		activityStateLinksList.add(createMeetsActivityStateLink(init, dead));
		activityStateLinksList.add(createMeetsActivityStateLink(init, terminated));
		activityStateLinksList.add(createMeetsActivityStateLink(init, executing));
		activityStateLinksList.add(createMeetsActivityStateLink(executing, terminated));
		activityStateLinksList.add(createMeetsActivityStateLink(executing, completed));
		activityStateLinksList.add(createMeetsActivityStateLink(executing, fault));
		activityStateLinks = (StateConstraint[]) activityStateLinksList.toArray();

	}

	public class BasicActivityConnector implements IActivityConnector {

		private ActivityState[] states;

		public BasicActivityConnector(ActivityState[] states) {
			this.states = states;
		}

		@Override
		public ActivityState[] getConnectionStates() {
			return states;
		}
	}

	@Override
	public EObject getEObject() {
		return (EObject) this.activity;
	}
}
