package de.uni_stuttgart.iaas.bpel.equivalence.console;

import org.apache.commons.lang.builder.HashCodeBuilder;

import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.pointalgebra.Variable;

public class TestMappingKeyConsole {

	public static void main(String[] args) {
		
		Variable v1a = new Variable(null , new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
		Variable v1b = new Variable(null , new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START));
		
		Variable v2a = new Variable(null , new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
		Variable v2b = new Variable(null , new TimePointDesc(BPELStateEnum.EXECUTING, TimeTypeEnum.START));
		
		int keyv1a = v1a.hashCode();
		int keyv1b = v1b.hashCode();
		int keyv2a = v2a.hashCode();
		int keyv2b = v2b.hashCode();
		
		System.out.println(keyv1a + " " + keyv1a);
	}

}
