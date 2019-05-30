package pl.wroc.pwr.cluster.local;

import pl.wroc.pwr.cluster.AbstractQueueWorker;
import pl.wroc.pwr.cluster.IQueue;
import pl.wroc.pwr.cluster.IQueueClient;

/**
 * Single-JVM client implementation.
 *
 * @author pmiwaszko
 */
public class LocalWorker extends AbstractQueueWorker {
	private final IQueueClient client;

	/**
	 * <code>LocalWorker</code> constructor.
	 */
	public LocalWorker(IQueue queue, IQueueClient client, Object param, int threadsCount, int workerQueueSize) {
		super(queue, param, threadsCount, workerQueueSize);
		this.client = client;
	}

	@Override
	protected IQueueClient getQueueClient(long id) {
		return client;
	}
}
