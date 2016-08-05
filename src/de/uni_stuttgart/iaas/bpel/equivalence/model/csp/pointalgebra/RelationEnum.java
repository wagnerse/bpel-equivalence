package de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra;

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
	LESS("<"),		// x < y iff x precedes y
	GREATER(">"), 	// x > y iff y precedes x
	EQUALS("="), 	// x = Y iff x, y are at the same point
	UNRELATED("||");	// x || y iff x, y belong to different branches ort points	
	
	private String value;
	
	private RelationEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
    
    public RelationEnum revert() {
    	if (this == RelationEnum.LESS) {
			return RelationEnum.GREATER;
		}
		else if (this == RelationEnum.GREATER) {
			return RelationEnum.LESS;
		}
		else {
			return this;
		}
    }
	
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
				throw new IllegalStateException("Unkown relation " + r2);
			}
		}
		else if (this == RelationEnum.GREATER) {
			if (r2 == RelationEnum.LESS) {
				RelationEnum[] result = {RelationEnum.LESS, RelationEnum.EQUALS, RelationEnum.UNRELATED, RelationEnum.GREATER};
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
				RelationEnum[] result = {RelationEnum.GREATER};
				return result;
			}
			else {
				throw new IllegalStateException("Unkown relation " + r2);
			}
		}
		else if (this == RelationEnum.UNRELATED) {
			if (r2 == RelationEnum.LESS) {
				RelationEnum[] result = {RelationEnum.UNRELATED};
				return result;
			}
			else if (r2 == RelationEnum.GREATER) {
				RelationEnum[] result = {RelationEnum.GREATER, RelationEnum.UNRELATED};
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
				throw new IllegalStateException("Unkown relation " + r2);
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
				throw new IllegalStateException("Unkown relation " + r2);
			}
		}
		else {
			throw new IllegalStateException("Unkown relation " + this);
		}
	}
}
