package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.cluster.IQueueClient;
import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.cluster.cajo.CajoClient;
import pl.wroc.pwr.cluster.cajo.CajoConnector;
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

public class DistributedMutationClient extends MutationClient {

	/**
	 * Creates distributed mutation client (may be running on multiple machine)
	 *
	 * @param testRun       initial tests run results
	 * @param targetClasses source classes to mutate
	 * @param operators     mutation operators descriptions
	 * @param workFactory   mutation work factory
	 */
	public DistributedMutationClient(IInitialTestsRun testRun, List<String> targetClasses,
									 List<IDescriptable> operators, IMutationWorkFactory workFactory, Observer workProgressObserver, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		super(testRun, targetClasses, operators, workFactory, workProgressObserver, MatrixE, MatrixC);
	}

	@Override
	public IMutationResult compute() throws InterruptedException, IOException, WorkException, MutationException {
		return createMutationResult(createQueueClient());
	}

	protected IQueueClient createQueueClient() throws InterruptedException, IOException {
		CajoClient client = new CajoClient(createConnector(), System.currentTimeMillis());
		client.addObserver(getWorkProgressObserver());
		return client;
	}

	protected CajoConnector createConnector() throws IOException {
		return new CajoConnector();
	}
}
