package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPConstraint;

/**
 * A point algebra constraint describes the relation between two time points.
 * 
 * @author Jonas Scheurich
 * 
 */
public class PAConstraint extends CSPConstraint{

	private List<RelationEnum> relations = new ArrayList<RelationEnum>();

	public PAConstraint(RelationEnum... relations) {
		this.relations.addAll(Arrays.asList(relations));
	}
	
	public PAConstraint(List<RelationEnum> relations) {
		this.relations.addAll(relations);
	}

	public PAConstraint(PAVariable from, PAVariable to, RelationEnum... relations) {
		super(from, to);
		this.relations.addAll(Arrays.asList(relations));
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

	@Override
	public String valueToString() {
		StringBuilder sb = new StringBuilder();
		for (RelationEnum r : this.relations) {
			if (!r.equals(relations.get(0))) {
				sb.append(" ");
			}
			sb.append(r.toString());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.getFrom().toString() + " " + valueToString() + " " + this.getTo().toString();
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
	
	public boolean equalsRelations(CSPConstraint obj) {
		if (!(obj instanceof PAConstraint))
			return false;
		if (obj == this)
			return true;
		PAConstraint rhs = (PAConstraint) obj;
		return new EqualsBuilder().append(getRelations(), rhs.getRelations()).isEquals();
	}
	
	public boolean equalsDirection(PAConstraint obj) {
		boolean result = true;
		if (!(((PAVariable)this.getFrom()).getTimePoint().getTimeType() 
				== ((PAVariable)obj.getFrom()).getTimePoint().getTimeType())) {
			result = false;
		}
		if (!(((PAVariable)this.getTo()).getTimePoint().getTimeType() 
				== ((PAVariable)obj.getTo()).getTimePoint().getTimeType())) {
			result = false;
		}
		
		return result;
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
	@Override
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

	@Override
	public boolean contradiction() {
		return (this.relations.size() == 0);
	}
	
	@Override
	public void reduceAction(CSPConstraint constraint) {
		if (!(constraint instanceof PAConstraint)) return;
		this.cutAdd((PAConstraint) constraint);
	}
}
