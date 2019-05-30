package pl.wroc.pwr.judy.hom;

public class IncompatibleMutationException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new exception with details of incompatible mutation
	 *
	 * @param operator           mutation operator name
	 * @param mutationPointIndex mutation point
	 * @param mutantIndex        mutant index
	 */
	public IncompatibleMutationException(String operator, int mutationPointIndex, int mutantIndex) {
		super(String.format("Incompatible mutation - cannot mutate with %s at point %d, mutant #%d", operator,
				mutationPointIndex, mutantIndex));
	}

}
