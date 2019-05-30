package pl.wroc.pwr.judy.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.arithmetic.*;
import pl.wroc.pwr.judy.operators.inheritance.*;
import pl.wroc.pwr.judy.operators.javaspec.*;
import pl.wroc.pwr.judy.operators.javaspec.collections.*;
import pl.wroc.pwr.judy.operators.jumps.*;
import pl.wroc.pwr.judy.operators.logical.*;
import pl.wroc.pwr.judy.operators.mno.*;
import pl.wroc.pwr.judy.operators.polymorphism.*;
import pl.wroc.pwr.judy.operators.shift.SIR_LeftOperand;
import pl.wroc.pwr.judy.operators.shift.SIR_Shl;
import pl.wroc.pwr.judy.operators.shift.SIR_Shr;
import pl.wroc.pwr.judy.operators.shift.SIR_Ushr;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Factory of mutation operators.
 */
public class MutationOperatorsFactory implements IMutationOperatorsFactory {
	private static final Logger LOGGER = LogManager.getLogger(MutationOperatorsFactory.class);
	private final List<String> names;
	private static final long serialVersionUID = 5555L;

	/**
	 * <code>MutationOperatorsFactory</code> constructor.
	 */
	public MutationOperatorsFactory(List<String> operators) {
		names = new LinkedList<>();
		// add all operators
		addAllOperators();
		// remove operators not in property file
		if (operators != null && !operators.isEmpty()) {
			for (Iterator<String> iterator = names.iterator(); iterator.hasNext(); ) {
				String packageName = iterator.next();
				int dot = packageName.lastIndexOf('.');
				String simpleName = packageName.substring(dot == -1 ? 0 : dot + 1);
				if (!operators.contains(simpleName)) {
					iterator.remove(); // remove operators not in properties
				}
			}
		}
	}

	@Override
	public List<IMutationOperator> create(IEnvironment env) {
		List<IMutationOperator> operators = new LinkedList<>();
		operators.addAll(instantiateOperators(env));
		return Collections.unmodifiableList(operators);
	}

	@Override
	public List<IDescriptable> getDescriptions() {
		List<IDescriptable> descriptions = new LinkedList<>();
		for (String name : names) {
			AbstractMutationOperator operator = instantiateMutationOperator(name);
			descriptions.add(operator);
		}
		return descriptions;
	}

	private List<IMutationOperator> instantiateOperators(IEnvironment env) {
		List<IMutationOperator> operators = new LinkedList<>();
		for (String name : names) {
			AbstractMutationOperator operator = instantiateMutationOperator(name);
			operator.setEnvironment(env);
			operators.add(operator);
		}
		return operators;
	}

	private AbstractMutationOperator instantiateMutationOperator(String name) {
		AbstractMutationOperator operator = null;
		try {
			Class<?> c = Class.forName(name);
			operator = (AbstractMutationOperator) c.newInstance();
		} catch (ClassNotFoundException e) {
			LOGGER.warn("Could not find mutation operator " + name + ": " + e.getMessage());
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.warn("Unknown error during instatiation of the " + name + " mutation operator: " + e.getMessage());
		}
		return operator;
	}

	private void addAllOperators() {
		// arithmetic
		add(AIR_Add.class);
		add(AIR_Div.class);
		add(AIR_LeftOperand.class);
		add(AIR_Mul.class);
		add(AIR_Rem.class);
		add(AIR_RightOperand.class);
		add(AIR_Sub.class);

		// // jumps
		add(JIR_Ifeq.class);
		add(JIR_Ifge.class);
		add(JIR_Ifgt.class);
		add(JIR_Ifle.class);
		add(JIR_Iflt.class);
		add(JIR_Ifne.class);
		add(JIR_Ifnull.class);

		// // logical
		add(LIR_And.class);
		add(LIR_LeftOperand.class);
		add(LIR_Or.class);
		add(LIR_RightOperand.class);
		add(LIR_Xor.class);

		// // shift
		add(SIR_LeftOperand.class);
		// // add(SIR_RightOperand.class); // does not make much sense
		add(SIR_Shl.class);
		add(SIR_Shr.class);
		add(SIR_Ushr.class);

		// // encapsulation
		// // add(AMC.class); // in current implementation it is almost
		// impossible to kill a mutant
		//
		// // inheritance
		add(IOD.class);
		add(IOP.class);
		add(IOR.class);
		add(IPC.class);
		add(ISD.class);
		add(ISI.class);
		//
		// // polymorphism
		add(OAC.class);
		add(OMD.class);
		add(OMR.class);
		add(PLD.class);
		add(PNC.class);
		add(PPD.class);
		add(PRV.class);
		//
		// // java specific
		add(EAM.class);
		add(EMM.class);
		add(EOA.class);
		add(EOC.class);
		add(JDC.class);
		add(JID.class);

		add(JTD.class);
		add(JTI.class);

		// java collection specific
		add(CCE.class);
		add(REV.class);
		add(LSF.class);
		add(DUL.class);
		add(ORV.class);
		add(CST.class);

		// mno
		add(FBD.class);
		add(SCR.class);
		add(CCD.class);
		add(EGE.class);
		add(CSR.class);
		add(CLR.class);

		// other
		// add(RemoveCalls.class); // creates too many mutants
	}

	private void add(Class<? extends AbstractMutationOperator> operator) {
		String name = operator.getCanonicalName();
		if (name != null) {
			names.add(name);
		}
	}
}
