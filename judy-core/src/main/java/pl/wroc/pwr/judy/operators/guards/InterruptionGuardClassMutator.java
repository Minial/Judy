package pl.wroc.pwr.judy.operators.guards;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;

public class InterruptionGuardClassMutator extends AbstractCoreClassMutater {
	private static final String INIT = "<init>";

	/**
	 * Creates counting InterruptionClassMutator
	 */
	public InterruptionGuardClassMutator() {
		super();
	}

	/**
	 * Creates mutating InterruptionClassMutator
	 *
	 * @param cv                 class visitor
	 * @param mutationPointIndex mutation point index
	 * @param mutantIndex        mutant index
	 */
	public InterruptionGuardClassMutator(final ClassVisitor cv, final int mutationPointIndex, final int mutantIndex) {
		super(cv, mutationPointIndex, mutantIndex);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		if (isAccessible(access) && !isConstructor(name)) {
			nextMutationPoint();
			if (shouldMutate()) {
				mv = new SleepMethodMutator(mv, access, desc);
			}
		}
		return mv;
	}

	private boolean isConstructor(String name) {
		return INIT.equals(name);
	}

	private boolean isAccessible(int access) {
		return access == Opcodes.ACC_PUBLIC || access == Opcodes.ACC_PROTECTED || access == Opcodes.ACC_STATIC
				|| access == 0;
	}
}