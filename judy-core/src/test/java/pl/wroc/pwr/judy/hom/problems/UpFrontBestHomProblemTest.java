package pl.wroc.pwr.judy.hom.problems;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.EncodingUtils;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.general.IMutantEvaluator;
import pl.wroc.pwr.judy.hom.AbstractBestHomProblemTest;
import pl.wroc.pwr.judy.hom.HomFactory;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;
import pl.wroc.pwr.judy.hom.research.IHomDataCollector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpFrontBestHomProblemTest extends AbstractBestHomProblemTest {

	private List<IMutationOperator> operators;
	private List<IMutant> foms;
	private IMutant m1;
	private IMutant m2;
	private IMutant m3;

	private UpFrontBestHomProblem problem;
	private IMutantEvaluator evaluator;

	private HomFactory homFactory;

	@Before
	public void setUp() throws Exception {
		operators = Arrays.asList(mockMutationOperator("MO1"), mockMutationOperator("MO2"),
				mockMutationOperator("MO3"), mockMutationOperator("MO3"));

		m1 = prepareMutant(0, 0, operators);
		m2 = prepareMutant(1, 1, operators);
		m3 = prepareMutant(2, 2, operators);

		foms = Arrays.asList(m1, m2, m3);

		homFactory = Mockito.mock(HomFactory.class);

		evaluator = Mockito.mock(IMutantEvaluator.class);

		calculators = new ArrayList<>();
	}

	@Test
	public void shouldCreateAndEvaluateHom() throws Exception {
		double objectiveValue = 0.1337;
		double objectiveValue2 = 0.7331;
		double objectiveValue3 = -10;

		BinaryVariable binaryMutant = prepareBinaryMutantRepresentation();
		List<IMutant> selectedFoms = prepareSelectedFoms();

		IMutant hom = Mockito.mock(IMutant.class);

		calculators.add(mockCalculator(objectiveValue, hom, selectedFoms, selectedFoms.size()));
		calculators.add(mockCalculator(objectiveValue2, hom, selectedFoms, selectedFoms.size()));
		calculators.add(mockCalculator(objectiveValue3, hom, selectedFoms, selectedFoms.size()));

		problem = createProblem(foms, mockFactory(hom, selectedFoms, operators));

		Solution s = prepareSolution(binaryMutant);

		problem.evaluate(s);
		assertEquals(calculators.size(), s.getNumberOfObjectives());
		assertEquals(objectiveValue, s.getObjective(0), PRECISION);
		assertEquals(objectiveValue2, s.getObjective(1), PRECISION);
		assertEquals(objectiveValue3, s.getObjective(2), PRECISION);
		Mockito.verify(evaluator).evaluate(Arrays.asList(hom), true);
	}

	@Test
	public void shouldPenalizeSolutionWhichCannotBeCreatedAsHomDueToIncompatibility() throws Exception {
		BinaryVariable binaryMutant = prepareBinaryMutantRepresentation();
		List<IMutant> selectedFoms = prepareSelectedFoms();

		mockInvalidValueCalculators(INVALID_VALUE);

		problem = createProblem(foms, mockFailingFactory(selectedFoms, operators));

		Solution s = prepareSolution(binaryMutant);
		problem.evaluate(s);

		assertSolutionIsPenalized(s);
	}

	@Test
	public void shouldPenalizeHomWithOrderBelowTwo() throws Exception {
		checkPenalization(3, 1);
	}

	@Test
	public void shouldPenalizeHomWithOrderOverMaximum() throws Exception {
		checkPenalization(17, 16);
	}

	private void checkPenalization(int mutantsCount, int cardinality) {
		BinaryVariable binaryMutant = createBinaryMutant(mutantsCount, cardinality);
		mockInvalidValueCalculators(INVALID_VALUE);

		problem = createProblem(createFoms(mutantsCount), null);

		Solution s = prepareSolution(binaryMutant);

		problem.evaluate(s);
		assertSolutionIsPenalized(s);
	}

	private UpFrontBestHomProblem createProblem(List<IMutant> mutants, HomFactory factory) {
		UpFrontBestHomProblem p = new UpFrontBestHomProblem(operators, evaluator, calculators, factory, mutants,
				Mockito.mock(IHomDataCollector.class), MAX_MUTATION_ORDER);
		return p;
	}

	private void assertSolutionIsPenalized(Solution s) {
		assertEquals(INVALID_VALUE, s.getObjective(0), PRECISION);
		assertEquals(INVALID_VALUE, s.getObjective(1), PRECISION);
	}

	@Test
	public void shouldPrepareEmptySolutionWithBinaryRepresentation() throws Exception {
		calculators.add(Mockito.mock(ObjectiveCalculator.class));
		calculators.add(Mockito.mock(ObjectiveCalculator.class));
		calculators.add(Mockito.mock(ObjectiveCalculator.class));

		problem = new UpFrontBestHomProblem(operators, evaluator, calculators, homFactory, foms,
				Mockito.mock(IHomDataCollector.class), MAX_MUTATION_ORDER);

		Solution s = problem.newSolution();
		assertEquals(1, s.getNumberOfVariables());
		assertEquals(3, s.getNumberOfObjectives());
		assertTrue(s.getVariable(0) instanceof BinaryVariable);

		BitSet variable = EncodingUtils.getBitSet(s.getVariable(0));
		assertEquals(0, variable.cardinality());
		boolean[] variableArray = EncodingUtils.getBinary(s.getVariable(0));
		assertEquals(foms.size(), variableArray.length);
	}

	private List<IMutant> prepareSelectedFoms() {
		List<IMutant> selectedFoms = new ArrayList<>();
		selectedFoms.add(m1);
		selectedFoms.add(m3);
		return selectedFoms;
	}

	private Solution prepareSolution(BinaryVariable binaryMutant) {
		Solution s = new Solution(1, calculators.size());
		s.setVariable(0, binaryMutant);
		return s;
	}

	private BinaryVariable prepareBinaryMutantRepresentation() {
		BinaryVariable binaryMutant = new BinaryVariable(foms.size());
		binaryMutant.set(0, true);
		binaryMutant.set(1, false);
		binaryMutant.set(2, true);
		return binaryMutant;
	}
}
