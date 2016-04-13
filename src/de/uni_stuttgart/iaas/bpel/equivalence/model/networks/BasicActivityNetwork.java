package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Activity;
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
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.BasicActivityNetwork.BasicActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * Creates a point algebra network for the BPEL basic activities
 * receive, reply, invoke, assign, throw, exit, wait, empty
 * 
 * @author Jonas Scheurich
 *
 */
public class BasicActivityNetwork extends AbstractActivityNetwork {

	private EClass support;
	private Activity activity;
	private List<PAVariable> variables = new ArrayList<PAVariable>();
	private List<PAConstraint> constraints = new ArrayList<PAConstraint>();

	public BasicActivityNetwork(AbstractActivityNetwork parentNetwork, EClass support, Activity subject, PANetwork network) {
		super(parentNetwork, network);
		this.support = support;
		this.activity = subject;
		
		initLocalNetwork();
	}
	
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
		PAVariable[] variableArray = new PAVariable[this.variables.size()];
		variableArray = variables.toArray(variableArray);
		return new BasicActivityConnector(variableArray);
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
		
		PAVariable startAborted = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START));
		PAVariable endAborted = this.getNetwork().createVariable(this.getEObject(), new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.END));
		this.variables.add(startAborted);
		this.variables.add(endAborted);
		
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
		constraints.add(new PAConstraint(startAborted, endAborted, RelationEnum.LESS));
		constraints.add(new PAConstraint(startTerminated, endTerminated, RelationEnum.LESS));
		constraints.add(new PAConstraint(startExe, endExe, RelationEnum.LESS));
		constraints.add(new PAConstraint(startFault, endFault, RelationEnum.LESS));
		constraints.add(new PAConstraint(startComp, endComp, RelationEnum.LESS));
		
		//create inter-state constraints for control flow
		constraints.add(new PAConstraint(endInitial, startDead, RelationEnum.EQUALS));
		constraints.add(new PAConstraint(endInitial, startAborted, RelationEnum.EQUALS));
		constraints.add(new PAConstraint(endInitial, startExe, RelationEnum.EQUALS));
		constraints.add(new PAConstraint(endExe, startTerminated, RelationEnum.EQUALS));
		constraints.add(new PAConstraint(endExe, startComp, RelationEnum.EQUALS));
		constraints.add(new PAConstraint(endExe, startFault, RelationEnum.EQUALS));
		
		//create inter-state constraints for exclusive flow
		//successor of init
		constraints.add(new PAConstraint(endDead, endExe, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endAborted, endExe, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endDead, endAborted, RelationEnum.UNRELATED));
		
		//successor of executing
		constraints.add(new PAConstraint(endTerminated, endComp, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endFault, endComp, RelationEnum.UNRELATED));
		constraints.add(new PAConstraint(endTerminated, endFault, RelationEnum.UNRELATED));
				
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
		return childMap;
	}

	public class BasicActivityConnector implements IActivityConnector {

		private PAVariable[] states;

		public BasicActivityConnector(PAVariable[] states) {
			this.states = states;
		}

		@Override
		public PAVariable[] getVariables() {
			return states;
		}
	}

	@Override
	public EObject getEObject() {
		return (EObject) this.activity;
	}

	@Override
	protected void initConstraintMap() {
		// not implemented
		
	}
}
