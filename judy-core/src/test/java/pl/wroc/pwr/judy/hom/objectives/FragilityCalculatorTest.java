package pl.wroc.pwr.judy.hom.objectives;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.general.TestResultList;

import java.util.*;

import static java.util.Collections.addAll;
import static org.junit.Assert.assertEquals;

public class FragilityCalculatorTest extends ObjectiveCalculatorTest {

	private FragilityCalculator calculator;

	@Before
	public void setUp() {
		calculator = new FragilityCalculator();
	}

	@Test
	public void shouldCountFragility() throws Exception {
		Set<String> testMethods = prepareTestMethods();

		Set<String> killingTestMethods = new HashSet<>();
		killingTestMethods.add("a");
		killingTestMethods.add("d");

		checkFragility(0.5, mockMutant(testMethods, killingTestMethods));
	}

	@Test
	public void shouldCountFragilityForDifferentMutants() throws Exception {
		Set<String> testMethods = new HashSet<>(Arrays.asList("a", "c", "d"));
		Set<String> killingTestMethods = new HashSet<>(Arrays.asList("a", "c"));
		Set<String> killingTestMethods2 = new HashSet<>(Arrays.asList("a"));

		IMutant weakMutant = mockMutant(prepareTestMethods(), killingTestMethods);
		IMutant mutant = mockMutant(testMethods, killingTestMethods2);

		double expectedFragility = 2 / 4.0;
		checkFragility(expectedFragility, weakMutant, mutant);
	}

	@Test
	public void shouldCountFragilityForOnlyWeakMutants() throws Exception {
		IMutant m1 = mockMutant(prepareTestMethods(), prepareTestMethods());
		IMutant m2 = mockMutant(prepareTestMethods(), prepareTestMethods());
		IMutant m3 = mockMutant(prepareTestMethods(), prepareTestMethods());

		checkFragility(1.0, m1, m2, m3);
	}

	@Test
	public void shouldUseFailingMethodsUnionForMutantsWithoutCommonFailingTestcase() throws Exception {
		IMutant m1 = mockMutant(prepareTestMethods(), new HashSet<>(Arrays.asList("a")));
		IMutant m2 = mockMutant(prepareTestMethods(), new HashSet<>(Arrays.asList("b")));

		checkFragility(0.5, m1, m2);
	}

	@Test
	public void shouldCountFitnessOfHomWeakerThanUsedFoms() throws Exception {
		Set<String> testMethods = new HashSet<>(Arrays.asList("a", "c", "d"));
		Set<String> killingTestMethods = new HashSet<>(Arrays.asList("a"));

		IMutant weakMutant = mockMutant(prepareTestMethods(), prepareTestMethods());
		IMutant mutant = mockMutant(testMethods, killingTestMethods);
		List<IMutant> foms = Arrays.asList(weakMutant, mutant);

		double fomsFragility = 4 / 4.0;
		assertEquals(0.33333, calculator.getFragility(mutant), PRECISION);
		assertEquals(fomsFragility, calculator.getFragility(foms), PRECISION);
	}

	@Test
	public void shouldCountfragilityForImmortalMutant() throws Exception {
		checkFragility(0.0, mockMutant(prepareTestMethods(), new HashSet<String>()));
	}

	@Test
	public void shouldCountfragilityVeryWeakMutant() throws Exception {
		checkFragility(1.0, mockMutant(prepareTestMethods(), prepareTestMethods()));
	}

	protected IMutant mockMutant(ITestResult testResult) {
		IMutant mutant = Mockito.mock(IMutant.class);
		TestResultList testResults = new TestResultList();
		testResults.add(testResult);
		Mockito.when(mutant.getResults()).thenReturn(testResults);
		return mutant;
	}

	protected Set<String> prepareTestMethods() {
		Set<String> testMethods = new HashSet<>();
		testMethods.add("a");
		testMethods.add("b");
		testMethods.add("c");
		testMethods.add("d");
		return testMethods;
	}

	protected IMutant mockMutant(Set<String> testMethods, Set<String> killingTestMethods) {
		ITestResult testResult = mockTestResult(testMethods, killingTestMethods);
		return mockMutant(testResult);
	}

	private ITestResult mockTestResult(Set<String> testMethods, Set<String> killingTestMethods) {
		ITestResult testResult = Mockito.mock(ITestResult.class);
		Mockito.when(testResult.getTestMethods()).thenReturn(testMethods);
		Mockito.when(testResult.getFailingTestMethods()).thenReturn(killingTestMethods);
		return testResult;
	}

	protected List<IMutant> prepareFoms(IMutant... mutants) {
		List<IMutant> foms = new ArrayList<>();
		addAll(foms, mutants);
		return foms;
	}

	private void checkFragility(double expected, IMutant... mutants) {
		List<IMutant> foms = prepareFoms(mutants);
		assertEquals(expected, calculator.getFragility(foms), PRECISION);
	}

	private void checkFragility(double expectedFragility, IMutant mutant) {
		assertEquals(expectedFragility, calculator.getFragility(mutant), PRECISION);
	}
}
