package de.uni_stuttgart.iaas.bpel.equivalence.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.NetworkFactoryRepo;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;

public abstract class AbstractActivityNetwork {

	private AbstractActivityNetwork parentNetwork;
	protected Map<EObject, AbstractActivityNetwork> childNetworks = new HashMap<EObject, AbstractActivityNetwork>();

	private Problem network;
	private Map<ConstraintMappingKey, RelationEnum[]> constraints = new HashMap<ConstraintMappingKey, RelationEnum[]>();

	@SuppressWarnings("unused")
	private AbstractActivityNetwork() {

	}

	public AbstractActivityNetwork(AbstractActivityNetwork parentNetwork, Problem network) {
		this.parentNetwork = parentNetwork;
		this.network = network;

		// add local links
		network.addConstraints(getLocalLinks());
	}

	public Problem linkActivityNetworkLayer() {
		
		// init local constraint map
		initConstraintMap();

		// create child network objects
		this.childNetworks = createChildNetworks();

		// perfrom pre processing
		doPreProcessing();

		// create links between local and sender
		for (AbstractActivityNetwork childNetwork : this.getChildNetworks()) {
			System.out.println(this.getNetworkName() + ": Create Constraints for child " + childNetwork.getNetworkName());
			for (Variable localVariable : this.getActivityConnector().getVariables()) {
				System.out.println("Create Constraints for " + getNetworkName() + ": " + localVariable.getName());
				for (Variable childVariable : childNetwork.getActivityConnector().getVariables()) {
					// create Key
					ConstraintMappingKey key = new ConstraintMappingKey(
							this, localVariable.getTimePoint(), 
							childNetwork, childVariable.getTimePoint());

					// create constraint link between this activity and the
					// child activity
					RelationEnum[] relations = this.getConnectionConstraints().get(key);
					Constraint constraint = new Constraint(relations);
					constraint.setFrom(localVariable);
					constraint.setTo(childVariable);
					network.addConstraint(constraint);
				}
			}
		}

		// perform post processing
		doPostProcessing();

		// perform child processing
		for (AbstractActivityNetwork cn : this.getChildNetworks()) {
			cn.linkActivityNetworkLayer();
		}

		return network;
	}

	public abstract EClass getSupportedEClass();

	public abstract EObject getEObject();

	public abstract IActivityConnector getActivityConnector();

	public abstract Constraint[] getLocalLinks();

	protected abstract Map<EObject, AbstractActivityNetwork> createChildNetworks();

	protected abstract void initConstraintMap();

	public abstract String getNetworkName();

	public Collection<AbstractActivityNetwork> getChildNetworks() {
		return this.childNetworks.values();
	}

	/**
	 * Add a constraint to the network.
	 * 
	 * @param l
	 * @param r
	 * @param types
	 *            (if types are empty the constraint is unrelated
	 */
	protected void putConstraint(AbstractActivityNetwork n1, TimePointDesc p1, 
			AbstractActivityNetwork n2, TimePointDesc p2, 
			RelationEnum... types) {
		constraints.put(new ConstraintMappingKey(n1, p1, n2, p2), types);
	}

	public Map<ConstraintMappingKey, RelationEnum[]> getConnectionConstraints() {
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

	public AbstractActivityNetwork createChildNetwork(EObject child) {
		if (child != null) {
			return NetworkFactoryRepo.getInstance().createElementNetwork(this, child, getNetwork());
		} else {
			return null;
		}
	}

	protected AbstractActivityNetwork getParentActivityNetwork() {
		return this.parentNetwork;
	}
	
	protected AbstractActivityNetwork getChildNetwork(EObject subject) {
		if (this.childNetworks.containsKey(subject)) {
			return childNetworks.get(subject);
		}
		else {
			return null;
		}
	}
}
