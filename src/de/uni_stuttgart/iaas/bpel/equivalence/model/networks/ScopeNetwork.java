package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Catch;
import org.eclipse.bpel.model.Scope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Constraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class ScopeNetwork extends AbstractActivityNetwork{
	
	private Scope scope;
	private Variable[] activityStates;
	private Constraint[] activityStateLinks;
	
	public ScopeNetwork(AbstractActivityNetwork parentNetwork, Scope subject, Problem network) {
		super(parentNetwork, network);
		this.scope = subject;
		initNetwork();
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
	
	private void initNetwork() {
		//TODO create local network		
	}
	
	@Override
	protected void initConstraintMap() {
		//TODO create constraint map
	}

	@Override
	public IActivityConnector getActivityConnector() {		
		return new ScopeConnector(activityStates);
	}

	@Override
	public Constraint[] getLocalLinks() {
		return activityStateLinks;
	}

	/**
	 * This class supports Scopes with fault handlers and activities.
	 */
	@Override
	protected AbstractActivityNetwork[] createChildNetworks() {
		List<AbstractActivityNetwork> childList = new ArrayList<AbstractActivityNetwork>();	
		
		// add activity
		AbstractActivityNetwork activity = createChildNetwork(scope.getActivity());
		if (activity != null) childList.add(activity);
		
		if (scope.getFaultHandlers() != null) {
			// add fault handlers
			for (Catch bpelCatch : scope.getFaultHandlers().getCatch()) {
				AbstractActivityNetwork catchNetwork = createChildNetwork(bpelCatch);
				if (catchNetwork != null)
					childList.add(catchNetwork);
			} 
		}
		// create return array
		AbstractActivityNetwork[] childArray = new AbstractActivityNetwork[childList.size()];
		childArray = childList.toArray(childArray);
		return childArray;
	}
	
	
	public class ScopeConnector implements IActivityConnector{
		
		private Variable[] states;
		
		public ScopeConnector(Variable[] states) {
			this.states = states;
		}

		@Override
		public Variable[] getConnectionStates() {
			return states;
		}
	}


	@Override
	public EObject getEObject() {
		return (EObject) this.scope;
	}

}
