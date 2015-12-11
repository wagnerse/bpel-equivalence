package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Scope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;

public class ScopeNetwork extends AbstractActivityNetwork{
	
	private Scope scope;
	private ActivityState[] activityStates;
	private StateConstraint[] activityStateLinks;

	public ScopeNetwork(Scope subject, NetworkSolver network) {
		super(network);
		this.scope = subject;
		initNetwork();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getScope();
	}
	
	private void initNetwork() {
		// states
		ActivityState init = getNetwork().createActivityState(getEObject(), BPELStateEnum.INITAL);
		ActivityState dead = getNetwork().createActivityState(getEObject(), BPELStateEnum.DEAD);
		ActivityState executing = getNetwork().createActivityState(getEObject(), BPELStateEnum.EXECUTING);
		ActivityState completed = getNetwork().createActivityState(getEObject(), BPELStateEnum.COMPLETED);
		ActivityState terminated = getNetwork().createActivityState(getEObject(), BPELStateEnum.TERMINATED);
		ActivityState faultHandling = getNetwork().createActivityState(getEObject(), BPELStateEnum.FAULT_HANDLING);
		ActivityState faultCought = getNetwork().createActivityState(getEObject(), BPELStateEnum.FAULT_COUGHT);
		ActivityState faultUncought = getNetwork().createActivityState(getEObject(), BPELStateEnum.FAULT_UNCOUGHT);
		
		ActivityState[] stateList = {
				init, 
				dead, 
				executing, 
				completed, 
				terminated, 
				faultHandling, 
				faultCought, 
				faultUncought};
		activityStates = stateList;
		
		// links
		List<StateConstraint> activityStateLinksList = new ArrayList<StateConstraint>();
		activityStateLinksList.add(createMeetsActivityStateLink(init, dead));
		activityStateLinksList.add(createMeetsActivityStateLink(init, terminated));
		activityStateLinksList.add(createMeetsActivityStateLink(init, executing));
		activityStateLinksList.add(createMeetsActivityStateLink(executing, terminated));
		activityStateLinksList.add(createMeetsActivityStateLink(executing, completed));
		activityStateLinksList.add(createMeetsActivityStateLink(executing, faultHandling));
		activityStateLinksList.add(createMeetsActivityStateLink(faultHandling, faultCought));
		activityStateLinksList.add(createMeetsActivityStateLink(faultHandling, faultUncought));
		activityStateLinks = (StateConstraint[]) activityStateLinksList.toArray();
		
	}
	

	@Override
	public Map<Pair<BPELStateEnum, BPELStateEnum>, Type[]> getConnectionTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActivityConnector getActivityConnector() {		
		return new ScopeConnector(activityStates);
	}

	@Override
	protected StateConstraint[] getLocalLinks() {
		return activityStateLinks;
	}

	/**
	 * This class supports Scopes with fault handlers and activities.
	 */
	@Override
	protected AbstractActivityNetwork[] getChildNetworks() {
		List<AbstractActivityNetwork> childList = new ArrayList<AbstractActivityNetwork>();
		childList.add(createChildNetwork((EObject) scope.getFaultHandlers()));
		childList.add(createChildNetwork((EObject) scope.getActivity()));
		
		return (AbstractActivityNetwork[]) childList.toArray();
	}
	
	
	public class ScopeConnector implements IActivityConnector{
		
		private ActivityState[] states;
		
		public ScopeConnector(ActivityState[] states) {
			this.states = states;
		}

		@Override
		public ActivityState[] getConnectionStates() {
			return states;
		}
	}


	@Override
	public EObject getEObject() {
		return (EObject) this.scope;
	}

}
