package pl.wroc.pwr.judy.hom.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.general.TestResultList;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TrivialMutantFilterTest {
	private static final String METHOD_2 = "T2";
	private static final String METHOD_1 = "T1";

	private TrivialMutantFilter filter;

	private Set<String> testMethods;
	private Set<String> failedMethods;
	private Set<String> successedMethods;

	@Before
	public void setUp() throws Exception {
		testMethods = new LinkedHashSet<>();
		testMethods.add(METHOD_1);
		testMethods.add(METHOD_2);

		failedMethods = new LinkedHashSet<>();
		failedMethods.add(METHOD_1);

		successedMethods = new LinkedHashSet<>();
		successedMethods.add(METHOD_2);

		filter = new TrivialMutantFilter();
	}

	@Test
	public void shouldFilterOutMutantKilledByAllTests() throws Exception {
		List<IMutant> mutants = Arrays.asList(createFailedMutant(), createSuccessMutant());
		List<IMutant> result = filter.filter(mutants);
		assertEquals(1, result.size());
	}

	@Test
	public void shouldFilterOutMutantKilledByNonTests() throws Exception {
		List<IMutant> mutants = Arrays.asList(createEmptyMutant(), createSuccessMutant());
		List<IMutant> result = filter.filter(mutants);
		assertEquals(1, result.size());
	}

	@Test
	public void shouldLeaveOnlyNonTrivialMutants() throws Exception {
		List<IMutant> mutants = Arrays.asList( // createEmptyMutant(),
				// createFailedMutant(),
				// createSuccessMutant(),
				createNonTrivialMutant(), createNonTrivialMutant());
		List<IMutant> result = filter.filter(mutants);
		assertEquals(2, result.size());
	}

	private IMutant createMutant(List<ITestResult> results) {
		IMutant mutant = Mockito.mock(IMutant.class);

		TestResultList trc = new TestResultList();
		trc.addAll(results);

		Mockito.when(mutant.getResults()).thenReturn(trc);
		return mutant;
	}

	private IMutant createSuccessMutant() {
		return createMutant(Arrays.asList(createSuccessTestResult(), createSuccessTestResult()));
	}

	private IMutant createFailedMutant() {
		return createMutant(Arrays.asList(createFailedTestResult(), createFailedTestResult()));
	}

	private IMutant createEmptyMutant() {
		return createMutant(new ArrayList<ITestResult>());
	}

	private IMutant createNonTrivialMutant() {
		return createMutant(Arrays.asList(createNonTrivialTestResult()));
	}

	private ITestResult createTestResult(Set<String> testMethods, Set<String> successMethods, Set<String> failedMethods) {
		ITestResult tr = Mockito.mock(ITestResult.class);
		Mockito.when(tr.getTestMethods()).thenReturn(testMethods);
		Mockito.when(tr.getSuccessfulTestMethods()).thenReturn(successMethods);
		Mockito.when(tr.getFailingTestMethods()).thenReturn(failedMethods);
		return tr;
	}

	private ITestResult createSuccessTestResult() {
		return createTestResult(testMethods, testMethods, new HashSet<String>());
	}

	private ITestResult createFailedTestResult() {
		return createTestResult(testMethods, new HashSet<String>(), testMethods);
	}

	private ITestResult createNonTrivialTestResult() {
		return createTestResult(testMethods, successedMethods, failedMethods);
	}
}
