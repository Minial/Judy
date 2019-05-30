package pl.wroc.pwr.judy.worker;

import java.io.IOException;
import java.rmi.RemoteException;

import pl.wroc.pwr.cluster.IQueueWorker;
import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.cluster.cajo.CajoConnector;
import pl.wroc.pwr.cluster.cajo.CajoWorker;
import pl.wroc.pwr.judy.IWorkerConfig;

/**
 * Worker implementation.
 *
 * @author pmiwaszko
 */
public class MutationWorker {
	private final IWorkerConfig config;

	/**
	 * <code>MutationWorker</code> constructor.
	 */
	public MutationWorker(IWorkerConfig config) {
		this.config = config;
	}

	/**
	 * Start worker.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws RemoteException
	 * @throws WorkException
	 */
	public void start() throws RemoteException, InterruptedException, IOException, WorkException {
		int threads = config.getThreadsCount();
		IQueueWorker worker = new CajoWorker(new CajoConnector(), config.getWorkspace(), threads, 1 + threads);
		worker.start();
	}
}
