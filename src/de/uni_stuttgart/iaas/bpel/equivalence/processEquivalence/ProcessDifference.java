package de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;

/**
 * The process difference describes the difference between two BPEL processes.
 * This class holds missing activities and the constraints that are different.
 * 
 * @author Jonas Scheurich
 *
 */
public class ProcessDifference {

	private boolean isEquals = false;
	private List<PAVariable> unexpectedStates = new ArrayList<PAVariable>();
	private List<String> missingStates = new ArrayList<String>();
	private List<Pair<CSPConstraint, CSPConstraint>> unequalConstraints = new ArrayList<Pair<CSPConstraint, CSPConstraint>>();
	
	public ProcessDifference() {
		// TODO Auto-generated constructor stub
	}
	
	public void setEquals(boolean isEquals) {
		this.isEquals = isEquals;
	}
	
	public void addUnexpectedStates(PAVariable i) {
		this.unexpectedStates.add(i);
	}
	
	public void addUnequalsConstraints(CSPConstraint c1, CSPConstraint c2) {
		this.unequalConstraints.add(new MutablePair<CSPConstraint, CSPConstraint>(c1, c2));
	}
	
	public void addMissingState(String i) {
		this.missingStates.add(i);
	}
	
	public boolean isEquals() {
		return isEquals;
	}
	
	/**
	 * List of the BPEL states, that are missing in one of the processes.
	 * @return
	 */
	public List<PAVariable> getUnexpectedStates() {
		return unexpectedStates;
	}
	
	/**
	 * List of the constraints, that are different in the two processes.
	 * @return
	 */
	public List<Pair<CSPConstraint, CSPConstraint>> getUnequalConstraints() {
		return unequalConstraints;
	}
	
	/**
	 * List of states missed by the equals configuration
	 * @return
	 */
	public List<String> getMissingStates() {
		return missingStates;
	}
}
