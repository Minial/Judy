package pl.wroc.pwr.cluster;

/**
 * Queued items consumer responsible for processing work items.
 *
 * @author pmiwaszko
 */
public interface IQueueWorker {
	/**
	 * Start processing.
	 */
	void start() throws WorkException;
}
