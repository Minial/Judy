package pl.wroc.pwr.judy.operators.encapsulation;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.utils.Accesses;

import static pl.wroc.pwr.judy.utils.Accesses.*;

/**
 * Access modifier change.
 * <p/>
 * Change the access level for instance variables and methods to other access
 * levels.
 * <p/>
 * Currently only widening of level is allowed.
 *
 * @author pmiwaszko
 */
public class AMC extends AbstractMutationOperator {
	@Override
	public String getDescription() {
		return "change access levels";
	}

	/**
	 * Check if method is applicable for mutation.
	 */
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !name.equals(INIT) && !name.equals(CLINIT) && !className.startsWith(JAVA);
	}

	protected boolean checkField(String className, int access, String name, String desc, String signature, Object value) {
		return !name.startsWith(THIS) && !className.startsWith(JAVA);
	}

	int getReplacement(int access, int mutantIndex) {
		return getAccess(access)[mutantIndex];
	}

	private int[] getAccess(int access) {
		if (isPrivate(access)) {
			int base = remove(access, ACC_PRIVATE);
			return new int[]{add(base, ACC_PROTECTED), add(base, ACC_PUBLIC), base};
		} else if (isProtected(access)) {
			int base = remove(access, ACC_PROTECTED);
			return new int[]{
					// add(base, ACC_PRIVATE),
					add(base, ACC_PUBLIC)
					// , base
			};
		} else if (isPublic(access)) {
			// int base = remove(access, ACC_PUBLIC);
			return new int[]{
					// add(base, ACC_PRIVATE), add(base, ACC_PROTECTED), base
			};
		} else {
			int base = access;
			return new int[]{
					// add(base, ACC_PRIVATE),
					add(base, ACC_PROTECTED), add(base, ACC_PUBLIC)};
		}
	}

	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutantIndex);
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

			if (checkMethod(className, access, name, desc, signature, exceptions)) {
				if (isCountingMutants()) {
					nextMutationPoint(getAccess(access).length);
				} else {
					nextMutationPoint();
				}
				if (shouldMutate()) {
					int replacement = getReplacement(access, getMutantIndex());
					setMutantDescription(Accesses.toString(access) + " method " + name + "(...) changed to "
							+ Accesses.toString(replacement));
					setMutatedMethodDescriptor(desc);
					setMutatedMethodName(name);
					return cv.visitMethod(replacement, name, desc, signature, exceptions);
				}
			}
			return cv.visitMethod(access, name, desc, signature, exceptions);
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			if (checkField(className, access, name, desc, signature, value)) {
				if (isCountingMutants()) {
					nextMutationPoint(getAccess(access).length);
				} else {
					nextMutationPoint();
				}
				if (shouldMutate()) {
					int replacement = getReplacement(access, getMutantIndex());
					setMutantDescription(Accesses.toString(access) + " field " + name + " changed to "
							+ Accesses.toString(replacement));
					setMutatedMethodDescriptor(desc);
					setMutatedMethodName(name);
					return cv.visitField(replacement, name, desc, signature, value);
				}
			}
			return cv.visitField(access, name, desc, signature, value);
		}
	}
}
