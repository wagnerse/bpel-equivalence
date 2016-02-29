package de.uni_stuttgart.iaas.bpel.equivalence.model.networks;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Catch;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.AbstractStubActivityNetwork;
import de.uni_stuttgart.iaas.bpel.equivalence.model.IActivityConnector;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.BPELStateEnum;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.NetworkSolver;
import de.uni_stuttgart.iaas.bpel.equivalence.model.alleninterval.StateConstraint;
import de.uni_stuttgart.iaas.bpel.equivalence.utils.EMFUtils;

/**
 * Handles the allen interval network for a BPEL fault handler catch
 * 
 * @author Jonas Scheurich
 *
 */
public class FHCatchNetwork extends AbstractStubActivityNetwork {

	private Catch faultHandlerCatch;
	private Activity activity;

	public FHCatchNetwork(AbstractActivityNetwork parentNetwork, Catch subject, NetworkSolver network) {
		super(parentNetwork, network);
		this.faultHandlerCatch = subject;
		this.activity = faultHandlerCatch.getActivity();
	}

	@Override
	public EClass getSupportedEClass() {
		return BPELPackage.eINSTANCE.getCatch();
	}

	@Override
	public String getNetworkName() {
		Object attribute = EMFUtils.getAttributeByName(faultHandlerCatch, "name");
		return (attribute instanceof String) ? (String) attribute : "[FaultHandlerCatch]";
	}

	@Override
	public EObject getEObject() {
		return faultHandlerCatch;
	}

	@Override
	protected AbstractActivityNetwork[] createChildNetworks() {
		AbstractActivityNetwork[] childs = { createChildNetwork(activity) };
		return childs;
	}

	@Override
	protected void initConstraintMap() {
		/**
		 * Constraints to FAULT and FAULT_HANDLING are the same, they are used
		 * for basic activities or scopes etc.
		 */

		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.INITAL, Type.Equals);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.EXECUTING);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.DEAD, Type.Meets);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.FAULT, Type.Before); // equals to fault handling
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.FAULT_HANDLING, Type.Before); // equals to fault handling
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.TERMINATED, Type.Before);
		putConstraint(BPELStateEnum.INITAL, BPELStateEnum.COMPLETED, Type.Before);

		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.INITAL, Type.Finishes);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.EXECUTING, Type.Meets);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.DEAD, Type.Meets);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.FAULT, Type.Before); // equals to fault handling
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.FAULT_HANDLING, Type.Before); // equals to fault handling
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.TERMINATED, Type.Before, Type.Meets);
		putConstraint(BPELStateEnum.EXECUTING, BPELStateEnum.COMPLETED, Type.Before);

		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.INITAL, Type.MetBy);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.EXECUTING);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.DEAD, Type.Equals);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.FAULT); // equals to fault handling
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.FAULT_HANDLING); // equals to fault handling
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.TERMINATED);
		putConstraint(BPELStateEnum.DEAD, BPELStateEnum.COMPLETED);

		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.INITAL, Type.MetBy);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.EXECUTING, Type.Equals);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.FAULT, Type.Meets);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.FAULT_HANDLING, Type.MetBy);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.TERMINATED, Type.Meets);
		putConstraint(BPELStateEnum.FAULT_HANDLING, BPELStateEnum.COMPLETED, Type.Meets);

		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.INITAL, Type.After);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.EXECUTING, Type.MetBy);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.FAULT, Type.Equals); // TODO check this
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.FAULT_HANDLING); // TODO check this
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.TERMINATED, Type.Finishes);
		putConstraint(BPELStateEnum.FAULT_UNCOUGHT, BPELStateEnum.COMPLETED);

		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.INITAL, Type.After);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.EXECUTING, Type.MetBy);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.FAULT);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.FAULT_HANDLING, Type.After); // TODO check this
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.TERMINATED);
		putConstraint(BPELStateEnum.FAULT_COUGHT, BPELStateEnum.COMPLETED, Type.Equals);

		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.INITAL, Type.MetBy);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.EXECUTING, Type.MetBy);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.DEAD);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.FAULT); // equals to fault handling
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.FAULT_HANDLING); // equals to fault handling
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.TERMINATED, Type.Equals, Type.FinishedBy);
		putConstraint(BPELStateEnum.TERMINATED, BPELStateEnum.COMPLETED);

		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.INITAL, Type.Finishes);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.EXECUTING);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.DEAD, Type.Equals);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.FAULT); // equals to fault handling
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.FAULT_HANDLING); // equals to fault handling
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.TERMINATED);
		putConstraint(BPELStateEnum.COMPLETED, BPELStateEnum.COMPLETED);
	}
}
