package de.uni_stuttgart.iaas.bpel.equivalence;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.BasicActivityFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.ProcessNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.ScopeNetworkFactory;

public class BpelEquivalence {
	
	public void init() {

		NetworkFactoryRepo.getInstance().registerFactory(new ProcessNetworkFactory());
		NetworkFactoryRepo.getInstance().registerFactory(new ScopeNetworkFactory());
		
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getReceive()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getReply()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getInvoke()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getAssign()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getThrow()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getExit()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getWait()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getEmpty()));
		//TODO check compensate, compensateScope, rethrow, validate
		
	}
	
	public NetworkSolver createNetwork(EObject eObject) {
		return NetworkFactoryRepo.getInstance().createElementNetwork(eObject, new NetworkSolver()).linkActivityNetworkLayer(null);
	}
	
	public boolean checkBpelEquivalence(NetworkSolver network1, NetworkSolver network2) {
		//TODO implement
		return false;
	}
	
}
