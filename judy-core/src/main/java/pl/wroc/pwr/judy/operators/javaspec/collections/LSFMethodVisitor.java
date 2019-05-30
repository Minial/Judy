package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import pl.wroc.pwr.judy.operators.common.AbstractInvokerMethodVisitor;

public class LSFMethodVisitor extends AbstractInvokerMethodVisitor {
	private static final String SHUFFLE_METHOD_DESC = "(Ljava/util/List;)V";
	private static final String SHUFFLE_METHOD = "shuffle";
	private static final String SHUFFLE_METHOD_CLASS = "java/util/Collections";

	/**
	 * @param mv     method visitor
	 * @param access method access opcode
	 * @param desc   method description
	 */
	public LSFMethodVisitor(MethodVisitor mv, int access, String desc) {
		super(mv, access, desc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invoke() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, SHUFFLE_METHOD_CLASS, SHUFFLE_METHOD, SHUFFLE_METHOD_DESC, false);
	}
}