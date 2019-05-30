package pl.wroc.pwr.judy.operators.logical;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Logical Instruction Replacement.
 * <p/>
 * Replace binary logical instructions with their left operands.
 *
 * @author pmiwaszko
 */
public class LIR_LeftOperand extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace binary logical instructions with their left operands";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IAND || opcode == IOR || opcode == IXOR || opcode == LAND || opcode == LOR || opcode == LXOR;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IAND:
			case IOR:
			case IXOR:
				mv.visitInsn(POP);
				break;
			case LAND:
			case LOR:
			case LXOR:
				mv.visitInsn(POP2);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
