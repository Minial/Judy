package pl.wroc.pwr.judy.operators.common;

/**
 * Observable interface for mutation notifications producers
 *
 * @author TM
 */
public interface MutationObservable {
	/**
	 * Add mutation observer to the notifications consumers
	 *
	 * @param observer mutation observer
	 */
	void addObserver(MutationObserver observer);

	/**
	 * Notify observers about mutation start line number
	 */
	void notifyObservers();
}
