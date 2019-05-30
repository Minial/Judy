package pl.wroc.pwr.cluster.cajo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.cluster.AbstractQueueWorker;
import pl.wroc.pwr.cluster.IQueueClient;
import pl.wroc.pwr.cluster.IQueueWorker;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cajo-based implementation of {@link IQueueWorker}.
 *
 * @author pmiwaszko
 */
public class CajoWorker extends AbstractQueueWorker {
	private static final Logger LOGGER = LogManager.getLogger(CajoWorker.class);
	private final CajoConnector cajo;
	private final Map<Long, IQueueClient> clients;

	/**
	 * <code>CajoWorker</code> constructor.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws RemoteException
	 */
	public CajoWorker(CajoConnector cajo, Object param, int threadsCount, int workerQueueSize) throws RemoteException,
			InterruptedException, IOException {
		super(cajo.getQueue(4, 600), param, threadsCount, workerQueueSize);
		this.cajo = cajo;
		clients = new ConcurrentHashMap<>();
	}

	@Override
	protected IQueueClient getQueueClient(long id) {
		IQueueClient client = clients.get(id);
		if (client == null) {
			try {
				Object[] queueClients = cajo.lookup(IQueueClient.class);
				for (Object c : queueClients) {
					IQueueClient proxy = (IQueueClient) cajo.proxy(c, IQueueClient.class);
					long proxyId = proxy.getId();
					if (!clients.containsKey(id)) {
						clients.put(proxyId, proxy);
					}
				}
			} catch (Exception e) {
				LOGGER.info("Exception", e);
				return null;
			}
		}
		return clients.get(id);
	}
}
