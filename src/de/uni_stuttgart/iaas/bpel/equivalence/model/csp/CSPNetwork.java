package de.uni_stuttgart.iaas.bpel.equivalence.model.csp;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *  A csp network contains constraints and variables
 *  
 * @author Jonas Scheurich
 *
 */
public abstract class CSPNetwork {
	
	private List<CSPVariable> variables = new LinkedList<CSPVariable>();
	private Map<Pair<CSPVariable, CSPVariable>, CSPConstraint> constraints = new HashMap<Pair<CSPVariable, CSPVariable>, CSPConstraint>();

	protected void addVariable(CSPVariable v) {
		this.variables.add(v);
	}
	
	public Collection<CSPVariable> getVariables() {
		 return this.variables;
	}
	

	public void addConstraint(CSPConstraint c) {
		if (containsToWayConstraint(c.getFrom(), c.getTo())) {
			reduceTwoWayConstraint(c, true);
		}
		else {
			Pair<CSPVariable, CSPVariable> key = new MutablePair<CSPVariable, CSPVariable>( c.getFrom(), c.getTo());
			this.constraints.put(key, c);
		}
	}
	
	public void addConstraints(CSPConstraint...constraints) {
		for (CSPConstraint c: constraints) {
			addConstraint(c);
		}
	}

	public boolean containsConstraint(CSPVariable from, CSPVariable to) {
		Pair<CSPVariable, CSPVariable> key = new MutablePair<CSPVariable, CSPVariable>(from, to);
		return this.constraints.containsKey(key);
	}
	
	public boolean containsToWayConstraint(CSPVariable from, CSPVariable to) {
		return this.containsConstraint(from, to) || this.containsConstraint(to, from);
	}
	
	
	public Collection<CSPConstraint> getConstraints() {
		return this.constraints.values();
	}
	
	public CSPConstraint getConstraint(CSPVariable from, CSPVariable to) {
		Pair<CSPVariable, CSPVariable> key = new MutablePair<CSPVariable, CSPVariable>(from, to);
		return this.constraints.get(key);

	}
	
	public CSPConstraint getTwoWayConstraint(CSPVariable from, CSPVariable to) {
		CSPConstraint c = this.getConstraint(from, to);
		CSPConstraint cRev = this.getConstraint(to, from);
		
		if (c != null && cRev == null) return c;
		else if (cRev != null && c == null) return cRev.revert();
		else if (c == null && cRev == null) return null;
		else if (c.equals(cRev)) return c;
		else throw new IllegalStateException("Multiple constraints." + c + ", " + cRev);
	}
	
	public boolean removeConstraint(CSPConstraint c) {
		if (this.containsConstraint(c.getFrom(), c.getTo())) {
			Pair<CSPVariable, CSPVariable> key = new MutablePair<CSPVariable, CSPVariable>(c.getFrom(), c.getTo());
			this.constraints.remove(key);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean removeTwoWayConstraint(CSPConstraint c) {
		return removeConstraint(c) || removeConstraint(c.revert());
	}
	
	public CSPConstraint reduceTwoWayConstraint(CSPConstraint newConstraint, boolean create) {
		
		// check contradiction
		if (newConstraint.contradiction()) {
			throw new IllegalStateException("Contradiction in " + newConstraint.getName());
		}

		// get the old constraint in original and reversed direction
		// only one should be found.
		CSPConstraint oldConstraint = this.getConstraint(
				newConstraint.getFrom(), 
				newConstraint.getTo());
		CSPConstraint oldConstraintRev = this.getConstraint(
				newConstraint.getTo(),
				newConstraint.getFrom());

		// return if the constraint is unavailable
		if (oldConstraint == null && oldConstraintRev == null) {
			if (create) {
				// if create mode is selected, create a T transition
				oldConstraint = this.newTConstraint(
						newConstraint.getFrom(), 
						newConstraint.getTo());
			}
			else {
				return null;
			}
		}

		// select original or reverted constraint
		// throw exception, if a original and a reversed constraint exists.
		CSPConstraint opOldConstraint;
		CSPConstraint opNewConstraint;
		if (oldConstraint != null && oldConstraintRev == null) {
			// select the original
			opOldConstraint = oldConstraint;
			opNewConstraint = newConstraint;
		} else if (oldConstraintRev != null && oldConstraint == null) {
			//select reverted
			opOldConstraint = oldConstraintRev;
			opNewConstraint = newConstraint.revert();
		} else if(oldConstraint.equals(oldConstraintRev)) {
			// if equals (from=to) select original
			opOldConstraint = oldConstraint;
			opNewConstraint = newConstraint;
		} else {
			throw new IllegalStateException("Multiple constraints." + oldConstraint + ", " + oldConstraintRev);
		}

		// reduce constraint
		opOldConstraint.reduceAction(opNewConstraint);
		
		// return the reduced constraint.
		return opOldConstraint;
	}

	public abstract CSPConstraint newTConstraint(CSPVariable from, CSPVariable to);

}
