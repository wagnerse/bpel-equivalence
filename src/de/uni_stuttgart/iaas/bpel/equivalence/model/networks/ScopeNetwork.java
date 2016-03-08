package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Catch;
import org.eclipse.bpel.model.Scope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.networks.ScopeNetwork.ScopeConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class ScopeNetwork extends AbstractActivityNetwork{
	
	private Scope scope;
	private Variable[] variables;
	private Constraint[] constraints;
	
	public ScopeNetwork(AbstractActivityNetwork parentNetwork, Scope subject, Problem network) {
		super(parentNetwork, network);
		this.scope = subject;
		initLocalNetwork();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getScope();
	}
	
	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(scope, "name");
		return (attribute instanceof String)? (String) attribute : "[Scope]";
	}
	
	protected void initLocalNetwork() {
		//TODO create local network		
	}
	
	@Override
	protected void initConstraintMap() {
		//TODO create constraint map
	}

	@Override
	public IActivityConnector getActivityConnector() {		
		return new ScopeConnector(variables);
	}

	@Override
	public Constraint[] getLocalLinks() {
		return constraints;
	}

	/**
	 * This class supports Scopes with fault handlers and activities.
	 */
	@Override
	protected Map<EObject, AbstractActivityNetwork> createChildNetworks() {
		Map<EObject, AbstractActivityNetwork> childMap = new HashMap<EObject, AbstractActivityNetwork>();	
		
		// add activity
		AbstractActivityNetwork activity = createChildNetwork(scope.getActivity());
		if (activity != null) childMap.put(this.getEObject(), activity);
		
		if (scope.getFaultHandlers() != null) {
			// add fault handlers
			for (Catch bpelCatch : scope.getFaultHandlers().getCatch()) {
				AbstractActivityNetwork catchNetwork = createChildNetwork(bpelCatch);
				if (catchNetwork != null)
					childMap.put(this.getEObject(), catchNetwork);
			} 
		}
		return childMap;
	}
	
	
	public class ScopeConnector implements IActivityConnector{
		
		private Variable[] states;
		
		public ScopeConnector(Variable[] states) {
			this.states = states;
		}

		@Override
		public Variable[] getVariables() {
			return states;
		}
	}


	@Override
	public EObject getEObject() {
		return (EObject) this.scope;
	}

}
