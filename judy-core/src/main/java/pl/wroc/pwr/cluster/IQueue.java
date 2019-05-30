package pl.wroc.pwr.cluster;

/**
 * Shared queue of work items to process.
 *
 * @author pmiwaszko
 */
public interface IQueue {
	/**
	 * Put work into the queue. Wait if queue is full.
	 */
	void put(final IWork work) throws WorkException;

	/**
	 * Take work from the queue. Wait for available item if necessary.
	 */
	IWork take() throws WorkException;
}
