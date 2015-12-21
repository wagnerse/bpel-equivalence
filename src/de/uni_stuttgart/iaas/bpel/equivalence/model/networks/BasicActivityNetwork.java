package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.Activity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;

public class BasicActivityNetwork extends AbstractActivityNetwork {

	private EClass support;
	private Activity activity;
	private ActivityState[] activityStates;
	private StateConstraint[] activityStateLinks;

	public BasicActivityNetwork(AbstractActivityNetwork parentNetwork, EClass support, Activity subject, NetworkSolver network) {
		super(parentNetwork, network);
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
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(activity, "name");
		return (attribute instanceof String)? (String) attribute : "[Activity]";
	}

	@Override
	public EClass getSupportedEClass() {
		return support;
	}

	@Override
	public IActivityConnector getActivityConnector() {
		return new BasicActivityConnector(activityStates);
	}

	@Override
	public StateConstraint[] getLocalLinks() {
		return activityStateLinks;
	}

	@Override
	protected AbstractActivityNetwork[] createChildNetworks() {
		AbstractActivityNetwork[] childs = {};
		return childs;
	}

	private void initNetwork() {
		// states
		ActivityState init = getNetwork().createActivityState(getEObject(), BPELStateEnum.INITAL);
		ActivityState dead = getNetwork().createActivityState(getEObject(), BPELStateEnum.DEAD);
		ActivityState executing = getNetwork().createActivityState(getEObject(), BPELStateEnum.EXECUTING);
		ActivityState completed = getNetwork().createActivityState(getEObject(), BPELStateEnum.COMPLETED);
		ActivityState terminated = getNetwork().createActivityState(getEObject(), BPELStateEnum.TERMINATED);
		ActivityState fault = getNetwork().createActivityState(getEObject(), BPELStateEnum.FAULT);

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
		
		activityStateLinks = new StateConstraint[activityStateLinksList.size()];
		activityStateLinks = activityStateLinksList.toArray(activityStateLinks);

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

	@Override
	protected void initConstraintMap() {
		// TODO Auto-generated method stub
		
	}
}
