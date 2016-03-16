package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.complexity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonas Scheurich
 * Contains the result of an Allen complexity calculation
 *
 */
public class TractableClassResult {
	
	public enum ComplexityResult {CONTAINED, NOT_CONTAINED};
	
	private String tractableClassName;
	private ComplexityResult result;
	private ConditionSet conditionSet;
	private List<Condition> uncontainedConditions = new ArrayList<Condition>();
	
	public TractableClassResult(String tractableClassName) {
		this.tractableClassName = tractableClassName;
	}
	
	public TractableClassResult(
			String tractableClassName,
			ComplexityResult result,
			ConditionSet conditionSet, 
			List<Condition> uncontainedConditions) {
		this.tractableClassName = tractableClassName;
		this.result = result;
		this.conditionSet = conditionSet;
		this.uncontainedConditions = uncontainedConditions;
	}
	
	public String getTractableClassName() {
		return tractableClassName;
	}
	
	public ComplexityResult getResult() {
		return result;
	}
	
	public ConditionSet getConditionSet() {
		return conditionSet;
	}
	
	public List<Condition> getUncontainedConditions() {
		return uncontainedConditions;
	}
	
	public void setResult(ComplexityResult result) {
		this.result = result;
	}
	
	public void setConditionSet(ConditionSet conditionSet) {
		this.conditionSet = conditionSet;
	}
	
	public void setUncontainedConditions(List<Condition> uncontainedConditions) {
		this.uncontainedConditions = uncontainedConditions;
	}

}
