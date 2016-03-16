package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Process;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractDefaultActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Creates a point algebra network for the BPEL activity {@link Process}
 */
public class ProcessNetwork extends AbstractDefaultActivityNetwork{
	
	private Process process;

	public ProcessNetwork(AbstractActivityNetwork parentNetwork, Process subject, Problem network) {
		super(parentNetwork, network);
		this.process = subject;
		
		initLocalNetwork();
	}
	
	public ProcessNetwork(AbstractActivityNetwork parentNetwork, EObject subject, Problem network) {		
		super(parentNetwork, network);
		this.process = (Process) subject;
		
		initLocalNetwork();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getProcess();
	}
	
	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(process, "name");
		return (attribute instanceof String)? (String) attribute : "[Activity]";
	}

	@Override
	protected Map<EObject, AbstractActivityNetwork> createChildNetworks() {
		Map<EObject, AbstractActivityNetwork> childMap = new HashMap<EObject, AbstractActivityNetwork>();
		childMap.put(this.getEObject(), createChildNetwork((EObject) process.getActivity()));
		return childMap;
	}

	@Override
	public EObject getEObject() {
		return (EObject) this.process;
	}
	
	protected void initLocalNetwork() {
		//TODO create local network		
	}

	@Override
	protected void initConstraintMap() {
		// not implemented
		
	}

}
