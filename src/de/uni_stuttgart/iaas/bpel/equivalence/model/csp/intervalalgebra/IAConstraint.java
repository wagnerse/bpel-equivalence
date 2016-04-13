package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPConstraint;

/**
 * A interval constraint describes the relation between two intervals.
 * 
 * @author Jonas Scheurich
 *
 */
public class IAConstraint extends CSPConstraint{

	private List<BranchingType> relations = new ArrayList<BranchingType>();
	
	public IAConstraint(BranchingType... relations) {
		this.relations.addAll(Arrays.asList(relations));
	}
	
	public IAConstraint(List<BranchingType> relations) {
		this.relations.addAll(relations);
	}
	
	public IAConstraint(IAVariable from, IAVariable to, BranchingType...relationArray) {
		this.relations.addAll(relations);
		this.setFrom(from);
		this.setTo(to);
	}

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
	public CSPConstraint revert() {
		List<BranchingType> relations = new ArrayList<BranchingType>();
		
		//revert relations
		for (BranchingType t: this.getRelations()) {
			relations.add(BranchingType.revert(t));			
		}
		
		IAConstraint reverted = new IAConstraint(relations);
		reverted.setFrom(this.getTo());
		reverted.setTo(this.getFrom());
		
		return reverted;
	}

	@Override
	public String toString() {
		return this.getFrom().toString() + " " + relationsToString() + " " + this.getTo().toString();
	}

	@Override
	public String valueToString() {
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
	public Object clone() {
		BranchingType[] relationArray = new BranchingType[this.getRelations().size()];
		relationArray = this.relations.toArray(relationArray);
		return new IAConstraint((IAVariable) this.getFrom(),(IAVariable) this.getTo(), relationArray);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IAConstraint))
			return false;
		if (obj == this)
			return true;

		IAConstraint rhs = (IAConstraint) obj;
		return new EqualsBuilder()
				.append(getFrom(), rhs.getFrom())
				.append(getRelations(), rhs.getRelations())
				.append(getTo(), rhs.getTo())
				.isEquals();
	}

	@Override
	public boolean contradiction() {
		return (this.getRelations().size() == 0);
	}

	@Override
	public void reduceAction(CSPConstraint constraint) {
		// not implemented
	}
	


}
