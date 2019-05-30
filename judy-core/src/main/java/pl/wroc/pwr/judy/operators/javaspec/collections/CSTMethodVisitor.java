package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractInvokerMethodVisitor;

import java.util.HashSet;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.getObjectType;
import static org.objectweb.asm.Type.getType;

public class CSTMethodVisitor extends AbstractInvokerMethodVisitor {
	private static final String INIT_DESCRIPTION = "(Ljava/util/Collection;)V";
	private static final String ADD_ALL_DESCRIPTION = "(Ljava/util/Collection;)Z";
	private static final String CLEAR_DESCRIPTION = "()V";
	private static final String INIT = "<init>";
	private static final String ADD_ALL_METHOD = "addAll";
	private static final String CLEAR_METHOD = "clear";
	private static final String LINKED_HASH_SET = "java/util/LinkedHashSet";
	private static final String HASH_SET = "java/util/HashSet";

	/**
	 * @param methodVisitor method visitor
	 * @param access        method access opcode
	 * @param description   method description
	 */
	public CSTMethodVisitor(MethodVisitor methodVisitor, int access, String description) {
		super(methodVisitor, access, description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void invoke() {
		final int linkedHashLocalVariableId = newLocal(getObjectType(getLastDescription().substring(2)));
		mv.visitVarInsn(ASTORE, linkedHashLocalVariableId);

		mv.visitTypeInsn(NEW, HASH_SET);
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, linkedHashLocalVariableId);
		mv.visitMethodInsn(INVOKESPECIAL, HASH_SET, INIT, INIT_DESCRIPTION, false);

		final int hashSetLocalVariableId = newLocal(getType(HashSet.class));
		mv.visitVarInsn(ASTORE, hashSetLocalVariableId);

		mv.visitVarInsn(ALOAD, linkedHashLocalVariableId);
		mv.visitMethodInsn(INVOKEVIRTUAL, LINKED_HASH_SET, CLEAR_METHOD, CLEAR_DESCRIPTION, false);

		mv.visitVarInsn(ALOAD, linkedHashLocalVariableId);
		mv.visitVarInsn(ALOAD, hashSetLocalVariableId);
		mv.visitMethodInsn(INVOKEVIRTUAL, LINKED_HASH_SET, ADD_ALL_METHOD, ADD_ALL_DESCRIPTION, false);
		mv.visitInsn(POP);

		mv.visitVarInsn(ALOAD, linkedHashLocalVariableId);
	}

}
