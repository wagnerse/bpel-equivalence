package de.uni_stuttgart.iaas.bpel.equivalence.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.bpel4chor.mergechoreography.ChoreographyPackage;
import org.bpel4chor.model.pbd.impl.ParticipantBehaviorDescription;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELExtensibleElement;
import org.eclipse.bpel.model.OpaqueActivity;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.impl.ProcessImpl;
import org.eclipse.emf.ecore.EObject;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.CSPVariable;
import de.uni_stuttgart.iaas.bpel.model.utilities.ExtendedActivityIterator;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Provide functionalities for {@link CSPNetwork} objects.
 * 
 * @author Jonas Scheurich
 */
public class NetworkUtils {

	/**
	 * Write a {@link CSPNetwork} into a csv file.
	 * 
	 * @param network
	 * @param file
	 * @throws IOException
	 */
	public static void saveAsCSV(CSPNetwork network, File file) throws IOException {

		FileWriter writer = new FileWriter(file);

		// write file options
		writer.append("sep=,\n");

		// write head line with activity name;
		writer.append(",");
		String actName = "";
		for (CSPVariable v : network.getVariables()) {
			if (!actName.equals(v.getBpelName())) {
				writer.append(actName + ", ");
				actName = v.getBpelName();
			}
				
		}
		
		writer.append("\n");
		
		// Write line with activity states
		for (CSPVariable v : network.getVariables()) {
			writer.append(v.getStateName() + ", ");
			actName = v.getBpelName();
			
				
		}
		
		writer.append("\n");

		// write data
		for (CSPVariable vl : network.getVariables()) {
			// write state name
			
			writer.append(vl.getName() + ", ");
			// write data
			for (CSPVariable vr : network.getVariables()) {
				// constraints 
				CSPConstraint constraint = network.getConstraint(vl, vr);
				if (constraint == null){
					CSPConstraint constraintToRev = network.getConstraint(vr, vl);
					if (constraintToRev != null) {
						constraint = constraintToRev.revert();
					}
				}
				if (constraint != null) {
					writer.append(constraint.valueToString());
				}
				writer.append(", ");
			} 
			
			// end of line
			writer.append("\n");
		}

		// finish
		writer.flush();
		writer.close();
	}
	
	
	public static void saveAsExcel(CSPNetwork network, File file, boolean tasksOnly) throws Exception {
		WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);
        
        String lastActName = "";
        int offset = 2;
        int firstActColumn = offset;
        int firstActRow= offset;
        int effectiveColumn = offset;
    	int effectiveRow = offset;
        
        WritableCellFormat headerCellFormat = new WritableCellFormat();
        headerCellFormat.setAlignment(Alignment.CENTRE);
        headerCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        CSPVariable[] variables = new CSPVariable[network.getVariables().size()];
        variables = network.getVariables().toArray(variables);
        // Create Header Cells: first row/column activity name and second row/column state name
        for (int i = 0; i < variables.length; i++) {
        	String currentActName = variables[i].getBpelName();
        	Object bpelElement = variables[i].getBpelElement();
        	if (bpelElement instanceof OpaqueActivity || !tasksOnly) {
        		
            	if (!currentActName.equals(lastActName)) {
            		Label actNameRowCell = new Label(effectiveColumn, 0, currentActName);
            		Label actNameColumnCell = new Label(0, effectiveRow, currentActName);
            		actNameRowCell.setCellFormat(headerCellFormat);
            		actNameColumnCell.setCellFormat(headerCellFormat);
            		sheet.addCell(actNameRowCell); 
            		sheet.addCell(actNameColumnCell);
            	}
    			Label stateNameRowCell = new Label(effectiveColumn, 1, variables[i].getStateName());
    			Label stateNameColumnCell = new Label(1, effectiveRow, variables[i].getStateName());
    			sheet.addCell(stateNameRowCell);
    			sheet.addCell(stateNameColumnCell);
    			
    			if (lastActName.length() != 0 && !currentActName.equals(lastActName)) {
    				sheet.mergeCells(firstActColumn, 0, effectiveColumn - 1, 0);
    				sheet.mergeCells(0, firstActRow, 0, effectiveRow - 1);
    				firstActColumn = effectiveColumn;
    				firstActRow = effectiveRow;
    			} else if (i == variables.length - 1) {
    				sheet.mergeCells(firstActColumn, 0, effectiveColumn, 0);
    				sheet.mergeCells(0, firstActRow, 0, effectiveRow);
    			}
    			
    			lastActName = currentActName; 
    			effectiveColumn++;
            	effectiveRow++;
			}
        	   	
		}
        
        // Write states      
    	effectiveRow = offset;
        // write data
 		for (CSPVariable vl : network.getVariables()) {
 			effectiveColumn = offset;
 			
 			if (vl.getBpelElement() instanceof OpaqueActivity || !tasksOnly) {
	 			// write data
	 			for (CSPVariable vr : network.getVariables()) {
	 				
	 	        	if (vr.getBpelElement() instanceof OpaqueActivity || !tasksOnly) {
	 	 				// constraints 
	 	 				CSPConstraint constraint = network.getConstraint(vl, vr);
	 	 				if (constraint == null){
	 	 					CSPConstraint constraintToRev = network.getConstraint(vr, vl);
	 	 					if (constraintToRev != null) {
	 	 						constraint = constraintToRev.revert();
	 	 					}
	 	 				}
	 	 				if (constraint != null) {
	 	 					Label stateCell = new Label(effectiveColumn, effectiveRow, constraint.valueToString());
	 	 					sheet.addCell(stateCell);
	 	 				}
	 	 				// Next column
		 				effectiveColumn++;
	 	        	}
	 			}
	 			// Next row
	 			effectiveRow++;
 			}


 		}
        workbook.write();
        workbook.close();
        	
	}
	
	public static AbstractActivityNetwork findNetwork(Collection<AbstractActivityNetwork> networks, Object subject) {
		for (AbstractActivityNetwork network : networks) {
			Object networkObj = network.getObject();
			System.out.println(networkObj);
			if (subject.equals(network.getObject())) {
				return network;
			} else {
				AbstractActivityNetwork foundNetwork = findNetwork(network.getChildNetworks(), subject);
				if (foundNetwork != null) {
					return foundNetwork;
				}
			}
		}
		
		return null;
		
	}
	
	
	
	
}
