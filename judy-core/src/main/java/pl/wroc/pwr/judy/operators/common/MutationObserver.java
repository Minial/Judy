package pl.wroc.pwr.judy.operators.common;

/**
 * Mutation observer interface is for notification receivers in Observer design
 * pattern
 *
 * @author TM
 */
public interface MutationObserver {
	/**
	 * @param lineNumber first line of mutation
	 */
	void onMutation(int lineNumber);
}
