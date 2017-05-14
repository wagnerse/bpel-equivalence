package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Process;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractDefaultActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * Creates a point algebra network for the BPEL activity {@link Process}
 * 
 * @author Jonas Scheurich
 * 
 */
public class ProcessNetwork extends AbstractDefaultActivityNetwork{
	
	private Process process;

	public ProcessNetwork(AbstractActivityNetwork parentNetwork, Process subject, PANetwork network) {
		super(parentNetwork, network);
		this.process = subject;
		
		initLocalNetwork();
	}
	
	public ProcessNetwork(AbstractActivityNetwork parentNetwork, EObject subject, PANetwork network) {		
		super(parentNetwork, network);
		this.process = (Process) subject;
		
		initLocalNetwork();
	}

	@Override
	public Class<?> getSupportedClass() {
		return Process.class;
	}
	
	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(process, "name");
		return (attribute instanceof String)? (String) attribute : "[Activity]";
	}

	@Override
	protected Map<Object, AbstractActivityNetwork> createChildNetworks() {
		Map<Object, AbstractActivityNetwork> childMap = new HashMap<Object, AbstractActivityNetwork>();
		childMap.put(this, createChildNetwork((EObject) process.getActivity()));
		return childMap;
	}

	@Override
	public Object getObject() {
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
