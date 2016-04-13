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
public class TimePointDesc implements Comparable<Object> {

	public enum TimeTypeEnum {
		START, END
	};

	private BPELStateEnum state;
	private TimeTypeEnum timeType;

	public TimePointDesc(BPELStateEnum state, TimeTypeEnum timeType) {
		super();
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

	@Override
	public int compareTo(Object o) {
		if(!(o instanceof TimePointDesc)) throw new IllegalArgumentException();
		
		if (this.state == ((TimePointDesc) o).getState())
		{
			if (this.timeType == ((TimePointDesc) o).getTimeType()) {
				return 0;
			}
			else if (this.timeType.ordinal() < ((TimePointDesc) o).getTimeType().ordinal()) {
				return -1;
			}
			else {
				return 1;
			}
		}
		else {
			if (this.state.ordinal() < ((TimePointDesc) o).getState().ordinal()) {
				return -1;
			}
			else {
				return 1;
			}
		}
	}

	@Override
	public String toString() {
		return this.getState().name() + this.getTimeType().name();
	}
}
