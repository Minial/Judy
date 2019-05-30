package pl.wroc.pwr.judy.operators.arithmetic;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Arithmetic Instruction Replacement.
 * <p/>
 * Replace basic binary arithmetic instructions with ADD (+).
 *
 * @author pmiwaszko
 */
public class AIR_Add extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace basic binary arithmetic instructions with ADD";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == ISUB || opcode == IMUL || opcode == IDIV || opcode == IREM || opcode == LSUB || opcode == LMUL
				|| opcode == LDIV || opcode == LREM || opcode == FSUB || opcode == FMUL || opcode == FDIV
				|| opcode == FREM || opcode == DSUB || opcode == DMUL || opcode == DDIV || opcode == DREM;
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case ISUB:
			case IMUL:
			case IDIV:
			case IREM:
				mv.visitInsn(IADD);
				break;
			case LSUB:
			case LMUL:
			case LDIV:
			case LREM:
				mv.visitInsn(LADD);
				break;
			case FSUB:
			case FMUL:
			case FDIV:
			case FREM:
				mv.visitInsn(FADD);
				break;
			case DSUB:
			case DMUL:
			case DDIV:
			case DREM:
				mv.visitInsn(DADD);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
