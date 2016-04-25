package de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateInstance;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPConstraint;

public class ProcessDifference {

	private boolean isEquals = false;
	private List<BPELStateInstance> unexpectedStates = new ArrayList<BPELStateInstance>();
	private List<Pair<CSPConstraint, CSPConstraint>> unequalConstraints = new ArrayList<Pair<CSPConstraint, CSPConstraint>>();
	
	public ProcessDifference() {
		// TODO Auto-generated constructor stub
	}
	
	public void setEquals(boolean isEquals) {
		this.isEquals = isEquals;
	}
	
	public void addUnexpectedStates(BPELStateInstance i) {
		this.unexpectedStates.add(i);
	}
	
	public void addUnequalsConstraints(CSPConstraint c1, CSPConstraint c2) {
		this.unequalConstraints.add(new MutablePair<CSPConstraint, CSPConstraint>(c1, c2));
	}
	
	public boolean isEquals() {
		return isEquals;
	}
	
	public List<BPELStateInstance> getUnexpectedStates() {
		return unexpectedStates;
	}
	
	public List<Pair<CSPConstraint, CSPConstraint>> getUnequalConstraints() {
		return unequalConstraints;
	}
}
