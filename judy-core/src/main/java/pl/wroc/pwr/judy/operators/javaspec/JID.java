package pl.wroc.pwr.judy.operators.javaspec;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.AbstractTreeClassMutater;
import pl.wroc.pwr.judy.utils.Cache;

import java.util.*;

/**
 * Field Initialization Deletion.
 * <p/>
 * Removes the initialization of field in the variable declaration so that
 * member variables are initialized to the appropriate default values of Java.
 * <p/>
 * Note: This operators depends on line numbers information stored in bytecode.
 *
 * @author pmiwaszko
 */
public class JID extends AbstractMutationOperator {
	private final Map<String, FieldInsns> cache = Collections.synchronizedMap(new Cache<String, FieldInsns>());

	@Override
	public String getDescription() {
		return "delete field initialization";
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
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cn.visitMethod(access, name, desc, signature, exceptions);
			if (INIT.equals(name)) {
				constructors.add((MethodNode) cn.methods.get(cn.methods.size() - 1));
			}
			return mv;
		}

		@Override
		protected void mutateClass(ClassNode cn) {
			if (constructors.size() == 1) {
				handleSingleConstructor(cn);
			} else {
				handleMultipleConstructors(cn);
			}
		}

		private void handleSingleConstructor(ClassNode cn) {
			MethodNode constructor = constructors.get(0);

			int firstLineNumberInNextMethod = 0;
			int lastLineNumberInConstructor = 0;
			int thisLineNumberInConstructor = -1;

			// get first line number in next method
			int index = cn.methods.indexOf(constructor);
			if (index + 1 < cn.methods.size()) {
				MethodNode nextMethod = (MethodNode) cn.methods.get(index + 1);
				ListIterator<?> iter = nextMethod.instructions.iterator();
				while (iter.hasNext()) {
					AbstractInsnNode inst = (AbstractInsnNode) iter.next();
					if (inst.getType() == AbstractInsnNode.LINE) {
						firstLineNumberInNextMethod = ((LineNumberNode) inst).line;
						break;
					}
				}
			}

			// get last line number in the constructor
			ListIterator<?> iter = constructor.instructions.iterator(constructor.instructions.size());
			while (iter.hasPrevious()) {
				AbstractInsnNode inst = (AbstractInsnNode) iter.previous();
				if (inst.getType() == AbstractInsnNode.LINE) {
					lastLineNumberInConstructor = ((LineNumberNode) inst).line;
					break;
				}
			}

			int lastLineNumber = 0;
			boolean seenInvokeSpecial = false;

			int startLineNumber = 0;
			int endLineNumber = 0;

			// iterate over instructions, if PUTFIELD instruction is found
			// outside
			// the constructor's scope remove it and pop appropriate values from
			// stack
			iter = constructor.instructions.iterator();
			while (iter.hasNext()) {
				AbstractInsnNode insn = (AbstractInsnNode) iter.next();
				if (insn.getType() == AbstractInsnNode.LINE) {
					lastLineNumber = ((LineNumberNode) insn).line;
				} else if (insn.getOpcode() == INVOKESPECIAL && !seenInvokeSpecial) {
					seenInvokeSpecial = true;
					// set start line number
					startLineNumber = lastLineNumber;

					// set end line number
					endLineNumber = lastLineNumberInConstructor;
					if (firstLineNumberInNextMethod < endLineNumber && firstLineNumberInNextMethod > startLineNumber) {
						endLineNumber = firstLineNumberInNextMethod;
					}
					if (thisLineNumberInConstructor >= startLineNumber) {
						endLineNumber = thisLineNumberInConstructor; // works
						// for
						// inner
						// classes
					}

					if (isCountingMutants()) {
						if (endLineNumber < startLineNumber) {
							// something strange is happening so it is better to
							// leave
							return;
						}
					}
				} else if (insn.getOpcode() == PUTFIELD && !seenInvokeSpecial) { // this$0
					// handling
					FieldInsnNode node = (FieldInsnNode) insn;
					if (node.name.startsWith(THIS) && node.owner.equals(className)) {
						thisLineNumberInConstructor = lastLineNumber;
					}
				} else if (insn.getOpcode() == PUTFIELD && seenInvokeSpecial) {
					if (lastLineNumber < startLineNumber || lastLineNumber > endLineNumber) {
						if (!isDefaultAssignment((FieldInsnNode) insn)) {
							nextMutationPoint();
							if (shouldMutate()) {
								new FieldInsn(lastLineNumber, constructor, (FieldInsnNode) insn).remove(constructors);
								setMutantLineNumber(lastLineNumber);
							}
						}
					}
				}
			}
		}

