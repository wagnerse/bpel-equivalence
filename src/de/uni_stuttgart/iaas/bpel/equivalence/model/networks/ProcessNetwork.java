package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Process;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractDefaultActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class ProcessNetwork extends AbstractDefaultActivityNetwork{
	
	private Process process;

	public ProcessNetwork(AbstractActivityNetwork parentNetwork, Process subject, Problem network) {
		super(parentNetwork, network);
		this.process = subject;
	}
	
	public ProcessNetwork(AbstractActivityNetwork parentNetwork, EObject subject, Problem network) {		
		super(parentNetwork, network);
		this.process = (Process) subject;
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
	protected AbstractActivityNetwork[] createChildNetworks() {
		AbstractActivityNetwork activity = createChildNetwork((EObject) process.getActivity());
		
		AbstractActivityNetwork[] childArray = {activity};

		return childArray;
	}

	@Override
	public EObject getEObject() {
		return (EObject) this.process;
	}

	@Override
	protected void initConstraintMap() {
		// not implemented
		
	}

}
