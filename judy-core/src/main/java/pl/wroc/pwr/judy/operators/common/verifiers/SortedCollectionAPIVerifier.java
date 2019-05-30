package pl.wroc.pwr.judy.operators.common.verifiers;

import java.util.Arrays;
import java.util.List;

/**
 * Verifies if method return type contains one of <code>java.util</code> classes
 * and interfaces providing <code>comparator()</code> method.
 *
 * @author TM
 */
public class SortedCollectionAPIVerifier extends TypeVerifier {
	/**
	 * List of known subclasses and subinterfaces providing
	 * <code>comparator()</code> method, taken from the javadoc documentation.
	 */
	private static final List<String> SORTED_TYPES = Arrays.asList("SortedSet", "TreeSet", "SortedMap",
			"ConcurrentSkipListMap", "TreeMap", "ConcurrentNavigableMap", "NavigableMap");

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<String> getTypes() {
		return SORTED_TYPES;
	}
}
