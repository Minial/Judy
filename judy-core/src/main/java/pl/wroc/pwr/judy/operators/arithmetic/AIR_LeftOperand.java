package pl.wroc.pwr.judy.operators.arithmetic;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Arithmetic Instruction Replacement.
 * <p/>
 * Replace basic binary arithmetic instructions with their left operands.
 *
 * @author pmiwaszko
 */
public class AIR_LeftOperand extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace basic binary arithmetic instructions with their left operands";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IADD || opcode == ISUB || opcode == IMUL || opcode == IDIV || opcode == IREM || opcode == LADD
				|| opcode == LSUB || opcode == LMUL || opcode == LDIV || opcode == LREM || opcode == FADD
				|| opcode == FSUB || opcode == FMUL || opcode == FDIV || opcode == FREM || opcode == DADD
				|| opcode == DSUB || opcode == DMUL || opcode == DDIV || opcode == DREM; // double
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IADD:
			case ISUB:
			case IMUL:
			case IDIV:
			case IREM:
				mv.visitInsn(POP);
				break;
			case LADD:
			case LSUB:
			case LMUL:
			case LDIV:
			case LREM:
				mv.visitInsn(POP2);
				break;
			case FADD:
			case FSUB:
			case FMUL:
			case FDIV:
			case FREM:
				mv.visitInsn(POP);
				break;
			case DADD:
			case DSUB:
			case DMUL:
			case DDIV:
			case DREM:
				mv.visitInsn(POP2);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
