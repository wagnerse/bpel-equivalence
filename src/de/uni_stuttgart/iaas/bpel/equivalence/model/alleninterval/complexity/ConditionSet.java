package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.complexity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BranchingType;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.complexity.Condition.PointEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.RelationEnum;

/**
 * 
 * @author Jonas Scheurich
 * A ConditionSet contains {@link Condition}s thats describes the relation
 * between the time following time points.
 * 
 * start_i - end_j
 * start_j - end_i
 * start_i - start_j
 * end_i - end_j
 *
 */
public class ConditionSet {
	
	Map<String, Condition> conditions = new HashMap<String, Condition>();
	
	/**
	 * Create a empty ConditionSet
	 */
	public ConditionSet() {
	}

	/**
	 * Create a ConditionSet from a 
	 * {@link org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type}
	 * @param t Type
	 */
	public ConditionSet(Type t) {
		initRelationConditions(BranchingType.fromType(t));
	}
	
	/**
	 * Create a ConditionSet from a {@link alleninterval.BranchingType}
	 * @param t Type
	 */
	public ConditionSet(BranchingType t) {
		initRelationConditions(t);
	}
	
	/**
	 * Get the Relation between two time points
	 * @param p1 The first Point
	 * @param p2 The second Point
	 * @return {@link Condition}
	 */
	public Condition getCondition(PointEnum p1, PointEnum p2) {
		return conditions.get(p1.name() + p2.name());
	}
	
	/**
	 * Get all conditions
	 * @return list of {@link Condition}
	 */
	public Collection<Condition> getConditions() {
		return conditions.values();
	}
	
	/**
	 * Perform a disjunction with this condition set and a second condition set.
	 * The result is stored in this condition set.
	 * 
	 * @param set2 {@link ConditionSet}
	 */
	public void orAdd(ConditionSet set2) {
		for (Condition c2: set2.getConditions()) {
			Condition c1 = getCondition(c2.getP1(), c2.getP2());
			// if the condition for p1 and p2 of c2 is set perform compose c1 with c2
			// else use c2
			if (c1 != null) {
				c1.or(c2);
			}
			else {
				setCondition(c2.getP1(), c2.getP2(), c2.getRelationsCopy());
			}	
		}
	}
	
	/**
	 * Create a condition from the time point p1 to the time point p2
	 * @param p1 
	 * @param p2
	 * @param relations list of relations
	 */
	public void setCondition(PointEnum p1, PointEnum p2, RelationEnum... relations) {
		Condition c = new Condition(p1, p2, relations);
		conditions.put(c.getPointKey(), c);
	}
	
	/**
	 * Create conditions for a constraint type
	 * @param t {@link BranchingType} the constraint type
	 */
	private void initRelationConditions(BranchingType t) {
		
		if (t == BranchingType.Before) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.GREATER);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.LESS);
		}
		else if (t == BranchingType.After) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.GREATER);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.GREATER);
		}
		else if (t == BranchingType.Meets) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.EQUALS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.LESS);
		}
		else if (t == BranchingType.MetBy) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.EQUALS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.GREATER);
		}
		else if (t == BranchingType.Overlaps) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.LESS);
		}
		else if (t == BranchingType.OverlappedBy) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.GREATER);
		}
		else if (t == BranchingType.During) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.LESS);
		}
		else if (t == BranchingType.Contains) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.GREATER);
		}
		else if (t == BranchingType.Starts) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.LESS);
		}
		else if (t == BranchingType.StartedBy) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.GREATER);
		}
		else if (t == BranchingType.Finishes) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.EQUALS);
		}
		else if (t == BranchingType.FinishedBy) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.EQUALS);
		}
		else if (t == BranchingType.Equals) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.EQUALS);
		}
		
		//Branching Types
		else if (t == BranchingType.PartiallyBefore) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.UNRELATED);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.PartiallyAfter) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.UNRELATED);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.PartiallyMeets) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.PartiallyMetBy) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.PartiallyOverlaps) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.PartiallyOverlappedBy) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.PartiallyStarts) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.Adjacent) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.UNRELATED);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.AdjacentBy) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.UNRELATED);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.Touches) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.Unrelated) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.UNRELATED);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.UNRELATED);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.UNRELATED);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.InitiallyBefore) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.UNRELATED);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.InitiallyAfter) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.UNRELATED);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.InitiallyMeets) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.LESS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.InitiallyMetBy) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.GREATER);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else if (t == BranchingType.InitiallyEquals) {
			setCondition(PointEnum.START_I, PointEnum.END_J, RelationEnum.LESS);
			setCondition(PointEnum.START_J, PointEnum.END_I, RelationEnum.LESS);
			setCondition(PointEnum.START_I, PointEnum.START_J, RelationEnum.EQUALS);
			setCondition(PointEnum.END_I, PointEnum.END_J, RelationEnum.UNRELATED);
		}
		else {
			throw new IllegalStateException("Type " + t.name() + " unkown.");
		}
	}
	
	/**
	 * Crate a string with the format /('{' p1 rel ['|' rel]+ p2 '}')
	 */
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
