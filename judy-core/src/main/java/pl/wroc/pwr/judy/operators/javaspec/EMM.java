package pl.wroc.pwr.judy.operators.javaspec;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMethodInfo;
import pl.wroc.pwr.judy.operators.common.AbstractMethodCallReplacemetOperator;
import pl.wroc.pwr.judy.utils.Cache;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Modifier Method Change.
 * <p/>
 * Change a modifier method name (<code>set<em>XYZ</em>()</code>) to other
 * compatible modifier method names.
 *
 * @author pmiwaszko
 */
public class EMM extends AbstractMethodCallReplacemetOperator<IMethodInfo> {
	private static final String SET = "set";
	private final Map<String, List<IMethodInfo>> cache = Collections
			.synchronizedMap(new Cache<String, List<IMethodInfo>>());

	@Override
	public String getDescription() {
		return "change a modifier method name to other compatible modifier method names";
	}

	@Override
	protected boolean checkMethodInsn(String className, int opcode, String owner, String name, String desc) {
		return name.startsWith(SET) && Type.getArgumentTypes(desc).length == 1
				&& Type.getReturnType(desc) == Type.VOID_TYPE;
	}

	@Override
	protected void mutateMethodInsn(MethodVisitor mv, String className, int opcode, String owner, String name,
									String desc, IMethodInfo replacement, boolean itf) {
		mv.visitMethodInsn(opcode, owner /* TODO EMM: should replace owner? */, replacement.getName(),
				replacement.getDesc(), itf);
	}

	@Override
	protected int countMethodInsnReplacements(String className, int opcode, String owner, String name, String desc) {
		return getMethodInsnReplacemnts(opcode, owner, name, desc).size();
	}

	@Override
	protected IMethodInfo getMethodInsnReplacement(String className, int opcode, String owner, String name,
												   String desc, int mutantIndex) {
		return getMethodInsnReplacemnts(opcode, owner, name, desc).get(mutantIndex);
	}

	private List<IMethodInfo> getMethodInsnReplacemnts(int opcode, String owner, String name, String desc) {
		List<IMethodInfo> methods = cache.get(owner + name + desc);
		if (methods == null) {
			methods = new LinkedList<>();
			IEnvironment env = getEnvironment();
			List<IMethodInfo> all = env.getAllMethods(owner);
			for (IMethodInfo method : all) {
				if (isReplacement(owner, name, desc, method)) {
					methods.add(method);
				}
			}
		}
		cache.put(owner + name + desc, methods);
		return methods;
	}

	private boolean isReplacement(String owner, String name, String desc, IMethodInfo replacement) {
		return replacement.getName().startsWith(SET) && desc.equals(replacement.getDesc())
				&& !replacement.getName().equals(name);
	}
}
