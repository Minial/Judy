package pl.wroc.pwr.judy.operators.common.verifiers;

import java.util.Arrays;
import java.util.List;

/**
 * Verifies if method return type contains one of
 * <code>java.util.Collection</code> or its subinterfaces.
 *
 * @author TM
 */
public class CollectionInterfaceAPIVerifier extends TypeVerifier {
	/**
	 * List of known subclasses and subinterfaces of java.util.Collection taken
	 * from its javadoc documentation.
	 */
	private static final List<String> COLLECTION_TYPES = Arrays.asList("Collection", "BeanContext",
			"BeanContextServices", "BlockingDeque", "BlockingQueue", "Deque", "List", "NavigableSet", "Queue", "Set",
			"SortedSet");

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<String> getTypes() {
		return COLLECTION_TYPES;
	}

}
