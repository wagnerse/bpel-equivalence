package de.uni_stuttgart.iaas.bpel.equivalence;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.BasicActivityFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.FHCatchNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.FlowNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.ProcessNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.ScopeNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;

public class BpelEquivalence {
	
	public BpelEquivalence() {
		initRepo();
	}
	
	private void initRepo() {

		NetworkFactoryRepo.getInstance().registerFactory(new ProcessNetworkFactory());
		NetworkFactoryRepo.getInstance().registerFactory(new ScopeNetworkFactory());
		NetworkFactoryRepo.getInstance().registerFactory(new FlowNetworkFactory());
		
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getReceive()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getReply()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getInvoke()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getAssign()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getThrow()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getExit()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getWait()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getEmpty()));
		//TODO check compensate, compensateScope, rethrow, validate
		
		NetworkFactoryRepo.getInstance().registerFactory(new FHCatchNetworkFactory());
			
	}
	
	public Problem createNetwork(EObject eObject) {
		return NetworkFactoryRepo.getInstance().createElementNetwork(null, eObject, new Problem()).linkActivityNetworkLayer();
	}
	
	public boolean checkBpelEquivalence(Problem network1, Problem network2) {
		//TODO implement
		return false;
	}
	
}
