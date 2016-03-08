package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Catch;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractStubActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * Handles the allen interval network for a BPEL fault handler catch
 * 
 * @author Jonas Scheurich
 *
 */
public class FHCatchNetwork extends AbstractStubActivityNetwork {

	private Catch faultHandlerCatch;
	private Activity activity;

	public FHCatchNetwork(AbstractActivityNetwork parentNetwork, Catch subject, Problem network) {
		super(parentNetwork, network);
		this.faultHandlerCatch = subject;
		this.activity = faultHandlerCatch.getActivity();
		
		initLocalNetwork();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getCatch();
	}

	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(faultHandlerCatch, "name");
		return (attribute instanceof String) ? (String) attribute : "[FaultHandlerCatch]";
	}

	@Override
	public EObject getEObject() {
		return faultHandlerCatch;
	}

	@Override
	protected Map<EObject, AbstractActivityNetwork> createChildNetworks() {
		Map<EObject, AbstractActivityNetwork> childMap = new HashMap<EObject, AbstractActivityNetwork>();
		childMap.put(this.getEObject(), createChildNetwork(activity));
		return childMap;
	}
	
	protected void initLocalNetwork() {
		//TODO create local network		
	}

	@Override
	protected void initConstraintMap() {
		//TODO create constraint map
	}
}
