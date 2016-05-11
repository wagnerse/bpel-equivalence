package de.uni_stuttgart.iaas.bpel.equivalence;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PASolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.BasicActivityFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.FHCatchNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.FlowNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.ProcessNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.factories.ScopeNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence.EqualsConfiguration;
import de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence.ProcessDifference;
import de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence.ProcessEquals;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.logger.PALogger;

/**
 * Calculate the equivalence of two BPEL processes
 * 
 * @author Jonas Scheurich
 */
public class BpelEquivalence {

	public BpelEquivalence() {
		initRepo();
		
		// init logger
		try {
			PALogger.setup(Level.INFO);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");

		}
	}

	private void initRepo() {

		NetworkFactoryRepo.getInstance().registerFactory(new ProcessNetworkFactory());
		NetworkFactoryRepo.getInstance().registerFactory(new ScopeNetworkFactory());
		NetworkFactoryRepo.getInstance().registerFactory(new FlowNetworkFactory());

		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getReceive()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getReply()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getInvoke()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getAssign()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getExit()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getWait()));
		NetworkFactoryRepo.getInstance().registerFactory(new BasicActivityFactory(BPELPackage.eINSTANCE.getEmpty()));
		// TODO check compensate, compensateScope, rethrow, validate

		NetworkFactoryRepo.getInstance().registerFactory(new FHCatchNetworkFactory());

	}
	
	public PANetwork createNetwork(EObject eObject) {
		return createNetwork(eObject, false);
	}

	/**
	 * Create point algebra network of a {@link EObject} and the containing
	 * child's.
	 * 
	 * @param eObject activity to create the network
	 * @param tryToIncomplete propagate until a contradiction occurs
	 * @return Point algebra network
	 */
	public PANetwork createNetwork(EObject eObject, boolean tryToIncomplete) {
		PANetwork problem =  NetworkFactoryRepo.getInstance()
				.createElementNetwork(null, eObject, new PANetwork(new PASolver())).linkActivityNetworkLayer();
		System.out.println("Start constraint probagation");
		try {
			problem.probagate();
		} catch (IllegalStateException e) {
			if (tryToIncomplete) {
				e.printStackTrace();
				return problem;
			}
			else {
				throw e;
			}
		}
		return problem;
	
	}

	/**
	 * Check the equivalence of two BPEL elements with point algebra networks
	 * 
	 * @param network1
	 * @param network2
	 * @param activities
	 * @return
	 */
	public ProcessDifference checkBpelEquivalence(PANetwork network1, PANetwork network2, List<String> activities) {		
		EqualsConfiguration config = new EqualsConfiguration(BPELStateEnum.EXECUTING, activities);
		
		return checkBpelEquivalence(network1, network2, config);
	}
	
	/**
	 * Check the equivalence of two BPEL elements with point algebra networks
	 * 
	 * @param network1
	 * @param network2
	 * @param config
	 * @return
	 */
	public ProcessDifference checkBpelEquivalence(PANetwork network1, PANetwork network2, EqualsConfiguration config) {
		ProcessEquals processAnalysis = new ProcessEquals();
		processAnalysis.equalProcesses(network1, network2, config);
		
		return processAnalysis.getProcessDifference();
	}

}
