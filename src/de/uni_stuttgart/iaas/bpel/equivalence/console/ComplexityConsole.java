package de.uni_stuttgart.iaas.bpel.equivalence.console;

import java.util.ArrayList;
import java.util.List;

import org.metacsp.framework.Variable;

import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.complexity.AllenComplexity;
import de.uni_stuttgart.iaas.bpel.equivalence.model.allenintervall.complexity.AllenComplexity.Complexity;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

public class ComplexityConsole {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if (args.length == 0) return;
		
		AllenComplexity allenComp = new AllenComplexity();
		
		// create network
		NetworkSolver network = new NetworkSolver();
		Variable i = network.createVariable();
		Variable j = network.createVariable();
		
		Type[] constraints = createConstraints(args);
		StateConstraint constraint = new StateConstraint(constraints);
		
		// check complexity
		Complexity complexity = allenComp.isNP(constraint);
	
		// outputs
		System.out.println("Complexity: " + complexity.name());
		System.out.println("Conditions: " + allenComp.getConditionSet().toString());

	}
	
	private static Type[] createConstraints(String[] args) {
		List<Type> typeList = new ArrayList<Type>();
		
		for (String a: args) {
			if (a.equals("b")) {
				typeList.add(Type.Before);
			}
			else if (a.equals("bi")) {
				typeList.add(Type.After);
			}
			else if (a.equals("m")) {
				typeList.add(Type.Meets);
			}
			else if (a.equals("mi")) {
				typeList.add(Type.MetBy);
			}
			else if (a.equals("o")) {
				typeList.add(Type.Overlaps);
			}
			else if (a.equals("oi")) {
				typeList.add(Type.OverlappedBy);
			}
			else if (a.equals("d")) {
				typeList.add(Type.During);
			}
			else if (a.equals("di")) {
				typeList.add(Type.Contains);
			}
			else if (a.equals("s")) {
				typeList.add(Type.Starts);
			}
			else if (a.equals("si")) {
				typeList.add(Type.StartedBy);
			}
			else if (a.equals("f")) {
				typeList.add(Type.Finishes);
			}
			else if (a.equals("fi")) {
				typeList.add(Type.FinishedBy);
			}
			else if (a.equals("e")) {
				typeList.add(Type.Equals);
			}
		}
		
		return typeList.toArray(new Type[typeList.size()]);
	}

}
