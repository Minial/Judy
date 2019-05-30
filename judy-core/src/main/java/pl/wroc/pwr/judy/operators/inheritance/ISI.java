package pl.wroc.pwr.judy.operators.inheritance;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Super Keyword Insertion.
 * <p/>
 * Insert <code>super</code> keyword before fields and methods calls.
 *
 * @author pmiwaszko
 */
public class ISI extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "insert super keyword before fields and methods calls";
	}

	@Override
	protected boolean checkFieldInsn(String className, int opcode, String owner, String name, String desc) {
		return className.equals(owner) && !name.startsWith(THIS) && getEnvironment().isHidingField(owner, name, desc);
	}

	@Override
	protected void mutateFieldInsn(MethodVisitor mv, String className, int opcode, String owner, String name,
								   String desc) {
		// replace owner with the super class
		mv.visitFieldInsn(opcode, getEnvironment().getClassInfo(className).getSuperClassName(), name, desc);
	}

	@Override
	protected boolean checkMethodInsn(String className, int opcode, String owner, String name, String desc) {
		return (opcode == INVOKEVIRTUAL || opcode == INVOKESTATIC) && className.equals(owner) && !INIT.equals(name)
				&& !CLINIT.equals(name) && getEnvironment().isOverridingMethod(className, name, desc);
	}

	@Override
	protected void mutateMethodInsn(MethodVisitor mv, String className, int opcode, String owner, String name,
									String desc, boolean itf) {
		// replace owner with the super class & change opcode
		int newOpcode = opcode;
		if (opcode == INVOKEVIRTUAL) {
			newOpcode = INVOKESPECIAL;
		}
		mv.visitMethodInsn(newOpcode, getEnvironment().getClassInfo(className).getSuperClassName(), name, desc, itf);
	}
}
