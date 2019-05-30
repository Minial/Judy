package pl.wroc.pwr.judy.operators.inheritance;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreMethodMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.utils.Accesses;

import java.util.List;

/**
 * Overridden Method Rename.
 * <p/>
 * Checks if an overriding method adversely affects other methods. Models the
 * situation that the overriding method is declared as a new method with
 * different name in the child class.
 *
 * @author pmiwaszko
 */
public class IOR extends AbstractMutationOperator {
	@Override
	public String getDescription() {
		return "rename overridden method";
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutantIndex);
	}

	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
	}

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

		private boolean checkClass() {
			IEnvironment env = getEnvironment();
			// test if class have subclasses
			subclasses = env.getDirectSubclasses(className);
			return subclasses != null && !subclasses.isEmpty();
		}

		private boolean checkMethod(int access, String className, String name, String desc, String signature,
									String[] exceptions) {
			return shouldMutate && !className.startsWith(JAVA);
		}

		@Override
		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
			className = name;
			shouldMutate = checkClass();
			// remove ACC_SUPER modifier (seems to be not necessary)
			int mAccess = access;
			if (shouldMutate) {
				mAccess = Accesses.remove(ACC_SUPER, access);
			}
			cv.visit(version, mAccess, name, signature, superName, interfaces);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
			if (checkMethod(access, className, name, desc, signature, exceptions)) {
				methodVisitor = new MethodMutater(methodVisitor);
			}
			return methodVisitor;
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
				if (opcode == INVOKEVIRTUAL && owner.equals(className)) {
					// check if method 'name' is overridden in a subclass
					IEnvironment env = getEnvironment();
					boolean isOverridden = false;
					for (String subclassName : subclasses) {
						if (env.isOverridingMethod(subclassName, name, desc)) {
							isOverridden = true;
							break;
						}
					}
					if (isOverridden) {
						nextMutationPoint();
						if (shouldMutate()) {
							setMutantLineNumber(lastLineNumber);
							// change opcode to use version from this class
							// (non-virtual call)
							mv.visitMethodInsn(INVOKESPECIAL, owner, name, desc, itf);
							return;
						}
					}
				}
				mv.visitMethodInsn(opcode, owner, name, desc, itf);
			}
		}

		private List<String> subclasses;
		private boolean shouldMutate;
	}
}
