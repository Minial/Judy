package pl.wroc.pwr.judy.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.common.MutationException;
import pl.wroc.pwr.judy.utils.Timer;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.io.IOException;
import java.util.List;

public class EmptyMutationClient extends MutationClient {
	private static final Logger LOGGER = LogManager.getLogger(MutationClient.class);

	/**
	 * Creates mutation client producing empty results for test run with no
	 * passing tests
	 *
	 * @param testRun   initial tests run results
	 * @param operators mutation operators descriptions
	 */
	public EmptyMutationClient(IInitialTestsRun testRun, List<IDescriptable> operators, MatrixExecution MatrixE) {
		super(testRun, null, operators, null, null, MatrixE);
	}

	@Override
	public IMutationResult compute() throws InterruptedException, IOException, WorkException, MutationException {
		LOGGER.warn("All initial tests failed! Nothing to mutate.");
		Timer timer = new Timer();
		return createResult(timer, getTestRun(), null);
	}

}
