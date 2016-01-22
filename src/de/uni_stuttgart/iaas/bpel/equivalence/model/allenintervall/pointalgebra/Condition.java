package de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra;

import java.util.Arrays;
import java.util.HashSet;

public class Condition {
	
	private PointEnum p1;
	private PointEnum p2;
	
	private HashSet<RelationEnum> relations;
	
	public enum PointEnum {START_I, END_I, START_J, END_J}
	
	public Condition(PointEnum p1, PointEnum p2, RelationEnum... relations) {
		this.p1 = p1;
		this.p2 = p2;
		this.relations = new HashSet<RelationEnum>(Arrays.asList(relations));
	}
	
	public Condition(PointEnum p1, PointEnum p2, HashSet<RelationEnum> relations) {
		this.p1 = p1;
		this.p2 = p2;
		this.relations = relations;
	}
	
	public void or(Condition cond2) {	
		if (this.getP1() == cond2.getP1() && this.getP2() == cond2.getP2()) {
			relations.addAll(cond2.getRelations());
		}
		else
		{
			throw new IllegalStateException("Points p1 or p2 are unequal.");
		}
	}
	
	public PointEnum getP1() {
		return p1;
	}
	
	public PointEnum getP2() {
		return p2;
	}
	
	public HashSet<RelationEnum> getRelations() {
		return relations;
	}
	
	public RelationEnum[] getRelationsCopy() {
		return relations.toArray(new RelationEnum[relations.size()]);
	}
	
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
