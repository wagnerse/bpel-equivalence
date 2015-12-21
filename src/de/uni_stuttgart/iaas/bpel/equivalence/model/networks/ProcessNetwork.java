package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.BPELPackage;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractDefaultActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class ProcessNetwork extends AbstractDefaultActivityNetwork{
	
	private Process process;

	public ProcessNetwork(AbstractActivityNetwork parentNetwork, Process subject, NetworkSolver network) {
		super(parentNetwork, network);
		this.process = subject;
	}
	
	public ProcessNetwork(AbstractActivityNetwork parentNetwork, EObject subject, NetworkSolver network) {		
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
		// TODO Auto-generated method stub
		
	}

}
