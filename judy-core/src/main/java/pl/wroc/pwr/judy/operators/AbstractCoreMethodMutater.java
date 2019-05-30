package pl.wroc.pwr.judy.operators;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * Abstract base class for all method mutaters.
 *
 * @author pmiwaszko
 */
public abstract class AbstractCoreMethodMutater extends AbstractMethodMutater {
	/**
	 * <code>AbstractCoreMethodMutater</code> constructor.
	 */
	public AbstractCoreMethodMutater(final MethodVisitor mv) {
		super(mv);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return mv.visitAnnotationDefault();
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
		return mv.visitAnnotation(desc, visible);
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(final int parameter, final String desc, final boolean visible) {
		return mv.visitParameterAnnotation(parameter, desc, visible);
	}

	@Override
	public void visitAttribute(final Attribute attr) {
		mv.visitAttribute(attr);
	}

	@Override
	public void visitCode() {
		mv.visitCode();
	}

	@Override
	public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack,
						   final Object[] stack) {
	}

	@Override
	public void visitInsn(final int opcode) {
		mv.visitInsn(opcode);
	}

	@Override
	public void visitIntInsn(final int opcode, final int operand) {
		mv.visitIntInsn(opcode, operand);
	}

	@Override
	public void visitVarInsn(final int opcode, final int var) {
		mv.visitVarInsn(opcode, var);
	}

	@Override
	public void visitTypeInsn(final int opcode, final String type) {
		mv.visitTypeInsn(opcode, type);
	}

	@Override
	public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
		mv.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, boolean itf) {
		mv.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	@Override
	public void visitJumpInsn(final int opcode, final Label label) {
		mv.visitJumpInsn(opcode, label);
	}

	@Override
	public void visitLabel(final Label label) {
		mv.visitLabel(label);
	}

	@Override
	public void visitLdcInsn(final Object cst) {
		mv.visitLdcInsn(cst);
	}

	@Override
	public void visitIincInsn(final int var, final int increment) {
		mv.visitIincInsn(var, increment);
	}

	@Override
	public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
		mv.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
		mv.visitLookupSwitchInsn(dflt, keys, labels);
	}

	@Override
	public void visitMultiANewArrayInsn(final String desc, final int dims) {
		mv.visitMultiANewArrayInsn(desc, dims);
	}

	@Override
	public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
		mv.visitTryCatchBlock(start, end, handler, type);
	}

	@Override
	public void visitLocalVariable(final String name, final String desc, final String signature, final Label start,
								   final Label end, final int index) {
		mv.visitLocalVariable(name, desc, signature, start, end, index);
	}

	@Override
	public void visitLineNumber(final int line, final Label start) {
		lastLineNumber = line;
		mv.visitLineNumber(line, start);
	}

	@Override
	public void visitMaxs(final int maxStack, final int maxLocals) {
		mv.visitMaxs(maxStack, maxLocals);
	}

	@Override
	public void visitEnd() {
		mv.visitEnd();
	}
}
