package de.uni_stuttgart.iaas.bpel.equivalence.model;

import java.util.HashMap;
import java.util.Map;

import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;

public abstract class AbstractDefaultActivityNetwork extends AbstractActivityNetwork{

	public AbstractDefaultActivityNetwork(AbstractActivityNetwork parentNetwork, Problem network) {
		super(parentNetwork, network);
	}

	@Override
	public Map<ConstraintMappingKey, RelationEnum[]> getConnectionConstraints() {
		return new HashMap<ConstraintMappingKey, RelationEnum[]>();
	}

	@Override
	public IActivityConnector getActivityConnector() {
		return new DefaultConnector();
	}

	@Override
	public Constraint[] getLocalLinks() {
		Constraint[] stateLinks = {};
		return stateLinks;
	}
	
	public class DefaultConnector implements IActivityConnector {

		private Variable[] states = {};

		public DefaultConnector() {
		}

		@Override
		public Variable[] getVariables() {
			return states;
		}

	}

}
