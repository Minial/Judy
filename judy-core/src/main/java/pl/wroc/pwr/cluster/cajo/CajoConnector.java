package pl.wroc.pwr.cluster.cajo;

import gnu.cajo.Cajo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.cluster.IQueue;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Common Cajo wrapper.
 *
 * @author pmiwaszko
 */
public class CajoConnector {
	private static final Logger LOGGER = LogManager.getLogger(CajoConnector.class);

	/**
	 * <code>CajoConnector</code> constructor.
	 */
	public CajoConnector() throws IOException {
		cajo = new Cajo();
	}

	/**
	 * @throws RemoteException
	 * @throws InterruptedException Get shared queue instance.
	 * @throws
	 */
	public IQueue getQueue(int retries, int timeout) throws InterruptedException, RemoteException {
		Thread.sleep(timeout);
		Object[] queues;
		try {
			queues = cajo.lookup(IQueue.class);
		} catch (Exception e) {
			LOGGER.info("Exception", e);
			throw new RemoteException("Unknown exception occured when doing Cajo lookup: " + e.getMessage());
		}
		IQueue queue = null;
		if (queues.length > 0) {
			queue = (IQueue) cajo.proxy(queues[0], IQueue.class);
			LOGGER.info("Successfully connected to shared server instance.");
			if (queues.length > 1) {
				LOGGER.error("More than one shared server instance available! Please review your configuration.");
			}
		} else {
			if (retries > 0) {
				LOGGER.error("Unable to connect to shared server instance. Retrying...");
				return getQueue(retries - 1, timeout + 600);
			}
			throw new RemoteException("Unable to connect to shared server instance");
		}
		return queue;
	}

	/**
	 * @see Cajo#export(Object)
	 */
	public void export(Object object) throws IOException {
		cajo.export(object);
	}

	/**
	 * @see Cajo#lookup(Class)
	 */
	public Object[] lookup(Class<?> methodSetInterface) throws Exception {
		return cajo.lookup(methodSetInterface);
	}

	/**
	 * @see Cajo#proxy(Object, Class)
	 */
	public Object proxy(Object reference, Class<?> methodSetInterface) {
		return cajo.proxy(reference, methodSetInterface);
	}

	/**
	 * @see Cajo#register(String, int)
	 */
	public void register(String hostname, int port) throws Exception {
		cajo.register(hostname, port);
	}

	private final Cajo cajo;
}
