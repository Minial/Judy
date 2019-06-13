package pl.wroc.pwr.judy.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.cluster.IQueueClient;
import pl.wroc.pwr.cluster.IQueueWorker;
import pl.wroc.pwr.cluster.Queue;
import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.cluster.local.LocalClient;
import pl.wroc.pwr.cluster.local.LocalWorker;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.common.MutationException;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

public class LocalMutationClient extends MutationClient {

	private static final int MIN_QUEUE_SIZE = 10;
	private String workspace;
	private int threadCount;

	/**
	 * Creates local mutation client (running on a single machine only)
	 *
	 * @param testRun       initial tests run results
	 * @param targetClasses source classes to mutate
	 * @param operators     mutation operators descriptions
	 * @param workFactory   mutation work factory
	 * @param workspace     workspace path
	 * @param threadCount   limit of threads
	 */
	public LocalMutationClient(IInitialTestsRun testRun, List<String> targetClasses, List<IDescriptable> operators,
							   IMutationWorkFactory workFactory, Observer workProgressObserver, String workspace, int threadCount, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		super(testRun, targetClasses, operators, workFactory, workProgressObserver, MatrixE, MatrixC);
		this.workspace = workspace;
		this.threadCount = threadCount;
	}

	@Override
	public IMutationResult compute() throws InterruptedException, IOException, WorkException, MutationException {
		final Queue queue = new Queue();
		final IQueueClient client = createQueueClient(queue);
		final IQueueWorker worker = createWorker(queue, client);
		runWorker(worker);
		return createMutationResult(client);
	}

	private void runWorker(final IQueueWorker worker) {
		new Thread(new Runnable() {
			private final Logger logger = LogManager.getLogger(this.getClass());

			@Override
			public void run() {
				try {
					worker.start();
				} catch (WorkException e) {
					logger.error("Unexpected worker error: " + e.getMessage() + "!");
					System.exit(1);
				}
			}
		}, "Worker thread").start();
	}

	protected IQueueWorker createWorker(final Queue queue, final IQueueClient client) {
		return new LocalWorker(queue, client, workspace, threadCount, getQueueSize());
	}

	protected IQueueClient createQueueClient(final Queue queue) {
		LocalClient client = new LocalClient(System.currentTimeMillis(), queue);
		client.addObserver(getWorkProgressObserver());
		return client;
	}

	/**
	 * Gets thread queue size
	 *
	 * @return queue size
	 */
	public int getQueueSize() {
		return threadCount * 2 + MIN_QUEUE_SIZE;
	}
}