		private void handleMultipleConstructors(ClassNode cn) {
			FieldInsns insns = cache.get(className);
			if (insns == null) {
				// collect PUTFIELD instructions
				insns = new FieldInsns(constructors, className);
				for (MethodNode constructor : constructors) {
					int lastLineNumber = 0;
					ListIterator<?> iter = constructor.instructions.iterator();
					while (iter.hasNext()) {
						AbstractInsnNode insn = (AbstractInsnNode) iter.next();
						if (insn.getType() == AbstractInsnNode.LINE) {
							lastLineNumber = ((LineNumberNode) insn).line;
						} else if (insn.getOpcode() == PUTFIELD) {
							FieldInsnNode node = (FieldInsnNode) insn;
							if (!node.name.startsWith(THIS) && lastLineNumber != 0) {
								if (!isDefaultAssignment(node)) {
									insns.add(lastLineNumber, constructor, node);
								}
							}
						}
					}
				}
			}
			cache.put(className, insns);

			// mutate
			for (int i = 0; i < insns.count(); i++) {
				nextMutationPoint();
				if (shouldMutate()) {
					for (FieldInsn insn : insns.get(i)) {
						insn.remove(constructors);
					}
					setMutantLineNumber(insns.getLineNumber(i));
				}
			}
		}

		private final List<MethodNode> constructors = new LinkedList<>();
	}

	/**
	 * Check if given field is assigned with default value.
	 */
	private boolean isDefaultAssignment(FieldInsnNode node) {
		switch (node.getPrevious().getOpcode()) {
			case ICONST_0: // intentional fall-trough cases
			case FCONST_0:
			case LCONST_0:
			case DCONST_0:
			case ACONST_NULL:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Class representing collection of {@link FieldInsn} objects.
	 */
	private class FieldInsns {
		public FieldInsns(List<MethodNode> constructors, String className) {
			int count = 0;
			// count constructors without explicit call of other constructors
			// via this(...);
			for (MethodNode constructor : constructors) {
				ListIterator<?> iter = constructor.instructions.iterator();
				while (iter.hasNext()) {
					AbstractInsnNode insn = (AbstractInsnNode) iter.next();
					if (insn.getOpcode() == INVOKESPECIAL) {
						MethodInsnNode node = (MethodInsnNode) insn;
						if (!node.owner.equals(className)) {
							count++;
						}
					}
				}
			}
			constructorCount = count;
		}

		public void add(int lineNumber, MethodNode constructor, FieldInsnNode node) {
			if (!compacted) {
				String key = lineNumber + node.name + node.owner;
				if (!map.containsKey(key)) {
					map.put(key, new HashMap<String, FieldInsn>());
				}
				map.get(key).put(constructor.desc, new FieldInsn(lineNumber, constructor, node));
			}
		}

		/**
		 * Remove non-applicable entries. This method must not be called before
		 * {@link #add(int, MethodNode, FieldInsnNode)} method.
		 */
		public void compact() {
			for (HashMap<String, FieldInsn> insns : map.values()) {
				if (insns.size() == constructorCount) {
					list.add(insns.values());
				}
			}
			map = null;
			compacted = true;
		}

		public int count() {
			if (!compacted) {
				compact();
			}
			return list.size();
		}

		/**
		 * Get set of {@link FieldInsn} objects, representing PUTFIELD
		 * instructions. Each instruction sets the same field but each comes
		 * from different constructor.
		 *
		 * @param i index that corresponds to one of class fields
		 */
		public Collection<FieldInsn> get(int i) {
			return list.get(i);
		}

		public int getLineNumber(int i) {
			Iterator<FieldInsn> iter = list.get(i).iterator();
			if (iter.hasNext()) {
				return iter.next().getLineNumber();
			}
			return 0;
		}

		// mapping of fields identifiers to the PUTFIELD instructions (grouped
		// by constructor desc)
		private LinkedHashMap<String, HashMap<String, FieldInsn>> map = new LinkedHashMap<>();
		private final List<Collection<FieldInsn>> list = new ArrayList<>();

		private final int constructorCount;
		private boolean compacted;
	}

	/**
	 * Representation of PUTFIELD instruction in constructor.
	 */
	class FieldInsn {
		public FieldInsn(int lineNumber, MethodNode constructor, FieldInsnNode node) {
			this.lineNumber = lineNumber;
			constructorDesc = constructor.desc;
			nodeIndex = constructor.instructions.indexOf(node);
		}

		public void remove(List<MethodNode> constructors) {
			for (MethodNode constructor : constructors) {
				if (constructor.desc.equals(constructorDesc)) {
					FieldInsnNode node = (FieldInsnNode) constructor.instructions.get(nodeIndex);
					int pop = POP;
					if (Type.getType(node.desc).getSize() == 2) {
						pop = POP2; // for long and double values
					}
					constructor.instructions.insertBefore(node, new InsnNode(pop)); // pop
					// value
					constructor.instructions.insertBefore(node, new InsnNode(POP)); // pop
					// object
					// reference
					constructor.instructions.remove(node);
					return;
				}
			}
		}

		public int getLineNumber() {
			return lineNumber;
		}

		private JID getOuterType() {
			return JID.this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + (constructorDesc == null ? 0 : constructorDesc.hashCode());
			return result;
		}

		/**
		 * This method uses only constructor description to distinguish between
		 * two {@link FieldInsn} objects.
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			FieldInsn other = (FieldInsn) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (constructorDesc == null) {
				if (other.constructorDesc != null) {
					return false;
				}
			} else if (!constructorDesc.equals(other.constructorDesc)) {
				return false;
			}
			return true;
		}

		private final int lineNumber;
		private final String constructorDesc;
		private final int nodeIndex;
	}
}
