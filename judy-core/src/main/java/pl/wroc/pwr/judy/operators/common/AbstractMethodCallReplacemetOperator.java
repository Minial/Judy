package pl.wroc.pwr.judy.operators.common;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreMethodMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.utils.Accesses;

/**
 * Abstract base class for mutation operators that replace methods calls.
 * <p/>
 * Any non-negative number of mutant per mutation point is supported.
 *
 * @author pmiwaszko
 */
public abstract class AbstractMethodCallReplacemetOperator<T> extends AbstractMutationOperator {
	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutantIndex);
	}

	// extension points

	/**
	 * Check if method is applicable for mutation.
	 */
	protected abstract boolean checkMethodInsn(final String className, final int opcode, final String owner,
											   final String name, final String desc);

	/**
	 * Count how many mutants can be possibly created for a given call. This
	 * method will be only called once.
	 */
	protected abstract int countMethodInsnReplacements(final String className, final int opcode, final String owner,
													   final String name, final String desc);

	/**
	 * Get replacement for a given call. This method should always return
	 * replacements in the same order.
	 */
	protected abstract T getMethodInsnReplacement(final String className, int opcode, String owner, String name,
												  String desc, int mutantIndex);

	/**
	 * Perform a method call mutation.
	 */
	protected abstract void mutateMethodInsn(final MethodVisitor mv, final String className, final int opcode,
											 final String owner, final String name, final String desc, T replacement, boolean itf);

	/**
	 * Custom class mutater for modifying method bodies.
	 */
	private class ClassMutater extends AbstractCoreClassMutater {
		/**
		 * Constructs new class mutater object for mutating classes.
		 */
		public ClassMutater(ClassVisitor cv, int mutationPointIndex, int mutantIndex) {
			super(cv, mutationPointIndex, mutantIndex);
		}

		/**
		 * Constructs new class mutater object for counting mutation points.
		 */
		public ClassMutater() {
			super();
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
			if (Accesses.is(ACC_BRIDGE, access) || Accesses.is(ACC_SYNTHETIC, access)) {
				return methodVisitor;
			}
			return new MethodMutater(methodVisitor);
		}

		/**
		 * Custom method mutater.
		 */
		private class MethodMutater extends AbstractCoreMethodMutater {
			public MethodMutater(final MethodVisitor mv) {
				super(mv);
			}

			@Override
			public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc,
										boolean itf) {
				if (checkMethodInsn(className, opcode, owner, name, desc)) {
					if (isCountingMutants()) {
						nextMutationPoint(countMethodInsnReplacements(className, opcode, owner, name, desc));
					} else {
						nextMutationPoint();
					}
					if (shouldMutate()) {
						T replacement = getMethodInsnReplacement(className, opcode, owner, name, desc, getMutantIndex());
						if (replacement != null) {
							setMutantLineNumber(lastLineNumber);
							mutateMethodInsn(mv, className, opcode, owner, name, desc, replacement, itf);
							return;
						} else {
							throw new IllegalStateException(this.getClass().getSimpleName()
									+ ": Replacement cannot be null!");
						}
					}
				}
				mv.visitMethodInsn(opcode, owner, name, desc, itf);
			}
		}
	}
}
