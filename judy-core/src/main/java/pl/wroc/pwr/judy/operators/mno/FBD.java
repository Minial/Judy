package pl.wroc.pwr.judy.operators.mno;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Break statements removal.
 * <p/>
 * Operator relies on removing break statements occured in switch block. As a
 * result of that, all the instructions will be executing till switch statement
 * ends. Even after the search variant was found.
 *
 * @author mnowak
 */

public class FBD extends AbstractTreeMethodMutationOperator {

	@Override
	public String getDescription() {
		return "break statements removal from switch";
	}

	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith("java");
	}

	@Override
	protected void mutate(MethodNode mn, AbstractClassMutater cm) throws Exception {
		int lastLineNumber = 0;
		int startSwitchLine = 0;
		boolean isSwitchInsn = false;
		LabelNode dflt = null;
		List<JumpInsnNode> gotosList = null;
		ListIterator<?> iter = mn.instructions.iterator();

		while (iter.hasNext()) {
			AbstractInsnNode insn = (AbstractInsnNode) iter.next();

			if (insn.getType() == AbstractInsnNode.LINE) {
				lastLineNumber = ((LineNumberNode) insn).line;
			} else if (insn.getOpcode() == TABLESWITCH) {
				gotosList = new ArrayList<>();
				startSwitchLine = lastLineNumber;
				isSwitchInsn = true;
				TableSwitchInsnNode switchInsn = (TableSwitchInsnNode) insn;
				dflt = switchInsn.dflt;
			} else if (dflt != null && dflt == insn) {
				isSwitchInsn = false;
				cm.nextMutationPoint();
				if (cm.shouldMutate()) {
					cm.setMutantLineNumber(startSwitchLine);
					for (JumpInsnNode jin : gotosList) {
						mn.instructions.remove(jin);
					}
				}
			} else if (insn instanceof JumpInsnNode) {
				if (isSwitchInsn) {
					JumpInsnNode gotoInsn = (JumpInsnNode) insn;
					if (isIfStatement(gotoInsn.getOpcode())) {
						gotosList.add(gotoInsn);
					}
				}
			}
		}
	}

	private boolean isIfStatement(int opcode) {
		return opcode != Opcodes.IFEQ && opcode != Opcodes.IFNE && opcode != Opcodes.IFLT && opcode != Opcodes.IFGE
				&& opcode != Opcodes.IFGT && opcode != Opcodes.IFLE && opcode != Opcodes.IFNULL
				&& opcode != Opcodes.IFNONNULL;
	}
}