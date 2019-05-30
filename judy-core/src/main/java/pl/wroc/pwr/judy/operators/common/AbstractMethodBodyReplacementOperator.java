package pl.wroc.pwr.judy.operators.common;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.*;
import pl.wroc.pwr.judy.utils.Accesses;

/**
 * Abstract base class for mutation operators that replace method bodies.
 * <p/>
 * Any non-negative number of mutant per mutation point is supported.
 *
 * @author pmiwaszko
 */
public abstract class AbstractMethodBodyReplacementOperator<T> extends AbstractMutationOperator {
	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutatntIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutatntIndex);
	}

	// extension points

	/**
	 * Check if method is applicable for mutation.
	 */
	protected abstract boolean checkMethod(String className, int access, String name, String desc, String signature,
										   String[] exceptions);

	/**
	 * Count how many mutants can be possibly created for a given method. This
	 * method will be only called once.
	 */
	protected abstract int countReplacements(String className, int access, String name, String desc, String signature,
											 String[] exceptions);

	/**
	 * Get replacement for a given method. This method should always return
	 * replacements in the same order.
	 */
	protected abstract T getReplacement(String className, int access, String name, String desc, String signature,
										String[] exceptions, int index);

	/**
	 * Generate code that prepares arguments for and invokes the new method.
	 * Returning a value (if any) is generated automatically.
	 */
	protected abstract void replace(MethodVisitor mv, String className, int access, String name, String desc,
									T replacement);

	/**
	 * Custom class mutater.
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

			if (checkMethod(className, access, name, desc, signature, exceptions)) {
				if (isCountingMutants()) {
					nextMutationPoint(countReplacements(className, access, name, desc, signature, exceptions));
				} else {
					nextMutationPoint();
				}

				if (shouldMutate()) {
					T replacement = getReplacement(className, access, name, desc, signature, exceptions,
							getMutantIndex());
					if (replacement != null) {
						AbstractMethodMutater mutater = new MethodMutater(methodVisitor, className, access, name, desc,
								replacement);
						setMutantLineNumber(mutater.getLastLineNumber());
						setMutantDescription(Accesses.toString(access) + " method " + name + "(...) replaced");

						setMutatedMethodDescriptor(desc);
						setMutatedMethodName(name);
						methodVisitor = mutater;
					} else {
						throw new IllegalStateException(this.getClass().getSimpleName()
								+ ": Replacement cannot be null!");
					}
				}
			}
			return methodVisitor;
		}

		/**
		 * Custom method mutater that replaces method bodies with another method
		 * call.
		 */
		private class MethodMutater extends AbstractCoreMethodMutater {
			public MethodMutater(final MethodVisitor mv, final String className, int access, String name, String desc,
								 T replacement) {
				super(mv);

				this.access = access;
				this.name = name;
				this.desc = desc;
				this.replacement = replacement;
			}

			// insert replacement before RETURN
			// keep first and last label & local variables

			@Override
			public void visitInsn(int opcode) {
				if (opcode >= IRETURN && opcode <= RETURN) {
					seenReturn = true;
					replace(mv, className, access, name, desc, replacement);
					mv.visitInsn(opcode);
				}
			}

			@Override
			public void visitLabel(Label label) {
				if (!seenLabel || seenReturn) {
					seenLabel = true;
					mv.visitLabel(label);
				}
			}

			@Override
			public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
				mv.visitLocalVariable(name, desc, signature, start, end, index);
			}

			// empty implementations

			@Override
			public void visitFieldInsn(int opcode, String owner, String name, String desc) {
			}

			@Override
			public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
			}

			@Override
			public void visitIincInsn(int var, int increment) {
			}

			@Override
			public void visitIntInsn(int opcode, int operand) {
			}

			@Override
			public void visitJumpInsn(int opcode, Label label) {
			}

			@Override
			public void visitLdcInsn(Object cst) {
			}

			@Override
			public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
			}

			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			}

			@Override
			public void visitMultiANewArrayInsn(String desc, int dims) {
			}

			@Override
			public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
			}

			@Override
			public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
			}

			@Override
			public void visitTypeInsn(int opcode, String type) {
			}

			@Override
			public void visitVarInsn(int opcode, int var) {
			}

			private final int access;
			private final String name;
			private final String desc;
			private final T replacement;

			private boolean seenLabel;
			private boolean seenReturn;
		}
	}
}
