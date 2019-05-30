package pl.wroc.pwr.judy.operators.mno;

import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;
import pl.wroc.pwr.judy.utils.ClassUtil;

import java.util.List;
import java.util.ListIterator;

/**
 * List to set replacement.
 * <p/>
 * Operator puts elements from a List based collection to Set based collection
 * just before return statement. This action causes uniqueness of elements,
 * which was not provided earlier. That is why collection can have less
 * elements.
 *
 * @author mnowak
 */
public class CLR extends AbstractTreeMethodMutationOperator {

	private static final String INIT_DESCRIPTION = "(Ljava/util/Collection;)V";
	private static final String ADD_ALL_DESCRIPTION = "(Ljava/util/Collection;)Z";
	private static final String CLEAR_DESCRIPTION = "()V";
	private static final String INIT = "<init>";
	private static final String ADD_ALL_METHOD = "addAll";
	private static final String CLEAR_METHOD = "clear";
	private static final String LIST = "java/util/List";
	private static final String HASH_SET = "java/util/HashSet";

	@Override
	public String getDescription() {
		return "change list to set that does not allow to duplicated elements";
	}

	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith("java") && isListDerivative(desc);
	}

	public String getReturnTypeAsString(String desc) {
		String temp = desc.split("\\)")[1];
		try {
			temp = temp.substring(1, temp.length() - 1);
		} catch (Exception e) {
			temp = "";
		}

		return temp;
	}

	/**
	 * Check if class implements List or extends other class which it implements
	 */
	public boolean isListDerivative(String desc) {
		String type = getReturnTypeAsString(desc);
		if (!type.equals("") && !type.startsWith("[") && !type.startsWith("L")) { // arrays
			// type
			// not
			// needed
			Class<?> cl = ClassUtil.getClass(type);
			if (cl != null) {
				return isListDerivative(cl);
			}
		}
		return false;
	}

	public boolean isListDerivative(Class<?> cl) {
		return List.class.isAssignableFrom(cl);
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
			} else if (insn.getOpcode() == ARETURN) {
				AbstractInsnNode prevInsn = insn.getPrevious();
				int maxLocals = mn.maxLocals;

				if (prevInsn instanceof VarInsnNode) {
					VarInsnNode varInsnNode = (VarInsnNode) prevInsn;
					int listStoreIndex = varInsnNode.var;

					cm.nextMutationPoint();
					if (cm.shouldMutate()) {
						cm.setMutantLineNumber(lastLineNumber);
						InsnList insnList = prepareInsnBeforeVarReturn(listStoreIndex, maxLocals);
						mn.instructions.insertBefore(varInsnNode, insnList);
					}
				} else if (prevInsn instanceof MethodInsnNode) {
					cm.nextMutationPoint();
					if (cm.shouldMutate()) {
						cm.setMutantLineNumber(lastLineNumber);
						InsnList insnList = prepareInsnListAfterMethodInvoke(maxLocals);
						mn.instructions.insertBefore(insn, insnList);
					}
				}
			}
		}
	}

	private void createNewHashSet(InsnList insnList, int listStoreIndex, int hashSetStoreIndex) {
		insnList.add(new TypeInsnNode(NEW, HASH_SET));
		insnList.add(new InsnNode(DUP));
		insnList.add(new VarInsnNode(ALOAD, listStoreIndex));
		insnList.add(new MethodInsnNode(INVOKESPECIAL, HASH_SET, INIT, INIT_DESCRIPTION, false));
		insnList.add(new VarInsnNode(ASTORE, hashSetStoreIndex));
	}

	private void clearList(InsnList insnList, int listStoreIndex) {
		insnList.add(new VarInsnNode(ALOAD, listStoreIndex));
		insnList.add(new MethodInsnNode(INVOKEVIRTUAL, LIST, CLEAR_METHOD, CLEAR_DESCRIPTION, true));
	}

	private void addAllElmsFromSetToList(InsnList insnList, int listStoreIndex, int hashSetStoreIndex) {
		insnList.add(new VarInsnNode(ALOAD, listStoreIndex));
		insnList.add(new VarInsnNode(ALOAD, hashSetStoreIndex));
		insnList.add(new MethodInsnNode(INVOKEVIRTUAL, LIST, ADD_ALL_METHOD, ADD_ALL_DESCRIPTION, true));
		insnList.add(new InsnNode(POP));
	}

	/**
	 * Preparing list with instructions needed to:
	 * <ul>
	 * <li>create temporary HashSet,</li>
	 * <li>add elements from list to new HashSet,</li>
	 * <li>delete all the elements from list,</li>
	 * <li>add elements from HashSet to list</li>
	 * </ul>
	 */
	private void prepareInsnList(InsnList insnList, int listStoreIndex, int hashSetStoreIndex) {
		createNewHashSet(insnList, listStoreIndex, hashSetStoreIndex);
		clearList(insnList, listStoreIndex);
		addAllElmsFromSetToList(insnList, listStoreIndex, hashSetStoreIndex);
	}

	/**
	 * @param maxLocals
	 * @return list of instructions injected before return, but after method
	 * from return invocation
	 * @see prepareInsnList
	 */
	private InsnList prepareInsnListAfterMethodInvoke(int maxLocals) {
		InsnList insnList = new InsnList();

		int returnMethodResultStoreIndex = maxLocals + 1;
		insnList.add(new VarInsnNode(ASTORE, returnMethodResultStoreIndex));

		int hashSetStoreIndex = returnMethodResultStoreIndex + 1;
		prepareInsnList(insnList, returnMethodResultStoreIndex, hashSetStoreIndex);

		insnList.add(new VarInsnNode(ALOAD, returnMethodResultStoreIndex));

		return insnList;
	}

	/**
	 * @param maxLocals
	 * @return list of instructions injected before method return (of variable)
	 * @see prepareInsnList
	 */
	private InsnList prepareInsnBeforeVarReturn(int listStoreIndex, int maxLocals) {
		InsnList insnList = new InsnList();
		int hashSetStoreIndex = maxLocals + 1;
		prepareInsnList(insnList, listStoreIndex, hashSetStoreIndex);

		return insnList;
	}
}