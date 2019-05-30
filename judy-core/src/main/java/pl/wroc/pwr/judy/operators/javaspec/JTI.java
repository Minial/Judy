package pl.wroc.pwr.judy.operators.javaspec;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IFieldInfo;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;
import pl.wroc.pwr.judy.utils.Accesses;

import java.util.ListIterator;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static pl.wroc.pwr.judy.utils.Accesses.isNot;

/**
 * <code>this</code> Keyword Insertion.
 * <p/>
 * Replace occurrences of <code>x</code> with <code>this.x</code> when
 * <code>x</code> is both a parameter and a class field.
 *
 * @author pmiwaszko
 * @author mnegacz
 */
public class JTI extends AbstractTreeMethodMutationOperator {

	private int lastLineNumber;

	@Override
	public String getDescription() {
		return "insert \"this\" keyword when field has the same name as parameter";
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
	protected void mutate(MethodNode method, AbstractClassMutater classMutater) {
		// collect class fields
		IEnvironment env = getEnvironment();
		IClassInfo classInfo = env.getClassInfo(classMutater.className);
		Map<String, String> fields = newHashMap();

		for (IFieldInfo field : classInfo.getDeclaredFields()) {
			if (isNot(field.getAccess(), ACC_SYNTHETIC)) {
				fields.put(field.getName(), field.getDescription());
			}
		}

		// get names of local variables compatible with fields
		Map<Integer, String> names = newHashMap();

		for (int i = 0; i <= Type.getArgumentTypes(method.desc).length && i < method.localVariables.size(); i++) {
			LocalVariableNode localVariable = (LocalVariableNode) method.localVariables.get(i);
			String description = fields.get(localVariable.name);

			if (description != null && description.equals(localVariable.desc)) {
				names.put(localVariable.index, localVariable.name);
			}
		}

		// iterate over instructions
		ListIterator<?> iterator = method.instructions.iterator();
		while (iterator.hasNext()) {
			AbstractInsnNode inst = (AbstractInsnNode) iterator.next();
			if (inst.getType() == AbstractInsnNode.LINE) {
				lastLineNumber = ((LineNumberNode) inst).line;
			} else if (inst.getType() == AbstractInsnNode.VAR_INSN) {
				VarInsnNode node = (VarInsnNode) inst;
				int opcode = node.getOpcode();
				if (opcode >= ILOAD && opcode <= ALOAD || opcode >= ISTORE && opcode <= ASTORE) {
					String name = names.get(node.var); // read name of the
					// variable
					if (name != null && fields.get(name) != null) { // variable
						// exists
						// and have
						// compatible
						// field
						classMutater.nextMutationPoint();
						if (classMutater.shouldMutate()) {
							classMutater.setMutantLineNumber(lastLineNumber);

							method.instructions.insertBefore(inst, new VarInsnNode(ALOAD, 0));
							if (opcode >= ILOAD && opcode <= ALOAD) {
								method.instructions.insertBefore(inst, new FieldInsnNode(GETFIELD,
										classMutater.className, name, fields.get(name)));
							} else { // opcode >= ISTORE && opcode <= ASTORE
								if (opcode == LSTORE || opcode == DSTORE) {
									// ALOAD 0 must be put before the value,
									// PUTFIELD next
									method.instructions.insertBefore(inst, new VarInsnNode(ALOAD, 0)); // dummy
									method.instructions.insertBefore(inst, new InsnNode(DUP2_X2)); // swap2
									method.instructions.insertBefore(inst, new InsnNode(POP2));
									method.instructions.insertBefore(inst, new FieldInsnNode(PUTFIELD,
											classMutater.className, name, fields.get(name)));
									method.instructions.insertBefore(inst, new InsnNode(POP)); // remove
									// dummy
								} else { // opcode = ISTORE, ASTORE or FSTORE
									method.instructions.insertBefore(inst, new InsnNode(SWAP));
									method.instructions.insertBefore(inst, new FieldInsnNode(PUTFIELD,
											classMutater.className, name, fields.get(name)));
								}
							}
							method.instructions.remove(inst);
						}
					}
				}
			}
		}
	}

}
