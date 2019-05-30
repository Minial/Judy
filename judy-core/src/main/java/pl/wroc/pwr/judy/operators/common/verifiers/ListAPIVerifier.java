package pl.wroc.pwr.judy.operators.common.verifiers;

import java.util.Arrays;
import java.util.List;

/**
 * Verifies if method return type contains one of <code>java.util.List</code>
 * subclasses or subinterfaces.
 *
 * @author TM
 */
public class ListAPIVerifier extends TypeVerifier {
	/**
	 * List of known subclasses and subinterfaces of java.util.List taken from
	 * its javadoc documentation.
	 */
	private static final List<String> LIST_TYPES = Arrays.asList("AbstractList", "AbstractSequentialList", "ArrayList",
			"AttributeList", "CopyOnWriteArrayList", "LinkedList", "RoleList", "RoleUnresolvedList", "Stack", "Vector",
			"List");

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<String> getTypes() {
		return LIST_TYPES;
	}

}
