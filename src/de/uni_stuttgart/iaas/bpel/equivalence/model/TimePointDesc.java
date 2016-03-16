package de.uni_stuttgart.iaas.bpel.equivalence.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * @author Jonas Scheurich
 * 
 * A time point description describes a time point (start or end) 
 * from the state of a BPEL element (see {@link BPELStateEnum})
 *
 */
public class TimePointDesc {

	public enum TimeTypeEnum {
		START, END
	};

	private BPELStateEnum state;
	private TimeTypeEnum timeType;

	public TimePointDesc(BPELStateEnum state, TimeTypeEnum timeType) {
		this.state = state;
		this.timeType = timeType;
	}

	public BPELStateEnum getState() {
		return state;
	}

	public TimeTypeEnum getTimeType() {
		return timeType;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(state).append(timeType).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TimePointDesc))
			return false;
		if (obj == this)
			return true;

		TimePointDesc rhs = (TimePointDesc) obj;
		return new EqualsBuilder().append(state, rhs.state).append(timeType, rhs.timeType).isEquals();
	}
}
