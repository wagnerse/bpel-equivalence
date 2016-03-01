package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

public class Constraint {
	
	private RelationEnum[] relations;
	private Variable from;
	private Variable to;
	
	public Constraint(RelationEnum... relations) {
		this.relations = relations;
	}
	
	public void setFrom(Variable from) {
		this.from = from;
	}
	
	public void setTo(Variable to) {
		this.to = to;
	}
	
	public RelationEnum[] getRelations() {
		return relations;
	}
	
	public Variable getFrom() {
		return from;
	}
	
	public Variable getTo() {
		return to;
	}

}
