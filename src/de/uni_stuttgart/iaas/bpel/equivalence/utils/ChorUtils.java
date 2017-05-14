package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import org.bpel4chor.mergechoreography.ChoreographyPackage;
import org.bpel4chor.mergechoreography.util.ChoreoMergeUtil;
import org.bpel4chor.model.pbd.impl.ParticipantBehaviorDescription;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Process;

import de.uni_stuttgart.iaas.bpel.model.utilities.ExtendedActivityIterator;

public class ChorUtils {
	
	public static ChoreographyPackage readChoreographyFromFile(String path) {
		ChoreographyPackage chor = new ChoreographyPackage(path);
		chor.initMergedProcess();
		 
		return chor; 
	}
	
	public static Activity findActivityInChor(String wsuid, ChoreographyPackage chor) {
		
		for (Process process : chor.getPbds()) {
			ParticipantBehaviorDescription pbd = new ParticipantBehaviorDescription(process);
			ExtendedActivityIterator actIter = new ExtendedActivityIterator(process);
			while (actIter.hasNext()) {
				Activity activitiy = actIter.next();
				if (wsuid.equals(pbd.getId(activitiy))) {
					return activitiy;
				}
			}	
		}
		
		return null;
	}
	
	

}
