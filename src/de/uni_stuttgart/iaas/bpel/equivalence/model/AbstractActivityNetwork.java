package de.uni_stuttgart.iaas.bpel.equivalence.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.Variable;
import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalConstraint;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.NetworkFactoryRepo;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BPELStateEnum;

public abstract class AbstractActivityNetwork {

	private NetworkSolver network;
	private Map<Pair<BPELStateEnum, BPELStateEnum>, Type[]> constraints = new HashMap<Pair<BPELStateEnum, BPELStateEnum>, Type[]>();


	@SuppressWarnings("unused")
	private AbstractActivityNetwork() {

	}

	public AbstractActivityNetwork(NetworkSolver network) {
		initConstraintMap();
		this.network = network;
	}

	public NetworkSolver linkActivityNetworkLayer(AbstractActivityNetwork parent) {

		// perfrom pre processing
		doPreProcessing();

		// add local links
		network.addConstraints(getLocalLinks());

		if (parent != null) {
			// link network
			for (ActivityState senderState : parent.getActivityConnector().getConnectionStates()) {
				for (ActivityState localState : this.getActivityConnector().getConnectionStates()) {
					Pair<BPELStateEnum, BPELStateEnum> key = new MutablePair<BPELStateEnum, BPELStateEnum>(
							senderState.getStateType(), localState.getStateType());
					QualitativeAllenIntervalConstraint.Type[] constraints = parent.getConnectionConstraints().get(key);

					StateConstraint constraint = new StateConstraint(constraints);
					constraint.setFrom(senderState);
					constraint.setTo(localState);

					network.addConstraint(constraint);
				}
			}
		}

		// perform post processing
		doPostProcessing();

		// perform child processing
		for (AbstractActivityNetwork cn : getChildNetworks()) {
			cn.linkActivityNetworkLayer(this);
		}

		return network;
	}

	public abstract EClass getSupportedEClass();
	
	public abstract EObject getEObject();

	public abstract IActivityConnector getActivityConnector();

	protected abstract StateConstraint[] getLocalLinks();

	protected abstract AbstractActivityNetwork[] getChildNetworks();
	
	protected abstract void initConstraintMap();
	
	protected void putConstraint(BPELStateEnum l, BPELStateEnum r, Type...types) {
		constraints.put(new MutablePair<BPELStateEnum, BPELStateEnum>(l, r), types);
	}

	public Map<Pair<BPELStateEnum, BPELStateEnum>, Type[]> getConnectionConstraints() {		
		return constraints;
	}

	protected void doPostProcessing() {
		// not implemented
	}

	protected void doPreProcessing() {
		// not implemented
	}

	protected NetworkSolver getNetwork() {
		return network;
	}

	protected AbstractActivityNetwork createChildNetwork(EObject child) {
		
		if (child != null) {		
			return NetworkFactoryRepo.getInstance().createElementNetwork(child, getNetwork());
		}
		else {
			return null;
		}
	}

	protected StateConstraint createMeetsActivityStateLink(Variable from, Variable to) {
		StateConstraint link = new StateConstraint(FuzzyAllenIntervalConstraint.Type.Meets);
		link.setFrom(from);
		link.setTo(to);
		return link;
	}

	protected StateConstraint createActivityStateLink(Variable from, Variable to,
			FuzzyAllenIntervalConstraint.Type type) {
		StateConstraint link = new StateConstraint(type);
		link.setFrom(from);
		link.setTo(to);
		return link;
	}

}
