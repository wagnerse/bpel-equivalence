package de.uni_stuttgart.iaas.bpel.equivalence.PA2IA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra.BranchingType;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra.IAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra.IANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.intervalalgebra.IAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;

/**
 * A transformation of point algebra networks to intervall algebra networks
 * 
 * @author Jonas Scheurich
 *
 */
public class PA2IA {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private PANetwork pointAlgebra;
	private List<IAMatch> matchDefinitions = new ArrayList<IAMatch>();
	private Map<IAVariable, Pair<PAVariable, PAVariable>> trace = new HashMap<IAVariable, Pair<PAVariable, PAVariable>>();

	public PA2IA(PANetwork pointAlgebra) {
		this.pointAlgebra = pointAlgebra;
		initMatchDefLinear();
		initMatchDefBranching();
	}
	
	/**
	 * Get the transformation trace, that describes the mapping of 
	 * a start end end time point to an interval.
	 * 
	 * @return
	 */
	public Map<IAVariable, Pair<PAVariable, PAVariable>> getTrace() {
		return trace;
	}
	
	public List<IAMatch> getMatchDefinitionsCopy() {
		List<IAMatch> matchDefinitionsCopy = new ArrayList<IAMatch>();
		for (IAMatch m: this.matchDefinitions) matchDefinitionsCopy.add(m);
		return matchDefinitionsCopy;
	}

	/**
	 * Transform the point algebra network to an interval algebra network.
	 * 
	 * @return
	 */
	public IANetwork transfrom() {
		IANetwork intervalAlgebra = new IANetwork();
		Collection<Pair<PAVariable, PAVariable>> variablePairs = pointAlgebra.getVariablePairs();

		// create interval algebra variables
		for (Pair<PAVariable, PAVariable> pair : variablePairs) {
			IAVariable interval = intervalAlgebra.createVariable(pair.getLeft().getBpelElement(),
					pair.getLeft().getTimePoint().getState());
			trace.put(interval, pair);
		}

		// create constraints
		for (CSPVariable inter1 : intervalAlgebra.getVariables()) {
			Pair<PAVariable, PAVariable> pair1 = trace.get(inter1);

			for (CSPVariable inter2 : intervalAlgebra.getVariables()) {
				Pair<PAVariable, PAVariable> pair2 = trace.get(inter2);

				// transform relations
				List<BranchingType> relations = getIntervalRelation(pair1.getLeft(), pair1.getRight(), pair2.getLeft(),
						pair2.getRight());

				// create constraint
				IAConstraint constraint = new IAConstraint(relations);
				constraint.setFrom(inter1);
				constraint.setTo(inter2);
				
				if (!intervalAlgebra.containsToWayConstraint(inter1, inter2)){
					intervalAlgebra.addConstraint(constraint);
				}
				else {
					// create logging
					IAConstraint exist = (IAConstraint) intervalAlgebra.getConstraint(inter1, inter2);
					IAConstraint existRev = (IAConstraint) intervalAlgebra.getConstraint(inter2, inter1);
					if (exist != null && existRev != null) {
						LOGGER.log(Level.WARNING, "PA2IA: Two interval constraints: " + exist.getName() + ", " + existRev.getName());
					}
					else if (exist != null && !constraint.equals(exist)) {
						LOGGER.log(Level.WARNING, "PA2IA: One unequal constraint exists.");
					}
					else if (existRev != null && !constraint.equals(existRev)) {
						LOGGER.log(Level.WARNING, "PA2IA: One unequal reverse constraint exists.");
					}
					else {
						// other constraint is equals: No logging.
					}
				}
			}
		}

		return intervalAlgebra;
	}

	/**
	 * Get the interval algebra relations between four time points.
	 * 
	 * @param leftStart
	 * @param leftEnd
	 * @param rightStart
	 * @param rightEnd
	 * @return
	 */
	public List<BranchingType> getIntervalRelation(PAVariable leftStart, PAVariable leftEnd, PAVariable rightStart,
			PAVariable rightEnd) {
		List<BranchingType> iaRelations = new ArrayList<BranchingType>();

		// Check if every registered matching definition matches.
		for (IAMatch mDef : matchDefinitions) {
			BranchingType iaRel = mDef.match(
					(PAConstraint) this.pointAlgebra.getTwoWayConstraint(leftStart, rightEnd),
					(PAConstraint) this.pointAlgebra.getTwoWayConstraint(rightStart, leftEnd),
					(PAConstraint) this.pointAlgebra.getTwoWayConstraint(leftStart, rightStart),
					(PAConstraint) this.pointAlgebra.getTwoWayConstraint(leftEnd, rightEnd));

			if (iaRel != null && !iaRelations.contains(iaRel)) {
				iaRelations.add(iaRel);
			}
		}

		// log if no interval constraint matches.
		if (iaRelations.size() == 0) {
			LOGGER.log(Level.WARNING, "PA2IA: No constraints for " + leftStart.getBpelElement().toString()
					+ rightStart.getBpelElement().toString());
		}

		return iaRelations;
	}

