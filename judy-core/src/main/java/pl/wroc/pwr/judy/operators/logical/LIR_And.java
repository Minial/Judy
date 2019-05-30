package pl.wroc.pwr.judy.operators.logical;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Logical Instruction Replacement
 * <p/>
 * Replace binary logical instructions with AND (&).
 *
 * @author pmiwaszko
 */
public class LIR_And extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace binary logical instructions with AND";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IOR || opcode == IXOR || opcode == LOR || opcode == LXOR;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IOR:
			case IXOR:
				mv.visitInsn(IAND);
				break;
			case LOR:
			case LXOR:
				mv.visitInsn(LAND);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
