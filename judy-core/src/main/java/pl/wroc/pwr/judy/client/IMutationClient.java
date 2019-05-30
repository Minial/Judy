package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.common.MutationException;

import java.io.IOException;
import java.rmi.RemoteException;

public interface IMutationClient {

	/**
	 * Start mutation analysis.
	 *
	 * @return mutation results
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws MutationException
	 * @throws WorkException
	 * @throws RemoteException
	 */
	IMutationResult compute() throws InterruptedException, IOException, WorkException, MutationException;

}