package pl.wroc.pwr.judy.operators.jumps;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Jump Instruction Replacement
 * <p/>
 * Replace jump instructions with IFLT (IF_ICMPLT).
 *
 * @author pmiwaszko
 */
public class JIR_Iflt extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace jump instructions with IFLT (IF_ICMPLT)";
	}

	@Override
	protected boolean checkJumpInsn(String className, final int opcode, final Label label) {
		return opcode == IF_ICMPEQ || opcode == IFEQ || opcode == IF_ICMPGE || opcode == IFGE || opcode == IF_ICMPGT
				|| opcode == IFGT || opcode == IF_ICMPLE || opcode == IFLE || opcode == IF_ICMPNE || opcode == IFNE; // !=0
	}

	@Override
	protected void mutateJumpInsn(MethodVisitor mv, String className, int opcode, final Label label) {
		switch (opcode) {
			case IF_ICMPEQ:
			case IF_ICMPGE:
			case IF_ICMPGT:
			case IF_ICMPLE:
			case IF_ICMPNE:
				mv.visitJumpInsn(IF_ICMPLT, label);
				break;
			case IFEQ:
			case IFGE:
			case IFGT:
			case IFLE:
			case IFNE:
				mv.visitJumpInsn(IFLT, label);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
