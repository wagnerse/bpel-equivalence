package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constraint {
	
	private List<RelationEnum> relations = new ArrayList<RelationEnum>();
	private Variable from;
	private Variable to;
	
	public Constraint(RelationEnum... relations) {
		this.relations.addAll(Arrays.asList(relations));
	}
	
	public Constraint(Variable from, Variable to, RelationEnum... relations) {
		this.from = from;
		this.to = to;
		this.relations.addAll(Arrays.asList(relations));
	}
	
	public void setFrom(Variable from) {
		this.from = from;
	}
	
	public void setTo(Variable to) {
		this.to = to;
	}
	
	public void addRelations(RelationEnum...newRelations) {
		addRelations(Arrays.asList(newRelations));
	}
	
	public void addRelations(List<RelationEnum> newRelations) {
		for(RelationEnum nr: newRelations) {
			if (!relations.contains(nr)) {
				relations.add(nr);
			}
		}
	}
	
	public List<RelationEnum> getRelations() {
		return relations;
	}
	
	public Variable getFrom() {
		return from;
	}
	
	public Variable getTo() {
		return to;
	}

	public String relationsToString() {
		StringBuilder sb = new StringBuilder();
		for (RelationEnum r: this.relations) {
			if (!r.equals(relations.get(0))) {
				sb.append(" ");
			}
			sb.append(r.name());
		}
		
		return sb.toString();
	}

}
