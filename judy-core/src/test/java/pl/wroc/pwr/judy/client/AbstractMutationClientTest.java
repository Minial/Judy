package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.cluster.IQueueClient;
import pl.wroc.pwr.cluster.IResult;
import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.cluster.WorkException;
import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.general.ICoverage;
import pl.wroc.pwr.judy.MatrixExecution;
import pl.wroc.pwr.judy.MatrixCoverage;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class AbstractMutationClientTest {
	private IInitialTestsRun testRun;
	private List<IDescriptable> operators;
	private List<String> targetClasses;
	private IMutationWorkFactory factory;
	private IQueueClient queueClient;
	private List<IResult> results;
	private Observer observer;
	public MatrixExecution MatrixE;

	@Before
	public void setUp() throws Exception {
		targetClasses = prepareTargetClasses();
		testRun = mockTestRun(prepareCoverage(targetClasses, 10));

		results = prepareResults();
		queueClient = mockQueueClient(results);

		factory = Mockito.mock(IMutationWorkFactory.class);
		observer = Mockito.mock(Observer.class);
		operators = prepareOperators();
	}

	protected IInitialTestsRun mockTestRun(ICoverage coverage) {
		IInitialTestsRun tr = Mockito.mock(IInitialTestsRun.class);
		Mockito.when(tr.getPassingResults()).thenReturn(new ArrayList<ITestResult>());
		Mockito.when(tr.getCoverage()).thenReturn(coverage);
		return tr;
	}

	protected ICoverage prepareCoverage(List<String> targetClasses, int coveringTestClasses) {
		ICoverage coverage = Mockito.mock(ICoverage.class);
		for (String tc : targetClasses) {
			Mockito.when(coverage.countCoveringTestClasses(tc)).thenReturn(coveringTestClasses);
		}
		return coverage;
	}

	protected List<String> prepareTargetClasses() {
		List<String> classes = new ArrayList<>();
		classes.add("A");
		classes.add("B");
		return classes;
	}

	protected List<IResult> prepareResults() {
		List<IResult> results = new ArrayList<>();
		results.add(Mockito.mock(IClassMutationResult.class));
		results.add(Mockito.mock(IClassMutationResult.class));
		results.add(Mockito.mock(IClassMutationResult.class));
		return results;
	}

	protected void verifyWorkCreation(IMutationWorkFactory factory, int workCount) {
		Mockito.verify(factory, Mockito.times(workCount)).create(Matchers.anyInt(), Matchers.any(TargetClass.class),
				Matchers.any(IEnvironmentFactory.class), MatrixE);
	}

	protected void veriftWorkScheduling(IQueueClient queueClient, int workCount) throws WorkException {
		Mockito.verify(queueClient, Mockito.times(workCount)).schedule((IWork) Matchers.anyObject());
	}

	protected IQueueClient mockQueueClient(List<IResult> result) throws InterruptedException {
		IQueueClient queueClient = Mockito.mock(IQueueClient.class);
		Mockito.when(queueClient.getResults()).thenReturn(result);
		return queueClient;
	}

	protected List<IDescriptable> prepareOperators() {
		List<IDescriptable> ops = new ArrayList<>();
		ops.add(Mockito.mock(IDescriptable.class));
		ops.add(Mockito.mock(IDescriptable.class));
		ops.add(Mockito.mock(IDescriptable.class));
		return ops;
	}

	public IInitialTestsRun getTestRun() {
		return testRun;
	}

	public List<IDescriptable> getOperators() {
		return operators;
	}

	public List<String> getTargetClasses() {
		return targetClasses;
	}

	public IMutationWorkFactory getFactory() {
		return factory;
	}

	public IQueueClient getQueueClient() {
		return queueClient;
	}

	public List<IResult> getResults() {
		return results;
	}

	public Observer getObserver() {
		return observer;
	}
}
