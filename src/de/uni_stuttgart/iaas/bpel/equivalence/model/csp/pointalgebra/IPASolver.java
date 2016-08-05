package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra;

public interface IPASolver {

	public boolean propagate();
	
	public void setProblem(PANetwork problem);
	
	public PANetwork getProblem();
}
