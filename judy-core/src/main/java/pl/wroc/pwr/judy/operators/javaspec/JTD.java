package pl.wroc.pwr.judy.operators.javaspec;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IFieldInfo;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;
import pl.wroc.pwr.judy.utils.Accesses;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * <code>this</code> Keyword Deletion.
 * <p/>
 * Replace occurrences of <code>this.x</code> with <code>x</code> when
 * <code>x</code> is both a parameter and an instance variable.
 *
 * @author pmiwaszko
 */
public class JTD extends AbstractTreeMethodMutationOperator {
	@Override
	public String getDescription() {
		return "delete this keyword when field has the same name as parameter";
	}

	/**
	 * Check if method is applicable for mutation.
	 */
	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		int length = Type.getArgumentTypes(desc).length;
		return !className.startsWith(JAVA) && !Accesses.isStatic(access)
				&& (length > 1 || !(INIT.equals(name) && className.contains("$")) && length > 0);
	}

	@Override
	protected void mutate(MethodNode mn, AbstractClassMutater cm) {
		// collect class fields
		IEnvironment env = getEnvironment();
		IClassInfo info = env.getClassInfo(cm.className);
		Map<String, String> fields = new HashMap<>();
		for (IFieldInfo field : info.getDeclaredFields()) {
			fields.put(field.getName(), field.getDescription());
		}

		// get indexes of local variables compatible with fields
		Map<String, Integer> indexes = new HashMap<>();
		for (Object obj : mn.localVariables.subList(0, Type.getArgumentTypes(mn.desc).length + 1)) {
			LocalVariableNode var = (LocalVariableNode) obj;
			String desc = fields.get(var.name);
			if (desc != null && desc.equals(var.desc)) {
				indexes.put(var.name, var.index);
			}
		}

		int lastLineNumber = 0;

		// iterate over instructions
		ListIterator<?> iter = mn.instructions.iterator();
		while (iter.hasNext()) {
			AbstractInsnNode inst = (AbstractInsnNode) iter.next();
			if (inst.getType() == AbstractInsnNode.LINE) {
				lastLineNumber = ((LineNumberNode) inst).line;
			} else if (inst.getType() == AbstractInsnNode.FIELD_INSN) {
				FieldInsnNode node = (FieldInsnNode) inst;
				int opcode = node.getOpcode();
				String desc = node.desc;
				Integer index = indexes.get(node.name); // read index of local
				// variable
				if (index != null) {
					cm.nextMutationPoint();
					if (cm.shouldMutate()) {
						cm.setMutantLineNumber(lastLineNumber);

						if (opcode == GETFIELD) {
							mn.instructions.insertBefore(inst, new InsnNode(POP)); // pop
							// this
							mn.instructions.insertBefore(inst, new VarInsnNode(Type.getType(desc).getOpcode(ILOAD),
									index));
						} else { // PUTFIELD
							mn.instructions.insertBefore(inst, new VarInsnNode(Type.getType(desc).getOpcode(ISTORE),
									index));
							mn.instructions.insertBefore(inst, new InsnNode(POP)); // pop
							// this
						}
						mn.instructions.remove(inst);
					}
				}
			}
		}
	}
}
