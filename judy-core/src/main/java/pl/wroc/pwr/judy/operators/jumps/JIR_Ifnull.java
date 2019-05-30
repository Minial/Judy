package pl.wroc.pwr.judy.operators.jumps;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Jump Instruction Replacement
 * <p/>
 * Replace jump instruction IFNULL with IFNONNULL and vice-versa.
 *
 * @author pmiwaszko
 */
public class JIR_Ifnull extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace jump instruction IFNULL with IFNONNULL and vice-versa";
	}

	@Override
	protected boolean checkJumpInsn(String className, final int opcode, final Label label) {
		return opcode == IFNULL || opcode == IFNONNULL;
	}

	@Override
	protected void mutateJumpInsn(MethodVisitor mv, String className, int opcode, final Label label) {
		switch (opcode) {
			case IFNULL:
				mv.visitJumpInsn(IFNONNULL, label);
				break;
			case IFNONNULL:
				mv.visitJumpInsn(IFNULL, label);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
