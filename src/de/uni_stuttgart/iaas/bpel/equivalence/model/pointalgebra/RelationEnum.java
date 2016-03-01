package de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra;

/**
 * 
 * @author Jonas Scheurich
 * Different point algebra base relations
 * 
 * LESS			// x < y iff x precedes y
 * GREATER 		// x > y iff y precedes x
 * EQUALS		// x = Y iff x, y are at the same point
 * UNEQUALS
 * UNRELATED	// x || y iff x, y belong to different branches ort points
 * 
 */
public enum RelationEnum {
	LESS,		// x < y iff x precedes y
	GREATER, 	// x > y iff y precedes x
	EQUALS, 	// x = Y iff x, y are at the same point
	UNEQUALS,
	UNRELATED,	// x || y iff x, y belong to different branches ort points
	
}
