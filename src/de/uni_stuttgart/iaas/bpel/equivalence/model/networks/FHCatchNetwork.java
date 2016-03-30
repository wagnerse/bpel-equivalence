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
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.FHCatchNetwork.FHConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Creates a point algebra network for the BPEL activity {@link Catch} (of a fault handler)
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
		constraints.add(new PAConstraint(startFault, endFault, RelationEnum.LESS));
		constraints.add(new PAConstraint(startComp, endComp, RelationEnum.LESS));
		
		//create inter-state constraints
		constraints.add(new PAConstraint(endInitial, startDead, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endInitial, startTerminated, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endInitial, startExe, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endExe, startTerminated, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endExe, startComp, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endExe, startFault, RelationEnum.EQUALS, RelationEnum.UNRELATED));	

		//create inter-state constraints for exclusive flow
		//successor of init
		constraints.add(new PAConstraint(startDead, startExe, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startExe, startTerminated, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startDead, startTerminated, RelationEnum.UNRELATED));
		
		//successor of executing
		constraints.add(new PAConstraint(startTerminated, startComp, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startTerminated, startFault, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(startFault, startComp, RelationEnum.UNRELATED));
	}

	@Override
	protected void initConstraintMap() {
		AbstractActivityNetwork actNetwork = super.getChildNetwork(activity);
		if (actNetwork == null) return;
		
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), 
				RelationEnum.EQUALS);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), 
				actNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END), 
				actNetwork, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END), 
				actNetwork, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
				actNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
				RelationEnum.EQUALS, RelationEnum.UNRELATED);
		this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), 
				actNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), 
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
