package de.uni_stuttgart.iaas.bpel.equivalence.model;

import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.metacsp.framework.Variable;
import org.metacsp.fuzzyAllenInterval.FuzzyAllenIntervalConstraint;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.NetworkFactoryRepo;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityState;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.ActivityStateLink;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BepelState;

public abstract class AbstractActivityNetwork {

	private NetworkSolver network;

	@SuppressWarnings("unused")
	private AbstractActivityNetwork() {

	}

	public AbstractActivityNetwork(NetworkSolver network) {
		this.network = network;
	}

	public NetworkSolver linkActivityNetworkLayer(AbstractActivityNetwork sender) {

		// perfrom pre processing
		doPreProcessing();

		// add local links
		network.addConstraints(getLocalLinks());

		if (sender != null) {
			// link network
			for (ActivityState senderState : sender.getActivityConnector().getConnectionStates()) {
				for (ActivityState localState : this.getActivityConnector().getConnectionStates()) {
					Pair<BepelState, BepelState> key = new MutablePair<BepelState, BepelState>(
							senderState.getStateType(), localState.getStateType());
					QualitativeAllenIntervalConstraint.Type[] constraints = sender.getConnectionTable().get(key);

					ActivityStateLink link = new ActivityStateLink(constraints);
					link.setFrom(senderState);
					link.setTo(localState);

					network.addLink(link);
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

	public abstract Map<Pair<BepelState, BepelState>, QualitativeAllenIntervalConstraint.Type[]> getConnectionTable();

	public abstract IActivityConnector getActivityConnector();

	protected abstract ActivityStateLink[] getLocalLinks();

	protected abstract AbstractActivityNetwork[] getChildNetworks();

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
		return NetworkFactoryRepo.getInstance().createElementNetwork(child, getNetwork());
	}

	protected ActivityStateLink createMeetsActivityStateLink(Variable from, Variable to) {
		ActivityStateLink link = new ActivityStateLink(FuzzyAllenIntervalConstraint.Type.Meets);
		link.setFrom(from);
		link.setTo(to);
		return link;
	}

	protected ActivityStateLink createActivityStateLink(Variable from, Variable to,
			FuzzyAllenIntervalConstraint.Type type) {
		ActivityStateLink link = new ActivityStateLink(type);
		link.setFrom(from);
		link.setTo(to);
		return link;
	}

}
