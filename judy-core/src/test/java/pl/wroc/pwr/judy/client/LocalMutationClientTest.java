package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.cluster.IQueueClient;
import pl.wroc.pwr.cluster.IQueueWorker;
import pl.wroc.pwr.cluster.Queue;
import pl.wroc.pwr.cluster.local.LocalClient;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.MatrixExecution;
import pl.wroc.pwr.judy.MatrixCoverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocalMutationClientTest extends AbstractMutationClientTest {

	private static final String WORKSPACE = "workspace";
	private static final int THREAD_COUNT = 10;

	private LocalMutationClient client;
	protected IQueueWorker worker;
	public MatrixExecution MatrixE;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		worker = Mockito.mock(IQueueWorker.class);
		client = createLocalMutationClientMockingCreationMethods();
	}

	@Test
	public void shouldScheduleWorkForEverySourceClassAndReturnResults() throws Exception {
		IMutationResult res = client.compute();

		assertEquals(getOperators(), res.getOperators());
		assertTrue(res.getTests().isEmpty());
		assertEquals(getResults().size(), res.getResults().size());

		veriftWorkScheduling(getQueueClient(), getTargetClasses().size());
		verifyWorkCreation(getFactory(), getTargetClasses().size());
	}

	@Test
	public void shouldReturnDefaultThreadQueueSize() throws Exception {
		client = new LocalMutationClient(getTestRun(), getTargetClasses(), getOperators(), getFactory(), getObserver(),
				WORKSPACE, 0, MatrixE);
		assertEquals(10, client.getQueueSize());
	}

	@Test
	public void shouldReturnThreadQueueSize() throws Exception {
		assertEquals(30, client.getQueueSize());
	}

	@Test
	public void shouldCreateCajoClient() throws Exception {
		client = createLocalMutationClientMockingWorkerCreation();
		IQueueClient result = client.createQueueClient(null);
		assertTrue(result instanceof LocalClient);
		LocalClient client = (LocalClient) result;
		assertEquals(1, client.countObservers());
	}

	private LocalMutationClient createLocalMutationClientMockingCreationMethods() {
		return new LocalMutationClient(getTestRun(), getTargetClasses(), getOperators(), getFactory(), getObserver(),
				WORKSPACE, THREAD_COUNT, MatrixE) {
			@Override
			protected IQueueClient createQueueClient(Queue queue) {
				return LocalMutationClientTest.this.getQueueClient();
			}

			@Override
			protected IQueueWorker createWorker(Queue queue, IQueueClient client) {
				return worker;
			}
		};
	}

	private LocalMutationClient createLocalMutationClientMockingWorkerCreation() {
		return new LocalMutationClient(getTestRun(), getTargetClasses(), getOperators(), getFactory(), getObserver(),
				WORKSPACE, THREAD_COUNT, MatrixE) {
			@Override
			protected IQueueWorker createWorker(Queue queue, IQueueClient client) {
				return worker;
			}
		};
	}
}
