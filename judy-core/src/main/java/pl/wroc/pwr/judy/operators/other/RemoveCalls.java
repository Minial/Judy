package pl.wroc.pwr.judy.operators.other;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Remove Calls.
 * <p/>
 * Change methods call to the default value of the return type.
 *
 * @author pmiwaszko
 */
public class RemoveCalls extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "remove methods calls";
	}

	@Override
	protected boolean checkMethodInsn(String className, int opcode, String owner, String name, String desc) {
		// no constructors & no System.exit() & sysout or syserr
		String sig = owner + '.' + name;
		return !(INIT.equals(name) || sig.startsWith("java/io/PrintStream.print") || sig
				.equals("java/lang/System.exit"));

		// for constructors:
		// if (INIT.equals(name))
		// {
		// mv.visitInsn(POP);
		// mv.visitInsn(POP);
		// mv.visitInsn(ACONST_NULL);
		// }
	}

	@Override
	protected void mutateMethodInsn(MethodVisitor mv, String className, int opcode, String owner, String name,
									String desc, boolean itf) {
		// pop arguments
		Type[] argumentsTypes = Type.getArgumentTypes(desc);
		for (int i = argumentsTypes.length - 1; i >= 0; i--) {
			Type argumentType = argumentsTypes[i];

			if (argumentType.getSize() == 1) {
				mv.visitInsn(POP);
			} else {
				mv.visitInsn(POP2);
			}
		}

		// pop 'this' reference for non-static calls
		if (opcode != INVOKESTATIC) {
			mv.visitInsn(POP);
		}

		// push default value
		Type returnType = Type.getReturnType(desc);
		if (returnType.equals(Type.BOOLEAN_TYPE) || returnType.equals(Type.BYTE_TYPE)
				|| returnType.equals(Type.SHORT_TYPE) || returnType.equals(Type.INT_TYPE)
				|| returnType.equals(Type.CHAR_TYPE)) {
			mv.visitInsn(ICONST_0);
		} else if (returnType.equals(Type.DOUBLE_TYPE)) {
			mv.visitInsn(DCONST_0);
		} else if (returnType.equals(Type.FLOAT_TYPE)) {
			mv.visitInsn(FCONST_0);
		} else if (returnType.equals(Type.LONG_TYPE)) {
			mv.visitInsn(LCONST_0);
		} else if (returnType.equals(Type.VOID_TYPE)) {
			// nothing should be put on stack
		} else {
			mv.visitInsn(ACONST_NULL);
		}
	}
}
