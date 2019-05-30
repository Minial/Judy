package pl.wroc.pwr.cluster;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.IClassMutationResult;

import java.util.concurrent.*;

/**
 * Abstract base class for {@link IQueueWorker} implementations.
 *
 * @author pmiwaszko
 */
public abstract class AbstractQueueWorker implements IQueueWorker {
	private static final Logger LOGGER = LogManager.getLogger(AbstractQueueWorker.class);
	private final IQueue queue;
	private final Object param;
	private final ExecutorService executor;

	/**
	 * <code>AbstractQueueWorker</code> constructor.
	 */
	public AbstractQueueWorker(final IQueue queue, final Object param, int threadsCount, int workerQueueSize) {
		if (workerQueueSize < threadsCount) {
			throw new IllegalArgumentException("Worker queue size must be higher than threads count");
		}
		this.queue = queue;
		this.param = param;
		executor = new ThreadPoolExecutor(threadsCount, threadsCount, 0, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(workerQueueSize), new BlockingHandler());
	}

	@Override
	public void start() throws WorkException {
		while (true) {
			final IWork work = queue.take();
			try {
				executor.execute(new Runnable() {
					private final Logger logger = LogManager.getLogger(this.getClass());

					@Override
					public void run() {
						try {
							IClassMutationResult result = work.run(param);
							complete(work.getClientId(), result);
						} catch (Exception e) {
							logger.warn("Exception", e);
							skip(work);
						}
					}
				});
			} catch (RejectedExecutionException e) {
				LOGGER.info("RejectedExecutionException", e);
				skip(work);
			}
		}
	}

	/**
	 * Get proxy of a client with a given id.
	 */
	protected abstract IQueueClient getQueueClient(long id);

	@SuppressWarnings("unused")
	private void retry(IWork work) {
		// TODO implement retrying
		IQueueClient client = getQueueClient(work.getClientId());
		if (work.canRetry()) {
			try {
				client.schedule(work);
			} catch (WorkException e) {
				LOGGER.warn("WorkException", e);
				client.skipped();
			}
		} else {
			client.skipped();
		}
	}

	private void skip(IWork work) {
		IQueueClient client = getQueueClient(work.getClientId());
		client.skipped();
	}

	private void complete(long clientId, IClassMutationResult result) {
		IQueueClient client = getQueueClient(clientId);
		client.complete(result);
	}

	/**
	 * Private implementation of blocking execution handler.
	 *
	 * @author pmiwaszko
	 */
	private static class BlockingHandler implements RejectedExecutionHandler {
		@Override
		public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
			try {
				executor.getQueue().put(runnable);
			} catch (InterruptedException e) {
				throw new RejectedExecutionException(e);
			}
		}
	}
}
