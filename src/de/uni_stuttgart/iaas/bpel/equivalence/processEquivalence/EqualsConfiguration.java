package de.uni_stuttgart.iaas.bpel.equivalence.processEquivalence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;

public class EqualsConfiguration {

	private List<String> activities = new ArrayList<String>();
	private List<BPELStateEnum> states = new ArrayList<BPELStateEnum>();
	
	public EqualsConfiguration() {
	}
	
	public EqualsConfiguration(BPELStateEnum state, String...activities) {
		this.addState(state);
		this.activities.addAll(Arrays.asList(activities));
	}
	
	public EqualsConfiguration(BPELStateEnum state, List<String> activities) {
		this.addState(state);
		this.activities.addAll(activities);
	}
	
	public void addActivity(String a) {
		this.activities.add(a);
	}
	
	public void addState(BPELStateEnum s) {
		this.states.add(s);
	}
	
	public List<String> getActivities() {
		return activities;
	}
	
	public List<BPELStateEnum> getStates() {
		return states;
	}
}
