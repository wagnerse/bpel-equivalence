package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;

public class IANetwork extends CSPNetwork{
	
	public void createVariable(EObject bpelElement) {
		//TODO implement
	}
	
	public Collection<PAVariable> getVariables() {
		 return null;
	}
	
	public void addConstraint(IAConstraint constraint) {
		//TODO implement
	}
	
	public void addConstraints(IAConstraint...constraints) {
		for (IAConstraint c: constraints) {
			addConstraint(c);
		}
	}

	public PAConstraint getConstraint(IAConstraint from, IAConstraint to) {
		//TODO implement
		return null;
	}

}
