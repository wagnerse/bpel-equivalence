package de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.complexity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra.Condition;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra.ConditionSet;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.pointalgebra.RelationEnum;

public class AllenComplexity {
	
	private List<HashSet<RelationEnum>> gammaX = new ArrayList<HashSet<RelationEnum>>();
	
	private ConditionSet condSet = new ConditionSet();

	public enum Complexity {P, NP};
	
	public AllenComplexity() {
		initGammaList();
	}
	
	public ConditionSet getConditionSet() {
		return condSet;
	}
	
	public Complexity isNP(StateConstraint c) {
		
		for(Type t : c.getTypes()) {
			ConditionSet condSet2 = new ConditionSet(t);
			condSet.compose(condSet2);
		}
		
		return checkComplexity(condSet);
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
