package pl.wroc.pwr.judy.operators.javaspec;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;
import pl.wroc.pwr.judy.utils.Accesses;

import java.util.ListIterator;

/**
 * Reference assignment and content assignment replacement.
 * <p/>
 * Replace reference assignment with content assignment (<code>.clone()</code>)
 * and vice-versa.
 *
 * @author pmiwaszko
 */
public class EOA extends AbstractTreeMethodMutationOperator {

	// Operator searches for (a = b) construction,
	// which is two instructions in the row:
	// LOAD & STORE
	// LOAD & PUTFIELD
	// GETFIELD & PUTFIELD
	// GETFIELD & STORE
	// No other constructions in between (including casting) are supported.
	// 'a' type must be super type of 'b' type. 'b' must implement Cloneable
	// and has 'clone' method.

	@Override
	public String getDescription() {
		return "replace reference assignment with content assignment (clone) and vice-versa"; // clone()
	}

	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith("java");
	}

	@Override
	protected void mutate(MethodNode mn, AbstractClassMutater cm) throws Exception {
		int lastLineNumber = 0;

		// iterate over instructions
		ListIterator<?> iter = mn.instructions.iterator();
		while (iter.hasNext()) {
			AbstractInsnNode insn = (AbstractInsnNode) iter.next();
			if (insn.getType() == AbstractInsnNode.LINE) {
				lastLineNumber = ((LineNumberNode) insn).line;
			}
			// insert clone
			else if (insn.getOpcode() == GETFIELD || insn.getOpcode() == ALOAD) {
				AbstractInsnNode nextNode = insn.getNext();
				if (nextNode != null && (nextNode.getOpcode() == PUTFIELD || nextNode.getOpcode() == ASTORE)) {
					// "nextNode" represent the left side of the assignment
					// "insn" represents the right side of the assignment
					String leftType = getType(nextNode, mn);
					String rightType = getType(insn, mn);

					if (rightType != null && leftType != null && isCloneable(rightType)) {
						cm.nextMutationPoint();
						if (cm.shouldMutate()) {
							cm.setMutantLineNumber(lastLineNumber);
							// inserting in reverse order
							mn.instructions.insert(insn, new TypeInsnNode(CHECKCAST, leftType));
							mn.instructions.insert(insn, new MethodInsnNode(INVOKEVIRTUAL, rightType, "clone",
									"()Ljava/lang/Object;", false));
						}
					}
				}
			}
			// remove clone
			else if (insn.getOpcode() == CHECKCAST) {
				AbstractInsnNode prevNode = insn.getPrevious();
				if (prevNode != null && prevNode.getOpcode() == INVOKEVIRTUAL) {
					MethodInsnNode method = (MethodInsnNode) prevNode;
					if ("clone".equals(method.name) && "()Ljava/lang/Object;".equals(method.desc)) {
						cm.nextMutationPoint();
						if (cm.shouldMutate()) {
							cm.setMutantLineNumber(lastLineNumber);
							// remove nodes
							mn.instructions.remove(prevNode);
							mn.instructions.remove(insn);
						}
					}
				}
			}
		}
	}

	private String getType(AbstractInsnNode node, MethodNode mn) {
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
		if (desc != null) {
			return Type.getType(desc).getInternalName();
		}
		return null;
	}

	private boolean isCloneable(String type) {
		if (!type.startsWith("[")) { // no arrays
			IEnvironment env = getEnvironment();
			// TODO EOA: check if type is implementing Cloneable interface
			for (IMethodInfo method : env.getAllMethods(type)) {
				if ("clone".equals(method.getName()) && "()Ljava/lang/Object;".equals(method.getDesc())
						&& Accesses.isPublic(method.getAccess())) {
					return true;
				}
			}
		}
		return false;
	}
}
