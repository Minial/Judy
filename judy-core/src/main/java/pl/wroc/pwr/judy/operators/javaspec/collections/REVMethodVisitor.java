package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import pl.wroc.pwr.judy.operators.common.AbstractInvokerMethodVisitor;

public class REVMethodVisitor extends AbstractInvokerMethodVisitor {
	private static final String REVERSE_METHOD_RETURN = "(Ljava/util/List;)V";
	private static final String REVERSE_METHOD = "reverse";
	private static final String REVERSE_METHOD_CLASS = "java/util/Collections";

	/**
	 * @param mv     method visitor
	 * @param access method access opcode
	 * @param desc   method description
	 */
	public REVMethodVisitor(MethodVisitor mv, int access, String desc) {
		super(mv, access, desc);
	}

	@Override
	public void invoke() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, REVERSE_METHOD_CLASS, REVERSE_METHOD, REVERSE_METHOD_RETURN, false);
	}
}