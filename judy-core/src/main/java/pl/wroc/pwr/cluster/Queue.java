package pl.wroc.pwr.cluster;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of {@link IQueue} interface based on
 * {@link LinkedBlockingQueue}.
 *
 * @author pmiwaszko
 */
public class Queue implements IQueue {
	private final BlockingQueue<IWork> queue;
	/**
	 * Default queue size.
	 */
	public static final int DEFAULT_QUEUE_SIZE = 500;

	/**
	 * <code>Queue</code> constructor.
	 */
	public Queue(int size) {
		queue = new LinkedBlockingQueue<>(size);
	}

	/**
	 * <code>Queue</code> constructor with default size.
	 */
	public Queue() {
		this(DEFAULT_QUEUE_SIZE);
	}

	@Override
	public void put(IWork work) throws WorkException {
		try {
			queue.put(work);
		} catch (InterruptedException e) {
			throw new WorkException(e);
		}
	}

	@Override
	public IWork take() throws WorkException {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			throw new WorkException(e);
		}
	}
}
