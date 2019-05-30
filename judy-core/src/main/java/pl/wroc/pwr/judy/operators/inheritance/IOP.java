package pl.wroc.pwr.judy.operators.inheritance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;
import pl.wroc.pwr.judy.utils.Cache;

import java.util.*;

/**
 * Overridden method calling position change.
 * <p/>
 * Moves calls to overridden method to the first and last statements of the
 * method.
 *
 * @author pmiwaszko
 */
public class IOP extends AbstractTreeMethodMutationOperator {
	private static final int TOP = 0;
	private static final int BOTTOM = 1;
	private static final Logger LOGGER = LogManager.getLogger(IOP.class);

	private final Map<String, List<Integer>> cache = Collections.synchronizedMap(new Cache<String, List<Integer>>());

	@Override
	public String getDescription() {
		return "relocate calls to overridden method";
	}

	/**
	 * Check if method is applicable for mutation.
	 */
	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith(JAVA) && !INIT.equals(name) && Type.getReturnType(desc).getSort() == Type.VOID;
	}

	private int getLocation(int i, String className, MethodNode mn, MethodInsnNode node, AbstractInsnNode lastReturnNode) {
		return getLocations(className, mn, node, lastReturnNode).get(i);
	}

	private int getLocationsCount(String className, MethodNode mn, MethodInsnNode node, AbstractInsnNode lastReturnNode) {
		return getLocations(className, mn, node, lastReturnNode).size();
	}

	@SuppressWarnings("unchecked")
	private List<Integer> getLocations(String className, MethodNode mn, MethodInsnNode node,
									   AbstractInsnNode lastReturnNode) {
		// get index of node to create cache key
		int index = mn.instructions.indexOf(node);

		List<Integer> locations = cache.get(className + index + node.desc);
		if (locations == null) {
			locations = new ArrayList<>(2);
			try {
				Frame[] frames = new Analyzer(new SourceInterpreter()).analyze(className, mn);
				Frame frame = frames[index];

				// get instruction that loaded 'this' reference
				int stackIndex = frame.getStackSize() - 1;
				stackIndex -= Type.getArgumentTypes(node.desc).length; // pop
				// arguments
				SourceValue stackValue = (SourceValue) frame.getStack(stackIndex);
				Set<AbstractInsnNode> insns = stackValue.insns; // instructions
				// that could
				// produce this
				// value

				// see if it was the first of instructions
				boolean found = false;
				for (AbstractInsnNode n : insns) {
					if (getPreviousInstruction(n) == null) {
						found = true;
						break;
					}
				}
				if (!found) {
					locations.add(TOP);
				}
			} catch (AnalyzerException e) {
				LOGGER.debug("AnalyzerExcpetion", e);
			}

			// test if node the last instruction before the last return
			if (getNextInstruction(node) != lastReturnNode) {
				locations.add(BOTTOM);
			}
		}
		cache.put(className + index + node.desc, locations);
		return locations;
	}

	@Override
	protected void mutate(MethodNode mn, AbstractClassMutater cm) throws AnalyzerException {
		// find last return statement
		AbstractInsnNode lastReturnNode = null;
		ListIterator<?> iter = mn.instructions.iterator(mn.instructions.size());
		while (iter.hasPrevious()) {
			AbstractInsnNode insn = (AbstractInsnNode) iter.previous();
			if (insn.getOpcode() >= IRETURN && insn.getOpcode() <= RETURN) {
				lastReturnNode = insn;
				break;
			}
		}

		int lastLineNumber = 0;

		// iterate over instructions
		iter = mn.instructions.iterator();
		while (iter.hasNext()) {
			AbstractInsnNode insn = (AbstractInsnNode) iter.next();
			if (insn.getType() == AbstractInsnNode.LINE) {
				lastLineNumber = ((LineNumberNode) insn).line;
			} else if (insn.getOpcode() == INVOKESPECIAL) {

				// this is simplified version of IOP operator:
				// i.e. it can only pass actual method arguments of void
				// method to overridden call and handles moving calls
				// only to the top and the bottom of the function

				MethodInsnNode node = (MethodInsnNode) insn;
				if (checkMethodInsn(cm.className, mn, node)) {
					if (cm.isCountingMutants()) {
						cm.nextMutationPoint(getLocationsCount(cm.className, mn, node, lastReturnNode));
					} else {
						cm.nextMutationPoint();
						if (cm.shouldMutate()) {
							int location = getLocation(cm.getMutantIndex(), cm.className, mn, node, lastReturnNode);
							removeMethodCall(mn, node); // remove method call
							// together with its
							// arguments

							if (location == TOP) {
								// insert at the beginning
								mn.instructions.insert(createMethodCall(node));
							} else if (location == BOTTOM) {
								// insert before last return
								mn.instructions.insertBefore(lastReturnNode, createMethodCall(node));
							}
							cm.setMutantLineNumber(lastLineNumber);
						}
					}
				}
			}
		}
	}

	/**
	 * Check if node represents an invocation of the overridden method.
	 */
	private boolean checkMethodInsn(String className, MethodNode mn, MethodInsnNode node) {
		String superClassName = getEnvironment().getClassInfo(className).getSuperClassName();
		return node.owner.equals(superClassName) && node.name.equals(mn.name) && node.desc.equals(mn.desc);
	}

	private void removeMethodCall(MethodNode mn, MethodInsnNode node) {
		Type[] argumentsTypes = Type.getArgumentTypes(node.desc);
		for (int i = argumentsTypes.length - 1; i >= 0; i--) {
			Type argumentType = argumentsTypes[i];
			if (argumentType.getSize() == 1) {
				mn.instructions.insertBefore(node, new InsnNode(POP));
			} else {
				mn.instructions.insertBefore(node, new InsnNode(POP2));
			}
		}
		mn.instructions.insertBefore(node, new InsnNode(POP)); // remove this
		mn.instructions.remove(node);
	}

	private InsnList createMethodCall(MethodInsnNode node) {
		InsnList list = new InsnList();
		list.add(new VarInsnNode(ALOAD, 0)); // load this
		int i = 0;
		for (Type type : Type.getArgumentTypes(node.desc)) {
			list.add(new VarInsnNode(type.getOpcode(ILOAD), ++i)); // load
			// argument
		}
		list.add(new MethodInsnNode(INVOKESPECIAL, node.owner, node.name, node.desc));
		return list;
	}

	private AbstractInsnNode getPreviousInstruction(AbstractInsnNode node) {
		AbstractInsnNode prev = node.getPrevious();
		if (prev == null || isInstruction(prev)) {
			return prev;
		}
		return getPreviousInstruction(prev);
	}

	private AbstractInsnNode getNextInstruction(AbstractInsnNode node) {
		AbstractInsnNode next = node.getNext();
		if (next == null || isInstruction(next)) {
			return next;
		}
		return getNextInstruction(next);
	}

	private boolean isInstruction(AbstractInsnNode node) {
		return node.getType() != AbstractInsnNode.LINE && node.getType() != AbstractInsnNode.LABEL
				&& node.getType() != AbstractInsnNode.FRAME;
	}
}
