package de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

public enum BranchingType {

	
	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A BEFORE B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/beforeQ.png alt=""> 
	 */
	Before,

	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A MEETS B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/meetsQ.png alt=""> 
	 */
	Meets,
	
	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A OVERLAPS B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/overlapsQ.png alt=""> 
	 */
	Overlaps,

	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A FINISHED-BY B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/finishedbyQ.png alt=""> 
	 */
	FinishedBy,
	
	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A CONTAINS B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/containsQ.png alt=""> 
	 */
	Contains,


	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A STARTED-BY B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/startedbyQ.png alt=""> 
	 */
	StartedBy,
	
	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A EQUALS B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/equalsQ.png alt=""> 
	 */
	Equals,

	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A STARTS B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/startsQ.png alt=""> 
	 */
	Starts,
	
	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A DURING B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/duringQ.png alt=""> 
	 */
	During,

	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A FINISHES B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/finishesQ.png alt=""> 
	 */
	Finishes,
	
	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A OVERLAPPED-BY B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/overlappedbyQ.png alt=""> 
	 */
	OverlappedBy,
	
	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A MET-BY B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/metbyQ.png alt=""> 
	 */
	MetBy,
	
	/**
	 * <br>&nbsp;&nbsp;&nbsp;Semantics: A AFTER B<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=../../images/afterQ.png alt=""> 
	 */
	After,
	
	//Branching Types
	
	PartiallyBefore,
	
	PartiallyAfter,
	
	PartiallyMeets,
	
	PartiallyMetBy,
	
	PartiallyOverlaps,
	
	PartiallyOverlappedBy,
	
	PartiallyStarts,
	
	Adjacent,
	
	AdjacentBy,
	
	Touches,
	
	Unrelated;
	
	public static BranchingType fromType(Type type) {

		if (type == Type.Before) {
			return BranchingType.Before;
		}
		else if (type == Type.Meets) {
			return BranchingType.Meets;
		}
		else if (type == Type.Overlaps) {
			return BranchingType.Overlaps;
		}
		else if (type == Type.FinishedBy) {
			return BranchingType.FinishedBy;
		}
		else if (type == Type.Contains) {
			return BranchingType.Contains;
		}
		else if (type == Type.StartedBy) {
			return BranchingType.StartedBy;
		}
		else if (type == Type.Equals) {
			return BranchingType.Equals;
		}
		else if (type == Type.Starts) {
			return BranchingType.Starts;
		}
		else if (type == Type.During) {
			return BranchingType.During;
		}
		else if (type == Type.Finishes) {
			return BranchingType.Finishes;
		}
		else if (type == Type.OverlappedBy) {
			return BranchingType.OverlappedBy;
		}
		else if (type == Type.MetBy) {
			return BranchingType.MetBy;
		}
		else if (type == Type.After) {
			return BranchingType.After;
		}
		else {
			throw new IllegalStateException("Can not transform branching types.");
		}
	}
	
	public Type toCSPType() {
		
		if (this == BranchingType.Before) {
			return Type.Before;
		}
		else if (this == BranchingType.Meets) {
			return Type.Meets;
		}
		else if (this == BranchingType.Overlaps) {
			return Type.Overlaps;
		}
		else if (this == BranchingType.FinishedBy) {
			return Type.FinishedBy;
		}
		else if (this == BranchingType.Contains) {
			return Type.Contains;
		}
		else if (this == BranchingType.StartedBy) {
			return Type.StartedBy;
		}
		else if (this == BranchingType.Equals) {
			return Type.Equals;
		}
		else if (this == BranchingType.Starts) {
			return Type.Starts;
		}
		else if (this == BranchingType.During) {
			return Type.During;
		}
		else if (this == BranchingType.Finishes) {
			return Type.Finishes;
		}
		else if (this == BranchingType.OverlappedBy) {
			return Type.OverlappedBy;
		}
		else if (this == BranchingType.MetBy) {
			return Type.MetBy;
		}
		else if (this == BranchingType.After) {
			return Type.After;
		}
		else {
			throw new IllegalStateException("Can not transform branching types.");
		}
	}

}
