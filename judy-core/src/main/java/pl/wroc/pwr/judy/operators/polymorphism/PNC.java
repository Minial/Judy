package pl.wroc.pwr.judy.operators.polymorphism;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.AbstractTreeMethodMutater;
import pl.wroc.pwr.judy.utils.Cache;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.google.common.collect.Lists.newLinkedList;
import static org.objectweb.asm.tree.AbstractInsnNode.LINE;

/**
 * Call <code>new</code> with child class type.
 * <p/>
 * Changes the type of an object instantiated with <code>new</code> to it's
 * child class type.
 *
 * @author pmiwaszko
 */
public class PNC extends AbstractMutationOperator {
	private final Map<String, List<String>> cache = Collections.synchronizedMap(new Cache<String, List<String>>());

	@Override
	public String getDescription() {
		return "call new with child class type";
	}

	/**
	 * Check if method is applicable for mutation.
	 */
	protected boolean checkMethod(int access, String className, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith(JAVA);
	}

	protected int countMethodInsnReplacements(MethodInsnNode node) {
		return getMethodInsnReplacemnts(node).size();
	}

	protected String getMethodInsnReplacement(MethodInsnNode node, int mutantIndex) {
		return getMethodInsnReplacemnts(node).get(mutantIndex);
	}

	private List<String> getMethodInsnReplacemnts(MethodInsnNode methodInst) {
		List<String> types = cache.get(methodInst.owner);
		if (types == null) {
			types = newLinkedList();
			IEnvironment env = getEnvironment();

			List<String> subtypes = env.getDirectSubclasses(methodInst.owner);
			for (String subtype : subtypes) {
				if (isReplacement(methodInst.desc, subtype)) {
					types.add(subtype);
				}
			}
		}
		cache.put(methodInst.owner, types);
		return types;
	}

	private boolean isReplacement(String desc, String replacement) {
		IEnvironment env = getEnvironment();
		// if subtype has the same constructor
		for (IMethodInfo m : env.getClassInfo(replacement).getDeclaredMethods()) {
			if (INIT.equals(m.getName()) && desc.equals(m.getDesc())) {
				return true;
			}
		}
		return false;

		// in MuJava there was an additional check to see if
		// a subtype has some overriding methods to eliminate
		// equivalent mutants - but what about e.g. different
		// field initialization in subtype constructor?
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new ClassMutater(classWriter, mutationPointIndex, mutantIndex);
	}

	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new ClassMutater();
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
			if (checkMethod(access, className, name, desc, signature, exceptions)) {
				methodVisitor = new MethodMutater(methodVisitor, access, name, desc, signature, exceptions);
			}
			return methodVisitor;
		}

		/**
		 * Custom method mutater based on ASM tree-API.
		 */
		private class MethodMutater extends AbstractTreeMethodMutater {

			public MethodMutater(MethodVisitor mv, int access, String name, String desc, String signature,
								 String[] exceptions) {
				super(mv, access, name, desc, signature, exceptions);
			}

			@Override
			protected void mutate(MethodNode mn) {
				TypeInsnNode newInstruction = null;
				int lastLineNumber = 0;

				ListIterator<?> iterator = mn.instructions.iterator();
				while (iterator.hasNext()) {
					AbstractInsnNode instruction = (AbstractInsnNode) iterator.next();

					if (isLineInstruction(instruction)) {
						lastLineNumber = ((LineNumberNode) instruction).line;
					} else if (isNewInstruction(instruction)) {
						newInstruction = (TypeInsnNode) instruction;
					} else if (isInvokeSpecialInstruction(instruction)) {
						if (newInstruction != null) {
							MethodInsnNode methodInstruction = (MethodInsnNode) instruction;
							if (isConstructor(methodInstruction)) {
								if (isCountingMutants()) {
									nextMutationPoint(countMethodInsnReplacements(methodInstruction));
								} else {
									nextMutationPoint();
								}
								if (shouldMutate()) {
									String replacement = getMethodInsnReplacement(methodInstruction, getMutantIndex());
									if (replacement != null) {
										setMutantLineNumber(lastLineNumber);

										// mutation
										newInstruction.desc = replacement;
										methodInstruction.owner = replacement;
									} else {
										throw new IllegalStateException(getClass().getSimpleName()
												+ ": Replacement cannot be null!");
									}
								}
								newInstruction = null; // last seen 'new' was
								// "consumed"
							}
						}
					}
				}
			}

			private boolean isConstructor(MethodInsnNode methodInstruction) {
				return INIT.equals(methodInstruction.name);
			}

			private boolean isInvokeSpecialInstruction(AbstractInsnNode instruction) {
				return instruction.getOpcode() == INVOKESPECIAL;
			}

			private boolean isNewInstruction(AbstractInsnNode instruction) {
				return instruction.getOpcode() == NEW;
			}

			private boolean isLineInstruction(AbstractInsnNode instruction) {
				return instruction.getType() == LINE;
			}

		}
	}

}
