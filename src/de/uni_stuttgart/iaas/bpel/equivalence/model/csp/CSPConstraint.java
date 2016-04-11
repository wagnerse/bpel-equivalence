package de.uni_stuttgart.iaas.bpel.equivalence.model.csp;

public class CSPConstraint {
	
	private CSPVariable from;
	private CSPVariable to;
	
	public CSPConstraint() {
		
	}
	
	public CSPConstraint(CSPVariable from, CSPVariable to) {
		this.setFrom(from);
		this.setTo(to);
	}	
	
	public void setTo(CSPVariable to) {
		this.to = to;
	}

	public void setFrom(CSPVariable from) {
		this.from = from;
	}
	
	public CSPVariable getFrom() {
		return from;
	}
	
	public CSPVariable getTo() {
		return to;
	}

}
