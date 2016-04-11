package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPConstraint;

public class IAConstraint extends CSPConstraint{

	private List<BranchingType> relations = new ArrayList<BranchingType>();
	
	public void addRelations(BranchingType... newRelations) {
		addRelations(Arrays.asList(newRelations));
	}

	public void addRelations(List<BranchingType> newRelations) {
		for (BranchingType nr : newRelations) {
			if (!relations.contains(nr)) {
				relations.add(nr);
			}
		}
	}
	
	public List<BranchingType> getRelations() {
		return relations;
	}
	
	public String relationsToString() {
		StringBuilder sb = new StringBuilder();
		for (BranchingType r : this.relations) {
			if (!r.equals(relations.get(0))) {
				sb.append(" ");
			}
			sb.append(r.toString());
		}
		return sb.toString();
	}
	

	@Override
	public String toString() {
		return this.getFrom().toString() + " " + relationsToString() + " " + this.getTo().toString();
	}
}
