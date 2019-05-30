package pl.wroc.pwr.judy.operators.shift;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Shift Instruction Replacement.
 * <p/>
 * Replace shift instructions with SHL (<<).
 *
 * @author pmiwaszko
 */
public class SIR_Shl extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace shift instructions with SHL";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == ISHR || opcode == IUSHR || opcode == LSHR || opcode == LUSHR;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case ISHR:
			case IUSHR:
				mv.visitInsn(ISHL);
				break;
			case LSHR:
			case LUSHR:
				mv.visitInsn(LSHL);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
