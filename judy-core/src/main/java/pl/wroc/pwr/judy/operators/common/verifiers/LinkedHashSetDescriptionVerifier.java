package pl.wroc.pwr.judy.operators.common.verifiers;

import java.util.Arrays;
import java.util.List;

/**
 * Verifies if method return type is LinkedHashSet.
 *
 * @author MH
 */
public class LinkedHashSetDescriptionVerifier extends TypeVerifier {

	private static final List<String> LIST_TYPES = Arrays.asList("LinkedHashSet");

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<String> getTypes() {
		return LIST_TYPES;
	}

}
