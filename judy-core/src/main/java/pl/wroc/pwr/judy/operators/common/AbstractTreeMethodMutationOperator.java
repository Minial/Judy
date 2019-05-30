package pl.wroc.pwr.judy.operators.common;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.AbstractTreeMethodMutater;
import pl.wroc.pwr.judy.utils.Accesses;

/**
 * Abstract base class for mutation operators that use
 * <code>org.objectweb.asm.tree</code> API to mutate method bodies.
 *
 * @author pmiwaszko
 */
public abstract class AbstractTreeMethodMutationOperator extends AbstractMutationOperator {
	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutantIndex);
	}

	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
	}

	// extension points

	/**
	 * Check if method is applicable for mutation.
	 */
	protected abstract boolean checkMethod(String className, int access, String name, String desc, String signature,
										   String[] exceptions);

	/**
	 * Perform a method mutation.
	 */
	protected abstract void mutate(MethodNode mn, AbstractClassMutater cm) throws Exception;

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
				return new MethodMutater(methodVisitor, access, name, desc, signature, exceptions);
			}
			return methodVisitor;
		}

		private class MethodMutater extends AbstractTreeMethodMutater {
			public MethodMutater(MethodVisitor mv, int access, String name, String desc, String signature,
								 String[] exceptions) {
				super(mv, access, name, desc, signature, exceptions);
			}

			@Override
			protected void mutate(MethodNode mn) throws Exception {
				AbstractTreeMethodMutationOperator.this.mutate(mn, ClassMutater.this);
			}
		}
	}
}
