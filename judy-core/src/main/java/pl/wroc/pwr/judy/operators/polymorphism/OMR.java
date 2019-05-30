package pl.wroc.pwr.judy.operators.polymorphism;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.operators.common.AbstractMethodBodyReplacementOperator;
import pl.wroc.pwr.judy.utils.Accesses;
import pl.wroc.pwr.judy.utils.Arguments;
import pl.wroc.pwr.judy.utils.Cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Overloading method contents change.
 * <p/>
 * Replaces the body of a method with the body of another method that has the
 * same name.
 *
 * @author pmiwaszko
 */
public class OMR extends AbstractMethodBodyReplacementOperator<int[]> {
	private static final int INITIAL_CAPACITY = 500;
	private final Map<String, ArrayList<int[]>> cache = Collections
			.synchronizedMap(new Cache<String, ArrayList<int[]>>());

	@Override
	public String getDescription() {
		return "change overloading method";
	}

	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		// custom methods with at least one argument
		return !Accesses.isStatic(access) && !className.startsWith(JAVA) && !INIT.equals(name)
				&& Type.getArgumentTypes(desc).length > 0;
	}

	@Override
	protected int countReplacements(String className, int access, String name, String desc, String signature,
									String[] exceptions) {
		return getMethodReplacemnts(access, className, name, desc, signature, exceptions).size();
	}

	@Override
	protected int[] getReplacement(String className, int access, String name, String desc, String signature,
								   String[] exceptions, int index) {
		return getMethodReplacemnts(access, className, name, desc, signature, exceptions).get(index);
	}

	@Override
	protected void replace(MethodVisitor mv, String className, int access, String name, String desc, int[] newOrder) {
		Type[] args = Type.getArgumentTypes(desc);
		Type ret = Type.getReturnType(desc);

		int opcode = INVOKEVIRTUAL;
		if (Accesses.isPrivate(access)) {
			opcode = INVOKESPECIAL;
		}
		if (Accesses.isStatic(access)) {
			opcode = INVOKESTATIC;
		}

		loadParameters(mv, opcode, args, newOrder);
		// TODO: Consider Java8 and interface default implementations
		mv.visitMethodInsn(opcode, className, name, Arguments.getDescForNewOrder(args, ret, newOrder), false);
	}

	/**
	 * Load arguments in new order from local variables.
	 */
	private void loadParameters(MethodVisitor mv, int opcode, Type[] args, int[] newOrder) {
		int thisVar = 1;
		if (opcode == INVOKESTATIC) {
			thisVar = 0;
		} else {
			mv.visitVarInsn(ALOAD, 0); // this
		}
		for (int element : newOrder) {
			mv.visitVarInsn(args[element].getOpcode(ILOAD), element + thisVar);
		}
	}

	private List<int[]> getMethodReplacemnts(int access, String className, String name, String desc, String signature,
											 String[] exceptions) {
		ArrayList<int[]> replacements = cache.get(className + name + desc);
		if (replacements == null) {
			replacements = new ArrayList<>(INITIAL_CAPACITY);
			IEnvironment env = getEnvironment();
			List<IMethodInfo> all = env.getClassInfo(className).getDeclaredMethods();
			for (IMethodInfo method : all) {
				if (isReplacement(className, name, desc, method)) {
					List<int[]> temp = Arguments.getCompatibleOrders(desc, method.getDesc());
					replacements.ensureCapacity(replacements.size() + temp.size());
					replacements.addAll(temp);
				}
			}
		}
		cache.put(className + name + desc, replacements);
		return replacements;
	}

	/**
	 * Test if a given methods have the same name and return type but different
	 * description as the original one.
	 */
	private boolean isReplacement(String owner, String name, String desc, IMethodInfo method) {
		// TODO OMR: what about exception types and signatures?
		return name.equals(method.getName()) && !desc.equals(method.getDesc())
				&& Type.getReturnType(desc).equals(Type.getReturnType(method.getDesc()));
	}
}
