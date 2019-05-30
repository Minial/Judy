package pl.wroc.pwr.judy.hom.problems;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.EncodingUtils;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.common.IMutationPoint;
import pl.wroc.pwr.judy.general.IMutantEvaluator;
import pl.wroc.pwr.judy.general.TestResultList;
import pl.wroc.pwr.judy.hom.AbstractBestHomProblemTest;
import pl.wroc.pwr.judy.hom.FomFactory;
import pl.wroc.pwr.judy.hom.HomFactory;
import pl.wroc.pwr.judy.hom.MutationInfo;
import pl.wroc.pwr.judy.hom.filter.IMutantFilter;
import pl.wroc.pwr.judy.hom.filter.TrivialMutantFilter;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;
import pl.wroc.pwr.judy.hom.research.IHomDataCollector;
import pl.wroc.pwr.judy.operators.MutationPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.*;

public class OnTheFlyBestHomProblemTest extends AbstractBestHomProblemTest {
	private static final String OPERATOR_NAME_A = "MO1";
	private static final String OPERATOR_NAME_B = "MO2";

	private OnTheFlyBestHomProblem problem;

	private IMutantEvaluator evaluator;
	private FomFactory fomFactory;

	private byte[] bytecode;

	private List<IMutationOperator> operators;
	private BinaryVariable binaryMutant;
	private List<IMutationPoint> pointsA;
	private List<IMutationPoint> pointsB;
	private IMutationOperator operatorA;
	private IMutationOperator operatorB;
	private HomFactory homFactory;
	private IMutantFilter dummyFilter;

	@Before
	public void setUp() throws Exception {
		bytecode = new byte[]{1, 2, 3};

		pointsA = preparePoints(0, 1);
		pointsB = preparePoints(0, 1, 2);

		operatorA = mockMutationOperator(OPERATOR_NAME_A, pointsA);
		operatorB = mockMutationOperator(OPERATOR_NAME_B, pointsB);
		operators = Arrays.asList(operatorA, operatorB);

		calculators = new ArrayList<>();

		evaluator = Mockito.mock(IMutantEvaluator.class);
		fomFactory = Mockito.mock(FomFactory.class);
		homFactory = Mockito.mock(HomFactory.class);

		binaryMutant = new BinaryVariable(10);

		problem = createProblem();

		dummyFilter = mockFilter();

	}

	@Test
	public void shouldPenalizeHomWithOrderBelowTwo() throws Exception {
		checkPenalization(3, 1);
	}

	@Test
	public void shouldPenalizeHomWithOrderOverMaximum() throws Exception {
		checkPenalization(17, 16);
	}

	private void checkPenalization(int fomsCount, int cardinality) {
		binaryMutant = createBinaryMutant(fomsCount, cardinality);
		mockInvalidValueCalculators(INVALID_VALUE);
		Solution s = prepareSolution(binaryMutant);

		problem = createProblem();
		problem.evaluate(s);

		assertSolutionIsPenalized(s);
	}

	private void assertSolutionIsPenalized(Solution s) {
		assertEquals(INVALID_VALUE, s.getObjective(0), PRECISION);
		assertEquals(INVALID_VALUE, s.getObjective(1), PRECISION);
	}

	@Test
	public void shouldNotEvaluateHomFromTrivialMutants() throws Exception {
		double objectiveValue = 0.545;
		double objectiveValue2 = 0.234;

		binaryMutant.set(0, true);
		binaryMutant.set(4, true);
		binaryMutant.set(5, true);

		List<IMutant> foms = createFoms(binaryMutant.cardinality());
		mockFomFactoryReturningFoms(new MutationInfo(operatorA, 0, 0), foms.get(0));
		mockFomFactoryReturningFoms(new MutationInfo(operatorB, 0, 0), foms.get(1));
		mockFomFactoryReturningFoms(new MutationInfo(operatorB, 0, 1), foms.get(2));

		IMutant hom = Mockito.mock(IMutant.class);

		calculators.add(mockCalculator(objectiveValue, hom, foms, foms.size()));
		calculators.add(mockCalculator(objectiveValue2, hom, foms, foms.size()));

		Solution s = prepareSolution(binaryMutant);

		problem = createProblem(mockFactory(hom, foms, operators), new TrivialMutantFilter());
		problem.evaluate(s);

		assertEquals(0.0, s.getObjective(0), PRECISION);
		assertEquals(0.0, s.getObjective(1), PRECISION);
		Mockito.verify(evaluator, Mockito.never()).evaluate(Arrays.asList(hom), true);
	}

