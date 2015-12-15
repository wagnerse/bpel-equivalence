package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.BPELPackage;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractDefaultActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;

public class ProcessNetwork extends AbstractDefaultActivityNetwork{
	
	private Process process;

	public ProcessNetwork(Process subject, NetworkSolver network) {
		super(network);
		this.process = subject;
	}
	
	public ProcessNetwork(EObject subject, NetworkSolver network) {		
		super(network);
		this.process = (Process) subject;
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getProcess();
	}

	@Override
	protected AbstractActivityNetwork[] getChildNetworks() {
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
