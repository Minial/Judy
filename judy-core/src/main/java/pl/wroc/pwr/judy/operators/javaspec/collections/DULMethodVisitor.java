package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.operators.common.AbstractInvokerMethodVisitor;
import pl.wroc.pwr.judy.operators.common.verifiers.ListInterfaceAPIVerifier;
import pl.wroc.pwr.judy.operators.common.verifiers.TypeVerifier;

import java.util.Random;

public class DULMethodVisitor extends AbstractInvokerMethodVisitor {
	private static final String RANDOM_CONSTRUCTOR_DESCRIPTION = "()V";
	private static final String SIZE_DESCRPTION = "()I";
	private static final String NEXT_DESCRIPTION = "(I)I";
	private static final String GET_DESCRIPTION = "(I)Ljava/lang/Object;";
	private static final String ADD_DESCRIPTION = "(Ljava/lang/Object;)Z";
	private static final String RANDOM_CLASS = "java/util/Random";
	private static final String SIZE_METHOD = "size";
	private static final String INIT = "<init>";
	private static final String ADD_METHOD = "add";
	private static final String GET_METHOD = "get";
	private static final String NEXT_INT_METHOD = "nextInt";

	private static final TypeVerifier RETURN_TYPE_VERIFIER = new ListInterfaceAPIVerifier();

	/**
	 * @param mv     method visitor
	 * @param access method access opcode
	 * @param desc   method description
	 */
	public DULMethodVisitor(final MethodVisitor mv, final int access, final String desc) {
		super(mv, access, desc);
	}

	/**
	 * The method adds couple lines of code which duplicates randomly selected
	 * list element. The lines of code are listed below:<br />
	 * <code>
	 * Random randomGenerator = new Random();<br/>
	 * int index = randomGenerator.nextInt(list.size());<br/>
	 * list.add(list.get(index));<br/>
	 * </code>
	 */
	@Override
	public void invoke() {
		int invokeOpCode = getInvokeOpCode();
		String returnType = getMethodReturnType().getInternalName();

		final int listStoreIndex = newLocal(getMethodReturnType());
		mv.visitVarInsn(Opcodes.ASTORE, listStoreIndex);
		mv.visitTypeInsn(Opcodes.NEW, RANDOM_CLASS);
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, RANDOM_CLASS, INIT, RANDOM_CONSTRUCTOR_DESCRIPTION, false);
		final int randomStoreIndex = newLocal(Type.getType(Random.class));
		mv.visitVarInsn(Opcodes.ASTORE, randomStoreIndex);
		mv.visitVarInsn(Opcodes.ALOAD, randomStoreIndex);
		mv.visitVarInsn(Opcodes.ALOAD, listStoreIndex);
		mv.visitMethodInsn(invokeOpCode, returnType, SIZE_METHOD, SIZE_DESCRPTION,
				invokeOpCode == Opcodes.INVOKEINTERFACE);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, RANDOM_CLASS, NEXT_INT_METHOD, NEXT_DESCRIPTION, false);
		final int sizeStoreIndex = newLocal(Type.INT_TYPE);
		mv.visitVarInsn(Opcodes.ISTORE, sizeStoreIndex);
		mv.visitVarInsn(Opcodes.ALOAD, listStoreIndex);
		mv.visitVarInsn(Opcodes.ALOAD, listStoreIndex);
		mv.visitVarInsn(Opcodes.ILOAD, sizeStoreIndex);
		mv.visitMethodInsn(invokeOpCode, returnType, GET_METHOD, GET_DESCRIPTION,
				invokeOpCode == Opcodes.INVOKEINTERFACE);
		mv.visitMethodInsn(invokeOpCode, returnType, ADD_METHOD, ADD_DESCRIPTION,
				invokeOpCode == Opcodes.INVOKEINTERFACE);
		mv.visitInsn(Opcodes.POP);
		mv.visitVarInsn(Opcodes.ALOAD, listStoreIndex);
	}

	private int getInvokeOpCode() {
		return RETURN_TYPE_VERIFIER.verifyType(getMethodReturnType()) ? Opcodes.INVOKEINTERFACE : Opcodes.INVOKEVIRTUAL;
	}

}