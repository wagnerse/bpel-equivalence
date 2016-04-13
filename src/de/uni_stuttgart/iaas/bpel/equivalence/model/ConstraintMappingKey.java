package de.uni_stuttgart.iaas.bpel.equivalence.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The constraint mapping key relates the start and end of a constraint
 * This class is used as hash key for the constraint mapping.
 *
 * @author Jonas Scheurich
 *
 */
public class ConstraintMappingKey {

	private TimePointDesc p1;
	private AbstractActivityNetwork n1;

	private TimePointDesc p2;
	private AbstractActivityNetwork n2;

	public ConstraintMappingKey(AbstractActivityNetwork n1, TimePointDesc p1, 
			AbstractActivityNetwork n2, TimePointDesc p2) {
		this.p1 = p1;
		this.n1 = n1;
		this.p2 = p2;
		this.n2 = n2;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(53, 67)
				.append(p1)
				.append(n1.getEObject())
				.append(p2)
				.append(n2.getEObject())
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConstraintMappingKey))
			return false;
		if (obj == this)
			return true;

		ConstraintMappingKey rhs = (ConstraintMappingKey) obj;
		return new EqualsBuilder()
				.append(p1, rhs.p1)
				.append(n1, rhs.n1)
				.append(p2, rhs.p2)
				.append(n2, rhs.n2)
				.isEquals();
	}

}
