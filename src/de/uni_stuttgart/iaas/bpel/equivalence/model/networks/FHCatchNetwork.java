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
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;
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
	private List<Variable> variables = new ArrayList<Variable>();
	private List<Constraint> constraints = new ArrayList<Constraint>();

	public FHCatchNetwork(AbstractActivityNetwork parentNetwork, Catch subject, Problem network) {
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
		constraints.add(new Constraint(startFault, endFault, RelationEnum.LESS));
		constraints.add(new Constraint(startComp, endComp, RelationEnum.LESS));
		
		//create inter-state constraints
		constraints.add(new Constraint(endInitial, startDead, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endInitial, startTerminated, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endInitial, startExe, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endExe, startTerminated, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endExe, startComp, RelationEnum.EQUALS, RelationEnum.UNRELATED));
		constraints.add(new Constraint(endExe, startFault, RelationEnum.EQUALS, RelationEnum.UNRELATED));	
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
		Variable[] variableArray = new Variable[this.variables.size()];
		variableArray = variables.toArray(variableArray);
		return new FHConnector(variableArray);
	}

	@Override
	public Constraint[] getLocalConstraints() {
		Constraint[] constraintArray = new Constraint[this.constraints.size()];
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

		private Variable[] states;

		public FHConnector(Variable[] states) {
			this.states = states;
		}

		@Override
		public Variable[] getVariables() {
			return states;
		}
	}
}
