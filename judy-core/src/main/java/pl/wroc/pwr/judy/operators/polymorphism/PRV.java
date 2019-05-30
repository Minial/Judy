package pl.wroc.pwr.judy.operators.polymorphism;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.AbstractTreeClassMutater;
import pl.wroc.pwr.judy.utils.Cache;

import java.util.*;

/**
 * Reference assignment with other compatible type.
 * <p/>
 * Change the operands of a reference assignment to be assigned to objects of
 * subclasses.
 *
 * @author pmiwaszko
 */
public class PRV extends AbstractMutationOperator {
	private final Map<String, List<AbstractInsnNode>> cache = Collections
			.synchronizedMap(new Cache<String, List<AbstractInsnNode>>());

	@Override
	public String getDescription() {
		return "change operands of reference assignment";
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
	private class ClassMutater extends AbstractTreeClassMutater {
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
		protected void mutateClass(ClassNode cn) {
			if (!cn.name.startsWith(JAVA)) {
				for (Object obj : cn.methods) {
					MethodNode mn = (MethodNode) obj;
					if (!INIT.equals(mn.name)) {
						mutate(mn, cn, ClassMutater.this);
					}
				}
			}
		}
	}

	protected void mutate(MethodNode mn, ClassNode cn, AbstractClassMutater cm) {
		int lastLineNumber = 0;

		// iterate over instructions
		ListIterator<?> iter = mn.instructions.iterator();
		while (iter.hasNext()) {
			AbstractInsnNode insn = (AbstractInsnNode) iter.next();
			if (insn.getType() == AbstractInsnNode.LINE) {
				lastLineNumber = ((LineNumberNode) insn).line;
			} else if (insn.getOpcode() == GETFIELD || insn.getOpcode() == ALOAD) {
				AbstractInsnNode nextNode = insn.getNext();
				if (nextNode != null && (nextNode.getOpcode() == PUTFIELD || nextNode.getOpcode() == ASTORE)) {
					// "nextNode" represent the left side of the assignment
					// "insn" represents the right side of the assignment
					String desc = getDesc(nextNode, mn);
					if (desc != null) {
						if (cm.isCountingMutants()) {
							cm.nextMutationPoint(countVarReplacements(desc, nextNode, insn, mn, cn));
						} else {
							cm.nextMutationPoint();
						}
						if (cm.shouldMutate()) {
							cm.setMutantLineNumber(lastLineNumber);
							AbstractInsnNode replacement = getVarReplacement(desc, nextNode, insn, mn, cn,
									cm.getMutantIndex());
							if (insn.getOpcode() == GETFIELD && replacement.getOpcode() == ALOAD) {
								mn.instructions.insertBefore(insn, new InsnNode(POP)); // pop
								// this
							} else if (insn.getOpcode() == ALOAD && replacement.getOpcode() == GETFIELD) {
								mn.instructions.insertBefore(insn, new VarInsnNode(ALOAD, 0)); // load
								// this
							}
							mn.instructions.insert(insn, replacement);
							mn.instructions.remove(insn);
						}
					}
				}
			}
		}
	}

	protected int countVarReplacements(String descLeft, AbstractInsnNode left, AbstractInsnNode right, MethodNode mn,
									   ClassNode cn) {
		return getVarReplacemnts(descLeft, left, right, mn, cn).size();
	}

	protected AbstractInsnNode getVarReplacement(String descLeft, AbstractInsnNode left, AbstractInsnNode right,
												 MethodNode mn, ClassNode cn, int mutantIndex) {
		return getVarReplacemnts(descLeft, left, right, mn, cn).get(mutantIndex);
	}

	private List<AbstractInsnNode> getVarReplacemnts(String descLeft, AbstractInsnNode left, AbstractInsnNode right,
													 MethodNode mn, ClassNode cn) {
		List<AbstractInsnNode> vars = getCompatibleVars(descLeft, mn, cn);
		// get compatible variables and fields excluding ones that are currently
		// in assignment
		for (Iterator<AbstractInsnNode> iterator = vars.iterator(); iterator.hasNext(); ) {
			AbstractInsnNode replacement = iterator.next();
			if (isSameVar(left, replacement) || isSameVar(right, replacement)) {
				iterator.remove();
			}
		}
		return vars;
	}

	private boolean isSameVar(AbstractInsnNode node, AbstractInsnNode other) {
		if (node.getType() != other.getType()) {
			return false;
		}
		if (node.getType() == AbstractInsnNode.VAR_INSN) {
			return ((VarInsnNode) node).var == ((VarInsnNode) other).var;
		} else if (node.getType() == AbstractInsnNode.FIELD_INSN) {
			FieldInsnNode fnode = (FieldInsnNode) node;
			FieldInsnNode fother = (FieldInsnNode) other;
			return fnode.name.equals(fother.name) && fnode.owner.equals(fother.owner);
		}
		return false;
	}

	private List<AbstractInsnNode> getCompatibleVars(String desc, MethodNode mn, ClassNode cn) {
		List<AbstractInsnNode> vars = cache.get(mn.name + desc);
		if (vars == null) {
			vars = new LinkedList<>();
			// look trough fields
			for (Object obj : cn.fields) {
				FieldNode field = (FieldNode) obj;
				// no this$0
				if (!field.name.startsWith(THIS)) {
					if (isAssignable(desc, field.desc)) {
						vars.add(new FieldInsnNode(GETFIELD, cn.name, field.name, field.desc));
					}
				}
			}
			// look through local variables
			for (Object obj : mn.localVariables) {
				LocalVariableNode var = (LocalVariableNode) obj;
				// no this
				if (var.index != 0) {
					if (isAssignable(desc, var.desc)) {
						vars.add(new VarInsnNode(ALOAD, var.index));
					}
				}
			}
		}
		cache.put(mn.name + desc, vars);
		return new LinkedList<>(vars);
	}

	private boolean isAssignable(String leftDesc, String rightDesc) {
		Type leftType = Type.getType(leftDesc);
		Type rightType = Type.getType(rightDesc);

		// simple types and arrays are not supported
		if (leftType.getSort() == Type.OBJECT && rightType.getSort() == Type.OBJECT) {
			String left = leftType.getInternalName();
			String right = rightType.getInternalName();

			IEnvironment env = getEnvironment();
			// check class name
			if (left.equals(right)) {
				return true;
			}
			// check super classes
			List<String> superClasses = env.getAllSuperclasses(right);
			for (String superClass : superClasses) {
				if (left.equals(superClass)) {
					return true;
				}
			}
			// check interfaces
			IClassInfo info = env.getClassInfo(right);
			for (String interf : info.getInterfaces()) {
				if (left.equals(interf)) {
					return true;
				}
			}
			// check interfaces of super classes
			for (String superClass : superClasses) {
				info = env.getClassInfo(superClass);
				for (String interf : info.getInterfaces()) {
					if (left.equals(interf)) {
						return true;
					}
				}
			}
		}
		// interfaces implemented by other interfaces are not supported
		return false;
	}

	private String getDesc(AbstractInsnNode node, MethodNode mn) {
		String desc = null;
		switch (node.getType()) {
			case AbstractInsnNode.FIELD_INSN:
				FieldInsnNode field = (FieldInsnNode) node;
				desc = field.desc;
				break;
			default: // VAR_INSN
				VarInsnNode var = (VarInsnNode) node;
				for (Object obj : mn.localVariables) {
					LocalVariableNode localVar = (LocalVariableNode) obj;
					if (localVar.index == var.var) {
						desc = localVar.desc;
						break;
					}
				}
				break;
		}
		return desc;
	}
}
