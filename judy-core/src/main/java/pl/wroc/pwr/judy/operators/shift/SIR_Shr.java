package pl.wroc.pwr.judy.operators.shift;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Shift Instruction Replacement.
 * <p/>
 * Replace shift instructions with SHR (>>).
 *
 * @author pmiwaszko
 */
public class SIR_Shr extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace shift instructions with SHR";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == ISHL || opcode == IUSHR || opcode == LSHL || opcode == LUSHR;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case ISHL:
			case IUSHR:
				mv.visitInsn(ISHR);
				break;
			case LSHL:
			case LUSHR:
				mv.visitInsn(LSHR);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
