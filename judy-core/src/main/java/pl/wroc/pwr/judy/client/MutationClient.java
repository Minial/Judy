package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.cluster.IQueueClient;
import pl.wroc.pwr.cluster.IResult;
import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.MutationException;
import pl.wroc.pwr.judy.utils.Timer;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

/**
 * Mutation client implementation. This class is responsible for submitting new
 * task and waiting for computed results of mutation analysis.
 *
 * @author pmiwaszko
 */
public abstract class MutationClient implements IMutationClient {
	private IInitialTestsRun testRun;
	private List<String> targetClasses;
	private List<IDescriptable> operators;
	private IMutationWorkFactory workFactory;
	private Observer workProgressObserver;
	public MatrixExecution MatrixE;
	public MatrixCoverage MatrixC;

	/**
	 * <code>MutationClient</code> constructor.
	 *
	 * @param testRun       initial JUnit tests run results with detailed information
	 *                      about coverage and class hierarchy
	 * @param targetClasses source classes to be mutated
	 * @param operators     mutation operators
	 * @param workFactory   factory for mutation works
	 */
	public MutationClient(IInitialTestsRun testRun, List<String> targetClasses, List<IDescriptable> operators,
						  IMutationWorkFactory workFactory, Observer workProgressObserver, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		this.testRun = testRun;
		this.targetClasses = targetClasses;
		this.operators = operators;
		this.workFactory = workFactory;
		this.workProgressObserver = workProgressObserver;
		this.MatrixE = MatrixE;
		this.MatrixC=MatrixC;
	}

	protected IMutationResult createMutationResult(IQueueClient client) throws WorkException, InterruptedException,
			MutationException {
		Timer timer = new Timer();
		List<IClassMutationResult> results = compute(client);
		return createResult(timer, testRun, results);
	}

	/**
	 * Schedule actual mutation analysis tasks and wait for results.
	 *
	 * @param client
	 * @throws WorkException
	 * @throws InterruptedException
	 */
	private List<IClassMutationResult> compute(IQueueClient client) throws WorkException, InterruptedException {
		IEnvironmentFactory factory = new EnvironmentFactory(testRun.getInheritance());

		EffortEstimator estimator = new EffortEstimator(testRun.getPassingResults().size(), targetClasses,
				testRun.getCoverage());

		for (String targetClassName : targetClasses) {
			float estimatedEffort = estimator.estimate(targetClassName);
			TargetClass tc = createTargetClass(targetClassName, estimatedEffort);
			client.schedule(createMutationWork(client.getId(), factory, tc, MatrixE, MatrixC));
			//System.out.println("hey !");
		}
		List<IClassMutationResult> temp = createMutationResults(client.getResults());
		return temp;
	}

	private TargetClass createTargetClass(String targetClassName, float estimatedEffort) {
		TargetClass tc = new TargetClass(targetClassName, estimatedEffort, testRun);
		return tc;
	}

	private List<IClassMutationResult> createMutationResults(List<IResult> resultList) {
		List<IClassMutationResult> list = new LinkedList<>();
		for (IResult res : resultList) {
			list.add((IClassMutationResult) res);
		}
		return list;
	}

	private IWork createMutationWork(long id, IEnvironmentFactory envFactory, TargetClass targetClass, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		return workFactory.create(id, targetClass, envFactory, MatrixE, MatrixC);
	}

	protected IMutationResult createResult(Timer timer, IInitialTestsRun tests, List<IClassMutationResult> results) {
		return new MutationResult(timer.getDuration(), operators, tests.getPassingResults(), results);
	}

	/**
	 * @return the testRun
	 */
	public IInitialTestsRun getTestRun() {
		return testRun;
	}

	/**
	 * Returns observer set for this mutation client.
	 *
	 * @return object which will be observing work progress
	 */
	public Observer getWorkProgressObserver() {
		return workProgressObserver;
	}
}
