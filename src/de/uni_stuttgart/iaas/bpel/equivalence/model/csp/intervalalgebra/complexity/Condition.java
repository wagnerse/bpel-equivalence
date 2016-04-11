package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra.complexity;

import java.util.Arrays;
import java.util.HashSet;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;

/**
 * 
 * @author Jonas Scheurich
 * A Condition describes a disjunction of relations between the time points p1 and p2
 *
 */
public class Condition {
	
	private PointEnum p1;
	private PointEnum p2;
	
	private HashSet<RelationEnum> relations;
	
	/**
	 * Describes the different time points	 *
	 */
	public enum PointEnum {START_I, END_I, START_J, END_J}
	
	/**
	 * Create a condition
	 * @param p1
	 * @param p2
	 * @param relations as array
	 */
	public Condition(PointEnum p1, PointEnum p2, RelationEnum... relations) {
		this.p1 = p1;
		this.p2 = p2;
		this.relations = new HashSet<RelationEnum>(Arrays.asList(relations));
	}
	
	/**
	 * Create a condition
	 * @param p1
	 * @param p2
	 * @param relations as HashSet
	 */
	public Condition(PointEnum p1, PointEnum p2, HashSet<RelationEnum> relations) {
		this.p1 = p1;
		this.p2 = p2;
		this.relations = relations;
	}
	
	/**
	 * Perform a disjunction with a second condition
	 * @param cond2 {@link Condition}
	 */
	public void or(Condition cond2) {	
		if (this.getP1() == cond2.getP1() && this.getP2() == cond2.getP2()) {
			relations.addAll(cond2.getRelations());
		}
		else
		{
			throw new IllegalStateException("Points p1 or p2 are unequal.");
		}
	}
	
	/**
	 * Get the description of point p1
	 * @return {@link PointEnum}
	 */
	public PointEnum getP1() {
		return p1;
	}
	
	/**
	 * Get the description of point p2
	 * @return {@link PointEnum}
	 */
	public PointEnum getP2() {
		return p2;
	}
	
	/**
	 * Get the disjuncted relations
	 * @return HashSet of {@link RelationEnum}
	 */
	public HashSet<RelationEnum> getRelations() {
		return relations;
	}
	
	/**
	 * Get the disjuncted relations as array copy
	 * @return array of {@link RelationEnum}
	 */
	public RelationEnum[] getRelationsCopy() {
		return relations.toArray(new RelationEnum[relations.size()]);
	}
	
	/**
	 * Get a string with the concatenation of the two points
	 * @return
	 */
	public String getPointKey() {
		return p1.name() + p2.name();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(p1.name() + " ");
		
		int count = 0;
		for(RelationEnum rel: this.getRelationsCopy()) {
			if (count == 0) {
				str.append(rel.name());
			}
			else {
				str.append("|" + rel.name());
			}
			count++;
		}
		
		str.append(" " + p2.name());
		return str.toString();
	}

}
