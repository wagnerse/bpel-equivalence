package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Catch;
import org.eclipse.bpel.model.Scope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ScopeNetwork.ScopeConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class ScopeNetwork extends AbstractActivityNetwork{
	
	private Scope scope;
	private ActivityState[] activityStates;
	private StateConstraint[] activityStateLinks;
	
	public ScopeNetwork(AbstractActivityNetwork parentNetwork, Scope subject, NetworkSolver network) {
		super(parentNetwork, network);
		this.scope = subject;
		initNetwork();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getScope();
	}
	
	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(scope, "name");
		return (attribute instanceof String)? (String) attribute : "[Scope]";
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
		
		activityStateLinks = new StateConstraint[activityStateLinksList.size()];
		activityStateLinks = activityStateLinksList.toArray(activityStateLinks);
		
	}
	
	@Override
	protected void initConstraintMap() {
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.INITAL, Type.Equals);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.EXECUTING, Type.Meets);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.DEAD, Type.Meets);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.FAULT, Type.Before);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.TERMINATED, Type.Meets, Type.Before);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.COMPLETED, Type.Before);
		
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.INITAL, Type.MetBy);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.EXECUTING, Type.Equals);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.FAULT, Type.Meets);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.TERMINATED, Type.Meets);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.COMPLETED, Type.Meets);
		
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.INITAL, Type.MetBy);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.EXECUTING);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.DEAD, Type.Equals);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.FAULT);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.TERMINATED);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.COMPLETED);
		
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.INITAL, Type.After);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.EXECUTING, Type.MetBy);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.FAULT, Type.Starts);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.TERMINATED);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.COMPLETED);
		
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.INITAL, Type.After);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.EXECUTING, Type.After);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.FAULT, Type.Finishes);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.TERMINATED);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.COMPLETED);
		
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.INITAL, Type.After);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.EXECUTING, Type.After);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.FAULT, Type.Finishes);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.TERMINATED);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.COMPLETED);

		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.INITAL, Type.After, Type.MetBy);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.EXECUTING, Type.MetBy);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.FAULT);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.TERMINATED, Type.Equals);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.COMPLETED);

		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.INITAL, Type.After);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.EXECUTING, Type.MetBy);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.FAULT);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.TERMINATED);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.COMPLETED, Type.Equals);
	}

	@Override
	public IActivityConnector getActivityConnector() {		
		return new ScopeConnector(activityStates);
	}

	@Override
	public StateConstraint[] getLocalLinks() {
		return activityStateLinks;
	}

	/**
	 * This class supports Scopes with fault handlers and activities.
	 */
	@Override
	protected AbstractActivityNetwork[] createChildNetworks() {
		List<AbstractActivityNetwork> childList = new ArrayList<AbstractActivityNetwork>();	
		
		// add activity
		AbstractActivityNetwork activity = createChildNetwork(scope.getActivity());
		if (activity != null) childList.add(activity);
		
		if (scope.getFaultHandlers() != null) {
			// add fault handlers
			for (Catch bpelCatch : scope.getFaultHandlers().getCatch()) {
				AbstractActivityNetwork catchNetwork = createChildNetwork(bpelCatch);
				if (catchNetwork != null)
					childList.add(catchNetwork);
			} 
		}
		// create return array
		AbstractActivityNetwork[] childArray = new AbstractActivityNetwork[childList.size()];
		childArray = childList.toArray(childArray);
		return childArray;
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
