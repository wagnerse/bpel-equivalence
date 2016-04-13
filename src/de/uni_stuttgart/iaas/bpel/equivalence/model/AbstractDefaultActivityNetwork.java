package de.uni_stuttgart.iaas.bpel.equivalence.model;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;

/**
 * This activity network supports BPEL elements without a local network.
 *
 * @author Jonas Scheurich
 * 
 */
public abstract class AbstractDefaultActivityNetwork extends AbstractActivityNetwork{

	public AbstractDefaultActivityNetwork(AbstractActivityNetwork parentNetwork, PANetwork network) {
		super(parentNetwork, network);
	}

	@Override
	public Map<ConstraintMappingKey, RelationEnum[]> getConstraintMapping() {
		return new HashMap<ConstraintMappingKey, RelationEnum[]>();
	}

	@Override
	public IActivityConnector getActivityConnector() {
		return new DefaultConnector();
	}

	@Override
	public PAConstraint[] getLocalConstraints() {
		PAConstraint[] stateLinks = {};
		return stateLinks;
	}
	
	public class DefaultConnector implements IActivityConnector {

		private PAVariable[] states = {};

		public DefaultConnector() {
		}

		@Override
		public PAVariable[] getVariables() {
			return states;
		}

	}

}
