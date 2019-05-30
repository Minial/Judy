package pl.wroc.pwr.judy.operators.arithmetic;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Arithmetic Instruction Replacement.
 * <p/>
 * Replace basic binary arithmetic instructions with SUB (-).
 *
 * @author pmiwaszko
 */
public class AIR_Sub extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace basic binary arithmetic instructions with SUB";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IADD || opcode == IMUL || opcode == IDIV || opcode == IREM || opcode == LADD || opcode == LMUL
				|| opcode == LDIV || opcode == LREM || opcode == FADD || opcode == FMUL || opcode == FDIV
				|| opcode == FREM || opcode == DADD || opcode == DMUL || opcode == DDIV || opcode == DREM; // double
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IADD:
			case IMUL:
			case IDIV:
			case IREM:
				mv.visitInsn(ISUB);
				break;
			case LADD:
			case LMUL:
			case LDIV:
			case LREM:
				mv.visitInsn(LSUB);
				break;
			case FADD:
			case FMUL:
			case FDIV:
			case FREM:
				mv.visitInsn(FSUB);
				break;
			case DADD:
			case DMUL:
			case DDIV:
			case DREM:
				mv.visitInsn(DSUB);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
