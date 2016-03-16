package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Provide functionalities for EMF elements
 *
 */
public class EMFUtils {

	/**
	 * Gett a attribute of a {@link EObject} by the attribute name.
	 * @param eObject
	 * @param attributeName
	 * @return
	 */
	public static Object getAttributeByName(EObject eObject, String attributeName) {
		Object attribute  = null;
		EList<EAttribute> eAllAttributes = eObject.eClass().getEAllAttributes();
		for (EAttribute eAttribute : eAllAttributes) {
			if (eAttribute.getName().equals(attributeName)) {
				attribute = eObject.eGet(eAttribute);
			}
		}
		return attribute;
	}
	
	/**
	 * Get the first element of an {@link EList}
	 * @param list
	 * @return
	 */
	public static EObject getEListFirst(EList<EObject> list) {
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

}
