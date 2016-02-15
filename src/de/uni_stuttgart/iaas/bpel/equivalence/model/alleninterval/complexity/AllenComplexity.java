package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.complexity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BranchingType;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.complexity.TractableClassResult.ComplexityResult;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.pointalgebra.Condition;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.pointalgebra.ConditionSet;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.pointalgebra.RelationEnum;

/**
 * 
 * @author Jonas Scheurich
 * This class provides different methods to calculate if a set of relations is contained in a given
 * tractable class. The calculation is performed on point algebra.
 * See M Broxvall - The Point Algebra for Branching Time Revisited for further informations.
 */
public class AllenComplexity {
	
	private List<HashSet<RelationEnum>> gammaX = new ArrayList<HashSet<RelationEnum>>();
	private Map<HashSet<BranchingType>, TractableClassResult> complexityCache = new HashMap<HashSet<BranchingType>, TractableClassResult>(); 
	
	public AllenComplexity() {
		initGammaList();
	}
	
	/**
	 * Calculate if a State Constraint is in the tractable class.
	 * Caching is disabled
	 * @param c {@link StateConstraint}
	 * @return {@link ComplexityResult}
	 */
	public TractableClassResult checkTractabilityClass(StateConstraint c) {
		return checkTractabilityClass(c, false);
	}
	
	/**
	 * Calculate if a State Constraint is in the tractable class.
	 * @param c {@link StateConstraint}
	 * @param caching enables result caching
	 * @return {@link ComplexityResult}
	 */
	public TractableClassResult checkTractabilityClass(StateConstraint c, boolean caching) {
		List<BranchingType> types = new ArrayList<BranchingType>();
		for(Type t : c.getTypes()) {
			types.add(BranchingType.fromType(t));
		}
		
		return checkTractabilityClass(types, caching);
	}
	
	/**
	 * Calculate if a list of constraint types is in the tractable class.
	 * Caching is disabled
	 * @param types {@link BranchingType}
	 * @return {@link ComplexityResult}
	 */
	public TractableClassResult checkTractabilityClass(List<BranchingType> types) {
		return checkTractabilityClass(types, false);
	}
	
	/**
	 * Calculate if a list of constraint types is in the tractable class.
	 * Caching is disabled
	 * @param types list of {@link BranchingType} (constraints in branching time)
	 * @param caching enables result caching
	 * @return {@link ComplexityResult}
	 */
	public TractableClassResult checkTractabilityClass(List<BranchingType> types, boolean caching) {
		// return a cached element if available
		HashSet<BranchingType> cachingKey = new HashSet<BranchingType>(types);
		if (caching && complexityCache.containsKey(cachingKey)) {
			return complexityCache.get(cachingKey);
		}
		
		// create a condition set from the constraint types
		ConditionSet condSet = new ConditionSet();
		for(BranchingType t : types) {
			// create a condition set for t
			ConditionSet condSet2 = new ConditionSet(t);
			
			// add the condition set (created from t)
			condSet.orAdd(condSet2);
		}
		
		// check if all relations are included in the gammaX
		List<Condition> uncontainedConditions = new ArrayList<Condition>();
		ComplexityResult result = ComplexityResult.CONTAINED;
		for (Condition c: condSet.getConditions()) {
			if (!gammaX.contains(c.getRelations())) {
				result = ComplexityResult.NOT_CONTAINED;
				uncontainedConditions.add(c);
			}
		}
		// Create result object
		return new TractableClassResult("A", result, condSet, uncontainedConditions);
	}
	
	/**
	 * Init as gamma A
	 * See M Broxvall - The Point Algebra for Branching Time Revisited, table 2
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
		gammaX.add(createRelationSet(RelationEnum.UNEQUALS));
		gammaX.add(createRelationSet(RelationEnum.LESS, RelationEnum.UNRELATED));
		gammaX.add(createRelationSet(RelationEnum.GREATER, RelationEnum.UNRELATED));
		gammaX.add(createRelationSet(RelationEnum.LESS, RelationEnum.UNRELATED, RelationEnum.EQUALS));
		gammaX.add(createRelationSet(RelationEnum.GREATER, RelationEnum.UNRELATED, RelationEnum.EQUALS));
	}
	
	/**
	 * Create a HashSet from a RelationEnum array.
	 * @param relations RelationEnum array
	 * @return HashSet of RelationEnum
	 */
	private HashSet<RelationEnum> createRelationSet(RelationEnum... relations) {
		return new HashSet<RelationEnum>(Arrays.asList(relations));
	}

}
