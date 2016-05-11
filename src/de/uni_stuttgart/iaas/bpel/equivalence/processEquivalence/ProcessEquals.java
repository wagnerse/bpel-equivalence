package de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateInstance;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

public class ProcessEquals {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private ProcessDifference result = null;

	public boolean equalProcesses(PANetwork process1, PANetwork process2, EqualsConfiguration config) {
		this.result = new ProcessDifference();
		Map<Pair<String, BPELStateEnum>, Pair<BPELStateInstance, BPELStateInstance>> states = 
				initMap(process1, process2, config);
		
		// check availability of the variables
		if(!checkStates(states)) {
			this.result.setEquals(false);
			return false;
		}

		// check relations
		for (BPELStateEnum s: config.getStates()) {
			for (String activityA: config.getActivities()) {
				for (String activityB: config.getActivities()) {
					if (activityA.equals(activityB)) continue;
					
					// get pairs
					Pair<BPELStateInstance, BPELStateInstance> pairA = states.get(new MutablePair<String, BPELStateEnum>(activityA, s));
					Pair<BPELStateInstance, BPELStateInstance> pairB = states.get(new MutablePair<String, BPELStateEnum>(activityB, s));
					
					// get constraints
					PAConstraint constraint1 = (PAConstraint) process1.getTwoWayConstraint(pairA.getLeft().getEnd(), pairB.getLeft().getStart());
					PAConstraint constraint2 = (PAConstraint) process2.getTwoWayConstraint(pairA.getRight().getEnd(), pairB.getRight().getStart());
					
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

		this.result.setEquals(true);
		return true;
	}
	
	private boolean checkStates(Map<Pair<String, BPELStateEnum>, Pair<BPELStateInstance, BPELStateInstance>> intervalls) {
		
		for (Pair<BPELStateInstance, BPELStateInstance> pair: intervalls.values()) {
			if (pair.getLeft() == null && pair.getRight() != null) {
				this.result.addUnexpectedStates(pair.getRight());
			}
			if (pair.getLeft() != null && pair.getRight() == null) {
				this.result.addUnexpectedStates(pair.getLeft());
			}
		}
		
		if (this.result.getUnexpectedStates().size() == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	private Map<Pair<String, BPELStateEnum>, Pair<BPELStateInstance, BPELStateInstance>> initMap(PANetwork process1, PANetwork process2, EqualsConfiguration config)  {
		Map<Pair<String, BPELStateEnum>, Pair<BPELStateInstance, BPELStateInstance>>  map = 
				new HashMap<Pair<String, BPELStateEnum>, Pair<BPELStateInstance, BPELStateInstance>> ();
		
		for (BPELStateInstance i1: process1.getVariablePairs()) {
			if (config.getActivities().contains(i1.getBpelName()) &&
					config.getStates().contains(i1.getState())) {
				
				// create the key
				Pair<String, BPELStateEnum> key = 
						new MutablePair<String, BPELStateEnum>((String) EMFUtils.getAttributeByName(i1.getBpelElement(), "name"), i1.getState());
				// create the value with first element
				Pair<BPELStateInstance, BPELStateInstance> value = 
						new MutablePair<BPELStateInstance, BPELStateInstance>(i1, null);
				// add to map
				map.put(key, value);
			}
		}
		
		for (BPELStateInstance i2: process2.getVariablePairs()) {
			if (config.getActivities().contains(i2.getBpelName()) &&
					config.getStates().contains(i2.getState())) {
				
				// create the key
				Pair<String, BPELStateEnum> key = 
						new MutablePair<String, BPELStateEnum>((String) EMFUtils.getAttributeByName(i2.getBpelElement(), "name"), i2.getState());
				// add second element to the value
				Pair<BPELStateInstance, BPELStateInstance> value; 
				if (map.containsKey(key)) {
					value = map.get(key);
					BPELStateInstance firstElement = value.getLeft();
					value = new MutablePair<BPELStateInstance, BPELStateInstance>(firstElement, i2);
				}
				else {
					value = new MutablePair<BPELStateInstance, BPELStateInstance>(null, i2);
				}
				// add to map
				map.put(key, value);
			}
		}
		
		return map;
	}
	
	public ProcessDifference getProcessDifference() {
		return this.result;
	}

}
