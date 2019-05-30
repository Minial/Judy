package pl.wroc.pwr.judy.operators.jumps;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Jump Instruction Replacement
 * <p/>
 * Replace jump instructions with IFGT (IF_ICMPGT).
 *
 * @author pmiwaszko
 */
public class JIR_Ifgt extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace jump instructions with IFGT (IF_ICMPGT)";
	}

	@Override
	protected boolean checkJumpInsn(String className, final int opcode, final Label label) {
		return opcode == IF_ICMPEQ || opcode == IFEQ || opcode == IF_ICMPGE || opcode == IFGE || opcode == IF_ICMPLE
				|| opcode == IFLE || opcode == IF_ICMPLT || opcode == IFLT || opcode == IF_ICMPNE || opcode == IFNE;
	}

	@Override
	protected void mutateJumpInsn(MethodVisitor mv, String className, int opcode, final Label label) {
		switch (opcode) {
			case IF_ICMPEQ:
			case IF_ICMPGE:
			case IF_ICMPLE:
			case IF_ICMPLT:
			case IF_ICMPNE:
				mv.visitJumpInsn(IF_ICMPGT, label);
				break;
			case IFEQ:
			case IFGE:
			case IFLE:
			case IFLT:
			case IFNE:
				mv.visitJumpInsn(IFGT, label);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
