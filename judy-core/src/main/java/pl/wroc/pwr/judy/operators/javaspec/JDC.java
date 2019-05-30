package pl.wroc.pwr.judy.operators.javaspec;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;

import java.util.ListIterator;

/**
 * Java-supported default constructor creation.
 * <p/>
 * Delete the implemented default constructor.
 * <p/>
 * Note: This operators depends on line numbers information stored in bytecode.
 *
 * @author pmiwaszko
 */
public class JDC extends AbstractTreeMethodMutationOperator {
	@Override
	public String getDescription() {
		return "delete the implemented default constructor";
	}

	/**
	 * Check if method is applicable for mutation.
	 */
	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith(JAVA) && isDefault(className, name, desc);
	}

	private boolean isDefault(String className, String name, String desc) {
		if (INIT.equals(name)) {
			int length = Type.getArgumentTypes(desc).length;
			if (length == 0 || className.contains("$") && length == 1) {
				IEnvironment env = getEnvironment();
				int count = 0;
				for (IMethodInfo methods : env.getClassInfo(className).getDeclaredMethods()) {
					if (methods.getName().equals(INIT)) {
						count++;
						if (count > 1) {
							break;
						}
					}
				}
				if (count == 1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void mutate(MethodNode mn, AbstractClassMutater cm) {
		int lastLineNumber = 0;
		int startLineNumber = 0;
		int endLineNumber = 0;
		boolean seenInvokeSpecial = false;

		// read last line number
		ListIterator<?> iter = mn.instructions.iterator(mn.instructions.size());
		while (iter.hasPrevious()) {
			AbstractInsnNode inst = (AbstractInsnNode) iter.previous();
			if (inst.getType() == AbstractInsnNode.LINE) {
				endLineNumber = ((LineNumberNode) inst).line;
				break;
			}
		}

		// iterate over instructions
		iter = mn.instructions.iterator();
		InsnList newInstructions = new InsnList();

		// after finding INVOKESPACIAL everything between this and
		// the last line number is removed; this way fields'
		// initializers are preserved, since they are located outside
		// the constuctor's body

		while (iter.hasNext()) {
			AbstractInsnNode inst = (AbstractInsnNode) iter.next();
			newInstructions.add(inst);
			if (inst.getType() == AbstractInsnNode.LINE) {
				lastLineNumber = ((LineNumberNode) inst).line;
				if (seenInvokeSpecial) {
					if (lastLineNumber > startLineNumber && lastLineNumber < endLineNumber) {
						cm.nextMutationPoint();
						if (cm.shouldMutate()) {
							cm.setMutantLineNumber(lastLineNumber);
							// remove everything after this point
							newInstructions.add(new InsnNode(RETURN));
							mn.instructions = newInstructions;
						}
						break;
					}
				}
			} else if (inst.getOpcode() == INVOKESPECIAL && !seenInvokeSpecial) {
				seenInvokeSpecial = true;
				startLineNumber = lastLineNumber;
				if (cm.isCountingMutants()) {
					if (endLineNumber < startLineNumber) {
						// something strange is happening so it is
						// better to leave
						return;
					}
				}
			}
		}
	}
}
