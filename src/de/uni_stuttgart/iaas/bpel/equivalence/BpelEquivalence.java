package de.uni_stuttgart.iaas.bpel.equivalence;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.BasicActivityFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.FHCatchNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.FlowNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.ProcessNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.ScopeNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.PASolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Problem;

/**
 * 
 * @author Jonas Scheurich
 *
 *         Calculate the equivalence of two BPEL processes
 *
 */
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
		// TODO check compensate, compensateScope, rethrow, validate

		NetworkFactoryRepo.getInstance().registerFactory(new FHCatchNetworkFactory());

	}

	/**
	 * Create point algebra network of a {@link EObject} and the containing
	 * child's.
	 * 
	 * @param eObject
	 * @return Point algebra network
	 */
	public Problem createNetwork(EObject eObject) {
		Problem problem =  NetworkFactoryRepo.getInstance()
				.createElementNetwork(null, eObject, new Problem(new PASolver())).linkActivityNetworkLayer();
		System.out.println("\nStart constraint probagation");
		problem.probagate();
		return problem;
	
	}

	/**
	 * Check the equivalence of two BPEL elements with point algebra networks
	 * 
	 * @param network1
	 * @param network2
	 * @return
	 */
	public boolean checkBpelEquivalence(Problem network1, Problem network2) {
		// TODO implement
		return false;
	}

}
