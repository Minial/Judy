package pl.wroc.pwr.judy.common;

import java.util.List;

/**
 * Common facade for reading classes hierarchy and structure details.
 *
 * @author pmiwaszko
 */
public interface IEnvironment {
	/**
	 * Get detailed information about given class. This is a facade method for
	 * {@link IClassInfoCache#get(String)}.
	 *
	 * @see IClassInfoCache
	 */
	IClassInfo getClassInfo(String className);

	/**
	 * Get super classes of the given class. This is a facade method for
	 * {@link IClassHierarchy#getSuperClasses(String)}.
	 *
	 * @see IClassHierarchy
	 */
	List<String> getAllSuperclasses(String className);

	/**
	 * Get subclasses of the given class. This is a facade method for
	 * {@link IClassHierarchy#getSubClasses(String)}.
	 *
	 * @see IClassHierarchy
	 */
	List<String> getDirectSubclasses(String className);

	/**
	 * Get methods declared in the given class and all its super classes.
	 */
	List<IMethodInfo> getAllMethods(String className);

	/**
	 * Check if super class of given class has a field that is hidden with the
	 * specified field.
	 */
	boolean isHidingField(String className, String fieldName, String fieldDesc);

	/**
	 * Check if super class of given class has a method that is overridden with
	 * the specified method.
	 */
	boolean isOverridingMethod(String className, String methodName, String methodDesc);
}
