package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class EMFUtils {

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

}
