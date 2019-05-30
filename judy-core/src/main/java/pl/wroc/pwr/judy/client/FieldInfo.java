package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.common.IFieldInfo;

/**
 * Immutable Java field representation.
 *
 * @author pmiwaszko
 * @author mnegacz
 */
class FieldInfo implements IFieldInfo {

	private final int access;

	private final String name;

	private final String description;

	private final String signature;

	private final Object value;

	public FieldInfo(int access, String name, String description, String signature, Object value) {
		this.access = access;
		this.name = name;
		this.description = description;
		this.signature = signature;
		this.value = value;
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
	public String getDescription() {
		return description;
	}

	@Override
	public String getSignature() {
		return signature;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name;
	}

}
