package pl.wroc.pwr.cluster;

import pl.wroc.pwr.judy.IClassMutationResult;

import java.util.List;

/**
 * Queued items producer.
 *
 * @author pmiwaszko
 */
public interface IQueueClient {
	/**
	 * Get unique id.
	 */
	long getId();

	/**
	 * Schedule new work.
	 *
	 * @throws WorkException when work is scheduled after getResults was invoked
	 */
	void schedule(final IWork work) throws WorkException;

	/**
	 * Notify about completed work.
	 */
	void complete(final IClassMutationResult result);

	/**
	 * Notify about skipped work.
	 */
	void skipped();

	/**
	 * Get results of scheduled works. No new work can be scheduled after this
	 * method was invoked.
	 *
	 * @throws InterruptedException
	 */
	List<IResult> getResults() throws InterruptedException;
}
