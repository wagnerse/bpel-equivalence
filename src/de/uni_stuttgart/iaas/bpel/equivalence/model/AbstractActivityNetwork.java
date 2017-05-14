package de.uni_stuttgart.iaas.bpel.equivalence.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.NetworkFactoryRepo;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;

/**
 *  A activity network creates three parts of the point algebra network for a specific BPEL element
 * (1) Local variables and constraints for the state model of the supported BPEL element
 *     (inter state constraints and intra state constraints).
 * (2) Constraints between the local variables and the variables of the child BPEL elements.
 *     ex: flow and the containing activities (inter activity constraints).
 * (3) Constraints between the variables of the  child BPEL elements (inter activity constraints).
 *     ex: link between activities of a flow.
 *
 * @author Jonas Scheurich
 * 
 */
public abstract class AbstractActivityNetwork {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private AbstractActivityNetwork parentNetwork;
	protected Map<Object, AbstractActivityNetwork> childNetworks = new HashMap<Object, AbstractActivityNetwork>();

	private PANetwork network;
	private Map<ConstraintMappingKey, RelationEnum[]> constraintsMapping = new HashMap<ConstraintMappingKey, RelationEnum[]>();

	@SuppressWarnings("unused")
	private AbstractActivityNetwork() {

	}

	public AbstractActivityNetwork(AbstractActivityNetwork parentNetwork, PANetwork network) {
		this.parentNetwork = parentNetwork;
		this.network = network;

	}

	/**
	 * Create the point algebra network of the supported BPEL element and the child elements (recursive)
	 * 
	 * @return A point algebra network
	 */
	public PANetwork linkActivityNetworkLayer() {
		LOGGER.info("Create Network for " + this.getNetworkName());

		// create child network objects
		this.childNetworks = createChildNetworks();
		
		// add local links
		network.addConstraints(getLocalConstraints());
		
		// init local constraint map
		initConstraintMap();
		
		// perfrom pre processing
		doPreProcessing();

		// create links between local and children
		for (AbstractActivityNetwork childNetwork : this.getChildNetworks()) {
			createConstraintsBetween(this, childNetwork);
		}
		
		//create links between the children
		for (AbstractActivityNetwork actNet1: this.getChildNetworks()) {
			for (AbstractActivityNetwork actNet2: this.getChildNetworks()) {
				if (actNet1 != actNet2) {
					createConstraintsBetween(actNet1, actNet2);
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
	
	/**
	 * Create constraints between two activity networks. Defined in the constraint mapping.
	 * @param actNet1
	 * @param actNet2
	 */
	public void createConstraintsBetween(AbstractActivityNetwork actNet1, AbstractActivityNetwork actNet2) {
		
		for (PAVariable var1 : actNet1.getActivityConnector().getVariables()) {			
			for (PAVariable var2 : actNet2.getActivityConnector().getVariables()) {
				// create Key
				ConstraintMappingKey key = new ConstraintMappingKey(
						actNet1, var1.getTimePoint(), 
						actNet2, var2.getTimePoint());
				
				if (this.constraintsMapping.containsKey(key)) {
					// create constraint link between this activity and the
					// child activity
					RelationEnum[] relations = this.constraintsMapping.get(key);
					PAConstraint constraint = new PAConstraint(relations);
					constraint.setFrom(var1);
					constraint.setTo(var2);
					this.getNetwork().addConstraint(constraint);
				}
			}
		}
	}

	/**
	 * Get net class object of the supported class to determine witch BPEL4Chor element
	 * is supported by this object
	 * 
	 * @return {@link Class<?>}
	 */
	public abstract Class<?> getSupportedClass();

	/**
	 * Get the specific {@link Object}, are the point network is created from.
	 * @return
	 */
	public abstract Object getObject();

	/**
	 * Get the activity connector of this activity network, that contains the local variables.
	 * @return
	 */
	public abstract IActivityConnector getActivityConnector();

	/**
	 * Get the constraints between the local variables:
	 * (1) Intra-state-constraints
	 * (2) Inter-state-constraints
	 * 
	 * @return
	 */
	public abstract PAConstraint[] getLocalConstraints();

	/**
	 * Create the activity network of the BPEL child elements of this BPEL element.
	 * Every child network is related to a BPEL child element.
	 * @return
	 */
	protected abstract Map<Object, AbstractActivityNetwork> createChildNetworks();

	/**
	 * Initialize the constraint map with a description of the inter activity constraints.
	 */
	protected abstract void initConstraintMap();

	/**
	 * Get the name of this activity network.
	 * @return
	 */
	public abstract String getNetworkName();
	
	/**
	 * Perform some actions before the constraints are created
	 */
	protected void doPostProcessing() {
		// not implemented
	}

	/**
	 * Perform some actions after the constraints are created
	 */
	protected void doPreProcessing() {
		// not implemented
	}

	/**
	 * Add a constraint to the constraint mapping
	 * 
	 * @param l
	 * @param r
	 * @param types
	 */
	protected void putConstraint(AbstractActivityNetwork n1, TimePointDesc p1, 
			AbstractActivityNetwork n2, TimePointDesc p2, 
			RelationEnum... types) {
		constraintsMapping.put(new ConstraintMappingKey(n1, p1, n2, p2), types);
	}

	/**
	 * Get the constraint mapping
	 * @return
	 */
	public Map<ConstraintMappingKey, RelationEnum[]> getConstraintMapping() {
		return constraintsMapping;
	}

	/**
	 * Get the current point algebra network
	 * @return
	 */
	protected PANetwork getNetwork() {
		return network;
	}

	/**
	 * Create a child network from a given {@link EObject}
	 * @param child
	 * @return
	 */
	public AbstractActivityNetwork createChildNetwork(EObject child) {
		if (child != null) {
			return NetworkFactoryRepo.getInstance().createElementNetwork(this, child, getNetwork());
		} else {
			return null;
		}
	}
	

	/**
	 * Get all child networks.
	 * @param subject
	 * @return
	 */
	protected AbstractActivityNetwork getChildNetwork(EObject subject) {
		if (this.childNetworks.containsKey(subject)) {
			return childNetworks.get(subject);
		}
		else {
			return null;
		}
	}
	
	
	
	/**
	 * Get the child networks of this network, created from the BPEL child elements
	 * @return
	 */
	public Collection<AbstractActivityNetwork> getChildNetworks() {
		return this.childNetworks.values();
	}
	 

	/**
	 * Get the parent activity network of this activity network
	 * @return
	 */
	protected AbstractActivityNetwork getParentActivityNetwork() {
		return this.parentNetwork;
	}
	
}
