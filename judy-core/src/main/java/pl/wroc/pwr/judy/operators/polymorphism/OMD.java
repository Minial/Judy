package pl.wroc.pwr.judy.operators.polymorphism;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.utils.Accesses;

/**
 * Overloading Method Deletion.
 * <p/>
 * Deletes overloading method declarations, one at a time in turn.
 *
 * @author pmiwaszko
 */
public class OMD extends AbstractMutationOperator {
	@Override
	public String getDescription() {
		return "delete overloading method declarations, one at a time";
	}

	/**
	 * Check if method is applicable for mutation.
	 */
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !Accesses.isStatic(access) && !INIT.equals(name) && !className.startsWith(JAVA)
				&& hasOverloadingMethods(className, name, desc);
	}

	private boolean hasOverloadingMethods(String className, String name, String desc) {
		IEnvironment env = getEnvironment();
		for (IMethodInfo method : env.getClassInfo(className).getDeclaredMethods()) {
			Type mRet = Type.getReturnType(desc);
			Type rRet = Type.getReturnType(method.getDesc());
			if (name.equals(method.getName()) && !desc.equals(method.getDesc()) && mRet.equals(rRet)
					&& !Accesses.isStatic(method.getAccess())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutatntIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutatntIndex);
	}

	// private inner types

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
			if (checkMethod(className, access, name, desc, signature, exceptions)) {
				nextMutationPoint();
				if (shouldMutate()) {
					setMutantDescription(Accesses.toString(access) + " overloading method " + name + "(...) deleted");
					return null;
				}
			}
			return cv.visitMethod(access, name, desc, signature, exceptions);
		}
	}
}
