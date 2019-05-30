package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.common.IMethodInfo;

import java.util.Collections;
import java.util.List;

/**
 * Immutable Java method representation.
 *
 * @author pmiwaszko
 */
class MethodInfo implements IMethodInfo {
	private final int access;
	private final String name;
	private final String desc;
	private final String signature;
	private final List<String> exceptions;

	public MethodInfo(int access, String name, String desc, String signature, List<String> exceptions) {
		this.access = access;
		this.name = name;
		this.desc = desc;
		this.signature = signature;
		this.exceptions = Collections.unmodifiableList(exceptions);
	}

	public MethodInfo(IMethodInfo info) {
		access = info.getAccess();
		name = info.getName();
		desc = info.getDesc();
		signature = info.getSignature();
		exceptions = info.getExceptions(); // TODO should copy?
	}

	@Override
	public int getAccess() {
		return access;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String getSignature() {
		return signature;
	}

	@Override
	public List<String> getExceptions() {
		return exceptions;
	}

	@Override
	public String toString() {
		return name;
	}
}
