package pl.wroc.pwr.judy.operators.logical;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Logical Instruction Replacement
 * <p/>
 * Replace binary logical instructions with OR (|).
 *
 * @author pmiwaszko
 */
public class LIR_Or extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace binary logical instructions with OR";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IAND || opcode == IXOR || opcode == LAND || opcode == LXOR;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IAND:
			case IXOR:
				mv.visitInsn(IOR);
				break;
			case LAND:
			case LXOR:
				mv.visitInsn(LOR);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
