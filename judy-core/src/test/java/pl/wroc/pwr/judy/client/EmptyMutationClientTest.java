package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.MatrixExecution;
import pl.wroc.pwr.judy.MatrixCoverage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EmptyMutationClientTest {

	private EmptyMutationClient client;
	private IInitialTestsRun testRun;
	private List<IDescriptable> operators;
	public MatrixExecution MatrixE;

	@Before
	public void setUp() throws Exception {
		testRun = mockEmptyTestRun();
		operators = new ArrayList<>();

		client = new EmptyMutationClient(testRun, operators, MatrixE);
	}

	public IInitialTestsRun mockEmptyTestRun() {
		IInitialTestsRun tr = Mockito.mock(IInitialTestsRun.class);
		Mockito.when(tr.getPassingResults()).thenReturn(new ArrayList<ITestResult>());
		return tr;
	}

	@Test
	public void shouldComputeEmptyResults() throws Exception {
		IMutationResult results = client.compute();
		assertTrue(results.getTests().isEmpty());
		assertEquals(results.getOperators(), operators);
		assertNull(results.getResults());
	}

}
