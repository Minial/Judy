package pl.wroc.pwr.judy.operators.common;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.AbstractTreeMethodMutater;
import pl.wroc.pwr.judy.utils.Accesses;

import java.util.ListIterator;
import java.util.Set;

import static org.objectweb.asm.Type.OBJECT;

/**
 * Abstract base class for mutation operator that change type of local variable
 * to super type, e.g. PPD and PLD operators.
 *
 * @author pmiwaszko
 */
public abstract class AbstractLocalVariableTypeReplacementOperator extends AbstractMutationOperator {

	/**
	 * Check if method is applicable for mutation.
	 */
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith(JAVA);
	}

	/**
	 * Check if local variable is applicable for mutation.
	 */
	protected abstract boolean checkVariable(LocalVariableNode variable, MethodNode mn, int i, String className);

	protected String getType(LocalVariableNode variable) {
		return Type.getType(variable.desc).getInternalName();
	}

	protected String getSuperType(LocalVariableNode variable) {
		return getEnvironment().getClassInfo(getType(variable)).getSuperClassName();
	}

	protected boolean hasSuperType(String varType, String varSuperType) {
		return !"java/lang/Object".equals(varType) && !"java/lang/Object".equals(varSuperType);
	}

	protected boolean isMethodArgument(MethodNode mn, String className, int i) {
		// this method assumes that 'this' and method's arguments are first
		// local variables
		int length = Type.getArgumentTypes(mn.desc).length;
		if (INIT.equals(mn.name) && className.contains("$")) {
			length--;
		}
		return i > 0 && i <= length;
	}

	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutantIndex);
	}

	/**
	 * Custom class mutater.
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
				methodVisitor = new MethodMutater(methodVisitor, access, name, desc, signature, exceptions);
			}
			return methodVisitor;
		}

		private class MethodMutater extends AbstractTreeMethodMutater {

			public MethodMutater(MethodVisitor mv, int access, String name, String desc, String signature,
								 String[] exceptions) {
				super(mv, access, name, desc, signature, exceptions);
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void mutate(MethodNode method) throws AnalyzerException {
				Analyzer analyzer = new Analyzer(new SourceInterpreter());
				Frame[] frames = analyzer.analyze(className, method);

				int i = 0;
				for (Object object : method.localVariables) {
					LocalVariableNode variable = (LocalVariableNode) object;

					if (OBJECT == Type.getType(variable.desc).getSort()) {
						String varType = getType(variable);
						String varSuperType = getSuperType(variable);

						if (checkVariable(variable, method, i, className)) {
							nextMutationPoint();
							if (shouldMutate()) {
								int varIndex = variable.index;

								// change local variable type
								variable.desc = "L" + varSuperType + ";";

								// iterate over instructions; when GETFIELD,
								// PUTFIELD or INVOKEVIRTUAL is found
								// and the object reference was loaded from a
								// local variable associated with a parameter
								// replace instruction with call to variable
								// super class.

								ListIterator<?> iter = method.instructions.iterator();
								while (iter.hasNext()) {
									AbstractInsnNode inst = (AbstractInsnNode) iter.next();
									if (inst.getType() == AbstractInsnNode.LINE) {
										lastLineNumber = ((LineNumberNode) inst).line;
									} else if (inst.getOpcode() == PUTFIELD || inst.getOpcode() == GETFIELD) {
										FieldInsnNode node = (FieldInsnNode) inst;
										if (node.owner.equals(varType)) {
											Frame frame = frames[method.instructions.indexOf(node)];
											int stackIndex = frame.getStackSize() - 1;
											if (inst.getOpcode() == PUTFIELD) {
												stackIndex--; // pop argument
												// reference
											}
											SourceValue stackValue = (SourceValue) frame.getStack(stackIndex); // object
											// reference
											Set<AbstractInsnNode> insns = stackValue.insns; // instructions
											// that
											// can
											// produce
											// this
											// value
											for (AbstractInsnNode n : insns) {
												if (n.getOpcode() == ALOAD) {
													VarInsnNode varNode = (VarInsnNode) n;
													if (varNode.var == varIndex) {
														// use a field form
														// superclass
														node.owner = varSuperType;
														setMutantLineNumber(lastLineNumber);
													}
												}
											}
										}
									} else if (inst.getOpcode() == INVOKEVIRTUAL) {
										MethodInsnNode node = (MethodInsnNode) inst;
										if (node.owner.equals(varType)) {
											Frame frame = frames[method.instructions.indexOf(node)];
											int stackIndex = frame.getStackSize() - 1;
											stackIndex -= Type.getArgumentTypes(node.desc).length; // pop
											// arguments
											// references
											SourceValue stackValue = (SourceValue) frame.getStack(stackIndex); // object
											// reference
											Set<AbstractInsnNode> insns = stackValue.insns; // instructions
											// that
											// can
											// produce
											// this
											// value
											for (AbstractInsnNode n : insns) {
												if (n.getOpcode() == ALOAD) {
													VarInsnNode varNode = (VarInsnNode) n;
													if (varNode.var == varIndex) {
														// change owner
														node.owner = varSuperType;
														setMutantLineNumber(lastLineNumber);
													}
												}
											}
										}
									}
								}
							}
						}
					}

					i++;
				}
			}

			@Override
			public int getLastLineNumber() {
				return lastLineNumber;
			}

			private int lastLineNumber;

		}
	}
}
