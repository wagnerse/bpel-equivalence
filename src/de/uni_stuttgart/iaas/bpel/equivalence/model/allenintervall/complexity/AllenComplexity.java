package de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.complexity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra.Condition;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra.ConditionSet;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra.RelationEnum;

public class AllenComplexity {
	
	private List<HashSet<RelationEnum>> gammaX = new ArrayList<HashSet<RelationEnum>>();
	private Map<HashSet<Type>, Complexity> complexityCache = new HashMap<HashSet<Type>, Complexity>();
	
	private ConditionSet condSet = null;

	public enum Complexity {P, NP};
	
	public AllenComplexity() {
		initGammaList();
	}
	
	public ConditionSet getConditionSet() {
		return condSet;
	}
	
	/**
	 * Calculate complexity without caching
	 * @param c {@link StateConstraint}
	 * @return {@link Complexity}
	 */
	public Complexity isNP(StateConstraint c) {
		return isNP(c, false);
	}
	
	/**
	 * Calculate complexity with caching.
	 * @param c {@link StateConstraint}
	 * @return {@link Complexity}
	 */
	public Complexity isNP(StateConstraint c, boolean caching) {
		condSet = new ConditionSet();
		HashSet<Type> cachingKey = new HashSet<Type>();
		for(Type t : c.getTypes()) {
			ConditionSet condSet2 = new ConditionSet(t);
			condSet.orAdd(condSet2);
			cachingKey.add(t);
		}
		
		if (caching && complexityCache.containsKey(cachingKey)) {
			return complexityCache.get(cachingKey);
		}
		else {
			return checkComplexity(condSet);
		}
	}
	
	private Complexity checkComplexity(ConditionSet condSet) {	
		Complexity result = Complexity.P;
		for (Condition c: condSet.getConditions()) {
			if (!gammaX.contains(c.getRelations())) {
				result = Complexity.NP;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Init as gamma A
	 */
	private void initGammaList() {
		gammaX.add(createRelationSet(RelationEnum.LESS));
		gammaX.add(createRelationSet(RelationEnum.GREATER));
		gammaX.add(createRelationSet(RelationEnum.LESS, RelationEnum.EQUALS));
		gammaX.add(createRelationSet(RelationEnum.GREATER, RelationEnum.EQUALS));
		gammaX.add(createRelationSet(RelationEnum.LESS, RelationEnum.GREATER));
		gammaX.add(createRelationSet(RelationEnum.LESS, RelationEnum.GREATER, RelationEnum.EQUALS));
		gammaX.add(createRelationSet(RelationEnum.UNRELATED));
		gammaX.add(createRelationSet(RelationEnum.UNRELATED, RelationEnum.EQUALS));
		gammaX.add(createRelationSet(RelationEnum.EQUALS));
		//TODO unequals
		gammaX.add(createRelationSet(RelationEnum.LESS, RelationEnum.UNRELATED));
		gammaX.add(createRelationSet(RelationEnum.GREATER, RelationEnum.UNRELATED));
		gammaX.add(createRelationSet(RelationEnum.LESS, RelationEnum.UNRELATED, RelationEnum.EQUALS));
		gammaX.add(createRelationSet(RelationEnum.GREATER, RelationEnum.UNRELATED, RelationEnum.EQUALS));
	}
	
	private HashSet<RelationEnum> createRelationSet(RelationEnum... relations) {
		return new HashSet<RelationEnum>(Arrays.asList(relations));
	}

}
