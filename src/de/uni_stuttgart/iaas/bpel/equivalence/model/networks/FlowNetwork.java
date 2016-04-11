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
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.FlowNetwork.FlowConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.BPELUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Creates a point algebra network for the BPEL activity {@link Flow}
 */
public class FlowNetwork extends AbstractActivityNetwork {

	private Flow flow;
	private List<PAVariable> variables = new ArrayList<PAVariable>();
	private List<PAConstraint> constraints = new ArrayList<PAConstraint>();
	
	private enum LinkTypeEnum {SINGLE, PARALLEL, EXCLUSIVE};

	public FlowNetwork(AbstractActivityNetwork parentNetwork, Flow subject, PANetwork network) {
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

	/**
	 * Create Constraints for start activities, final activities and the links
	 * 
	 */
	@Override
	protected void initConstraintMap() {
		//FIXME: aborted state
		
		// handle start activities and final activities specialized constraints
		for (Activity act : flow.getActivities()) {
			AbstractActivityNetwork actNetwork = this.getChildNetwork(act);
			if (actNetwork == null) continue;
			
			// handle activity basic constraint map
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), 
					actNetwork, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), 
					RelationEnum.EQUALS);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
					actNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
					RelationEnum.EQUALS, RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), 
					actNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.END), 
					RelationEnum.EQUALS, RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
					actNetwork, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
					RelationEnum.EQUALS, RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
					actNetwork, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
					RelationEnum.EQUALS, RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
					actNetwork, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
					RelationEnum.EQUALS, RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END), 
					actNetwork, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.END), 
					RelationEnum.EQUALS, RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
					actNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
					RelationEnum.EQUALS, RelationEnum.GREATER, RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), 
					actNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.END), 
					RelationEnum.EQUALS, RelationEnum.GREATER);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), 
					actNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
					RelationEnum.UNRELATED, RelationEnum.GREATER);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
					actNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), 
					RelationEnum.UNRELATED, RelationEnum.GREATER);
			
			
			if (act.getTargets() == null ||(act.getTargets() != null && act.getTargets().getChildren().size() == 0)) {
				// handle start activities specialized constraints
				this.putConstraint(
						this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
						actNetwork,	new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), 
						RelationEnum.EQUALS, RelationEnum.UNRELATED);
			}
			if (act.getSources() == null || (act.getSources() != null && act.getSources().getChildren().size() == 0)) {
				// handle final activities specialized constraints
				this.putConstraint(
						this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), 
						actNetwork,	new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), 
						RelationEnum.EQUALS, RelationEnum.GREATER, RelationEnum.UNRELATED);
			}

		}
		
		// handle link constraints
		for (Link link : flow.getLinks().getChildren()) {
			if (link.getSources().size() > 0 && link.getTargets().size() > 0) {
				createLinkConstraints(link, getLinkType(link));
			}
		}

		// handle constraints between xor/parallel activities
		for (Activity act : flow.getActivities()) {
			if (act.getSources() != null) {
				createLinkTargetConstraints(act.getSources(), getLinkType(act.getSources()));
			}
		}
	}

	/**
	 * Create the Constraint Mapping SourceAct.EXECUTING.END [linkConstraints]
	 * TargetAct.EXECUTING.START for all links
	 * 
	 * Create the Constraint Mapping SourceAct.DEAD.START =, ||
	 * TargetAct.DEAD.START for all links
	 * 
	 * @param link
	 */
	private void createLinkConstraints(Link link, LinkTypeEnum linkType) {
		List<Activity> sources = BPELUtils.getSources(link);
		List<Activity> targets = BPELUtils.getTargets(link);
		
		//define constraints
		RelationEnum[] executionConstraints;
		if (linkType == LinkTypeEnum.SINGLE || linkType == LinkTypeEnum.PARALLEL) {
			RelationEnum[] parallel = {RelationEnum.EQUALS};
			executionConstraints = parallel;
		}
		else if (linkType == LinkTypeEnum.EXCLUSIVE) {
			RelationEnum[] exclusive = {RelationEnum.EQUALS, RelationEnum.UNRELATED};
			executionConstraints = exclusive;
		}
		else {
			throw new IllegalStateException();
		}
		RelationEnum[] deadConstraints = {RelationEnum.EQUALS, RelationEnum.UNRELATED};

		// Create constraint from src to trg state of the activities
		for (Activity src : sources) {
			for (Activity trg : targets) {
				AbstractActivityNetwork srcNetwork = super.getChildNetwork(src);
				AbstractActivityNetwork trgNetwork = super.getChildNetwork(trg);
				if (srcNetwork != null && trgNetwork != null) {
					this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START),
							trgNetwork, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START),
							executionConstraints);
					this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), 
							srcNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START),
							deadConstraints);
				}
			}
		}
		
	}
	
	/**
	 * Returns the link type of a Sources element by the BPEL extension element descibet below.
	 * 
	 * <bpel:empty>
	 *    <bpel:sources>
     *       <ext:equivalenceanotation>[exclusive|parallel]</ext:equivalenceanotation>
     *       ...
     *    </bpel:sources>
     * </bpel:empty>
	 * 
	 * @param sources
	 * @return {@link LinkTypeEnum}
	 */
	private LinkTypeEnum getLinkType(Sources sources) {
		if (sources.getChildren().size() < 2) return LinkTypeEnum.SINGLE;
		
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
		
		// select link type
		if (type.equals("paralell")) {
			return LinkTypeEnum.PARALLEL;
		}
		else if (type.equals("exclusive")) {
			return LinkTypeEnum.EXCLUSIVE;
		}
		else {
			throw new IllegalStateException("Unkown equivalenceanotation type");
		}
	}
	
	private LinkTypeEnum getLinkType(Link link) {
		// get the sources object witch this link is part of
		Sources sources = link.getSources().get(0).getActivity().getSources();
		return getLinkType(sources);
	}
	/**
	 * Create a constraint edge between all following activities.
	 * If the branching condition is exclusive a {@link RelationEnum.UNRELATED} relation
	 * If the branching condition is parallel a {@link RelationEnum.EQUALS} relation
	 * @param sources
	 */
	private void createLinkTargetConstraints(Sources sources, LinkTypeEnum linkType) {
		if (linkType == LinkTypeEnum.SINGLE) return;
		
		// get extension element (<paralell/>  or <exclusive/>
		RelationEnum relation;
		if (linkType == LinkTypeEnum.PARALLEL) {
			relation = RelationEnum.EQUALS;
		}
		else if (linkType == LinkTypeEnum.EXCLUSIVE) {
			relation = RelationEnum.UNRELATED;
		}
		else {
			throw new IllegalStateException();
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
		PAVariable[] variableArray = new PAVariable[this.variables.size()];
		variableArray = variables.toArray(variableArray);
		return new FlowConnector(variableArray);
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

		// add activities
		for (EObject a : flow.getActivities()) {
			AbstractActivityNetwork activity = createChildNetwork(a);
			if (activity != null)
				childMap.put(a, activity);
		}
		return childMap;
	}

	public class FlowConnector implements IActivityConnector {

		private PAVariable[] states;

		public FlowConnector(PAVariable[] states) {
			this.states = states;
		}

		@Override
		public PAVariable[] getVariables() {
			return states;
		}
	}

}
