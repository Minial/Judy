package pl.wroc.pwr.judy.operators.common;

import org.objectweb.asm.*;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreMethodMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.utils.Accesses;

/**
 * Abstract base class for mutation operators that use
 * <code>org.objectweb.asm</code> Core API to mutate method bodies.
 * <p/>
 * Only one mutant per mutation point is supported.
 *
 * @author pmiwaszko
 */
public abstract class AbstractCoreMethodMutaionOperator extends AbstractMutationOperator {
	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutantIndex);
	}

	// extension points

	/**
	 * Check if method is applicable for mutation.
	 */
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith("java");
	}

	protected boolean checkAnnotationDefault(final String className) {
		return false;
	}

	protected boolean checkAnnotation(final String className, final String desc, final boolean visible) {
		return false;
	}

	protected boolean checkParameterAnnotation(final String className, final int parameter, final String desc,
											   final boolean visible) {
		return false;
	}

	protected boolean checkAttribute(final String className, final Attribute attr) {
		return false;
	}

	protected boolean checkCode(final String className) {
		return false;
	}

	protected boolean checkInsn(final String className, final int opcode) {
		return false;
	}

	protected boolean checkIntInsn(final String className, final int opcode, final int operand) {
		return false;
	}

	protected boolean checkVarInsn(final String className, final int opcode, final int var) {
		return false;
	}

	protected boolean checkTypeInsn(final String className, final int opcode, final String type) {
		return false;
	}

	protected boolean checkFieldInsn(final String className, final int opcode, final String owner, final String name,
									 final String desc) {
		return false;
	}

	protected boolean checkMethodInsn(final String className, final int opcode, final String owner, final String name,
									  final String desc) {
		return false;
	}

	protected boolean checkJumpInsn(final String className, final int opcode, final Label label) {
		return false;
	}

	protected boolean checkLabel(final String className, final Label label) {
		return false;
	}

	protected boolean checkLdcInsn(final String className, final Object cst) {
		return false;
	}

	protected boolean checkIincInsn(final String className, final int var, final int increment) {
		return false;
	}

	protected boolean checkTableSwitchInsn(final String className, final int min, final int max, final Label dflt,
										   final Label[] labels) {
		return false;
	}

	protected boolean checkLookupSwitchInsn(final String className, final Label dflt, final int[] keys,
											final Label[] labels) {
		return false;
	}

	protected boolean checkMultiANewArrayInsn(final String className, final String desc, final int dims) {
		return false;
	}

	protected boolean checkTryCatchBlock(final String className, final Label start, final Label end,
										 final Label handler, final String type) {
		return false;
	}

	protected boolean checkLocalVariable(final String className, final String name, final String desc,
										 final String signature, final Label start, final Label end, final int index) {
		return false;
	}

	protected boolean checkEnd(final String className) {
		return false;
	}

	protected AnnotationVisitor mutateAnnotationDefault(final MethodVisitor mv, final String className) {
		return mv.visitAnnotationDefault();
	}

	protected AnnotationVisitor mutateAnnotation(final MethodVisitor mv, final String className, final String desc,
												 final boolean visible) {
		return mv.visitAnnotation(desc, visible);
	}

	protected AnnotationVisitor mutateParameterAnnotation(final MethodVisitor mv, final String className,
														  final int parameter, final String desc, final boolean visible) {
		return mv.visitParameterAnnotation(parameter, desc, visible);
	}

	protected void mutateAttribute(final MethodVisitor mv, final String className, final Attribute attr) {
		mv.visitAttribute(attr);
	}

	protected void mutateCode(final MethodVisitor mv, final String className) {
		mv.visitCode();
	}

	protected void mutateInsn(final MethodVisitor mv, final String className, final int opcode) {
		mv.visitInsn(opcode);
	}

	protected void mutateIntInsn(final MethodVisitor mv, final String className, final int opcode, final int operand) {
		mv.visitIntInsn(opcode, operand);
	}

	protected void mutateVarInsn(final MethodVisitor mv, final String className, final int opcode, final int var) {
		mv.visitVarInsn(opcode, var);
	}

	protected void mutateTypeInsn(final MethodVisitor mv, final String className, final int opcode, final String type) {
		mv.visitTypeInsn(opcode, type);
	}

	protected void mutateFieldInsn(final MethodVisitor mv, final String className, final int opcode,
								   final String owner, final String name, final String desc) {
		mv.visitFieldInsn(opcode, owner, name, desc);
	}

	protected void mutateMethodInsn(final MethodVisitor mv, final String className, final int opcode,
									final String owner, final String name, final String desc, boolean itf) {
		mv.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	protected void mutateJumpInsn(final MethodVisitor mv, final String className, final int opcode, final Label label) {
		mv.visitJumpInsn(opcode, label);
	}

	protected void mutateLabel(final MethodVisitor mv, final String className, final Label label) {
		mv.visitLabel(label);
	}

	protected void mutateLdcInsn(final MethodVisitor mv, final String className, final Object cst) {
		mv.visitLdcInsn(cst);
	}

	protected void mutateIincInsn(final MethodVisitor mv, final String className, final int var, final int increment) {
		mv.visitIincInsn(var, increment);
	}

	protected void mutateTableSwitchInsn(final MethodVisitor mv, final String className, final int min, final int max,
										 final Label dflt, final Label[] labels) {
		mv.visitTableSwitchInsn(min, max, dflt, labels);
	}

	protected void mutateLookupSwitchInsn(final MethodVisitor mv, final String className, final Label dflt,
										  final int[] keys, final Label[] labels) {
		mv.visitLookupSwitchInsn(dflt, keys, labels);
	}

	protected void mutateMultiANewArrayInsn(final MethodVisitor mv, final String className, final String desc,
											final int dims) {
		mv.visitMultiANewArrayInsn(desc, dims);
	}

	protected void mutateTryCatchBlock(final MethodVisitor mv, final String className, final Label start,
									   final Label end, final Label handler, final String type) {
		mv.visitTryCatchBlock(start, end, handler, type);
	}

	protected void mutateLocalVariable(final MethodVisitor mv, final String className, final String name,
									   final String desc, final String signature, final Label start, final Label end, final int index) {
		mv.visitLocalVariable(name, desc, signature, start, end, index);
	}

	protected void mutateEnd(final MethodVisitor mv, final String className) {
		mv.visitEnd();
	}

	/**
	 * Custom class mutater for modifying method bodies.
	 */
	private class ClassMutater extends AbstractCoreClassMutater {
		/**
		 * Constructs new class mutater object for mutating classes.
		 */
		public ClassMutater(ClassVisitor cv, int mutationPointIndex, int mutantIndex) {
			super(cv, mutationPointIndex, mutantIndex);
		}

		/**
		 * Constructs new class mutater object for counting mutation points.
		 */
		public ClassMutater() {
			super();
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
			if (Accesses.is(ACC_BRIDGE, access) || Accesses.is(ACC_SYNTHETIC, access)) {
				return methodVisitor;
			}
			if (checkMethod(className, access, name, desc, signature, exceptions)) {
				methodVisitor = new MethodMutater(methodVisitor);
			}
			return methodVisitor;
		}

		/**
		 * Custom method mutater.
		 */
		private class MethodMutater extends AbstractCoreMethodMutater {
			public MethodMutater(final MethodVisitor mv) {
				super(mv);
			}

			@Override
			public AnnotationVisitor visitAnnotationDefault() {
				if (checkAnnotationDefault(className)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						return mutateAnnotationDefault(mv, className);
					}
				}
				return mv.visitAnnotationDefault();
			}

			@Override
			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
				if (checkAnnotation(className, desc, visible)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						return mutateAnnotation(mv, className, desc, visible);
					}
				}
				return mv.visitAnnotation(desc, visible);
			}

			@Override
			public AnnotationVisitor visitParameterAnnotation(final int parameter, final String desc,
															  final boolean visible) {
				if (checkParameterAnnotation(className, parameter, desc, visible)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						return mutateParameterAnnotation(mv, className, parameter, desc, visible);
					}
				}
				return mv.visitParameterAnnotation(parameter, desc, visible);
			}

			@Override
			public void visitAttribute(final Attribute attr) {
				if (checkAttribute(className, attr)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateAttribute(mv, className, attr);
						return;
					}
				}
				mv.visitAttribute(attr);
			}

			@Override
			public void visitCode() {
				if (checkCode(className)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateCode(mv, className);
						return;
					}
				}
				mv.visitCode();
			}

			@Override
			public void visitInsn(final int opcode) {
				if (checkInsn(className, opcode)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateInsn(mv, className, opcode);
						return;
					}
				}
				mv.visitInsn(opcode);
			}

			@Override
			public void visitIntInsn(final int opcode, final int operand) {
				if (checkIntInsn(className, opcode, operand)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateIntInsn(mv, className, opcode, operand);
						return;
					}
				}
				mv.visitIntInsn(opcode, operand);
			}

			@Override
			public void visitVarInsn(final int opcode, final int operand) {
				if (checkVarInsn(className, opcode, operand)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateVarInsn(mv, className, opcode, operand);
						return;
					}
				}
				mv.visitVarInsn(opcode, operand);
			}

			@Override
			public void visitTypeInsn(final int opcode, final String type) {
				if (checkTypeInsn(className, opcode, type)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateTypeInsn(mv, className, opcode, type);
						return;
					}
				}
				mv.visitTypeInsn(opcode, type);
			}

			@Override
			public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
				if (checkFieldInsn(className, opcode, owner, name, desc)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateFieldInsn(mv, className, opcode, owner, name, desc);
						return;
					}
				}
				mv.visitFieldInsn(opcode, owner, name, desc);
			}

			@Override
			public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc,
										boolean itf) {
				if (checkMethodInsn(className, opcode, owner, name, desc)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateMethodInsn(mv, className, opcode, owner, name, desc, false);
						return;
					}
				}
				mv.visitMethodInsn(opcode, owner, name, desc, itf);
			}

			@Override
			public void visitJumpInsn(final int opcode, final Label label) {
				if (checkJumpInsn(className, opcode, label)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateJumpInsn(mv, className, opcode, label);
						return;
					}
				}
				mv.visitJumpInsn(opcode, label);
			}

			@Override
			public void visitLabel(final Label label) {
				if (checkLabel(className, label)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateLabel(mv, className, label);
						return;
					}
				}
				mv.visitLabel(label);
			}

			@Override
			public void visitLdcInsn(final Object cst) {
				if (checkLdcInsn(className, cst)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateLdcInsn(mv, className, cst);
						return;
					}
				}
				mv.visitLdcInsn(cst);
			}

			@Override
			public void visitIincInsn(final int var, final int increment) {
				if (checkIincInsn(className, var, increment)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateIincInsn(mv, className, var, increment);
						return;
					}
				}
				mv.visitIincInsn(var, increment);
			}

			@Override
			public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
				if (checkTableSwitchInsn(className, min, max, dflt, labels)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateTableSwitchInsn(mv, className, min, max, dflt, labels);
						return;
					}
				}
				mv.visitTableSwitchInsn(min, max, dflt, labels);
			}

			@Override
			public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
				if (checkLookupSwitchInsn(className, dflt, keys, labels)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateLookupSwitchInsn(mv, className, dflt, keys, labels);
						return;
					}
				}
				mv.visitLookupSwitchInsn(dflt, keys, labels);
			}

			@Override
			public void visitMultiANewArrayInsn(final String desc, final int dims) {
				if (checkMultiANewArrayInsn(className, desc, dims)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateMultiANewArrayInsn(mv, className, desc, dims);
						return;
					}
				}
				mv.visitMultiANewArrayInsn(desc, dims);
			}

			@Override
			public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
				if (checkTryCatchBlock(className, start, end, handler, type)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateTryCatchBlock(mv, className, start, end, handler, type);
						return;
					}
				}
				mv.visitTryCatchBlock(start, end, handler, type);
			}

			@Override
			public void visitLocalVariable(final String name, final String desc, final String signature,
										   final Label start, final Label end, final int index) {
				if (checkLocalVariable(className, name, desc, signature, start, end, index)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateLocalVariable(mv, className, name, desc, signature, start, end, index);
						return;
					}
				}
				mv.visitLocalVariable(name, desc, signature, start, end, index);
			}

			@Override
			public void visitEnd() {
				if (checkEnd(className)) {
					nextMutationPoint();
					if (shouldMutate()) {
						setMutantLineNumber(lastLineNumber);
						mutateEnd(mv, className);
						return;
					}
				}
				mv.visitEnd();
			}
		}
	}
}
