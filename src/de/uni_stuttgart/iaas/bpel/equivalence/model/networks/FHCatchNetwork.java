package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Catch;
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
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.FHCatchNetwork.FHConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * Creates a point algebra network for the BPEL activity {@link Catch} (of a fault handler)
 * 
 * @author Jonas Scheurich
 * 
 */
public class FHCatchNetwork extends AbstractActivityNetwork {

	private Catch faultHandlerCatch;
	private Activity activity;
	private List<PAVariable> variables = new ArrayList<PAVariable>();
	private List<PAConstraint> constraints = new ArrayList<PAConstraint>();

	public FHCatchNetwork(AbstractActivityNetwork parentNetwork, Catch subject, PANetwork network) {
		super(parentNetwork, network);
		this.faultHandlerCatch = subject;
		this.activity = faultHandlerCatch.getActivity();
		
		initLocalNetwork();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getCatch();
	}

	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(faultHandlerCatch, "name");
		return (attribute instanceof String) ? (String) attribute : "[FaultHandlerCatch]";
	}

	@Override
	public EObject getEObject() {
		return faultHandlerCatch;
	}

	protected void initLocalNetwork() {
		//create variables
				PAVariable startInitial = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
				this.variables.add(startInitial);
				
				PAVariable startDead = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START));
				this.variables.add(startDead);
				
				PAVariable startAborted = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START));
				this.variables.add(startAborted);
				
				PAVariable startTerminating = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START));
				this.variables.add(startTerminating);
				
				PAVariable startTerminated = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START));
				this.variables.add(startTerminated);
				
				PAVariable startExe = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
				this.variables.add(startExe);
				
				PAVariable startFault = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START));
				this.variables.add(startFault);
				
				PAVariable startComp = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START));
				this.variables.add(startComp);
				
				//create inter-state constraints for control flow
				constraints.add(new PAConstraint(startInitial, startDead, RelationEnum.LESS));
				constraints.add(new PAConstraint(startInitial, startAborted, RelationEnum.LESS));
				constraints.add(new PAConstraint(startInitial, startExe, RelationEnum.LESS));
				constraints.add(new PAConstraint(startExe, startTerminating, RelationEnum.LESS));
				constraints.add(new PAConstraint(startExe, startComp, RelationEnum.LESS));
				constraints.add(new PAConstraint(startExe, startFault, RelationEnum.LESS));
				constraints.add(new PAConstraint(startTerminating, startTerminated, RelationEnum.LESS));
				
				//create inter-state constraints for exclusive flow
				//successor of init
				constraints.add(new PAConstraint(startDead, startExe, RelationEnum.UNRELATED));
				constraints.add(new PAConstraint(startAborted, startExe, RelationEnum.UNRELATED));
				constraints.add(new PAConstraint(startDead, startAborted, RelationEnum.UNRELATED));
				
				//successor of executing
				constraints.add(new PAConstraint(startTerminating, startComp, RelationEnum.UNRELATED));
				constraints.add(new PAConstraint(startFault, startComp, RelationEnum.UNRELATED));
				constraints.add(new PAConstraint(startTerminating, startFault, RelationEnum.UNRELATED));
	}

	@Override
	protected void initConstraintMap() {
		
		AbstractActivityNetwork actNetwork = super.getChildNetwork(activity);
		if (actNetwork == null) return;
		
		// init with fault handler
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), 
				RelationEnum.EQUALS);
		
		// If the fault handler transfers into aborted state,
		// the activity transfers into aborted state
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START), 
				RelationEnum.EQUALS);
		
		// If the fault handler transfers into executing state,
		// the activity transfers into executing state.
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
				RelationEnum.EQUALS);
		
		// If the fault handler transfers into dead state,
		// the activity transfers into dead state.
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
				RelationEnum.EQUALS);
		
		// If the activity transfers into fault state
		// the fault handler transfers into fault state.
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		
		// If the activity transfers into terminating state
		// the fault handler transfers into terminating state.
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		
		// If the activity transfers into completed or fault caught state,
		// the fault handler transfers into completed state.
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.FAULT_CAUGHT, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		
		
	}
	
	@Override
	public IActivityConnector getActivityConnector() {
		PAVariable[] variableArray = new PAVariable[this.variables.size()];
		variableArray = variables.toArray(variableArray);
		return new FHConnector(variableArray);
	}

	@Override
	public PAConstraint[] getLocalConstraints() {
		PAConstraint[] constraintArray = new PAConstraint[this.constraints.size()];
		constraintArray = constraints.toArray(constraintArray);
		return constraintArray;
	}

	@Override
	protected Map<EObject, AbstractActivityNetwork> createChildNetworks() {
		Map<EObject, AbstractActivityNetwork> childMap = new HashMap<EObject, AbstractActivityNetwork>();
		if (this.activity != null) {
			childMap.put(this.getEObject(), createChildNetwork(activity));
		}
		return childMap;
	}
	
	public class FHConnector implements IActivityConnector {

		private PAVariable[] states;

		public FHConnector(PAVariable[] states) {
			this.states = states;
		}

		@Override
		public PAVariable[] getVariables() {
			return states;
		}
	}
}
