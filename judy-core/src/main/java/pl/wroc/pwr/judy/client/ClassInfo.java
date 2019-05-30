package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IFieldInfo;
import pl.wroc.pwr.judy.common.IMethodInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Immutable Java class representation.
 *
 * @author pmiwaszko
 */
class ClassInfo implements IClassInfo {
	private final int access;
	private final String className;
	private final String signature;
	private final String superClassName;
	private final List<String> interfaces;
	private final List<IMethodInfo> declaredMethods;
	private final List<IFieldInfo> declaredFields;
	public static final IClassInfo EMPTY_CACHE = new ClassInfo(0, null, null, null, new LinkedList<String>(),
			new LinkedList<IFieldInfo>(), new LinkedList<IMethodInfo>());

	public ClassInfo(int access, String name, String signature, String superName, List<String> interfaces,
					 List<IFieldInfo> fields, List<IMethodInfo> methods) {
		this.access = access;
		className = name;
		this.signature = signature;
		superClassName = superName;
		this.interfaces = Collections.unmodifiableList(interfaces);
		declaredFields = Collections.unmodifiableList(fields);
		declaredMethods = Collections.unmodifiableList(methods);
	}

	@Override
	public int getAccess() {
		return access;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public List<String> getInterfaces() {
		return interfaces;
	}

	@Override
	public List<IFieldInfo> getDeclaredFields() {
		return declaredFields;
	}

	@Override
	public List<IMethodInfo> getDeclaredMethods() {
		return declaredMethods;
	}

	@Override
	public String getSignature() {
		return signature;
	}

	@Override
	public String getSuperClassName() {
		return superClassName;
	}

	@Override
	public String toString() {
		return className;
	}
}
