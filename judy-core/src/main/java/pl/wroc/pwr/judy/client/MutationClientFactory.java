package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.IClientConfig;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;
import java.util.Observer;

public class MutationClientFactory implements IMutationClientFactory {

	private IInitialTestsRun testRun;
	private List<IDescriptable> operatorDescriptions;
	private boolean useCluster;
	private List<String> targetClasses;
	private IMutationWorkFactory workFactory;
	private String workspace;
	private int threadsCount;
	private Observer workProgressObserver;
	public MatrixExecution MatrixE;

	/**
	 * Creates mutation client factory with passed with Judy configuration
	 *
	 * @param config  Judy configuration
	 * @param testRun initial JUnit test run results
	 */
	public MutationClientFactory(IClientConfig config, IInitialTestsRun testRun, MatrixExecution MatrixE) {
		this.testRun = testRun;
		operatorDescriptions = config.getOperatorsFactory().getDescriptions();
		useCluster = config.useCluster();
		targetClasses = config.getTargetClasses();
		workFactory = config.getWorkFactory();
		workspace = config.getWorkspace();
		threadsCount = config.getThreadsCount();
		workProgressObserver = config.getResultFormatter().getWorkObserver();
		this.MatrixE = MatrixE;// add matrix
	}

	@Override
	public IMutationClient createClient() {
		if (testRun.getPassingResults().isEmpty()) {
			return new EmptyMutationClient(testRun, operatorDescriptions, MatrixE);
		}
		return useCluster ? createDistributedClient() : createLocalClient();
	}

	private LocalMutationClient createLocalClient() {
		return new LocalMutationClient(testRun, targetClasses, operatorDescriptions, workFactory, workProgressObserver,
				workspace, threadsCount, MatrixE);
	}

	private DistributedMutationClient createDistributedClient() {
		return new DistributedMutationClient(testRun, targetClasses, operatorDescriptions, workFactory,
				workProgressObserver, MatrixE);
	}

	/**
	 * @return the workFactory
	 */
	public IMutationWorkFactory getWorkFactory() {
		return workFactory;
	}

	/**
	 * @param workFactory the workFactory to set
	 */
	@Override
	public void setWorkFactory(IMutationWorkFactory workFactory) {
		this.workFactory = workFactory;
	}

	/**
	 * @return the useCluster
	 */
	public boolean isUseCluster() {
		return useCluster;
	}

	/**
	 * @param useCluster the useCluster to set
	 */
	public void setUseCluster(boolean useCluster) {
		this.useCluster = useCluster;
	}

}
