package de.uni_stuttgart.iaas.bpel.equivalence.PA2IA;

import org.apache.commons.lang.builder.EqualsBuilder;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra.BranchingType;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;

/**
 * This class describes a interval algebra relation with four point algebra relations.
 * 
 * @author Jonas Scheurich
 *
 */
public class IAMatch {

	private RelationEnum sI_eJ;
	private RelationEnum sJ_eI;
	private RelationEnum sI_sJ;
	private RelationEnum eI_eJ;

	BranchingType intervalRelation;

	public IAMatch(RelationEnum sI_eJ, RelationEnum sJ_eI, RelationEnum sI_sJ, RelationEnum eI_eJ,
			BranchingType intervalRelation) {
		this.sI_eJ = sI_eJ;
		this.sJ_eI = sJ_eI;
		this.sI_sJ = sI_sJ;
		this.eI_eJ = eI_eJ;
		this.intervalRelation = intervalRelation;
	}
	
	public BranchingType getIntervalRelation() {
		return intervalRelation;
	}

	/**
	 * Check if a point algebra relation holds between four time points.
	 * 
	 * @param startI_endJ
	 * @param startJ_endI
	 * @param startI_startJ
	 * @param endI_endJ
	 * @return
	 */
	public BranchingType match(PAConstraint startI_endJ, PAConstraint startJ_endI, 
			PAConstraint startI_startJ,PAConstraint endI_endJ) {
		if (startI_endJ == null || startJ_endI == null || startI_startJ == null || startJ_endI == null) {
			return null;
		}
		
		BranchingType result = intervalRelation;
		
		if (!startI_endJ.getRelations().contains(sI_eJ)) {
			result = null;
		}
		if (!startJ_endI.getRelations().contains(sJ_eI)) {
			result = null;
		}
		if (!startI_startJ.getRelations().contains(sI_sJ)) {
			result = null;
		}
		if (!endI_endJ.getRelations().contains(eI_eJ)) {
			result = null;
		}
			
		return result;
	}
	
	public boolean equalsRelations(Object obj) {
		if (!(obj instanceof IAMatch))
			return false;
		if (obj == this)
			return true;

		IAMatch rhs = (IAMatch) obj;
		return new EqualsBuilder()
				.append(sI_eJ, rhs.sI_eJ)
				.append(sJ_eI, rhs.sJ_eI)
				.append(sI_sJ, rhs.sI_sJ)
				.append(eI_eJ, rhs.eI_eJ)
				.isEquals();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IAMatch))
			return false;
		if (obj == this)
			return true;

		IAMatch rhs = (IAMatch) obj;
		return new EqualsBuilder()
				.append(intervalRelation, rhs.intervalRelation)
				.isEquals()
				&& this.equalsRelations(rhs);
	}

	@Override
	public String toString() {
		return "{IAMatch " + this.intervalRelation.name() + "}";
	}
}
