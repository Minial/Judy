package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.cluster.IQueueClient;
import pl.wroc.pwr.cluster.cajo.CajoClient;
import pl.wroc.pwr.cluster.cajo.CajoConnector;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.MatrixExecution;
import pl.wroc.pwr.judy.MatrixCoverage;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DistributedMutationClientTest extends AbstractMutationClientTest {

	private DistributedMutationClient client;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		client = createClientWithQueueClientMock();
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
	public void shouldCreateCajoClient() throws Exception {
		client = createClientMockingCajoConnector();
		IQueueClient result = client.createQueueClient();
		assertTrue(result instanceof CajoClient);
		CajoClient client = (CajoClient) result;
		assertEquals(1, client.countObservers());
	}

	private DistributedMutationClient createClientWithQueueClientMock() {
		return new DistributedMutationClient(getTestRun(), getTargetClasses(), getOperators(), getFactory(),
				getObserver(), MatrixE) {
			@Override
			protected IQueueClient createQueueClient() throws InterruptedException, IOException {
				return DistributedMutationClientTest.this.getQueueClient();
			}
		};
	}

	private DistributedMutationClient createClientMockingCajoConnector() {
		return new DistributedMutationClient(getTestRun(), getTargetClasses(), getOperators(), getFactory(),
				getObserver(), MatrixE) {
			@Override
			protected CajoConnector createConnector() throws IOException {
				return Mockito.mock(CajoConnector.class);
			}
		};
	}
}
