package pl.wroc.pwr.judy.operators.polymorphism;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.operators.common.AbstractMethodCallReplacemetOperator;
import pl.wroc.pwr.judy.utils.Accesses;
import pl.wroc.pwr.judy.utils.Arguments;
import pl.wroc.pwr.judy.utils.Cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Argument of overloading method change.
 * <p/>
 * Change the order or the number of the arguments in method invocations.
 *
 * @author pmiwaszko
 */
public class OAC extends AbstractMethodCallReplacemetOperator<int[]> {
	private static final int INITIAL_CAPACITY = 500;

	private final Map<String, ArrayList<int[]>> cache = Collections
			.synchronizedMap(new Cache<String, ArrayList<int[]>>());

	@Override
	public String getDescription() {
		return "change order or number of arguments in method invocations";
	}

	@Override
	protected boolean checkMethodInsn(String className, int opcode, String owner, String name, String desc) {
		// custom methods with at least one argument
		return opcode != INVOKESTATIC && !owner.startsWith(JAVA) && !INIT.equals(name)
				&& Type.getArgumentTypes(desc).length > 0;
	}

	@Override
	protected void mutateMethodInsn(MethodVisitor mv, String className, int opcode, String owner, String name,
									String desc, int[] newOrder, boolean itf) {
		Type[] args = Type.getArgumentTypes(desc);
		Type ret = Type.getReturnType(desc);

		// prepare arguments on stack
		prepareStack(mv, args, newOrder);

		// TODO OAC: opcode for static methods

		mv.visitMethodInsn(opcode, owner, name, Arguments.getDescForNewOrder(args, ret, newOrder), itf);
	}

	@Override
	protected int countMethodInsnReplacements(String className, int opcode, String owner, String name, String desc) {
		return getMethodInsnReplacemnts(opcode, owner, name, desc).size();
	}

	@Override
	protected int[] getMethodInsnReplacement(String className, int opcode, String owner, String name, String desc,
											 int mutantIndex) {
		return getMethodInsnReplacemnts(opcode, owner, name, desc).get(mutantIndex);
	}

	/**
	 * Replace order of arguments on the stack.
	 */
	private void prepareStack(MethodVisitor mv, Type[] args, int[] newOrder) {
		Label start = new Label();
		Label end = new Label();
		mv.visitLabel(start);
		// store original arguments in local variables
		for (int i = args.length - 1; i >= 0; i--) {
			mv.visitVarInsn(args[i].getOpcode(ISTORE), Short.MAX_VALUE - i);
		}
		// load arguments in new order from local variables
		for (int element : newOrder) {
			mv.visitVarInsn(args[element].getOpcode(ILOAD), Short.MAX_VALUE - element);
		}
		mv.visitLabel(end);
		// visit local variables
		for (int i = 0; i < args.length; i++) {
			String varDesc = args[i].getDescriptor();
			int varIndex = Short.MAX_VALUE - i;
			mv.visitLocalVariable("LCV" + varIndex, varDesc, null, start, end, varIndex);
		}
	}

	/**
	 * Collect all possible replacements for the given method.
	 */
	private List<int[]> getMethodInsnReplacemnts(int opcode, String owner, String name, String desc) {
		ArrayList<int[]> replacements = cache.get(owner + name + desc);
		if (replacements == null) {
			replacements = new ArrayList<>(INITIAL_CAPACITY);
			IEnvironment env = getEnvironment();
			List<IMethodInfo> all = env.getClassInfo(owner).getDeclaredMethods();
			for (IMethodInfo method : all) {
				if (isReplacement(owner, name, desc, method)) {
					List<int[]> temp = Arguments.getCompatibleOrders(desc, method.getDesc());
					replacements.ensureCapacity(replacements.size() + temp.size());
					replacements.addAll(temp);
				}
			}
		}
		cache.put(owner + name + desc, replacements);
		return replacements;
	}

	/**
	 * Test if a given methods have the same name and return type as the
	 * original one.
	 */
	private boolean isReplacement(String owner, String name, String desc, IMethodInfo method) {
		// TODO OAC: what about exception types and signatures?
		return !Accesses.isStatic(method.getAccess()) && name.equals(method.getName())
				&& Type.getReturnType(desc).equals(Type.getReturnType(method.getDesc()));
	}
}