	@Test
	public void shouldNotEvaluateAlreadyEvaluatedFoms() throws Exception {
		binaryMutant.set(0, true);
		binaryMutant.set(4, true);

		List<IMutant> foms = createFoms(1);
		foms.add(mockEvalueatedFom());

		mockFomFactoryReturningFoms(new MutationInfo(operatorA, 0, 0), foms.get(0));
		mockFomFactoryReturningFoms(new MutationInfo(operatorB, 0, 0), foms.get(1));

		IMutant hom = Mockito.mock(IMutant.class);
		Solution s = prepareSolution(binaryMutant);

		problem = createProblem(mockFactory(hom, foms, operators));
		problem.evaluate(s);

		Mockito.verify(evaluator).evaluate(Arrays.asList(hom), true);
		Mockito.verify(evaluator).evaluate(foms.subList(0, 1), false);
	}

	@Test
	public void shouldCreateAndEvaluateHom() throws Exception {
		double objectiveValue = 0.1337;
		double objectiveValue2 = 0.7331;

		binaryMutant.set(0, true);
		binaryMutant.set(4, true);
		binaryMutant.set(5, true);

		List<IMutant> foms = createFoms(binaryMutant.cardinality());
		mockFomFactoryReturningFoms(new MutationInfo(operatorA, 0, 0), foms.get(0));
		mockFomFactoryReturningFoms(new MutationInfo(operatorB, 0, 0), foms.get(1));
		mockFomFactoryReturningFoms(new MutationInfo(operatorB, 0, 1), foms.get(2));

		IMutant hom = Mockito.mock(IMutant.class);

		calculators.add(mockCalculator(objectiveValue, hom, foms, foms.size()));
		calculators.add(mockCalculator(objectiveValue2, hom, foms, foms.size()));

		Solution s = prepareSolution(binaryMutant);

		problem = createProblem(mockFactory(hom, foms, operators));
		problem.evaluate(s);

		assertEquals(objectiveValue, s.getObjective(0), PRECISION);
		assertEquals(objectiveValue2, s.getObjective(1), PRECISION);

		Mockito.verify(evaluator).evaluate(Arrays.asList(hom), true);
	}

	@Test
	public void shouldPenalizeSolutionWhichCannotBeCreatedAsHomDueToIncompatibility() throws Exception {
		binaryMutant.set(0, true);
		binaryMutant.set(2, true);
		binaryMutant.set(4, true);

		List<IMutant> foms = createFoms(binaryMutant.cardinality());
		mockFomFactoryReturningFoms(new MutationInfo(operatorA, 0, 0), foms.get(0));
		mockFomFactoryReturningFoms(new MutationInfo(operatorA, 1, 0), foms.get(1));
		mockFomFactoryReturningFoms(new MutationInfo(operatorB, 0, 0), foms.get(2));

		mockInvalidValueCalculators(INVALID_VALUE);
		Solution s = prepareSolution(binaryMutant);

		HomFactory factory = mockFailingFactory(foms, operators);
		problem = createProblem(factory);
		problem.evaluate(s);

		assertSolutionIsPenalized(s);
	}

	@Test
	public void shouldLoadAllPossibleMutations() throws Exception {
		List<MutationInfo> possibleMutations = problem.getPossibleMutations();

		assertNotNull(possibleMutations);
		assertEquals(10, possibleMutations.size());

		assertTrue(possibleMutations.contains(new MutationInfo(operatorA, 0, 0)));
		assertTrue(possibleMutations.contains(new MutationInfo(operatorA, 0, 1)));
		assertTrue(possibleMutations.contains(new MutationInfo(operatorA, 1, 0)));
		assertTrue(possibleMutations.contains(new MutationInfo(operatorA, 1, 1)));

		assertTrue(possibleMutations.contains(new MutationInfo(operatorB, 0, 0)));
		assertTrue(possibleMutations.contains(new MutationInfo(operatorB, 0, 1)));
		assertTrue(possibleMutations.contains(new MutationInfo(operatorB, 1, 0)));
		assertTrue(possibleMutations.contains(new MutationInfo(operatorB, 1, 1)));
		assertTrue(possibleMutations.contains(new MutationInfo(operatorB, 2, 0)));
		assertTrue(possibleMutations.contains(new MutationInfo(operatorB, 2, 1)));
	}

