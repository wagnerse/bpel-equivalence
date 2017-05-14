package de.uni_stuttgart.iaas.bpel.equivalence;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityNetworkFactory;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;

/**
 * The NetworkFactoryRepo contains the factory objects for all supported activity networks.
 * This class creates a network object for a given BPEL element if this element is supported.
 * 
 * @author Jonas Scheurich
 */
public class NetworkFactoryRepo {
	
	private Map<Class<?>, IActivityNetworkFactory> factories = new HashMap<Class<?>, IActivityNetworkFactory>();
	
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
	
	/**
	 * Create a {@link AbstractActivityNetwork} from a given {@link Object} if a suitable
	 * factory is registered.
	 * @param parentNetwork The parent network holds the parent {@link Object}
	 * @param object current BPEL element
	 * @param network a point algebra network
	 * @return A activity network object
	 */
	public AbstractActivityNetwork createElementNetwork(AbstractActivityNetwork parentNetwork, Object object, PANetwork network) {
		Class<?> clazz = object.getClass();		
		if (!factories.containsKey(clazz)) {
			throw new RuntimeException("No factory found for class " + clazz.getName());
		}
		else {
			return factories.get(clazz).createElementNetwork(parentNetwork, object, network);
		}
	}
	
	/**
	 * Register a factory to support the point algebra creation of a BPEL element
	 * @param factory
	 */
	public void registerFactory(IActivityNetworkFactory factory) {
		factories.put(factory.getSupportedClass(), factory);
	}

}
