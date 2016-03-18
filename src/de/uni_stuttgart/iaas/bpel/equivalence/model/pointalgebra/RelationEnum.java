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
	UNRELATED;	// x || y iff x, y belong to different branches ort points	
	
	public RelationEnum[] compose(RelationEnum r2) {
		
		if (this == RelationEnum.LESS) {
			if (r2 == RelationEnum.LESS) {
				RelationEnum[] result = {RelationEnum.LESS};
				return result;
			}
			else if (r2 == RelationEnum.GREATER) {
				RelationEnum[] result = {RelationEnum.LESS, RelationEnum.EQUALS, RelationEnum.GREATER};
				return result;
			}
			else if (r2 == RelationEnum.UNRELATED) {
				RelationEnum[] result = {RelationEnum.LESS, RelationEnum.UNRELATED};
				return result;
			}
			else if (r2 == RelationEnum.EQUALS) {
				RelationEnum[] result = {RelationEnum.LESS};
				return result;
			}
			else {
				throw new IllegalStateException();
			}
		}
		else if (this == RelationEnum.GREATER) {
			if (r2 == RelationEnum.LESS) {
				RelationEnum[] result = {RelationEnum.LESS, RelationEnum.EQUALS, RelationEnum.UNRELATED, RelationEnum.GREATER};
				return result;
			}
			else if (r2 == RelationEnum.GREATER) {
				RelationEnum[] result = {RelationEnum.LESS};
				return result;
			}
			else if (r2 == RelationEnum.UNRELATED) {
				RelationEnum[] result = {RelationEnum.UNRELATED};
				return result;
			}
			else if (r2 == RelationEnum.EQUALS) {
				RelationEnum[] result = {RelationEnum.GREATER};
				return result;
			}
			else {
				throw new IllegalStateException();
			}
		}
		else if (this == RelationEnum.UNRELATED) {
			if (r2 == RelationEnum.LESS) {
				RelationEnum[] result = {RelationEnum.UNRELATED};
				return result;
			}
			else if (r2 == RelationEnum.GREATER) {
				RelationEnum[] result = {RelationEnum.LESS, RelationEnum.UNRELATED};
				return result;
			}
			else if (r2 == RelationEnum.UNRELATED) {
				RelationEnum[] result = {RelationEnum.LESS, RelationEnum.EQUALS, RelationEnum.UNRELATED, RelationEnum.GREATER};
				return result;
			}
			else if (r2 == RelationEnum.EQUALS) {
				RelationEnum[] result = {RelationEnum.UNRELATED};
				return result;
			}
			else {
				throw new IllegalStateException();
			}
		}
		else if (this == RelationEnum.EQUALS) {
			if (r2 == RelationEnum.LESS) {
				RelationEnum[] result = {RelationEnum.LESS};
				return result;
			}
			else if (r2 == RelationEnum.GREATER) {
				RelationEnum[] result = {RelationEnum.GREATER};
				return result;
			}
			else if (r2 == RelationEnum.UNRELATED) {
				RelationEnum[] result = {RelationEnum.UNRELATED};
				return result;
			}
			else if (r2 == RelationEnum.EQUALS) {
				RelationEnum[] result = {RelationEnum.EQUALS};
				return result;
			}
			else {
				throw new IllegalStateException();
			}
		}
		else {
			throw new IllegalStateException();
		}
	}
}
