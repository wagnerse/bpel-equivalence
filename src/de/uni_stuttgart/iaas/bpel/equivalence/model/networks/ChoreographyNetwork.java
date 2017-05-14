package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import java.util.HashMap;
import java.util.Map;

import org.bpel4chor.mergechoreography.ChoreographyPackage;
import org.bpel4chor.mergechoreography.util.ChoreoMergeUtil;
import org.bpel4chor.mergechoreography.util.MLEnvironment;
import org.bpel4chor.mergechoreography.util.MLEnvironmentAnalyzer;
import org.bpel4chor.mergechoreography.util.MergePreProcessorForEH;
import org.bpel4chor.model.topology.impl.MessageLink;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Pick;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Receive;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractDefaultActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc;
import de.uni_stuttgart.iaas.bpel.equivalence.model.TimePointDesc.TimeTypeEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.PANetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.csp.pointalgebra.RelationEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.ChorUtils;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.NetworkUtils;

public class ChoreographyNetwork extends AbstractDefaultActivityNetwork {
	
	private ChoreographyPackage choreography;
	
	public ChoreographyNetwork(AbstractActivityNetwork parentNetwork, ChoreographyPackage subject, PANetwork network) {
		super(parentNetwork, network);
		this.choreography = subject;

	}

	@Override
	public Class<?> getSupportedClass() {
		return ChoreographyPackage.class;
	}

	@Override
	public Object getObject() {
		return this.choreography;
	}
	
	@Override
	public PANetwork linkActivityNetworkLayer() {
		PANetwork network = super.linkActivityNetworkLayer();
		initConstraints();
		
		return network;
	}

	@Override
	protected Map<Object, AbstractActivityNetwork> createChildNetworks() {
		Map<Object, AbstractActivityNetwork> childMap = new HashMap<Object, AbstractActivityNetwork>();

		// add process models
		for (Process process : choreography.getPbds()) {
			AbstractActivityNetwork processNetwork = createChildNetwork(process);
			if (processNetwork != null)
				childMap.put(process, processNetwork);
		}
		return childMap;
	}
	
	@Override
	protected void initConstraintMap() {
		
	}
 
	protected void initConstraints() {
		
		constrainInitStatesOfActs();
		
		// Add constraints between send and receive activities connected via message links
		for (MessageLink msgLink : this.choreography.getTopology().getMessageLinks()) {
			// Get send, receive activities and their direct predecessors and successor 
			Activity sendAct = 
					ChorUtils.findActivityInChor(msgLink.getSendActivity(), this.choreography);
			Activity rcvAct = 
					ChorUtils.findActivityInChor(msgLink.getReceiveActivity(), this.choreography);
			
			if (sendAct instanceof Invoke && rcvAct instanceof Receive) {
				initInvokeReceiveConstraintMap((Invoke) sendAct, (Receive) rcvAct) ;
			} else if (sendAct instanceof Invoke && rcvAct instanceof Pick) {
				initInvokePickConstraintMap((Invoke) sendAct, (Pick) rcvAct);
			}
		}	
	}
	
	/**
	 * Create constraint between root activities of all processes of the 
	 * choreography specifying that all activities go at the same time into the init state.
	 */
	private void constrainInitStatesOfActs() {
		
		// A choreography contains at least on process that has exactly one child activity.
		Process srcProcess = this.choreography.getPbds().get(0);
		AbstractActivityNetwork srcProcessNetwork = NetworkUtils.findNetwork(getChildNetworks(), srcProcess);
		AbstractActivityNetwork srcRootActNetwork = srcProcessNetwork.getChildNetworks().iterator().next();
		
		
		for (Process trgProcess : this.choreography.getPbds()) {
			// Get network of root activity of process
			AbstractActivityNetwork trgProcessNetwork = NetworkUtils.findNetwork(getChildNetworks(), trgProcess);
			AbstractActivityNetwork trgRootActNetwork = trgProcessNetwork.getChildNetworks().iterator().next();
			
			if (!srcProcess.equals(trgProcess)) {	
				this.putConstraint(srcRootActNetwork, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START),
						trgRootActNetwork, new TimePointDesc(BPELStateEnum.INITAL, TimeTypeEnum.START),
						RelationEnum.EQUALS);
				this.createConstraintsBetween(srcRootActNetwork, trgRootActNetwork);
			}

		}
		
	}
	
	private void initInvokeReceiveConstraintMap(Invoke invoke, Receive receive) {
		AbstractActivityNetwork srcNetwork = NetworkUtils.findNetwork(getChildNetworks(), invoke);
		AbstractActivityNetwork trgNetwork = NetworkUtils.findNetwork(getChildNetworks(), receive);

		if (srcNetwork != null && trgNetwork != null) {
			
			// Receive completes when Invoke has entered completed state 
			this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START),
					trgNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START),
					RelationEnum.LESS, RelationEnum.EQUALS, RelationEnum.UNRELATED);
			
			// if invoke does not transition into completed, the receive does it neither
			this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.DEAD, TimeTypeEnum.START),
					trgNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START),
					RelationEnum.UNRELATED);
			this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.ABORTED, TimeTypeEnum.START),
					trgNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START),
					RelationEnum.UNRELATED);
			this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.FAULT, TimeTypeEnum.START),
					trgNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START),
					RelationEnum.UNRELATED);
			this.putConstraint(srcNetwork, new TimePointDesc(BPELStateEnum.TERMINATED, TimeTypeEnum.START),
					trgNetwork, new TimePointDesc(BPELStateEnum.COMPLETED, TimeTypeEnum.START),
					RelationEnum.UNRELATED); 
			
			this.createConstraintsBetween(srcNetwork, trgNetwork);
		} else {
			System.out.println("Source or target network for Invoke and Receive interaction not found");
		}
	}
	
	private void initInvokePickConstraintMap(Invoke invoke, Pick pick) {
		
	}

	@Override
	public String getNetworkName() {
		return "[Choreography]";
	}

}
