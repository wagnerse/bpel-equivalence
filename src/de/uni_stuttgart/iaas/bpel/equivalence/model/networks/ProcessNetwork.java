package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;
import java.util.List;

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
		List<AbstractActivityNetwork> childList = new ArrayList<AbstractActivityNetwork>();
		childList.add(createChildNetwork((EObject) process.getActivity()));
		
		return (AbstractActivityNetwork[]) childList.toArray();
	}

}
