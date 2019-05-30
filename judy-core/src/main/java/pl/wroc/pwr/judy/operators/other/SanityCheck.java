package pl.wroc.pwr.judy.operators.other;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * This mutation operator does nothing. It is a sanity check to test if
 * invariants and return values are correct.
 *
 * @author Grzegorz
 */
public class SanityCheck extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "sanity check, does nothing";
	}

	@Override
	protected boolean checkInsn(String className, int opcode) {
		return opcode == ISUB || opcode == IMUL || opcode == IDIV || opcode == IREM || opcode == LSUB || opcode == LMUL
				|| opcode == LDIV || opcode == LREM || opcode == FSUB || opcode == FMUL || opcode == FDIV
				|| opcode == FREM || opcode == DSUB || opcode == DMUL || opcode == DDIV || opcode == DREM; // double
	}

	@Override
	protected void mutateInsn(MethodVisitor mv, String className, int opcode) {
		mv.visitInsn(opcode); // do nothing
	}
}
