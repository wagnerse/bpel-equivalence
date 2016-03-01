package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

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
	protected AbstractActivityNetwork[] createChildNetworks() {
		AbstractActivityNetwork[] childs = { createChildNetwork(activity) };
		return childs;
	}

	@Override
	protected void initConstraintMap() {
		//TODO create constraint map
	}
}
