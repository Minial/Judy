package pl.wroc.pwr.judy.operators.common.verifiers;

import java.util.Arrays;
import java.util.List;

/**
 * Verifies if method return type contains one of
 * <code>java.util.Collection</code> subclasses or subinterfaces.
 *
 * @author TM
 */
public class CollectionAPIVerifier extends TypeVerifier {
	/**
	 * List of known subinterfaces of java.util.Collection taken from its
	 * javadoc documentation.
	 */
	private static final List<String> COLLECTION_TYPES = Arrays.asList("Collection", "AbstractCollection",
			"AbstractList", "AbstractQueue", "AbstractSequentialList", "AbstractSet", "ArrayBlockingQueue",
			"ArrayDeque", "ArrayList", "AttributeList", "BeanContextServicesSupport", "BeanContextSupport",
			"ConcurrentLinkedQueue", "ConcurrentSkipListSet", "CopyOnWriteArrayList", "CopyOnWriteArraySet",
			"DelayQueue", "EnumSet", "HashSet", "JobStateReasons", "LinkedBlockingDeque", "LinkedBlockingQueue",
			"LinkedHashSet", "LinkedList", "PriorityBlockingQueue", "PriorityQueue", "RoleList", "RoleUnresolvedList",
			"Stack", "SynchronousQueue", "TreeSet", "Vector", "BeanContext", "BeanContextServices", "BlockingDeque",
			"BlockingQueue", "Deque", "List", "NavigableSet", "Queue", "Set", "SortedSet");

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<String> getTypes() {
		return COLLECTION_TYPES;
	}

}
