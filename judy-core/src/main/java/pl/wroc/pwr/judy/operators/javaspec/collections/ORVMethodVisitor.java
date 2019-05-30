package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.operators.common.AbstractInvokerMethodVisitor;

public class ORVMethodVisitor extends AbstractInvokerMethodVisitor {

	private static final String SET_SUFFIX = "Set";

	private static final String COMPARATOR_METHOD = "comparator";
	private static final String COMPARATOR_OBJECT = "Ljava/util/Comparator";
	private static final String COMPARATOR_METHOD_DESC = "()Ljava/util/Comparator;";

	private static final String JAVA_UTIL_COLLECTIONS_CLASS = "java/util/Collections";

	private static final String COLLECTION_COMPARATOR_CONSTRUCTOR_DESC = "(Ljava/util/Comparator;)V";

	private static final String REVERSE_ORDER_METHOD = "reverseOrder";
	private static final String REVERSE_ORDER_METHOD_DESC = "(Ljava/util/Comparator;)Ljava/util/Comparator;";

	private static final String ADD_ALL_METHOD = "addAll";
	private static final String ADD_ALL_METHOD_DESC = "(Ljava/util/Collection;)Z";

	private static final String PUT_ALL_METHOD = "putAll";
	private static final String PUT_ALL_METHOD_DESC = "(Ljava/util/Map;)V";

	private static final String INIT_METHOD = "<init>";

	/**
	 * @param mv     method visitor
	 * @param access method access opcode
	 * @param desc   mutated method description
	 */
	public ORVMethodVisitor(final MethodVisitor mv, final int access, final String desc) {
		super(mv, access, desc);
	}

	@Override
	protected void handleStatic() {
		invoke();
	}

	@Override
	protected void handleLocalVariable() {
		invoke();
	}

	@Override
	protected void handleField() {
		handleLocalVariable();
	}

	@Override
	protected void handleInvocation() {
		invoke();
	}

	@Override
	public void invoke() {
		String collectionType = getMethodReturnType().getInternalName();

		final int originalSetIndex = newLocal(getMethodReturnType());
		mv.visitVarInsn(Opcodes.ASTORE, originalSetIndex);

		mv.visitVarInsn(Opcodes.ALOAD, originalSetIndex);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, collectionType, COMPARATOR_METHOD, COMPARATOR_METHOD_DESC, false);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_UTIL_COLLECTIONS_CLASS, REVERSE_ORDER_METHOD,
				REVERSE_ORDER_METHOD_DESC, false);
		final int comparatorStoreIndex = newLocal(Type.getObjectType(COMPARATOR_OBJECT));
		mv.visitVarInsn(Opcodes.ASTORE, comparatorStoreIndex);

		mv.visitTypeInsn(Opcodes.NEW, collectionType);
		mv.visitInsn(Opcodes.DUP);
		mv.visitVarInsn(Opcodes.ALOAD, comparatorStoreIndex);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, collectionType, INIT_METHOD, COLLECTION_COMPARATOR_CONSTRUCTOR_DESC);

		final int setStoreIndex = newLocal(getMethodReturnType());

		mv.visitVarInsn(Opcodes.ASTORE, setStoreIndex);

		mv.visitVarInsn(Opcodes.ALOAD, setStoreIndex);
		mv.visitVarInsn(Opcodes.ALOAD, originalSetIndex);
		if (getMethodReturnType().getClassName().endsWith(SET_SUFFIX)) {
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, collectionType, ADD_ALL_METHOD, ADD_ALL_METHOD_DESC, false);
			mv.visitInsn(Opcodes.POP);
		} else {
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, collectionType, PUT_ALL_METHOD, PUT_ALL_METHOD_DESC, false);
		}

		mv.visitVarInsn(Opcodes.ALOAD, setStoreIndex);
	}
}