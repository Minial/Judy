package pl.wroc.pwr.judy.operators.arithmetic;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Arithmetic Instruction Replacement.
 * <p/>
 * Replace basic binary arithmetic instructions with DIV (/).
 *
 * @author pmiwaszko
 */
public class AIR_Div extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace basic binary arithmetic instructions with DIV";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == IADD || opcode == ISUB || opcode == IMUL || opcode == IREM || opcode == LADD || opcode == LSUB
				|| opcode == LMUL || opcode == LREM || opcode == FADD || opcode == FSUB || opcode == FMUL
				|| opcode == FREM || opcode == DADD || opcode == DSUB || opcode == DMUL || opcode == DREM; // double
	}

	// TODO AIR_Div: consider checkIincInst! (also applies to other AIRs)

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		switch (opcode) {
			case IADD:
			case ISUB:
			case IMUL:
			case IREM:
				mv.visitInsn(IDIV);
				break;
			case LADD:
			case LSUB:
			case LMUL:
			case LREM:
				mv.visitInsn(LDIV);
				break;
			case FADD:
			case FSUB:
			case FMUL:
			case FREM:
				mv.visitInsn(FDIV);
				break;
			case DADD:
			case DSUB:
			case DMUL:
			case DREM:
				mv.visitInsn(DDIV);
				break;
			default:
				// TODO equivalent mutation, insert generated error code
		}
	}
}
