package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import pl.wroc.pwr.judy.operators.common.AbstractInvokerMethodVisitor;
import pl.wroc.pwr.judy.operators.common.verifiers.CollectionInterfaceAPIVerifier;
import pl.wroc.pwr.judy.operators.common.verifiers.TypeVerifier;

public class CCEMethodVisitor extends AbstractInvokerMethodVisitor {
	private static final String CLEAR_METHOD_RETURN = "()V";
	private static final String CLEAR_METHOD = "clear";
	private static final TypeVerifier RETURN_TYPE_VERIFIER = new CollectionInterfaceAPIVerifier();

	/**
	 * @param mv     method visitor
	 * @param access method access opcode
	 * @param desc   method description
	 */
	public CCEMethodVisitor(MethodVisitor mv, int access, String desc) {
		super(mv, access, desc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void invoke() {
		int invokeOpCode = getInvokeOpCode();
		mv.visitMethodInsn(invokeOpCode, getMethodReturnType().getInternalName(), CLEAR_METHOD, CLEAR_METHOD_RETURN,
				invokeOpCode == Opcodes.INVOKEINTERFACE);
	}

	private int getInvokeOpCode() {
		return RETURN_TYPE_VERIFIER.verifyType(getMethodReturnType()) ? Opcodes.INVOKEINTERFACE : Opcodes.INVOKEVIRTUAL;
	}
}