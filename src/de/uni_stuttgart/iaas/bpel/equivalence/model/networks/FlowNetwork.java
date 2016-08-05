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
 * Creates a point algebra network for the BPEL activity {@link Flow}
 * 
 * @author Jonas Scheurich
 * 
 */
public class FlowNetwork extends AbstractActivityNetwork {

	private Flow flow;
	private List<PAVariable> variables = new ArrayList<PAVariable>();
	private List<PAConstraint> constraints = new ArrayList<PAConstraint>();

	private enum LinkTypeEnum {
		SINGLE, PARALLEL, EXCLUSIVE
	};

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

		PAVariable startComp = this.getNetwork().createVariable(this.getEObject(),
				new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START));
		this.variables.add(startComp);

		// create inter-state constraints for control flow
		constraints.add(new PAConstraint(startInitial, startDead, RelationEnum.LESS));
		constraints.add(new PAConstraint(startInitial, startAborted, RelationEnum.LESS));
		constraints.add(new PAConstraint(startInitial, startExe, RelationEnum.LESS));
		constraints.add(new PAConstraint(startExe, startTerminating, RelationEnum.LESS));
		constraints.add(new PAConstraint(startExe, startComp, RelationEnum.LESS));
		constraints.add(new PAConstraint(startExe, startFault, RelationEnum.LESS));
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
	}

	/**
	 * Create Constraints for start activities, final activities and the links
	 * 
	 */
	@Override
	protected void initConstraintMap() {

		// handle start activities and final activities specialized constraints
		for (Activity act : flow.getActivities()) {
			AbstractActivityNetwork actNetwork = this.getChildNetwork(act);
			if (actNetwork == null)
				continue;

			// handle activity basic constraint map
			// the activity transfers into intit, if the flow transfers into init
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START), RelationEnum.EQUALS, RelationEnum.UNRELATED);

			// the activity transfers into aborted or terminating, if the flow
			// transfers into aborted
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START), RelationEnum.EQUALS);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), RelationEnum.EQUALS, RelationEnum.UNRELATED);

			// the activity transfers into dead, if the flow
			// transfers into dead
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), RelationEnum.EQUALS);

			// if the activity transfers into fault, the flow transfers into
			// fault
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), RelationEnum.EQUALS, RelationEnum.UNRELATED);

			// activity transfers into terminating, if the flow
			// transfers into fault
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), RelationEnum.EQUALS, RelationEnum.UNRELATED);

			// the activity transfers into aborted or terminating, if the flow
			// transfers
			// into terminating
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START), RelationEnum.EQUALS, RelationEnum.UNRELATED);
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.TERMINATING, TimeTypeEnum.START), RelationEnum.EQUALS, RelationEnum.UNRELATED);

			// the flow transfers into terminated when, of after a activity
			// transfers into terminated
			this.putConstraint(this, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), actNetwork,
					new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START), 
					RelationEnum.EQUALS, RelationEnum.GREATER, RelationEnum.UNRELATED);

			
			
			// handle unsynchronized and end activities
			if (act.getTargets() == null || (act.getTargets() != null && act.getTargets().getChildren().size() == 0)) {
				// handle start activities specialized constraints
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), actNetwork,
						new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START), RelationEnum.EQUALS);
			}
			if (act.getSources() == null || (act.getSources() != null && act.getSources().getChildren().size() == 0)) {
				// handle final activities specialized constraints
				this.putConstraint(this, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), actNetwork,
						new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START), RelationEnum.EQUALS,
						RelationEnum.GREATER);
			}
		}

		// handle link constraints with DPE and fault2aborted
		if (flow.getLinks() != null) {
			for (Link link : flow.getLinks().getChildren()) {
				if (link.getSources().size() > 0 && link.getTargets().size() > 0) {
					createLinkConstraints(link, getLinkType(link));
				}
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
	 * Create the Constraint Mapping SourceAct.COMPLETED.START [linkConstraints]
	 * TargetAct.EXECUTING.START for all links
	 * 
	 * Create the Constraint Mapping SourceAct.DEAD.START =
	 * TargetAct.DEAD.START for all links
	 * 
	 * Create the Constraint Mapping SourceAct.FAULT.START =
	 * TargetAct.ABORTED.START
	 * 
	 * @param link
	 */
	private void createLinkConstraints(Link link, LinkTypeEnum linkType) {
		List<Activity> sources = BPELUtils.getSources(link);
		List<Activity> targets = BPELUtils.getTargets(link);

		// define constraints
		RelationEnum[] executionConstraints;
		if (linkType == LinkTypeEnum.SINGLE || linkType == LinkTypeEnum.PARALLEL) {
			RelationEnum[] parallel = { RelationEnum.EQUALS };
			executionConstraints = parallel;
		} else if (linkType == LinkTypeEnum.EXCLUSIVE) {
			RelationEnum[] exclusive = { RelationEnum.EQUALS };
			executionConstraints = exclusive;
		} else {
			throw new IllegalStateException();
		}
		RelationEnum[] deadConstraints = {RelationEnum.EQUALS};
		RelationEnum[] faultConstraints = {RelationEnum.EQUALS};

		// Create constraint from src to trg state of the activities
		for (Activity src : sources) {
			for (Activity trg : targets) {
				AbstractActivityNetwork srcNetwork = super.getChildNetwork(src);
				AbstractActivityNetwork trgNetwork = super.getChildNetwork(trg);
				if (srcNetwork != null && trgNetwork != null) {
					// create flow mapping
					this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START),
							trgNetwork, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START),
							executionConstraints);
					
					// create DPE mapping
					//FIXME trgNetwork
					this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START),
							srcNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START), deadConstraints);
					
					//create fault mapping
					//FIXME trgNetwork
					this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START),
							srcNetwork, new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START), faultConstraints);
				}
			}
		}

	}

	/**
	 * Returns the link type of a Sources element by the BPEL extension element
	 * descibet below.
	 * 
	 * <bpel:empty> 
	 *   <bpel:sources> 
	 *     <ext:equivalenceanotation>[exclusive|parallel]</ext:equivalenceanotation> 
	 *     ... 
	 *   </bpel:sources>
	 * </bpel:empty>
	 * 
	 * @param sources
	 * @return {@link LinkTypeEnum}
	 */
	private LinkTypeEnum getLinkType(Sources sources) {
		if (sources.getChildren().size() < 2)
			return LinkTypeEnum.SINGLE;

		// get extension element (<paralell/> or <exclusive/>
		String type = "paralell";
		for (Object ext : sources.getEExtensibilityElements()) {
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
		} else if (type.equals("exclusive")) {
			return LinkTypeEnum.EXCLUSIVE;
		} else {
			throw new IllegalStateException("Unkown equivalenceanotation type");
		}
	}

	private LinkTypeEnum getLinkType(Link link) {
		// get the sources object witch this link is part of
		Sources sources = link.getSources().get(0).getActivity().getSources();
		return getLinkType(sources);
	}

	/**
	 * Create a constraint edge between all following activities. If the
	 * branching condition is exclusive a {@link RelationEnum.UNRELATED}
	 * relation If the branching condition is parallel a
	 * {@link RelationEnum.EQUALS} relation
	 * 
	 * @param sources
	 */
	private void createLinkTargetConstraints(Sources sources, LinkTypeEnum linkType) {
		if (linkType == LinkTypeEnum.SINGLE)
			return;

		// get extension element (<paralell/> or <exclusive/>
		RelationEnum[] relations;
		if (linkType == LinkTypeEnum.PARALLEL) {
			RelationEnum[] relList = { RelationEnum.LESS, RelationEnum.EQUALS, RelationEnum.GREATER };
			relations = relList;
		} else if (linkType == LinkTypeEnum.EXCLUSIVE) {
			RelationEnum[] relList = { RelationEnum.UNRELATED };
			relations = relList;
		} else {
			throw new IllegalStateException();
		}

		// get targets of the outgoing links of this source
		List<Activity> targetList = new ArrayList<Activity>();
		for (Source src : sources.getChildren()) {
			for (Target trg : src.getLink().getTargets()) {
				targetList.add(trg.getActivity());
			}
		}
		// create relation between all targets of the links
		for (Activity act1 : targetList) {
			for (Activity act2 : targetList) {
				if (act1 != act2) {
					AbstractActivityNetwork n1 = this.getChildNetwork(act1);
					AbstractActivityNetwork n2 = this.getChildNetwork(act2);
					this.putConstraint(n1, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), n2,
							new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.END), relations);
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
