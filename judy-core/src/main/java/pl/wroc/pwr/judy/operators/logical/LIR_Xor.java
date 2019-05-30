package pl.wroc.pwr.judy.operators.logical;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Logical Instruction Replacement
 * <p/>
 * Replace binary logical instructions with XOR (^).
 *
 * @author pmiwaszko
 */
public class LIR_Xor extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace binary logical instructions with XOR";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IAND || opcode == IOR || opcode == LAND || opcode == LOR;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IAND:
			case IOR:
				mv.visitInsn(IXOR);
				break;
			case LAND:
			case LOR:
				mv.visitInsn(LXOR);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
