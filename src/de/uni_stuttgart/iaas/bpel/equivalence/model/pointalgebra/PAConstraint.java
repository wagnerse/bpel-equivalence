package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * 
 * @author Jonas Scheurich
 * 
 *         A constraint describes the relation between two time points.
 */
public class PAConstraint {

	private PAVariable from;
	private PAVariable to;
	private List<RelationEnum> relations = new ArrayList<RelationEnum>();

	public PAConstraint(RelationEnum... relations) {
		this.relations.addAll(Arrays.asList(relations));
	}
	
	public PAConstraint(List<RelationEnum> relations) {
		this.relations.addAll(relations);
	}

	public PAConstraint(PAVariable from, PAVariable to, RelationEnum... relations) {
		this.setFrom(from);
		this.setTo(to);
		this.relations.addAll(Arrays.asList(relations));
	}
	
	public void setTo(PAVariable to) {
		this.to = to;
	}

	public void setFrom(PAVariable from) {
		this.from = from;
	}
	
	public PAVariable getFrom() {
		return from;
	}
	
	public PAVariable getTo() {
		return to;
	}

	public void addRelations(RelationEnum... newRelations) {
		addRelations(Arrays.asList(newRelations));
	}

	public void addRelations(List<RelationEnum> newRelations) {
		for (RelationEnum nr : newRelations) {
			if (!relations.contains(nr)) {
				relations.add(nr);
			}
		}
	}

	public void setRelations(List<RelationEnum> relations) {
		this.relations = relations;
	}

	public List<RelationEnum> getRelations() {
		return relations;
	}

	public String relationsToString() {
		StringBuilder sb = new StringBuilder();
		for (RelationEnum r : this.relations) {
			if (!r.equals(relations.get(0))) {
				sb.append(" ");
			}
			sb.append(r.name());
		}

		return sb.toString();
	}
	
	public String getName() {
		return ((PAVariable) this.getFrom()).getName() 
				+ " " + relationsToString() 
				+ " " + ((PAVariable) this.getTo()).getName();
	}

	@Override
	public String toString() {
		return this.getFrom().toString() + " " + relationsToString() + " " + this.getTo().toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PAConstraint))
			return false;
		if (obj == this)
			return true;

		PAConstraint rhs = (PAConstraint) obj;
		return new EqualsBuilder().append(getFrom(), rhs.getFrom()).append(getRelations(), rhs.getRelations())
				.append(getTo(), rhs.getTo()).isEquals();
	}

	@Override
	public Object clone() {
		RelationEnum[] relationArray = new RelationEnum[this.relations.size()];
		relationArray = this.relations.toArray(relationArray);
		return new PAConstraint((PAVariable) this.getFrom(), (PAVariable) this.getTo(), relationArray);
	}

	/**
	 * Calculate the composition of this constraint and a second This constraint
	 * will not changed
	 * 
	 * @param c
	 * @return
	 */
	public PAConstraint compose(PAConstraint c) {
		PAConstraint result = new PAConstraint();

		// check direction of the edges
		if (!this.getTo().equals(c.getFrom()))
			throw new IllegalArgumentException("Direction not match");

		result.setFrom(this.getFrom());
		result.setTo(c.getTo());

		List<RelationEnum> relationComp = new ArrayList<RelationEnum>();

		for (RelationEnum r1 : this.getRelations()) {
			for (RelationEnum r2 : c.getRelations()) {
				for (RelationEnum r : r1.compose(r2)) {
					if (!relationComp.contains(r)) {
						relationComp.add(r);
					}
				}
			}
		}

		result.addRelations(relationComp);
		return result;
	}

	/**
	 * Calculate the cut of this constraint with a second This constraint will
	 * not changed.
	 * 
	 * @param c
	 * @return
	 */
	public PAConstraint cut(PAConstraint c) {
		PAConstraint result = new PAConstraint();
		result.setTo(this.getTo());
		result.setFrom(this.getFrom());
		
		List<RelationEnum> relationCut = new ArrayList<RelationEnum>();

		for (RelationEnum r : this.getRelations()) {
			if (c.getRelations().contains(r)) {
				relationCut.add(r);
			}
		}
		
		//System.out.println("Cut " + this + " with " + c + " = " + relationCut);

		result.addRelations(relationCut);
		return result;
	}
	
	public void cutAdd(PAConstraint c) {
		PAConstraint result = this.cut(c);
		this.relations.clear();
		for(RelationEnum r: result.getRelations()) {
			this.relations.add(r);
		}
	}

	/**
	 * Get the relations in reverted direction.
	 * Less and greater are reverted in greater and less.
	 * Equals and unrelated are unchanged.
	 * 
	 * @return
	 */
	public PAConstraint revert() {
		List<RelationEnum> result = new ArrayList<RelationEnum>();
		
		for (RelationEnum r: this.relations) {
			if (r == RelationEnum.LESS) {
				result.add(RelationEnum.GREATER);
			}
			else if (r == RelationEnum.GREATER) {
				result.add(RelationEnum.LESS);
			}
			else {
				result.add(r);
			}
		}
		
		PAConstraint revConstraint = new PAConstraint(result);
		revConstraint.setTo(this.getFrom());
		revConstraint.setFrom(this.getTo());

		return revConstraint;
	}

	/**
	 * Calculate the cut of this constraint with a second This constraint will
	 * be changed.
	 * 
	 * @param c
	 * @return
	 */
	public static PAConstraint newTConstraint(PAVariable from, PAVariable to) {
		PAConstraint tConstraint = new PAConstraint(from, to, 
				RelationEnum.LESS, 
				RelationEnum.EQUALS, 
				RelationEnum.UNRELATED,
				RelationEnum.GREATER);
		
		return tConstraint;
	}
}
