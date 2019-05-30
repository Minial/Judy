package pl.wroc.pwr.judy.operators.common.verifiers;

import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.List;

/**
 * Base class for type verification - both method return types and Type
 *
 * @author TM
 */
public abstract class TypeVerifier {
	private static final String JAVA_UTIL_PREFIX = "java/util/";
	private static final List<Type> NON_CLASS_TYPES = Arrays.asList(Type.BOOLEAN_TYPE, Type.BYTE_TYPE, Type.CHAR_TYPE,
			Type.DOUBLE_TYPE, Type.FLOAT_TYPE, Type.INT_TYPE, Type.LONG_TYPE, Type.SHORT_TYPE, Type.VOID_TYPE);

	/**
	 * Verifies if given method returns expected type, based on method
	 * description
	 *
	 * @param description method description
	 * @return true if method returns expected type
	 */
	public boolean verifyMethodDescription(String description) {
		return verifyType(Type.getReturnType(description));
	}

	/**
	 * Verifies if given type is one of predefined types based
	 *
	 * @param type ASM type
	 * @return true if ASM type is one of predefined types
	 */
	public boolean verifyType(Type type) {
		if (isObjectType(type)) {
			String internalName = type.getInternalName().replace(JAVA_UTIL_PREFIX, "");
			return getTypes().contains(internalName);
		}
		return false;
	}

	private boolean isObjectType(Type type) {
		return !NON_CLASS_TYPES.contains(type);
	}

	/**
	 * Gets list of expected types (class names)
	 *
	 * @return list of expected types
	 */
	protected abstract List<String> getTypes();
}
