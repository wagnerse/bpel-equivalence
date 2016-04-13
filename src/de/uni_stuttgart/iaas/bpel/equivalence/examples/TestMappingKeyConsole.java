package de.uni_stuttgart.iaas.bpel.equivalence.examples;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PAVariable;

public class TestMappingKeyConsole {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		PAVariable v1a = new PAVariable(0, null, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
		PAVariable v1b = new PAVariable(0, null, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
		
		PAVariable v2a = new PAVariable(0, null, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
		PAVariable v2b = new PAVariable(0, null, new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
		
		int keyv1a = v1a.hashCode();
		int keyv1b = v1b.hashCode();
		int keyv2a = v2a.hashCode();
		int keyv2b = v2b.hashCode();
		
		System.out.println(keyv1a + " " + keyv1a);
	}

}
