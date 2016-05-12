package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Catch;
import org.eclipse.bpel.model.FaultHandler;
import org.eclipse.bpel.model.Scope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ScopeNetwork.ScopeConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * Creates a point algebra network for the BPEL activity {@link Scope}
 * 
 * @author Jonas Scheurich
 */
public class ScopeNetwork extends AbstractActivityNetwork {

	private Scope scope;
	private List<PAVariable> variables = new ArrayList<PAVariable>();
	private List<PAConstraint> constraints = new ArrayList<PAConstraint>();

	public ScopeNetwork(AbstractActivityNetwork parentNetwork, Scope subject, PANetwork network) {
		super(parentNetwork, network);
		this.scope = subject;
		initLocalNetwork();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getScope();
	}

	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(scope, "name");
		return (attribute instanceof String) ? (String) attribute : "[Scope]";
	}

	protected void initLocalNetwork() {
		// create variables
		PAVariable startInitial = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
		this.variables.add(startInitial);

		PAVariable startDead = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START));
		this.variables.add(startDead);

		PAVariable startAborted = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START));
		this.variables.add(startAborted);

		PAVariable startTerminating = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START));
		this.variables.add(startTerminating);

		PAVariable startTerminated = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START));
		this.variables.add(startTerminated);

		PAVariable startExe = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
		this.variables.add(startExe);

		PAVariable startFault = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START));
		this.variables.add(startFault);
		
		PAVariable startFaultHandling = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START));
		this.variables.add(startFaultHandling);

		PAVariable startFaultCaught = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.START));
		this.variables.add(startFaultCaught);

		PAVariable startComp = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START));
		this.variables.add(startComp);

		// create inter-state constraints for control flow
		constraints.add(new PAConstraint(startInitial, startDead, RelationEnum.LESS));
		constraints.add(new PAConstraint(startInitial, startAborted, RelationEnum.LESS));
		constraints.add(new PAConstraint(startInitial, startExe, RelationEnum.LESS));
		constraints.add(new PAConstraint(startExe, startTerminating, RelationEnum.LESS));
		constraints.add(new PAConstraint(startExe, startComp, RelationEnum.LESS));
		constraints.add(new PAConstraint(startExe, startFaultHandling, RelationEnum.LESS));
		constraints.add(new PAConstraint(startFaultHandling, startFault, RelationEnum.LESS));
		constraints.add(new PAConstraint(startFaultHandling, startFaultCaught, RelationEnum.LESS));
		constraints.add(new PAConstraint(startTerminating, startTerminated, RelationEnum.LESS));

		// create inter-state constraints for exclusive flow
		// successor of init
		constraints.add(new PAConstraint(startDead, startExe, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startAborted, startExe, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startDead, startAborted, RelationEnum.UNRELATED));

		// successor of executing
		constraints.add(new PAConstraint(startTerminating, startComp, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startFault, startComp, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startTerminating, startFault, RelationEnum.UNRELATED));

		// successor of fault handling
		constraints.add(new PAConstraint(startFault, startFaultCaught, RelationEnum.UNRELATED));
	}

	@Override
	protected void initConstraintMap() {
		// FIXME aborted state, without end state

		AbstractActivityNetwork actNetwork = super.getChildNetwork(scope.getActivity());

		if (actNetwork != null) {
			// handle activity basic constraint map

			// init with scope
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), RelationEnum.EQUALS);

			// if the scope goes to the execution state, transfer the activity
			// to execution
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);

			// if the scope goes to dead, transfer the activity to dead.
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);

			// if the scope goes to fault, or the activty goes to fault,
			// transfer the other to fault.
			// if the activity is terminated or completed, no action is
			// performed.
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);

			// the fault state of the activity ends with the fault or
			// fault_caught state of the scope
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);

			// if the activity goes to fault_caught, transfer the scope to
			// completed.
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);

			// if the scope or the activity is terminated, transfer
			// scope/activity to terminated
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), RelationEnum.EQUALS);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);

			// if the activity is completed, transfer the scope to completed.
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), RelationEnum.EQUALS);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
		}

		// handle fault handler constraint map
		initFaultHandlerConstraintMap(scope.getFaultHandlers());

	}

	/**
	 * Create constraints between this scope and the fault handlers
	 * 
	 * @param faultHandler
	 */
	private void initFaultHandlerConstraintMap(FaultHandler faultHandler) {
		if (faultHandler == null)
			return;
		//FIXME without end state
		for (Catch bpelCatch : faultHandler.getCatch()) {
			AbstractActivityNetwork fhNetwork = super.getChildNetwork(bpelCatch);
			if (fhNetwork != null) {
				// init with scope
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), fhNetwork,
						new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), RelationEnum.EQUALS);

				// go to execution or dead of the fault handler if scope starts
				// fault handling
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START), fhNetwork,
						new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START), fhNetwork,
						new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);

				// got to dead of the fault handler if the scope is marked as
				// dead
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), fhNetwork,
						new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), fhNetwork,
						new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);

				// if the scope completes mark the fault handler as dead.
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), fhNetwork,
						new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);

				// if the scope terminates, terminate the fault handler
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), fhNetwork,
						new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.END), fhNetwork,
						new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.END), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);

				// if the fault handler completes, mark the scope as fault
				// caught or fault
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), fhNetwork,
						new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END), fhNetwork,
						new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.START), fhNetwork,
						new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.START), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.END), fhNetwork,
						new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.END), RelationEnum.EQUALS,
						RelationEnum.UNRELATED);
			}
		}
	}

	@Override
	public IActivityConnector getActivityConnector() {
		PAVariable[] variableArray = new PAVariable[this.variables.size()];
		variableArray = variables.toArray(variableArray);
		return new ScopeConnector(variableArray);
	}

	@Override
	public PAConstraint[] getLocalConstraints() {
		PAConstraint[] constraintArray = new PAConstraint[this.constraints.size()];
		constraintArray = constraints.toArray(constraintArray);
		return constraintArray;
	}

	/**
	 * This class supports Scopes with fault handlers and activities.
	 */
	@Override
	protected Map<EObject, AbstractActivityNetwork> createChildNetworks() {
		Map<EObject, AbstractActivityNetwork> childMap = new HashMap<EObject, AbstractActivityNetwork>();

		// add activity
		AbstractActivityNetwork activity = createChildNetwork(scope.getActivity());
		if (activity != null)
			childMap.put(scope.getActivity(), activity);

		// ad fault handlers
		if (scope.getFaultHandlers() != null) {
			// add fault handlers
			for (Catch bpelCatch : scope.getFaultHandlers().getCatch()) {
				AbstractActivityNetwork catchNetwork = createChildNetwork(bpelCatch);
				if (catchNetwork != null)
					childMap.put(bpelCatch, catchNetwork);
			}
		}
		return childMap;
	}

	public class ScopeConnector implements IActivityConnector {

		private PAVariable[] states;

		public ScopeConnector(PAVariable[] states) {
			this.states = states;
		}

		@Override
		public PAVariable[] getVariables() {
			return states;
		}
	}

	@Override
	public EObject getEObject() {
		return (EObject) this.scope;
	}

}
