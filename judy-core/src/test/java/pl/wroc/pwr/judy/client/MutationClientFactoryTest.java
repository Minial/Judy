package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.IClientConfig;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.helpers.ObjectHelper;
import pl.wroc.pwr.judy.MatrixExecution;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.work.MutationResultFormatter;

import java.util.Arrays;
import java.util.Observer;

import static org.junit.Assert.*;

public class MutationClientFactoryTest {

	private IMutationClientFactory factory;
	private IClientConfig config;
	private IInitialTestsRun testRun;
	private IMutationOperatorsFactory operatorsFactory;
	private Observer observer;
	public MatrixExecution MatrixE;
	
	@Before
	public void setUp() throws Exception {
		testRun = Mockito.mock(IInitialTestsRun.class);
		operatorsFactory = Mockito.mock(IMutationOperatorsFactory.class);
		observer = Mockito.mock(Observer.class);
		config = mockConfig(operatorsFactory);
		factory = new MutationClientFactory(config, testRun, MatrixE);
	}

	@Test
	public void shouldCreateLocalClient() throws Exception {
		mockTestResultsWithPassingTests();
		IMutationClient mutationClient = createAndCheckClient(false);
		assertTrue(mutationClient instanceof LocalMutationClient);
		LocalMutationClient localMutationClient = (LocalMutationClient) mutationClient;
		assertSame(observer, localMutationClient.getWorkProgressObserver());
	}

	@Test
	public void shouldCreateDistributedClient() throws Exception {
		mockTestResultsWithPassingTests();
		IMutationClient mutationClient = createAndCheckClient(true);
		assertTrue(mutationClient instanceof DistributedMutationClient);
		DistributedMutationClient distributedMutationClient = (DistributedMutationClient) mutationClient;
		assertSame(observer, distributedMutationClient.getWorkProgressObserver());
	}

	@Test
	public void shouldCreateEmptyClientWhenAllTestsFail() throws Exception {
		assertTrue(createAndCheckClient(true) instanceof EmptyMutationClient);
	}

	private IClientConfig mockConfig(IMutationOperatorsFactory operatorsFactory) {
		MutationResultFormatter resultFormatter = Mockito.mock(MutationResultFormatter.class);
		Mockito.when(resultFormatter.getWorkObserver()).thenReturn(observer);

		IClientConfig config = Mockito.mock(IClientConfig.class);
		Mockito.when(config.getOperatorsFactory()).thenReturn(operatorsFactory);
		Mockito.when(config.getResultFormatter()).thenReturn(resultFormatter);
		return config;
	}

	private void mockTestResultsWithPassingTests() {
		Mockito.when(testRun.getPassingResults()).thenReturn(Arrays.asList(mockTestResult(), mockTestResult()));
	}

	private ITestResult mockTestResult() {
		return Mockito.mock(ITestResult.class);
	}

	private IMutationClient createAndCheckClient(boolean useCluster) {
		ObjectHelper.setFieldValue(factory, "useCluster", useCluster);
		IMutationClient client = factory.createClient();
		assertNotNull(client);
		return client;
	}
}
