package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.common.IClassHierarchy;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for inheritance hierarchy access. This class is thread safe.
 *
 * @author pmiwaszko
 */
class ClassHierarchy implements IClassHierarchy {
	private final Map<String, String> superclasses;
	private final Map<String, List<String>> subclasses;

	/**
	 * <code>ClassHierarchy</code> constructor.
	 *
	 * @param inheritance map associating class names with their super class names
	 */
	public ClassHierarchy(Map<String, String> inheritance) {
		superclasses = Collections.unmodifiableMap(inheritance);
		subclasses = new ConcurrentHashMap<>();
	}

	@Override
	public List<String> getDirectSubclasses(String name) {
		String className = name;
		if (className != null) {
			className = className.replace('.', '/');
			if (subclasses.containsKey(className)) {
				return subclasses.get(className);
			}

			List<String> directSubclasses = new LinkedList<>();
			for (Entry<String, String> entry : superclasses.entrySet()) {
				if (entry.getValue().equals(className)) {
					directSubclasses.add(entry.getKey());
				}
			}
			directSubclasses = Collections.unmodifiableList(directSubclasses);
			subclasses.put(className, directSubclasses);
			return directSubclasses;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<String> getAllSuperclasses(String name) {
		String className = name;
		List<String> classes = new LinkedList<>();
		if (className != null) {
			className = className.replace('.', '/');
			while (superclasses.containsKey(className)) {
				className = superclasses.get(className);
				classes.add(className);
			}
		}
		return Collections.unmodifiableList(classes);
	}
}
