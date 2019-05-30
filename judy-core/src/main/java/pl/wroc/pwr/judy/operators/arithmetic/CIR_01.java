package pl.wroc.pwr.judy.operators.arithmetic;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Constant Instruction Replacement.
 * <p/>
 * Replace 0 (false) with 1 (true) and vice-versa.
 *
 * @author pmiwaszko
 */
public class CIR_01 extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace 0 (false) with 1 (true) and vice-versa";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		// TODO CIR_O1: this covers both logical true&false and numerical 0&1
		// should we try to find out a way to cover just true&false?
		// or rather go for LCONST_0, DCONST_0, FCONST_0 as well?
		return opcode == ICONST_0 || opcode == ICONST_1;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case ICONST_0:
				mv.visitInsn(ICONST_1);
				break;
			case ICONST_1:
				mv.visitInsn(ICONST_0);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
