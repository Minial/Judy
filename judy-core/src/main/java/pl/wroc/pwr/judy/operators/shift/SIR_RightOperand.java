package pl.wroc.pwr.judy.operators.shift;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Shift Instructions Replacement.
 * <p/>
 * Replace shift instructions with their right operands.
 *
 * @author pmiwaszko
 */
public class SIR_RightOperand extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace shift instructions with their right operands";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == ISHL || opcode == ISHR || opcode == IUSHR || opcode == LSHL || opcode == LSHR
				|| opcode == LUSHR;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case ISHL:
			case ISHR:
			case IUSHR:
				mv.visitInsn(SWAP);
				mv.visitInsn(POP);
				break;
			case LSHL:
			case LSHR:
			case LUSHR:
				mv.visitInsn(DUP);
				mv.visitInsn(DUP2_X2);
				mv.visitInsn(POP2);
				mv.visitInsn(POP2);
				mv.visitInsn(POP);
				mv.visitInsn(I2L);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
