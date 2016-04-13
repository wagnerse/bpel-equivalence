package de.uni_stuttgart.iaas.bpel.equivalence.examples;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.uni_stuttgart.iaas.bpel.equivalence.PA2IA.IAMatch;
import de.uni_stuttgart.iaas.bpel.equivalence.PA2IA.PA2IA;

public class CheckIAMatch {
	public static void main(String[] args) {
		PA2IA trans = new PA2IA(null);
		List<Pair<IAMatch, IAMatch>> equalMatchDefs = new ArrayList<Pair<IAMatch, IAMatch>>();
		
		System.out.println("Equal PA2IA match definitions:\n");
		for (IAMatch m1: trans.getMatchDefinitionsCopy()) {
			for (IAMatch m2: trans.getMatchDefinitionsCopy()) {
				if (!m1.equals(m2) && m1.equalsRelations(m2)) {
					Pair<IAMatch, IAMatch> pair1 = new MutablePair<IAMatch, IAMatch>(m1, m2);
					Pair<IAMatch, IAMatch> pair2 = new MutablePair<IAMatch, IAMatch>(m2, m1);
					if (!equalMatchDefs.contains(pair1) && !equalMatchDefs.contains(pair2)) {
						equalMatchDefs.add(pair1);
					}
				}
			}
		}
		
		for (Pair<IAMatch, IAMatch> p: equalMatchDefs) {
			System.out.println(p.getLeft().getIntervalRelation() + "  =  " + p.getRight().getIntervalRelation());
		}
	}
}
