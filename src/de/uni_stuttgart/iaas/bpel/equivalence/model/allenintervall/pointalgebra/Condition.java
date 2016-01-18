package de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra;

import java.util.Arrays;
import java.util.HashSet;

public class Condition {
	
	private PointEnum p1;
	private PointEnum p2;
	
	private HashSet<RelationEnum> relations;
	
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
	
	public void compose(Condition cond2) {
		
		boolean illegalState = false;
		
		if (equalRelation(this, RelationEnum.LESS)) {
			
			if (equalRelation(cond2, RelationEnum.LESS)) {
				// <
				//no action
			}
			else if (equalRelation(cond2, RelationEnum.GREATER)) {
				// < = >
				this.relations.clear();
				this.relations.add(RelationEnum.LESS);
				this.relations.add(RelationEnum.EQUALS);
				this.relations.add(RelationEnum.GREATER);
			}
			else if (equalRelation(cond2, RelationEnum.UNRELATED)) {
				// < ||
				this.relations.clear();
				this.relations.add(RelationEnum.LESS);
				this.relations.add(RelationEnum.UNRELATED);
			}
			else if (equalRelation(cond2, RelationEnum.EQUALS)) {
				// <
				// no action
			}
			else {
				illegalState = true;
			}
			
		}
		else if (equalRelation(this, RelationEnum.GREATER)) {
			
			if (equalRelation(cond2, RelationEnum.LESS)) {
				// T (< = || >)
				setRelationT();
			}
			else if (equalRelation(cond2, RelationEnum.GREATER)) {
				// >
				// no action
			}
			else if (equalRelation(cond2, RelationEnum.UNRELATED)) {
				// ||
				this.relations.clear();
				this.relations.add(RelationEnum.UNRELATED);
			}
			else if (equalRelation(cond2, RelationEnum.EQUALS)) {
				// >
				// no action
			}
			else {
				illegalState = true;
			}
		}
		else if (equalRelation(this, RelationEnum.UNRELATED)) {
			
			if (equalRelation(cond2, RelationEnum.LESS)) {
				// ||
				// no action
			}
			else if (equalRelation(cond2, RelationEnum.GREATER)) {
				// > ||
				this.relations.clear();
				this.relations.add(RelationEnum.GREATER);
				this.relations.add(RelationEnum.UNRELATED);
			}
			else if (equalRelation(cond2, RelationEnum.UNRELATED)) {
				// T
				setRelationT();
			}
			else if (equalRelation(cond2, RelationEnum.EQUALS)) {
				// ||
				// no action
			}
			else {
				illegalState = true;
			}
		}
		else if (equalRelation(this, RelationEnum.EQUALS)) {
			
			if (equalRelation(cond2, RelationEnum.LESS)) {
				// <
				this.relations.clear();
				this.relations.add(RelationEnum.LESS);
			}
			else if (equalRelation(cond2, RelationEnum.GREATER)) {
				// >
				this.relations.clear();
				this.relations.add(RelationEnum.GREATER);
			}
			else if (equalRelation(cond2, RelationEnum.UNRELATED)) {
				// ||
				this.relations.clear();
				this.relations.add(RelationEnum.UNRELATED);
			}
			else if (equalRelation(cond2, RelationEnum.EQUALS)) {
				// =
				// no action
			}
			else {
				illegalState = true;
			}
		}
		else {
			illegalState = true;
		}
		
		if (illegalState) {
			throw new IllegalStateException("Composition not supported:" + this.toString() + " with " + cond2.toString());
		}
	}
	
	private void setRelationT() {
		// T (< = || >)
		this.relations.clear();
		this.relations.add(RelationEnum.GREATER);
		this.relations.add(RelationEnum.LESS);
		this.relations.add(RelationEnum.EQUALS);
		this.relations.add(RelationEnum.UNRELATED);
	}
	
	private boolean equalRelation(Condition con, RelationEnum... relList) {
		if (con.getRelations().size() != relList.length) return false;
		
		boolean result = true;
		
		for (RelationEnum rel: relList) {
			if (!con.getRelations().contains(rel)) {
				result = false;
			}
		}
		
		return result;
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
		
		for(RelationEnum rel: this.getRelationsCopy()) {
			str.append(rel.name());
		}
		
		str.append(" " + p2.name());
		return str.toString();
	}
	
	public enum PointEnum {START_I, END_I, START_J, END_J}

}
