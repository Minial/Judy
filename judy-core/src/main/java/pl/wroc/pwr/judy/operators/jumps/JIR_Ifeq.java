package pl.wroc.pwr.judy.operators.jumps;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Jump Instruction Replacement
 * <p/>
 * Replace jump instructions with IFEQ (IF_ICMPEQ, IF_ACMPEQ).
 *
 * @author pmiwaszko
 */
public class JIR_Ifeq extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace jump instructions with IFEQ (IF_ICMPEQ, IF_ACMPEQ)";
	}

	@Override
	protected boolean checkJumpInsn(String className, final int opcode, final Label label) {
		return opcode == IF_ICMPGE || opcode == IFGE || opcode == IF_ICMPGT || opcode == IFGT || opcode == IF_ICMPLE
				|| opcode == IFLE || opcode == IF_ICMPLT || opcode == IFLT || opcode == IF_ICMPNE || opcode == IFNE
				|| opcode == IF_ACMPNE; // !=0
	}

	@Override
	protected void mutateJumpInsn(MethodVisitor mv, String className, int opcode, final Label label) {
		switch (opcode) {
			case IF_ICMPGE:
			case IF_ICMPGT:
			case IF_ICMPLE:
			case IF_ICMPLT:
			case IF_ICMPNE:
				mv.visitJumpInsn(IF_ICMPEQ, label);
				break;
			case IFGE:
			case IFGT:
			case IFLE:
			case IFLT:
			case IFNE:
				mv.visitJumpInsn(IFEQ, label);
				break;
			case IF_ACMPNE:
				mv.visitJumpInsn(IF_ACMPEQ, label);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
