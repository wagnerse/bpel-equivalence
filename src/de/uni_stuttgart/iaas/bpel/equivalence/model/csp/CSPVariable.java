package de.uni_stuttgart.iaas.bpel.equivalence.model.csp;

/**
 * Describes a variable in a csp network.
 * 
 * @author Jonas Scheurich
 *
 */
public abstract class CSPVariable {
	private int id;

	public CSPVariable(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public abstract String getName();
	
	@Override
	public abstract boolean equals(Object obj);
	
}
