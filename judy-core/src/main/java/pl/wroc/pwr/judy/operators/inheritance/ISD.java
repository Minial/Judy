package pl.wroc.pwr.judy.operators.inheritance;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Super Keyword Deletion.
 * <p/>
 * Delete <code>super</code> keyword before fields and methods calls.
 *
 * @author pmiwaszko
 */
public class ISD extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "delete super keyword before fields and methods calls";
	}

	@Override
	protected boolean checkFieldInsn(String className, int opcode, String owner, String name, String desc) {
		IEnvironment env = getEnvironment();
		String superClassName = env.getClassInfo(className).getSuperClassName();
		return superClassName.equals(owner) && !name.startsWith(THIS) && env.isHidingField(className, name, desc);
	}

	@Override
	protected void mutateFieldInsn(MethodVisitor mv, String className, int opcode, String owner, String name,
								   String desc) {
		// replace owner with the super class
		mv.visitFieldInsn(opcode, className, name, desc);
	}

	@Override
	protected boolean checkMethodInsn(String className, int opcode, String owner, String name, String desc) {
		IEnvironment env = getEnvironment();
		String superClassName = env.getClassInfo(className).getSuperClassName();
		return (opcode == INVOKESPECIAL || opcode == INVOKESTATIC) && superClassName.equals(owner)
				&& !INIT.equals(name) && !CLINIT.equals(name) && env.isOverridingMethod(className, name, desc);
	}

	@Override
	protected void mutateMethodInsn(MethodVisitor mv, String className, int opcode, String owner, String name,
									String desc, boolean itf) {
		// replace owner with the subtype class & change opcode
		int newOpcode = opcode;
		if (opcode == INVOKESPECIAL) {
			newOpcode = INVOKEVIRTUAL;
		}
		mv.visitMethodInsn(newOpcode, className, name, desc, itf);
	}
}
