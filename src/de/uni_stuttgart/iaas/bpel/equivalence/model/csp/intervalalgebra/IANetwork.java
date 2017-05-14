package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;

/**
 *  A interval network contains constraints and intervals
 *  
 * @author Jonas Scheurich
 *
 */
public class IANetwork extends CSPNetwork{
	
	private int idCount = 0;

	public IAVariable createVariable(Object bpelElement, BPELStateEnum state) {
		IAVariable newVariable = new IAVariable(idCount++, bpelElement, state);
		super.addVariable(newVariable);
		super.addConstraint(new IAConstraint(newVariable, newVariable, BranchingType.Equals));
		return newVariable;
	}

	@Override
	public CSPConstraint newTConstraint(CSPVariable from, CSPVariable to) {
		if (!(from instanceof IAVariable) || !(to instanceof IAVariable)) return null;
		
		// create a list of all branching type relations
		List<BranchingType> relations = new ArrayList<BranchingType>();
		for (BranchingType r: BranchingType.values()) relations.add(r);
		
		//create constraint
		IAConstraint constraint = new IAConstraint(relations);
		constraint.setFrom(from);
		constraint.setTo(to);
		
		return constraint;
	}


}
