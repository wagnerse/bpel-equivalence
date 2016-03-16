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
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Creates a point algebra network for the BPEL activity {@link Scope}
 */
public class ScopeNetwork extends AbstractActivityNetwork{
	
	private Scope scope;
	private List<Variable> variables = new ArrayList<Variable>();
	private List<Constraint> constraints = new ArrayList<Constraint>();
	
	public ScopeNetwork(AbstractActivityNetwork parentNetwork, Scope subject, Problem network) {
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
		Variable startInitial = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
		Variable endInitial = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.END));
		this.variables.add(startInitial);
		this.variables.add(endInitial);
		
		Variable startDead = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START));
		Variable endDead = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END));
		this.variables.add(startDead);
		this.variables.add(endDead);
		
		Variable startTerminated = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START));
		Variable endTerminated = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END));
		this.variables.add(startTerminated);
		this.variables.add(endTerminated);
		
		Variable startExe = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
		Variable endExe = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END));
		this.variables.add(startExe);
		this.variables.add(endExe);
		
		Variable startFaultHandling = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.START));
		Variable endFaultHandling = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT_HANDLING, TimeTypeEnum.END));
		this.variables.add(startFaultHandling);
		this.variables.add(endFaultHandling);
		
		Variable startFaultUnCaught = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT_UNCOUGHT, TimeTypeEnum.START));
		Variable endFaultUnCaught = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT_UNCOUGHT, TimeTypeEnum.END));
		this.variables.add(startFaultUnCaught);
		this.variables.add(endFaultUnCaught);

		Variable startFault = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START));
		Variable endFault = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END));
		this.variables.add(startFault);
		this.variables.add(endFault);
		
		Variable startComp = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START));
		Variable endComp = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END));
		this.variables.add(startComp);
		this.variables.add(endComp);
		
		//create intra-state constraints		
		constraints.add(new Constraint(startInitial, endInitial, RelationEnum.LESS));
		constraints.add(new Constraint(startDead, endDead, RelationEnum.LESS));
		constraints.add(new Constraint(startTerminated, endTerminated, RelationEnum.LESS));
		constraints.add(new Constraint(startExe, endExe, RelationEnum.LESS));
		constraints.add(new Constraint(startFaultHandling, endFaultHandling, RelationEnum.LESS));
		constraints.add(new Constraint(startFaultUnCaught, endFaultUnCaught, RelationEnum.LESS));
		constraints.add(new Constraint(startFault, endFault, RelationEnum.LESS));
		constraints.add(new Constraint(startComp, endComp, RelationEnum.LESS));
		
		//create inter-state constraints
		constraints.add(new Constraint(endInitial, startDead, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endInitial, startTerminated, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endInitial, startExe, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endExe, startTerminated, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endExe, startComp, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endExe, startFaultHandling, RelationEnum.EQUALS, RelationEnum.UNRELATED));	
		constraints.add(new Constraint(endFaultHandling, startFault, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endFaultHandling, startFaultUnCaught, RelationEnum.EQUALS, RelationEnum.UNRELATED));
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
		Variable[] variableArray = new Variable[this.variables.size()];
		variableArray = variables.toArray(variableArray);
		return new ScopeConnector(variableArray);
	}

	@Override
	public Constraint[] getLocalConstraints() {
		Constraint[] constraintArray = new Constraint[this.constraints.size()];
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
		
		private Variable[] states;
		
		public ScopeConnector(Variable[] states) {
			this.states = states;
		}

		@Override
		public Variable[] getVariables() {
			return states;
		}
	}


	@Override
	public EObject getEObject() {
		return (EObject) this.scope;
	}

}
