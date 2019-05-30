package pl.wroc.pwr.judy.operators.jumps;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Jump Instruction Replacement
 * <p/>
 * Replace jump instructions with IFNE (IF_ICMPNE, IF_ACMPNE).
 *
 * @author pmiwaszko
 */
public class JIR_Ifne extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace jump instructions with IFNE (IF_ICMPNE, IF_ACMPNE)";
	}

	@Override
	protected boolean checkJumpInsn(String className, final int opcode, final Label label) {
		return opcode == IF_ICMPEQ || opcode == IFEQ || opcode == IF_ACMPEQ || opcode == IF_ICMPGE || opcode == IFGE
				|| opcode == IF_ICMPGT || opcode == IFGT || opcode == IF_ICMPLE || opcode == IFLE
				|| opcode == IF_ICMPLT || opcode == IFLT; // < 0
	}

	@Override
	protected void mutateJumpInsn(MethodVisitor mv, String className, int opcode, final Label label) {
		switch (opcode) {
			case IF_ICMPEQ:
			case IF_ICMPGE:
			case IF_ICMPGT:
			case IF_ICMPLE:
			case IF_ICMPLT:
				mv.visitJumpInsn(IF_ICMPNE, label);
				break;
			case IFEQ:
			case IFGE:
			case IFGT:
			case IFLE:
			case IFLT:
				mv.visitJumpInsn(IFNE, label);
				break;
			case IF_ACMPEQ:
				mv.visitJumpInsn(IF_ACMPNE, label);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