	@Test
	public void shouldLoadAllPossibleMutationsOnlyOnce() throws Exception {
		problem.newSolution();
		problem.newSolution();

		Mockito.verify(operatorA, Mockito.times(1)).getMutationPoints(bytecode);
		Mockito.verify(operatorB, Mockito.times(1)).getMutationPoints(bytecode);
	}

	@Test
	public void shouldPrepareEmptySolutionWithBinaryRepresentation() throws Exception {
		calculators.add(Mockito.mock(ObjectiveCalculator.class));
		calculators.add(Mockito.mock(ObjectiveCalculator.class));

		problem = createProblem();

		Solution s = problem.newSolution();
		assertEquals(1, s.getNumberOfVariables());
		assertEquals(2, s.getNumberOfObjectives());
		assertTrue(s.getVariable(0) instanceof BinaryVariable);

		BitSet variable = EncodingUtils.getBitSet(s.getVariable(0));
		assertEquals(0, variable.cardinality());
		boolean[] variableArray = EncodingUtils.getBinary(s.getVariable(0));
		assertEquals(10, variableArray.length);
	}

	private OnTheFlyBestHomProblem createProblem(HomFactory homFactory, IMutantFilter filter) {
		OnTheFlyBestHomProblem problem = new OnTheFlyBestHomProblem(operators, evaluator, calculators, homFactory,
				fomFactory, bytecode, Mockito.mock(IHomDataCollector.class), MAX_MUTATION_ORDER, filter);
		return problem;
	}

	private OnTheFlyBestHomProblem createProblem(HomFactory homFactory) {
		OnTheFlyBestHomProblem problem = new OnTheFlyBestHomProblem(operators, evaluator, calculators, homFactory,
				fomFactory, bytecode, Mockito.mock(IHomDataCollector.class), MAX_MUTATION_ORDER, dummyFilter);
		return problem;
	}

	private OnTheFlyBestHomProblem createProblem() {
		OnTheFlyBestHomProblem problem = new OnTheFlyBestHomProblem(operators, evaluator, calculators, homFactory,
				fomFactory, bytecode, Mockito.mock(IHomDataCollector.class), MAX_MUTATION_ORDER, dummyFilter);
		return problem;
	}

	private IMutant mockEvalueatedFom() {
		IMutant mutant = Mockito.mock(IMutant.class);

		TestResultList results = new TestResultList();
		results.add(Mockito.mock(ITestResult.class));

		Mockito.when(mutant.getResults()).thenReturn(results);
		return mutant;
	}

	private void mockFomFactoryReturningFoms(MutationInfo mutationInfo, IMutant fom) {
		Mockito.when(fomFactory.createFom(mutationInfo)).thenReturn(fom);
	}

	private Solution prepareSolution(BinaryVariable binaryMutant) {
		Solution s = new Solution(1, calculators.size());
		s.setVariable(0, binaryMutant);
		return s;
	}

	private List<IMutationPoint> preparePoints(int... points) {
		List<IMutationPoint> mutations = new ArrayList<>();
		for (int point : points) {
			mutations.add(new MutationPoint(point, 2));
		}
		return mutations;
	}

	private IMutationOperator mockMutationOperator(String name, List<IMutationPoint> points) {
		IMutationOperator operator = mockMutationOperator(name);
		Mockito.when(operator.getMutationPoints(bytecode)).thenReturn(points);
		return operator;
	}

	private IMutantFilter mockFilter() {
		return new IMutantFilter() {
			@Override
			public List<IMutant> filter(List<IMutant> mutants) {
				return mutants;
			}
		};
	}
}
