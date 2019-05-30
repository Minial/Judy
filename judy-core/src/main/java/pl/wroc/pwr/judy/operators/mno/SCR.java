package pl.wroc.pwr.judy.operators.mno;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.common.AbstractTreeMethodMutationOperator;
import pl.wroc.pwr.judy.utils.ClassUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Removal of stream closing.
 * <p/>
 * Change close method call to one of the methods implemented in superclass.
 *
 * @author mnowak
 */
public class SCR extends AbstractTreeMethodMutationOperator {

	private List<String> closedStreams = null;

	@Override
	public String getDescription() {
		return "stream closing removal";
	}

	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		return !className.startsWith("java");
	}

	@Override
	protected void mutate(MethodNode mn, AbstractClassMutater cm) throws Exception {
		int lastLineNumber = 0;
		closedStreams = new ArrayList<>();
		String shouldMutateMethodOwner = "";

		// iterate over instructions
		ListIterator<?> iter = mn.instructions.iterator();
		while (iter.hasNext()) {
			AbstractInsnNode insn = (AbstractInsnNode) iter.next();

			if (insn.getType() == AbstractInsnNode.LINE) {
				lastLineNumber = ((LineNumberNode) insn).line;
			} else if (insn.getOpcode() == INVOKEVIRTUAL) {

				MethodInsnNode method = (MethodInsnNode) insn;
				AbstractInsnNode prevInsn = insn.getPrevious();
				String methodOwner = method.owner;
				String ownerLine = methodOwner + lastLineNumber;

				if (method.name.equals("close") && isStreamSubclass(methodOwner)) {

					if (!closedStreams.contains(ownerLine)) {
						closedStreams.add(ownerLine);
						cm.nextMutationPoint();
						if (cm.shouldMutate()) {
							/*
							 * shouldMutateMethodOwner is necessery when more
							 * than one occurrence of close invokation appeared
							 * in the bytecode. e.g., in finally block one
							 * invokation of close method causes two invokation
							 * in bytecode. That is why we need to know what is
							 * a method owner and even what is a line where it
							 * occurred, because there is possible to have more
							 * than one close for one owner (in more instances).
							 * 
							 * Warning! In case with two instances of the same
							 * owner mutants are generated correctly, but there
							 * are some strange problems with test passing (e.g.
							 * it passed, despite the fact it should not have).
							 */
							shouldMutateMethodOwner = ownerLine;
							cm.setMutantLineNumber(lastLineNumber);

							replaceCloseInvokation(mn, method, prevInsn);
						}
					} else {
						if (shouldMutateMethodOwner.equals(ownerLine)) {
							replaceCloseInvokation(mn, method, prevInsn);
						}
					}
				}
			}
		}
	}

	private void replaceCloseInvokation(MethodNode mn, MethodInsnNode method, AbstractInsnNode prevInsn) {
		Class<?> cl = ClassUtil.getClass(method.owner);
		String newMethodName = getProperMethod(cl);
		mn.instructions.remove(method);
		mn.instructions.insert(prevInsn, new MethodInsnNode(INVOKEVIRTUAL, method.owner, newMethodName, method.desc,
				method.itf));
	}

	private String getProperMethod(Class<?> cl) {
		String newMethodName = "";

		if (isInStream(cl)) {
			newMethodName = "available";
		} else if (isOutStream(cl)) {
			newMethodName = "flush";
		}

		return newMethodName;
	}

	private boolean isStreamSubclass(String owner) {
		Class<?> cl = ClassUtil.getClass(owner);
		return isStreamSubclass(cl);
	}

	private boolean isStreamSubclass(Class<?> cl) {
		return cl != null && (isInStream(cl) || isOutStream(cl));
	}

	private boolean isInStream(Class<?> cl) {
		return InputStream.class.isAssignableFrom(cl) || Reader.class.isAssignableFrom(cl);
	}

	private boolean isOutStream(Class<?> cl) {
		return OutputStream.class.isAssignableFrom(cl) || Writer.class.isAssignableFrom(cl);
	}
}
