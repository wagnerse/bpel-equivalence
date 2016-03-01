package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import org.eclipse.bpel.model.Activity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class BasicActivityNetwork extends AbstractActivityNetwork {

	private EClass support;
	private Activity activity;
	private Variable[] variables;
	private Constraint[] constraints;

	public BasicActivityNetwork(AbstractActivityNetwork parentNetwork, EClass support, Activity subject, Problem network) {
		super(parentNetwork, network);
		this.support = support;
		this.activity = subject;
		initNetwork();
	}
	
	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(activity, "name");
		return (attribute instanceof String)? (String) attribute : "[Activity]";
	}

	@Override
	public EClass getSupportedEClass() {
		return support;
	}

	@Override
	public IActivityConnector getActivityConnector() {
		return new BasicActivityConnector(variables);
	}

	@Override
	public Constraint[] getLocalLinks() {
		return constraints;
	}

	@Override
	protected AbstractActivityNetwork[] createChildNetworks() {
		AbstractActivityNetwork[] childs = {};
		return childs;
	}

	private void initNetwork() {
		//TODO init local network
	}

	public class BasicActivityConnector implements IActivityConnector {

		private Variable[] states;

		public BasicActivityConnector(Variable[] states) {
			this.states = states;
		}

		@Override
		public Variable[] getVariables() {
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
