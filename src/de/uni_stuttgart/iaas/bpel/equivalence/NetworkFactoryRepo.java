package de.uni_stuttgart.iaas.bpel.equivalence;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;

public class NetworkFactoryRepo {
	
	private Map<EClass, IActivityNetworkFactory> factories = new HashMap<EClass, IActivityNetworkFactory>();
	
	private static NetworkFactoryRepo instance = null;
	
	private NetworkFactoryRepo() {
	}
	
	public static NetworkFactoryRepo getInstance() {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (NetworkFactoryRepo.class) {
				if (instance == null) {
					instance = new NetworkFactoryRepo();
				}
			}
		}
		return instance;
	}
	
	public AbstractActivityNetwork createElementNetwork(EObject eobject, NetworkSolver network) {
		EClass eClass = eobject.eClass();		
		if (!factories.containsKey(eClass)) {
			return null; //TODO stub?
		}
		else {
			return factories.get(eClass).createElementNetwork(eobject, network);
		}
	}
	
	public void registerFactory(IActivityNetworkFactory factory) {
		factories.put(factory.getSupportedEClass(), factory);
	}

}
