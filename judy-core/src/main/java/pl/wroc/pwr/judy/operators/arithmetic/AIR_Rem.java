package pl.wroc.pwr.judy.operators.arithmetic;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Arithmetic Instruction Replacement.
 * <p/>
 * Replace basic binary arithmetic instructions with REM (%).
 *
 * @author pmiwaszko
 */
public class AIR_Rem extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace basic binary arithmetic instructions with REM";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IADD || opcode == ISUB || opcode == IMUL || opcode == IDIV || opcode == LADD || opcode == LSUB
				|| opcode == LMUL || opcode == LDIV || opcode == FADD || opcode == FSUB || opcode == FMUL
				|| opcode == FDIV || opcode == DADD || opcode == DSUB || opcode == DMUL || opcode == DDIV; // double
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IADD:
			case ISUB:
			case IMUL:
			case IDIV:
				mv.visitInsn(IREM);
				break;
			case LADD:
			case LSUB:
			case LMUL:
			case LDIV:
				mv.visitInsn(LREM);
				break;
			case FADD:
			case FSUB:
			case FMUL:
			case FDIV:
				mv.visitInsn(FREM);
				break;
			case DADD:
			case DSUB:
			case DMUL:
			case DDIV:
				mv.visitInsn(DREM);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
