package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Flow;
import org.eclipse.bpel.model.Link;
import org.eclipse.bpel.model.Source;
import org.eclipse.bpel.model.Sources;
import org.eclipse.bpel.model.Target;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.FlowNetwork.FlowConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class FlowNetwork extends AbstractActivityNetwork {

	private Flow flow;
	private List<Variable> variables = new ArrayList<Variable>();
	private List<Constraint> constraints = new ArrayList<Constraint>();

	public FlowNetwork(AbstractActivityNetwork parentNetwork, Flow subject, Problem network) {
		super(parentNetwork, network);
		this.flow = subject;

		initLocalNetwork();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getFlow();
	}

	@Override
	public EObject getEObject() {
		return flow;
	}

	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(flow, "name");
		return (attribute instanceof String) ? (String) attribute : "[Flow]";
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
		constraints.add(new Constraint(endInitial, startDead, RelationEnum.EQUALS));
		constraints.add(new Constraint(endInitial, startTerminated, RelationEnum.EQUALS));
		constraints.add(new Constraint(endInitial, startExe, RelationEnum.EQUALS));
		constraints.add(new Constraint(endExe, startTerminated, RelationEnum.EQUALS));
		constraints.add(new Constraint(endExe, startComp, RelationEnum.EQUALS));
		constraints.add(new Constraint(endExe, startFault, RelationEnum.EQUALS));
	}

	/**
	 * Create Constraints for start activities, final activities and the links
	 * 
	 */
	@Override
	protected void initConstraintMap() {
		// handle start activities and final activities
		for (Activity act : flow.getActivities()) {
			if (act.getTargets() == null ||(act.getTargets() != null && act.getTargets().getChildren().size() == 0)) {
				// handle start activities
				AbstractActivityNetwork actNetwork = this.getChildNetwork(act);
				if (actNetwork != null) {
					this.putConstraint(
							this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
							actNetwork,	new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
							RelationEnum.EQUALS);
				}
			}
			if (act.getSources() == null || (act.getSources() != null && act.getSources().getChildren().size() == 0)) {
				// handle final activities
				AbstractActivityNetwork actNetwork = this.getChildNetwork(act);
				if (actNetwork != null) {
					this.putConstraint(
							this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), 
							actNetwork,	new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), 
							RelationEnum.EQUALS, RelationEnum.GREATER);
				}
			}

		}
		
		// handle links
		for (Link link : flow.getLinks().getChildren()) {
			if (link.getSources().size() > 0 && link.getTargets().size() > 0) {
				createLinkConstraints(link);
			}
		}

		// create relation between xor/parallel activities
		for (Activity act : flow.getActivities()) {
			if (act.getSources() != null && act.getSources().getChildren().size() >= 2) {
				createXorParallelConstraints(act.getSources());
			}
		}
	}

	/**
	 * Create the Constraint Mapping SourceAct.EXECUTING.END =
	 * TargetAct.EXECUTING.START for all links
	 * 
	 * @param link
	 */
	private void createLinkConstraints(Link link) {
		List<Activity> sources = BPELUtils.getSources(link);
		List<Activity> targets = BPELUtils.getTargets(link);

		// Create constraint from src to trg state of the activities
		for (Activity src : sources) {
			for (Activity trg : targets) {
				AbstractActivityNetwork srcNetwork = super.getChildNetwork(src);
				AbstractActivityNetwork trgNetwork = super.getChildNetwork(trg);
				if (srcNetwork != null && trgNetwork != null) {
					this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END),
							trgNetwork, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START),
							RelationEnum.EQUALS);
				}
			}
		}
		
	}
	
	/**
	 * Create a constraint edge between all following activities.
	 * If the branching condition is exclusive a {@link RelationEnum.UNRELATED} relation
	 * If the branching condition is parallel a {@link RelationEnum.EQUALS} relation
	 * 
	 * The branching condition should be anotated by an BPEL extension of the activity:
	 * 
	 * <bpel:empty>
	 *    <bpel:sources>
     *       <ext:equivalenceanotation>[exclusive|parallel]</ext:equivalenceanotation>
     *       ...
     *    </bpel:sources>
     * </bpel:empty>
	 * 
	 * @param sources
	 */
	private void createXorParallelConstraints(Sources sources) {
		// get extension element (<paralell/>  or <exclusive/>
		String type = "paralell";
		for (Object ext: sources.getEExtensibilityElements()) {
			if (ext instanceof UnknownExtensibilityElement) {
				org.w3c.dom.Element element = ((UnknownExtensibilityElement) ext).getElement();
				if (element.getLocalName().equals("equivalenceanotation")) {
					type = element.getTextContent();
				}
			}
		}
		
		// select relation
		RelationEnum relation;
		if (type.equals("paralell")) {
			relation = RelationEnum.EQUALS;
		}
		else if (type.equals("exclusive")) {
			relation = RelationEnum.UNRELATED;
		}
		else {
			throw new IllegalStateException("Unkown equivalenceanotation type");
		}
		
		// get targets of the outgoing links of this source
		List<Activity> targetList = new ArrayList<Activity>();
		for (Source src: sources.getChildren()) {
			for (Target trg: src.getLink().getTargets()) {
				targetList.add(trg.getActivity());
			}
		}
		// create relation between all targets of the links
		for(Activity act1: targetList) {
			for(Activity act2: targetList) {
				if (act1 != act2) {
					AbstractActivityNetwork n1 = this.getChildNetwork(act1);
					AbstractActivityNetwork n2 = this.getChildNetwork(act2);
					this.putConstraint(
							n1, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
							n2,	new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
							relation);
				}
			}
		}
	}

	@Override
	public IActivityConnector getActivityConnector() {
		Variable[] variableArray = new Variable[this.variables.size()];
		variableArray = variables.toArray(variableArray);
		return new FlowConnector(variableArray);
	}

	@Override
	public Constraint[] getLocalLinks() {
		Constraint[] constraintArray = new Constraint[this.constraints.size()];
		constraintArray = constraints.toArray(constraintArray);
		return constraintArray;
	}

	@Override
	protected Map<EObject, AbstractActivityNetwork> createChildNetworks() {
		Map<EObject, AbstractActivityNetwork> childMap = new HashMap<EObject, AbstractActivityNetwork>();

		// add activities
		for (EObject a : flow.getActivities()) {
			AbstractActivityNetwork activity = createChildNetwork(a);
			if (activity != null)
				childMap.put(a, activity);
		}
		return childMap;
	}

	public class FlowConnector implements IActivityConnector {

		private Variable[] states;

		public FlowConnector(Variable[] states) {
			this.states = states;
		}

		@Override
		public Variable[] getVariables() {
			return states;
		}
	}

}
