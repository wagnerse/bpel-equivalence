package de.uni_stuttgart.iaas.bpel.equivalence.model.csp;

/**
 * A csp constraint describes the relation between two csp variables.
 * 
 * @author Jonas Scheurich
 *
 */
public abstract class CSPConstraint {
	
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
	
	public String getName() {
		return (this.getFrom()).getName() 
				+ " " + valueToString() 
				+ " " + (this.getTo()).getName();
	}

	public abstract CSPConstraint revert();

	@Override
	public abstract String toString();
	
	public abstract String valueToString();
	
	@Override
	public abstract Object clone();
	
	@Override
	public abstract boolean equals(Object obj);
	
	public abstract boolean contradiction();
	
	public abstract void reduceAction(CSPConstraint constraint);

}
