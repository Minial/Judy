package pl.wroc.pwr.cluster.cajo;

import pl.wroc.pwr.cluster.AbstractQueueClient;
import pl.wroc.pwr.cluster.IQueueClient;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Cajo-based implementation of {@link IQueueClient}.
 *
 * @author pmiwaszko
 */
public class CajoClient extends AbstractQueueClient {
	/**
	 * <code>CajoClient</code> constructor.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws RemoteException
	 * @throws
	 * @throws IOException
	 */
	public CajoClient(CajoConnector cajo, long id) throws RemoteException, InterruptedException, IOException {
		super(id, cajo.getQueue(4, 600));
		cajo.export(this);
	}
}
