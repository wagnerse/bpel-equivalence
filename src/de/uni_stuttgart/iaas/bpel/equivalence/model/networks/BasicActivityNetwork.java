package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpel.model.Activity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.BasicActivityNetwork.BasicActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class BasicActivityNetwork extends AbstractActivityNetwork {

	private EClass support;
	private Activity activity;
	private Variable[] variables;
	private Constraint[] constraints;

	public BasicActivityNetwork(AbstractActivityNetwork parentNetwork, EClass support, Activity subject, Problem network) {
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
		return new BasicActivityConnector(variables);
	}
	
	protected void initLocalNetwork() {
		Variable startInitial = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
		Variable endInitial = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.END));
		
		Variable startDead = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START));
		Variable endDead = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END));
		
		Variable startTerminated = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START));
		Variable endTerminated = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END));
		
		Variable startExe = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
		Variable endExe = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END));
		
		Variable startFault = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START));
		Variable endFault = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.END));
		
		Variable startComp = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START));
		Variable endComp = new Variable(this.getEObject(), new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END));
		
		Variable[] variableArray = { 
				startInitial,
				endInitial,
				startDead,
				endDead,
				startTerminated,
				endTerminated,
				startExe,
				endExe,
				startFault,
				endFault,
				startComp,
				endComp
		};
		this.variables = variableArray;
	}

	@Override
	public Constraint[] getLocalLinks() {
		return constraints;
	}

	@Override
	protected Map<EObject, AbstractActivityNetwork> createChildNetworks() {
		Map<EObject, AbstractActivityNetwork> childMap = new HashMap<EObject, AbstractActivityNetwork>();
		return childMap;
	}

	public class BasicActivityConnector implements IActivityConnector {

		private Variable[] states;

		public BasicActivityConnector(Variable[] states) {
			this.states = states;
		}

		@Override
		public Variable[] getVariables() {
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
