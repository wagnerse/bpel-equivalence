package de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra.Condition.PointEnum;

public class ConditionSet {
	
	Map<String, Condition> conditions = new HashMap<String, Condition>();
	
	public ConditionSet() {
	}

	public ConditionSet(Type t) {
		initRelationConditions(t);
	}
	
	public Condition getCondition(PointEnum p1, PointEnum p2) {
		return conditions.get(p1.name() + p2.name());
	}
	
	public Collection<Condition> getConditions() {
		return conditions.values();
	}

	public void compose(ConditionSet set2) {
		for (Condition c2: set2.getConditions()) {
			Condition c1 = getCondition(c2.getP1(), c2.getP2());
			// if the condition for p1 and p2 of c2 is set perform compose c1 with c2
			// else use c2
			if (c1 != null) {
				c1.compose(c2);
			}
			else {
				setCondition(c2.getP1(), c2.getP2(), c2.getRelationsCopy());
			}	
		}
	}
	
	public void setCondition(PointEnum p1, PointEnum p2, RelationEnum... relations) {
		Condition c = new Condition(p1, p2, relations);
		conditions.put(c.getPointKey(), c);
	}
	
	private void initRelationConditions(Type t) {
		
		if (t == Type.Before) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
		}
		else if (t == Type.After) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
		}
		else if (t == Type.Meets || t == Type.MetBy) {
			setCondition(PointEnum.END_I, PointEnum.START_J, RelationEnum.EQUALS);
		}
		else if (t == Type.Overlaps) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.LESS);
		}
		else if (t == Type.OverlappedBy) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.GREATER);
		}
		else if (t == Type.During) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.LESS);
		}
		else if (t == Type.Contains) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.GREATER);
		}
		else if (t == Type.Starts) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.LESS);
		}
		else if (t == Type.StartedBy) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.GREATER);
		}
		else if (t == Type.Finishes) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.EQUALS);
		}
		else if (t == Type.FinishedBy) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.EQUALS);
		}
		else if (t == Type.Equals) {
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.EQUALS);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("{");
		for (Condition cond: this.getConditions()) {
			str.append(cond.toString() + ", ");
		}
		str.replace(str.length()-2, str.length(), "");
		str.append("}");
		
		return str.toString();
	}

}
