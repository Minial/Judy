package pl.wroc.pwr.judy.hom;

import org.mockito.Mockito;
import org.moeaframework.core.variable.BinaryVariable;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.general.TestResultList;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;
import pl.wroc.pwr.judy.operators.MutantBytecode;
import pl.wroc.pwr.judy.work.Mutant;

import java.util.ArrayList;
import java.util.List;

public class AbstractBestHomProblemTest {

	public static final double PRECISION = 0.0001;
	protected static final double INVALID_VALUE = Integer.MAX_VALUE;
	protected static final int MAX_MUTATION_ORDER = 15;

	protected List<ObjectiveCalculator> calculators;

	protected IMutationOperator mockMutationOperator(String name) {
		IMutationOperator operator = Mockito.mock(IMutationOperator.class);
		Mockito.when(operator.getName()).thenReturn(name);
		return operator;
	}

	protected HomFactory mockFactory(IMutant hom, List<IMutant> foms, List<IMutationOperator> operators)
			throws IncompatibleMutationException {
		HomFactory factory = Mockito.mock(HomFactory.class);
		Mockito.when(factory.create(foms, operators)).thenReturn(hom);
		return factory;
	}

	protected ObjectiveCalculator mockCalculator(double objective, IMutant hom, List<IMutant> foms, int order) {
		ObjectiveCalculator calc = Mockito.mock(ObjectiveCalculator.class);
		Mockito.when(calc.calculate(hom, foms, order)).thenReturn(objective);
		return calc;
	}

	protected IMutant prepareMutant(int index, int lineNumber, List<IMutationOperator> operators) {
		return new Mutant(operators.get(index).getName(), index, index, "", lineNumber, "", new MutantBytecode(
				new byte[]{0, 0, 0}, lineNumber), index);
	}

	protected ObjectiveCalculator mockPenalizingCalculator(double value) {
		ObjectiveCalculator calc = Mockito.mock(ObjectiveCalculator.class);
		Mockito.when(calc.getWorstValue()).thenReturn(value);
		return calc;
	}

	protected ObjectiveCalculator mockCalculatorReturningInvalidValue(double value) {
		ObjectiveCalculator calc = Mockito.mock(ObjectiveCalculator.class);
		Mockito.when(calc.getWorstValue()).thenReturn(value);
		return calc;
	}

	/**
	 * @return
	 */
	protected List<IMutant> createFoms(int count) {
		List<IMutant> foms = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			foms.add(mockMutant());
		}
		return foms;
	}

	private IMutant mockMutant() {
		IMutant mutant = Mockito.mock(IMutant.class);
		Mockito.when(mutant.getResults()).thenReturn(new TestResultList());
		return mutant;
	}

	protected void mockInvalidValueCalculators(double invalidValue) {
		calculators.add(mockCalculatorReturningInvalidValue(invalidValue));
		calculators.add(mockCalculatorReturningInvalidValue(invalidValue));
	}

	protected BinaryVariable createBinaryMutant(int foms, int cardinality) {
		BinaryVariable binaryMutant = new BinaryVariable(foms);
		for (int i = 0; i < cardinality; i++) {
			binaryMutant.set(i, true);
		}
		return binaryMutant;
	}

	protected HomFactory mockFailingFactory(List<IMutant> foms, List<IMutationOperator> operators)
			throws IncompatibleMutationException {
		HomFactory factory = Mockito.mock(HomFactory.class);
		Mockito.when(factory.create(foms, operators)).thenThrow(new IncompatibleMutationException("", -1, -1));
		return factory;
	}

}
