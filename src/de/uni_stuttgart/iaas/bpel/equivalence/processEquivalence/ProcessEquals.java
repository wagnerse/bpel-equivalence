package de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;

/**
 * Checks if two BPEL processes are equivalent. 
 * 
 * @author Jonas Scheurich
 *
 */
public class ProcessEquals {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private ProcessDifference result = null;

	/**
	 * Check if two BPEL processes (represented as point algebra network) are equivalent
	 * by this two conditions:
	 * 
	 * (1) Both processes holds the same time points 
	 *     that are requested by the configuration
	 * (2) The relations between this time points are equals.
	 * 
	 * @param process1
	 * @param process2
	 * @param config
	 * @return
	 */
	public boolean equalProcesses(PANetwork process1, PANetwork process2, EqualsConfiguration config) {
		this.result = new ProcessDifference();
		this.result.setEquals(true);
		Map<Pair<String, BPELStateEnum>, Pair<PAVariable, PAVariable>> states = 
				initMap(process1, process2, config);
		
		// check availability of the variables
		if(!checkStates(config, states)) {
			this.result.setEquals(false);
			return this.result.isEquals();
		}

		// check relations
		for (BPELStateEnum s: config.getStates()) {
			for (String activityA: config.getActivities()) {
				for (String activityB: config.getActivities()) {
					if (activityA.equals(activityB)) continue;
					
					// create pairs of the start time points of the given state of activtity A and B
					// Pair A contains the start time point of the given state of the activity A in both processes
					// Pair B contains the start time point of the given state of the activity B in both processes
					Pair<PAVariable, PAVariable> pairA = states.get(new MutablePair<String, BPELStateEnum>(activityA, s));
					Pair<PAVariable, PAVariable> pairB = states.get(new MutablePair<String, BPELStateEnum>(activityB, s));
					
					// get constraints
					PAConstraint constraint1 = (PAConstraint) process1.getTwoWayConstraint(pairA.getLeft(), pairB.getLeft());
					PAConstraint constraint2 = (PAConstraint) process2.getTwoWayConstraint(pairA.getRight(), pairB.getRight());
					
					// check & correct constraint direction
					if (!constraint1.equalsDirection(constraint2)) {
						constraint2 = constraint2.revert();
						LOGGER.info("Process equal check: Revert constraint " 
								+ "to be conform with first process constraint: "
								+ constraint2);
					}
					
					if (!constraint1.equalsRelations(constraint2)) {
						this.result.addUnequalsConstraints(constraint1, constraint2);
						this.result.setEquals(false);
					}
					
				}
			}
		}

		return this.result.isEquals();
	}
	
	/**
	 * Check if the BPEL states of a activity are hold in both processes.
	 * @param config 
	 * 
	 * @param intervalls
	 * @return
	 */
	private boolean checkStates(EqualsConfiguration config, Map<Pair<String, BPELStateEnum>, Pair<PAVariable, PAVariable>> timePoints) {
		
		// check completeness of the variable pairs
		for (Pair<PAVariable, PAVariable> pair: timePoints.values()) {
			if (pair.getLeft() == null && pair.getRight() != null) {
				this.result.addUnexpectedStates(pair.getRight());
			}
			if (pair.getLeft() != null && pair.getRight() == null) {
				this.result.addUnexpectedStates(pair.getLeft());
			}
		}
		
		// check soundness to the activities in the configuration
		for (String act: config.getActivities()) {
			for (BPELStateEnum s: config.getStates()) {
				// create the key to check the availability of the requested state
				Pair<String, BPELStateEnum> key = 
						new MutablePair<String, BPELStateEnum>(act , s);
				
				//check key
				if (!timePoints.containsKey(key)) {
					this.result.addMissingState(act);
				}
			}
		}
		
		if (this.result.getUnexpectedStates().size() == 0
				&& result.getMissingStates().size() == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Init the states map with the pair of the activities of the two processes
	 * in respect of the configuration. States outside the configuration are not
	 * part of the map.
	 * 
	 * Each value contains the both corresponding activities of the two processes.
	 * The key of the map is created by the activity name and a {@link BPELStateEnum}.
	 * 
	 * @param process1
	 * @param process2
	 * @param config
	 * @return
	 */
	private Map<Pair<String, BPELStateEnum>, Pair<PAVariable, PAVariable>> initMap(PANetwork process1, PANetwork process2, EqualsConfiguration config)  {
		Map<Pair<String, BPELStateEnum>, Pair<PAVariable, PAVariable>>  map = 
				new HashMap<Pair<String, BPELStateEnum>, Pair<PAVariable, PAVariable>> ();
		
		for (CSPVariable v1: process1.getVariables()) {
			if (!(v1 instanceof PAVariable)) continue;
			PAVariable time1 = (PAVariable) v1;
			if (config.getActivities().contains(time1.getBpelName()) 
					&& config.getStates().contains(time1.getTimePoint().getState())
					&& time1.getTimePoint().getTimeType() == TimeTypeEnum.START) {
				
				// create the key
				Pair<String, BPELStateEnum> key = 
						new MutablePair<String, BPELStateEnum>(
								time1.getBpelName(), 
								time1.getTimePoint().getState());
				// create the value with first element
				Pair<PAVariable, PAVariable> value = 
						new MutablePair<PAVariable, PAVariable>(time1, null);
				// add to map
				map.put(key, value);
			}
		}
		
		for (CSPVariable v2: process2.getVariables()) {
			if (!(v2 instanceof PAVariable)) continue;
			PAVariable time2 = (PAVariable) v2;
			if (config.getActivities().contains(time2.getBpelName()) 
					&& config.getStates().contains(time2.getTimePoint().getState())
					&& time2.getTimePoint().getTimeType() == TimeTypeEnum.START) {
				
				// create the key
				Pair<String, BPELStateEnum> key = 
						new MutablePair<String, BPELStateEnum>(
								time2.getBpelName(), 
								time2.getTimePoint().getState());
				// add second element to the value
				Pair<PAVariable, PAVariable> value; 
				if (map.containsKey(key)) {
					value = map.get(key);
					PAVariable firstElement = value.getLeft();
					value = new MutablePair<PAVariable, PAVariable>(firstElement, time2);
				}
				else {
					value = new MutablePair<PAVariable, PAVariable>(null, time2);
				}
				// add to map
				map.put(key, value);
			}
		}
		
		return map;
	}
	
	/**
	 * Get the difference report of the last analysis run.
	 * @return
	 */
	public ProcessDifference getProcessDifference() {
		return this.result;
	}

}
