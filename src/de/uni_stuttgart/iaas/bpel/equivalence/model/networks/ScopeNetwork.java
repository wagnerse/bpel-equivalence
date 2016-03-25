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
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ScopeNetwork.ScopeConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Creates a point algebra network for the BPEL activity {@link Scope}
 */
public class ScopeNetwork extends AbstractActivityNetwork{
	
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
		return (attribute instanceof String)? (String) attribute : "[Scope]";
	}
	
	protected void initLocalNetwork() {
		//create variables
		PAVariable startInitial = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
		PAVariable endInitial = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.END));
		this.variables.add(startInitial);
		this.variables.add(endInitial);
		
		PAVariable startDead = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START));
		PAVariable endDead = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END));
		this.variables.add(startDead);
		this.variables.add(endDead);
		
		PAVariable startTerminated = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START));
		PAVariable endTerminated = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END));
		this.variables.add(startTerminated);
		this.variables.add(endTerminated);
		
		PAVariable startExe = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
		PAVariable endExe = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END));
		this.variables.add(startExe);
		this.variables.add(endExe);
		
		PAVariable startFaultHandling = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START));
		PAVariable endFaultHandling = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.END));
		this.variables.add(startFaultHandling);
		this.variables.add(endFaultHandling);
		
		PAVariable startFaultCaught = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT_UNCOUGHT, TimeTypeEnum.START));
		PAVariable endFaultCaught = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT_UNCOUGHT, TimeTypeEnum.END));
		this.variables.add(startFaultCaught);
		this.variables.add(endFaultCaught);

		PAVariable startFault = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START));
		PAVariable endFault = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END));
		this.variables.add(startFault);
		this.variables.add(endFault);
		
		PAVariable startComp = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START));
		PAVariable endComp = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END));
		this.variables.add(startComp);
		this.variables.add(endComp);
		
		//create intra-state constraints		
		constraints.add(new PAConstraint(startInitial, endInitial, RelationEnum.LESS));
		constraints.add(new PAConstraint(startDead, endDead, RelationEnum.LESS));
		constraints.add(new PAConstraint(startTerminated, endTerminated, RelationEnum.LESS));
		constraints.add(new PAConstraint(startExe, endExe, RelationEnum.LESS));
		constraints.add(new PAConstraint(startFaultHandling, endFaultHandling, RelationEnum.LESS));
		constraints.add(new PAConstraint(startFaultCaught, endFaultCaught, RelationEnum.LESS));
		constraints.add(new PAConstraint(startFault, endFault, RelationEnum.LESS));
		constraints.add(new PAConstraint(startComp, endComp, RelationEnum.LESS));
		
		//create inter-state constraints
		constraints.add(new PAConstraint(endInitial, startDead, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endInitial, startTerminated, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endInitial, startExe, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endExe, startTerminated, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endExe, startComp, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endExe, startFaultHandling, RelationEnum.EQUALS, RelationEnum.UNRELATED));	
		constraints.add(new PAConstraint(endFaultHandling, startFault, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endFaultHandling, startFaultCaught, RelationEnum.EQUALS, RelationEnum.UNRELATED));

		//create inter-state constraints for exclusive flow
		constraints.add(new PAConstraint(startDead, startExe, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startTerminated, startComp, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startFaultHandling, startComp, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startFault, startFaultCaught, RelationEnum.UNRELATED));
	}
	
	@Override
	protected void initConstraintMap() {
		
		AbstractActivityNetwork actNetwork = super.getChildNetwork(scope.getActivity());
		
		if (actNetwork != null) {
			// handle activity basic constraint map
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), RelationEnum.EQUALS);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), RelationEnum.EQUALS);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END), actNetwork,
					new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END), RelationEnum.EQUALS,
					RelationEnum.UNRELATED);
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
	 * @param faultHandler
	 */
	private void initFaultHandlerConstraintMap(FaultHandler faultHandler) {
		if (faultHandler == null) return;
		
		for (Catch bpelCatch: faultHandler.getCatch()) {
			AbstractActivityNetwork fhNetwork = super.getChildNetwork(bpelCatch);
				if (fhNetwork != null) {
				// init with scope
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), 
						RelationEnum.EQUALS);
				
				//go to execution or dead of the fault handler if scope starts fault handling
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				
				//got to dead of the fault handler if the scope is marked as dead
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				
				// if the scope completes mark the fault handler as dead.
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				
				// if the scope terminates, terminate the fault handler
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				
				// if the fault handler completes, mark the scope as fault caught or fault
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_UNCOUGHT, TimeTypeEnum.START), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.FAULT_UNCOUGHT, TimeTypeEnum.START), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT_UNCOUGHT, TimeTypeEnum.END), 
						fhNetwork,	new TimePointDesc(BPELStateEnum.FAULT_UNCOUGHT, TimeTypeEnum.END), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
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
		if (activity != null) childMap.put(scope.getActivity(), activity);
		
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
	
	
	public class ScopeConnector implements IActivityConnector{
		
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