	/**
	 * Init the matching definitions for linear intervall algebra relations (see
	 * Ragni - Branching Allen Reasoning with Intervals in Branching Time)
	 */
	private void initMatchDefLinear() {
		// before, after
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.GREATER, RelationEnum.LESS,
				RelationEnum.LESS, BranchingType.Before));
		this.matchDefinitions.add(new IAMatch(RelationEnum.GREATER, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.GREATER, BranchingType.After));

		// meet, meet by
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.EQUALS, RelationEnum.LESS,
				RelationEnum.LESS, BranchingType.Meets));
		this.matchDefinitions.add(new IAMatch(RelationEnum.EQUALS, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.GREATER, BranchingType.MetBy));

		// overlaps, overlapped by
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.LESS,
				RelationEnum.LESS, BranchingType.Overlaps));
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.GREATER, BranchingType.OverlappedBy));

		// during, contains
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.LESS, BranchingType.During));
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.GREATER, BranchingType.Contains));

		// starts, started by
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.EQUALS,
				RelationEnum.LESS, BranchingType.Starts));
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.EQUALS,
				RelationEnum.GREATER, BranchingType.StartedBy));

		// finishes, finished by
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.EQUALS, BranchingType.Finishes));
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.LESS,
				RelationEnum.EQUALS, BranchingType.FinishedBy));

		// equals
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.EQUALS,
				RelationEnum.EQUALS, BranchingType.Equals));

	}

	/**
	 * Init the matching definitions for non-linear intervall algebra relations
	 * (see Ragni - Branching Allen Reasoning with Intervals in Branching Time)
	 */
	private void initMatchDefBranching() {
		// partially before, partially after
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.UNRELATED, RelationEnum.LESS,
				RelationEnum.UNRELATED, BranchingType.PartiallyBefore));
		this.matchDefinitions.add(new IAMatch(RelationEnum.UNRELATED, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.UNRELATED, BranchingType.PartiallyAfter));

		// partially meets, partially met by
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.LESS,
				RelationEnum.UNRELATED, BranchingType.PartiallyMeets));
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.UNRELATED, BranchingType.PartiallyMetBy));

		// partially starts
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.EQUALS,
				RelationEnum.UNRELATED, BranchingType.PartiallyStarts));

		// partially overlaps, partially overlapped by
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.LESS,
				RelationEnum.UNRELATED, BranchingType.PartiallyOverlaps));
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.UNRELATED, BranchingType.PartiallyOverlappedBy));

		// adjacent, adjacent by
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.UNRELATED, RelationEnum.LESS,
				RelationEnum.UNRELATED, BranchingType.Adjacent));
		this.matchDefinitions.add(new IAMatch(RelationEnum.UNRELATED, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.UNRELATED, BranchingType.AdjacentBy));

		// touches
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.EQUALS,
				RelationEnum.UNRELATED, BranchingType.Touches));

		// unrelated
		this.matchDefinitions.add(new IAMatch(RelationEnum.UNRELATED, RelationEnum.UNRELATED, RelationEnum.UNRELATED,
				RelationEnum.UNRELATED, BranchingType.Unrelated));

		// initially before, initially after
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.UNRELATED, RelationEnum.LESS,
				RelationEnum.UNRELATED, BranchingType.InitiallyBefore));
		this.matchDefinitions.add(new IAMatch(RelationEnum.UNRELATED, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.UNRELATED, BranchingType.InitiallyAfter));

		// initially meet, initially met by
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.LESS,
				RelationEnum.UNRELATED, BranchingType.InitiallyMeets));
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.GREATER,
				RelationEnum.UNRELATED, BranchingType.InitiallyMetBy));

		// initially equals
		this.matchDefinitions.add(new IAMatch(RelationEnum.LESS, RelationEnum.LESS, RelationEnum.EQUALS,
				RelationEnum.UNRELATED, BranchingType.InitiallyEquals));

	}
}
