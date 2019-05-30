package pl.wroc.pwr.judy.operators.inheritance;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreMethodMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;

/**
 * Explicit call of a parent's constructor deletion.
 * <p/>
 * Deletes super constructor calls, causing the default constructor of the
 * parent class to be called.
 *
 * @author pmiwaszko
 */
public class IPC extends AbstractMutationOperator {
	@Override
	public String getDescription() {
		return "delete super constructor call";
	}

	/**
	 * Check if method is applicable for mutation.
	 */
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith(JAVA) && canReplaceSuperCall(className, name, desc);
	}

	/**
	 * Test if a given method is constructor and if super class has a default
	 * constructor.
	 */
	private boolean canReplaceSuperCall(String className, String name, String desc) {
		if (INIT.equals(name)) {
			IEnvironment env = getEnvironment();
			IClassInfo superClass = env.getClassInfo(env.getClassInfo(className).getSuperClassName());
			for (IMethodInfo method : superClass.getDeclaredMethods()) {
				if (isDefultConstructor(superClass.getClassName(), method)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if given method is a default constructor.
	 */
	boolean isDefultConstructor(String className, IMethodInfo method) {
		return INIT.equals(method.getName()) && method.getDesc().equals(getDefualtConstructorDesc(className));
	}

	/**
	 * Get description of default constructor in a given class.
	 */
	String getDefualtConstructorDesc(String className) {
		if (isNestedClass(className)) {
			// nested classes have default constructor with hidden parameter
			return "(L" + getOuterClassName(className) + ";)V";
		} else {
			return "()V";
		}
	}

	/**
	 * Test if given class is nested.
	 */
	boolean isNestedClass(String className) {
		return className.contains("$");
	}

	/**
	 * Get name of outer class for a given nested class.
	 */
	String getOuterClassName(String className) {
		return className.substring(0, className.indexOf('$'));
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
			if (checkMethod(className, access, name, desc, signature, exceptions)) {
				return new MethodMutater(methodVisitor);
			}
			return methodVisitor;
		}

		/**
		 * Custom method mutater that replaces method bodies with another method
		 * call.
		 */
		private class MethodMutater extends AbstractCoreMethodMutater {
			public MethodMutater(final MethodVisitor mv) {
				super(mv);
			}

			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
				if (!seenSuperCall && opcode == INVOKESPECIAL && INIT.equals(name)
						&& !getDefualtConstructorDesc(owner).equals(desc)) {
					seenSuperCall = true;
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);

						int leaveArgsOnStack = 1; // leave this reference
						if (isNestedClass(owner)) {
							leaveArgsOnStack = 2; // leave this and outer class
							// references
						}

						// pop arguments
						Type[] argumentsTypes = Type.getArgumentTypes(desc);
						for (int i = argumentsTypes.length - leaveArgsOnStack; i >= 0; i--) {
							Type argumentType = argumentsTypes[i];
							if (argumentType.getSize() == 1) {
								mv.visitInsn(POP);
							} else {
								mv.visitInsn(POP2);
							}
						}

						// visit default constructor
						mv.visitMethodInsn(opcode, owner, name, getDefualtConstructorDesc(owner), itf);
						return;
					}
				}
				mv.visitMethodInsn(opcode, owner, name, desc, itf);
			}

			private boolean seenSuperCall = false;
		}
	}
}
