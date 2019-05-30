package pl.wroc.pwr.judy.operators.arithmetic;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Arithmetic Instruction Replacement.
 * <p/>
 * Replace basic binary arithmetic instructions with MUL (*).
 *
 * @author pmiwaszko
 */
public class AIR_Mul extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace basic binary arithmetic instructions with MUL";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IADD || opcode == ISUB || opcode == IDIV || opcode == IREM || opcode == LADD || opcode == LSUB
				|| opcode == LDIV || opcode == LREM || opcode == FADD || opcode == FSUB || opcode == FDIV
				|| opcode == FREM || opcode == DADD || opcode == DSUB || opcode == DDIV || opcode == DREM; // double
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IADD:
			case ISUB:
			case IDIV:
			case IREM:
				mv.visitInsn(IMUL);
				break;
			case LADD:
			case LSUB:
			case LDIV:
			case LREM:
				mv.visitInsn(LMUL);
				break;
			case FADD:
			case FSUB:
			case FDIV:
			case FREM:
				mv.visitInsn(FMUL);
				break;
			case DADD:
			case DSUB:
			case DDIV:
			case DREM:
				mv.visitInsn(DMUL);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
