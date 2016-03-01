package de.uni_stuttgart.iaas.bpel.equivalence.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.Variable;
import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalConstraint;

import de.uni_stuttgart.iaas.bpel.equivalence.NetworkFactoryRepo;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;

public abstract class AbstractActivityNetwork {
	
	private AbstractActivityNetwork parentNetwork;
	protected AbstractActivityNetwork[] childNetworks;
	
	private Problem network;
	private Map<Pair<BPELStateEnum, BPELStateEnum>, RelationEnum[]> constraints = new HashMap<Pair<BPELStateEnum, BPELStateEnum>, RelationEnum[]>();


	@SuppressWarnings("unused")
	private AbstractActivityNetwork() {

	}

	public AbstractActivityNetwork(AbstractActivityNetwork parentNetwork, Problem network) {
		initConstraintMap();
		this.parentNetwork = parentNetwork;
		this.network = network;
	}

	public Problem linkActivityNetworkLayer() {
		this.childNetworks = createChildNetworks();

		// perfrom pre processing
		doPreProcessing();

		// add local links
		network.addConstraints(getLocalLinks());

		//TODO create links between local and sender
		/*if (parentNetwork != null) {
			// link network
			for (ActivityState senderState : parentNetwork.getActivityConnector().getConnectionStates()) {
				System.out.println("Create Constraints for " + getNetworkName() + ": " + senderState.getName());
				for (ActivityState localState : this.getActivityConnector().getConnectionStates()) {
					// create Key
					Pair<BPELStateEnum, BPELStateEnum> key = new MutablePair<BPELStateEnum, BPELStateEnum>(
							senderState.getStateType(), localState.getStateType());
					// get constraints
					QualitativeAllenIntervalConstraint.Type[] constraints = parentNetwork.getConnectionConstraints().get(key);

					// create constraint link
					StateConstraint constraint = new StateConstraint(constraints);
					constraint.setFrom(senderState);
					constraint.setTo(localState);
					network.addConstraint(constraint);
				}
			}
		}*/

		// perform post processing
		doPostProcessing();

		// perform child processing
		for (AbstractActivityNetwork cn : getChildNetworks()) {
			cn.linkActivityNetworkLayer();
		}

		return network;
	}

	public abstract EClass getSupportedEClass();
	
	public abstract EObject getEObject();

	public abstract IActivityConnector getActivityConnector();

	public abstract Constraint[] getLocalLinks();

	protected abstract AbstractActivityNetwork[] createChildNetworks();
	
	protected abstract void initConstraintMap();
	
	public abstract String getNetworkName();
	
	public AbstractActivityNetwork[] getChildNetworks() {
		return this.childNetworks;
	}
	
	/**
	 * Add a constraint to the network.
	 * @param l
	 * @param r
	 * @param types (if types are empty the constraint is unrelated
	 */
	protected void putConstraint(BPELStateEnum l, BPELStateEnum r, RelationEnum...types) {
		//TODO check unrelated as empty constraint.
		constraints.put(new MutablePair<BPELStateEnum, BPELStateEnum>(l, r), types);
	}

	public Map<Pair<BPELStateEnum, BPELStateEnum>, RelationEnum[]> getConnectionConstraints() {		
		return constraints;
	}

	protected void doPostProcessing() {
		// not implemented
	}

	protected void doPreProcessing() {
		// not implemented
	}

	protected Problem getNetwork() {
		return network;
	}

	protected AbstractActivityNetwork createChildNetwork(EObject child) {
		
		if (child != null) {		
			return NetworkFactoryRepo.getInstance().createElementNetwork(this, child, getNetwork());
		}
		else {
			return null;
		}
	}
	
	protected AbstractActivityNetwork getParentActivityNetwork() {
		return this.parentNetwork;
	}

	//FIXME delete?
	/*protected StateConstraint createMeetsActivityStateLink(Variable from, Variable to) {
		StateConstraint link = new StateConstraint(FuzzyAllenIntervalConstraint.Type.Meets);
		link.setFrom(from);
		link.setTo(to);
		return link;
	}

	//FIXME delete?
	protected StateConstraint createActivityStateLink(Variable from, Variable to,
			FuzzyAllenIntervalConstraint.Type type) {
		StateConstraint link = new StateConstraint(type);
		link.setFrom(from);
		link.setTo(to);
		return link;
	}*/

}
