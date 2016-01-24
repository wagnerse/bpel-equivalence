package de.uni_stuttgart.iaas.bpel.equivalence.console;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BranchingType;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.complexity.AllenComplexity;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.complexity.AllenComplexity.Complexity;

public class ComplexityConsole {

	public static void main(String[] args) {
		if (args.length == 0) return;
		
		AllenComplexity allenComp = new AllenComplexity();
		
		try {
			// check complexity
			Complexity complexity = allenComp.isNP(createConstraints(args));

			// outputs
			System.out.println("Complexity: " + complexity.name());
			System.out.println("Conditions: " + allenComp.getConditionSet().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static List<BranchingType> createConstraints(String[] args) {
		List<BranchingType> typeList = new ArrayList<BranchingType>();
		
		for (String a: args) {
			if (a.equals("b")) {
				typeList.add(BranchingType.Before);
			}
			else if (a.equals("bi")) {
				typeList.add(BranchingType.After);
			}
			else if (a.equals("m")) {
				typeList.add(BranchingType.Meets);
			}
			else if (a.equals("mi")) {
				typeList.add(BranchingType.MetBy);
			}
			else if (a.equals("o")) {
				typeList.add(BranchingType.Overlaps);
			}
			else if (a.equals("oi")) {
				typeList.add(BranchingType.OverlappedBy);
			}
			else if (a.equals("d")) {
				typeList.add(BranchingType.During);
			}
			else if (a.equals("di")) {
				typeList.add(BranchingType.Contains);
			}
			else if (a.equals("s")) {
				typeList.add(BranchingType.Starts);
			}
			else if (a.equals("si")) {
				typeList.add(BranchingType.StartedBy);
			}
			else if (a.equals("f")) {
				typeList.add(BranchingType.Finishes);
			}
			else if (a.equals("fi")) {
				typeList.add(BranchingType.FinishedBy);
			}
			else if (a.equals("e")) {
				typeList.add(BranchingType.Equals);
			}
			else if (a.equals("pb")) {
				typeList.add(BranchingType.PartiallyBefore);
			}
			else if (a.equals("pbi")) {
				typeList.add(BranchingType.PartiallyAfter);
			}
			else if (a.equals("pm")) {
				typeList.add(BranchingType.PartiallyMeets);
			}
			else if (a.equals("pmi")) {
				typeList.add(BranchingType.PartiallyMetBy);
			}
			else if (a.equals("po")) {
				typeList.add(BranchingType.PartiallyOverlaps);
			}
			else if (a.equals("poi")) {
				typeList.add(BranchingType.PartiallyOverlappedBy);
			}
			else if (a.equals("ps")) {
				typeList.add(BranchingType.PartiallyStarts);
			}
			else if (a.equals("a")) {
				typeList.add(BranchingType.Adjacent);
			}
			else if (a.equals("ai")) {
				typeList.add(BranchingType.AdjacentBy);
			}
			else if (a.equals("t")) {
				typeList.add(BranchingType.Touches);
			}
			else if (a.equals("u")) {
				typeList.add(BranchingType.Unrelated);
			}
		}
		
		return typeList;
	}

}
