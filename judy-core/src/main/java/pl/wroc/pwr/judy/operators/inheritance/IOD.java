package pl.wroc.pwr.judy.operators.inheritance;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.operators.common.AbstractMethodBodyReplacementOperator;

/**
 * Overriding Method Deletion.
 * <p/>
 * Deletes an entire declaration of an overriding method in a subclass so that
 * references to the method uses the parent's version. The mutant act as if
 * there is no overriding method for the method.
 * <p/>
 * Note: this implementation does not remove method declaration, but replaces
 * overriding method's body with parent's version instead.
 *
 * @author pmiwaszko
 */
public class IOD extends AbstractMethodBodyReplacementOperator<String> {
	@Override
	public String getDescription() {
		return "delete overriding method";
	}

	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		IEnvironment env = getEnvironment();
		return !className.startsWith(JAVA) && !INIT.equals(name) && env.isOverridingMethod(className, name, desc);
	}

	@Override
	protected int countReplacements(String className, int access, String name, String desc, String signature,
									String[] exceptions) {
		return 1;
	}

	@Override
	protected String getReplacement(String className, int access, String name, String desc, String signature,
									String[] exceptions, int index) {
		IEnvironment env = getEnvironment();
		return env.getClassInfo(className).getSuperClassName();
	}

	@Override
	protected void replace(MethodVisitor mv, String className, int access, String name, String desc, String replacement) {
		// load parameters
		Type[] args = Type.getArgumentTypes(desc);

		mv.visitVarInsn(ALOAD, 0); // this
		for (int i = 0; i < args.length; i++) {
			mv.visitVarInsn(args[i].getOpcode(ILOAD), i + 1);
		}
		// TODO: Consider Java8 and interface default implementations
		mv.visitMethodInsn(INVOKESPECIAL, replacement, name, desc, false);
	}
}
