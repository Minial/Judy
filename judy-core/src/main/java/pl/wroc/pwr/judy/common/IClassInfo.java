package pl.wroc.pwr.judy.common;

import java.util.List;

/**
 * Java class representation.
 *
 * @author pmiwaszko
 */
public interface IClassInfo {
	/**
	 * Get access of class.
	 *
	 * @see pl.wroc.pwr.judy.utils.Accesses
	 */
	int getAccess();

	/**
	 * Get the fully qualified internal name of a class where '.' are replaced
	 * by '/'.
	 */
	String getClassName();

	/**
	 * Get interfaces implemented by this class.
	 */
	List<String> getInterfaces();

	/**
	 * Get fields declared in this class. Inherited fields are not included.
	 */
	List<IFieldInfo> getDeclaredFields();

	/**
	 * Get methods declared in this class. Inherited methods are not included.
	 */
	List<IMethodInfo> getDeclaredMethods();

	/**
	 * Get signature of this class.
	 */
	String getSignature();

	/**
	 * Get the fully qualified internal name of a super class where '.' are
	 * replaced by '/'.
	 */
	String getSuperClassName();

	@Override
	String toString();
}
