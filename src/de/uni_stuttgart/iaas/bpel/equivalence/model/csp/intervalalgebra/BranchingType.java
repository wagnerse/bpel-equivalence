package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

/**
 * 
 * @author Jonas Scheurich
 * 
 * Allens intervall constraints extended with branching constraints
 * 
 * See paper Rangi etal. - Branching Allen Reasoning with Intervals in Branching Time
 *
 */
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
	
	Unrelated,
	
	InitiallyBefore,
	
	InitiallyAfter,
	
	InitiallyMeets,
	
	InitiallyMetBy,
	
	InitiallyEquals;
	
	/**
	 * Transform from a {@link org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type}
	 * @param type {@link Type}
	 * @return {@link BranchingType}
	 */
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
	
	/**
	 * Transform this enum to a {@link org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type}
	 * @return {@link Type}
	 */
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
	
	public static BranchingType revert(BranchingType t) {
		if (t == BranchingType.Before) {
			return BranchingType.After;
		}
		else if (t == BranchingType.After) {
			return BranchingType.Before;
		}
		else if (t == BranchingType.Meets) {
			return BranchingType.MetBy;
		}
		else if (t == BranchingType.MetBy) {
			return BranchingType.Meets;
		}
		else if (t == BranchingType.Overlaps) {
			return BranchingType.OverlappedBy;
		}
		else if (t == BranchingType.OverlappedBy) {
			return BranchingType.Overlaps;
		}
		else if (t == BranchingType.During) {
			return BranchingType.Contains;
		}
		else if (t == BranchingType.Contains) {
			return BranchingType.During;
		}
		else if (t == BranchingType.Starts) {
			return BranchingType.StartedBy;
		}
		else if (t == BranchingType.StartedBy) {
			return BranchingType.Starts;
		}
		else if (t == BranchingType.Finishes) {
			return BranchingType.FinishedBy;
		}
		else if (t == BranchingType.FinishedBy) {
			return BranchingType.Finishes;
		}
		else if (t == BranchingType.Equals) {
			return BranchingType.Equals;
		}
		else if (t == BranchingType.PartiallyBefore) {
			return BranchingType.PartiallyAfter;
		}
		else if (t == BranchingType.PartiallyAfter) {
			return BranchingType.PartiallyBefore;
		}
		else if (t == BranchingType.PartiallyMeets) {
			return BranchingType.PartiallyMetBy;
		}
		else if (t == BranchingType.PartiallyMetBy) {
			return BranchingType.PartiallyMeets;
		}
		else if (t == BranchingType.PartiallyStarts) {
			return BranchingType.PartiallyStarts;
		}
		else if (t == BranchingType.PartiallyOverlaps) {
			return BranchingType.PartiallyOverlappedBy;
		}
		else if (t == BranchingType.PartiallyOverlappedBy) {
			return BranchingType.PartiallyOverlaps;
		}
		else if (t == BranchingType.Adjacent) {
			return BranchingType.AdjacentBy;
		}
		else if (t == BranchingType.AdjacentBy) {
			return BranchingType.Adjacent;
		}
		else if (t == BranchingType.Touches) {
			return BranchingType.Touches;
		}
		else if (t == BranchingType.Unrelated) {
			return BranchingType.Unrelated;
		}
		else if (t == BranchingType.InitiallyBefore) {
			return BranchingType.InitiallyAfter;
		}
		else if (t == BranchingType.InitiallyAfter) {
			return BranchingType.InitiallyBefore;
		}
		else if (t == BranchingType.InitiallyMeets) {
			return BranchingType.InitiallyMetBy;
		}
		else if (t == BranchingType.InitiallyMetBy) {
			return BranchingType.InitiallyMeets;
		}
		else if (t == BranchingType.InitiallyEquals) {
			return BranchingType.InitiallyEquals;
		}
		else {
			return t;
		}
	}

}
