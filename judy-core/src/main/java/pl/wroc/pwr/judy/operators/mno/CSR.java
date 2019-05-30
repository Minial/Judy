package pl.wroc.pwr.judy.operators.mno;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;

import java.util.ListIterator;

/**
 * Collection synchronization removal.
 * <p/>
 * Remove call of synchronizing method and replace it by given argument.
 *
 * @author mnowak
 */
public class CSR extends AbstractTreeMethodMutationOperator {

	@Override
	public String getDescription() {
		return "collection synchronization removal";
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
			} else if (insn.getOpcode() == INVOKESTATIC) {
				MethodInsnNode method = (MethodInsnNode) insn;
				if (isSynchronizing(method)) {
					cm.nextMutationPoint();
					if (cm.shouldMutate()) {
						cm.setMutantLineNumber(lastLineNumber);
						mn.instructions.remove(method);
					}
				}
			}
		}
	}

	private boolean isSynchronizing(MethodInsnNode method) {
		return method.owner.equals("java/util/Collections") && method.name.contains("synchronized");
	}
}
