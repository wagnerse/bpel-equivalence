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
 * Creates a point algebra network for the BPEL basic activities receive, reply,
 * invoke, assign, throw, exit, wait, empty
 * 
 * @author Jonas Scheurich
 *
 */
public class BasicActivityNetwork extends AbstractActivityNetwork {

	private EClass support;
	private Activity activity;
	private List<PAVariable> variables = new ArrayList<PAVariable>();
	private List<PAConstraint> constraints = new ArrayList<PAConstraint>();

	public BasicActivityNetwork(AbstractActivityNetwork parentNetwork, EClass support, Activity subject,
			PANetwork network) {
		super(parentNetwork, network);
		this.support = support;
		this.activity = subject;

		initLocalNetwork();
	}

	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(activity, "name");
		return (attribute instanceof String) ? (String) attribute : "[Activity]";
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
