package pl.wroc.pwr.judy.common;

import java.util.List;

/**
 * Interface for inheritance hierarchy access.
 *
 * @author pmiwaszko
 */
public interface IClassHierarchy {
	/**
	 * Returns list of super classes of the given class. Given class is not
	 * included.
	 *
	 * @return list of classes higher in hierarchy or empty list
	 */
	List<String> getAllSuperclasses(String className);

	/**
	 * Returns list of direct subclasses of the given class. Given class is not
	 * included.
	 *
	 * @return list of classes lower in hierarchy or empty list
	 */
	List<String> getDirectSubclasses(String className);
}
