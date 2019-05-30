package pl.wroc.pwr.judy.operators.shift;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Shift Instruction Replacement.
 * <p/>
 * Replace shift instructions with USHR (>>>).
 *
 * @author pmiwaszko
 */
public class SIR_Ushr extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace shift instructions with USHR";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == ISHL || opcode == ISHR || opcode == LSHL || opcode == LSHR;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case ISHL:
			case ISHR:
				mv.visitInsn(IUSHR);
				break;
			case LSHL:
			case LSHR:
				mv.visitInsn(LUSHR);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
