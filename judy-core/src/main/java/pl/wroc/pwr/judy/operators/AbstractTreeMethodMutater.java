package pl.wroc.pwr.judy.operators;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

/**
 * Abstract base class for all method mutaters.
 *
 * @author pmiwaszko
 */
public abstract class AbstractTreeMethodMutater extends AbstractMethodMutater {
	protected final MethodNode methodNode;

	/**
	 * <code>AbstractTreeMethodMutater</code> constructor.
	 */
	public AbstractTreeMethodMutater(final MethodVisitor mv, int access, String name, String desc, String signature,
									 String[] exceptions) {
		super(mv);
		methodNode = new MethodNode(Opcodes.ASM5, access, name, desc, signature, exceptions);
	}

	/**
	 * Perform mutation.
	 */
	protected abstract void mutate(MethodNode mn) throws Exception;

	/**
	 * Generate error bytecode to mark incorrect or equivalent mutants.
	 */
	protected void generateError() {
		methodNode.instructions.clear();
		methodNode.instructions.add(new TypeInsnNode(Opcodes.NEW, "java/lang/Error"));
		methodNode.instructions.add(new InsnNode(Opcodes.DUP));
		methodNode.instructions.add(new LdcInsnNode("Invalid mutation!"));
		methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Error", "<init>", "(Ljava/lang/String;)V", false));
		methodNode.instructions.add(new InsnNode(Opcodes.ATHROW));
		methodNode.accept(mv);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return methodNode.visitAnnotationDefault();
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
		return methodNode.visitAnnotation(desc, visible);
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(final int parameter, final String desc, final boolean visible) {
		return methodNode.visitParameterAnnotation(parameter, desc, visible);
	}

	@Override
	public void visitAttribute(final Attribute attr) {
		methodNode.visitAttribute(attr);
	}

	@Override
	public void visitCode() {
		methodNode.visitCode();
	}

	@Override
	public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack,
						   final Object[] stack) {
	}

	@Override
	public void visitInsn(final int opcode) {
		methodNode.visitInsn(opcode);
	}

	@Override
	public void visitIntInsn(final int opcode, final int operand) {
		methodNode.visitIntInsn(opcode, operand);
	}

	@Override
	public void visitVarInsn(final int opcode, final int var) {
		methodNode.visitVarInsn(opcode, var);
	}

	@Override
	public void visitTypeInsn(final int opcode, final String type) {
		methodNode.visitTypeInsn(opcode, type);
	}

	@Override
	public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
		methodNode.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, boolean itf) {
		methodNode.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	@Override
	public void visitJumpInsn(final int opcode, final Label label) {
		methodNode.visitJumpInsn(opcode, label);
	}

	@Override
	public void visitLabel(final Label label) {
		methodNode.visitLabel(label);
	}

	@Override
	public void visitLdcInsn(final Object cst) {
		methodNode.visitLdcInsn(cst);
	}

	@Override
	public void visitIincInsn(final int var, final int increment) {
		methodNode.visitIincInsn(var, increment);
	}

	@Override
	public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
		methodNode.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
		methodNode.visitLookupSwitchInsn(dflt, keys, labels);
	}

	@Override
	public void visitMultiANewArrayInsn(final String desc, final int dims) {
		methodNode.visitMultiANewArrayInsn(desc, dims);
	}

	@Override
	public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
		methodNode.visitTryCatchBlock(start, end, handler, type);
	}

	@Override
	public void visitLocalVariable(final String name, final String desc, final String signature, final Label start,
								   final Label end, final int index) {
		methodNode.visitLocalVariable(name, desc, signature, start, end, index);
	}

	@Override
	public void visitLineNumber(final int line, final Label start) {
		methodNode.visitLineNumber(line, start);
	}

	@Override
	public void visitMaxs(final int maxStack, final int maxLocals) {
		methodNode.visitMaxs(maxStack, maxLocals);
	}

	@Override
	public void visitEnd() {
		try {
			mutate(methodNode);
			methodNode.accept(mv);
		} catch (Exception e) {
			generateError();
		}
	}
}
