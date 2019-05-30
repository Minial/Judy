package pl.wroc.pwr.cluster.local;

import pl.wroc.pwr.cluster.AbstractQueueClient;
import pl.wroc.pwr.cluster.IQueue;

/**
 * Single-JVM client implementation.
 *
 * @author pmiwaszko
 */
public class LocalClient extends AbstractQueueClient {
	/**
	 * <code>LocalClient</code> constructor.
	 */
	public LocalClient(long l, IQueue queue) {
		super(l, queue);
	}
}
