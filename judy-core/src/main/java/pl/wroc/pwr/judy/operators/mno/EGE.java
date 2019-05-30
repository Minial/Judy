package pl.wroc.pwr.judy.operators.mno;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreMethodMutater;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;
import pl.wroc.pwr.judy.utils.ClassUtil;

/**
 * Generalization of caught errors.
 * <p/>
 * Operator would cause the generalization of a catched error to its superclass
 * type.
 *
 * @author mnowak
 */
public class EGE extends AbstractCoreMethodMutaionOperator {

	@Override
	public String getDescription() {
		return "generalization of caught errors";
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

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
			return new MethodMutater(methodVisitor);
		}

		/**
		 * Custom method mutater.
		 */
		private class MethodMutater extends AbstractCoreMethodMutater {

			private final static String GENERAL_EXCEPTION_TYPE = "java/lang/Exception";

			public MethodMutater(final MethodVisitor mv) {
				super(mv);
			}

			@Override
			public void visitInsn(final int opcode) {
				mv.visitInsn(opcode);
			}

			@Override
			public void visitTypeInsn(final int opcode, final String type) {
				if (opcode == NEW && isException(type)) {
					mv.visitTypeInsn(opcode, GENERAL_EXCEPTION_TYPE);
				}
				mv.visitTypeInsn(opcode, type);
			}

			@Override
			public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc,
										boolean itf) {
				if (opcode == INVOKESPECIAL && isException(owner)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mv.visitMethodInsn(opcode, GENERAL_EXCEPTION_TYPE, name, desc, false);
						return;
					}
				}
				mv.visitMethodInsn(opcode, owner, name, desc, itf);
			}

			private boolean isException(String type) {
				Class<?> cl = ClassUtil.getClass(type);
				if (cl != null && Exception.class.isAssignableFrom(cl)) {
					return true;
				}
				return false;
			}
		}
	}
}
