package pl.wroc.pwr.judy.hom.utils;

import org.moeaframework.core.PRNG;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Variable;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.variable.BinaryVariable;

import java.util.ArrayList;

/**
 * Extension of MOEA RandomInitialization class. By default all variables are
 * initialized randomly. Because of the MutationOrder objective we want to set
 * the first population size to not bigger than maximal order specified by user.
 *
 * @author Maciej Szewczyszyn
 */
public class CustomBinaryVariableInitialization extends RandomInitialization {

	/**
	 * The maximum order of HOM
	 */
	private final int maxOrder;

	public CustomBinaryVariableInitialization(Problem problem, int populationSize, int maxOrder) {
		super(problem, populationSize);
		this.maxOrder = maxOrder;
	}

	@Override
	protected void initialize(Variable variable) {
		if (variable instanceof BinaryVariable) {
			BinaryVariable binary = (BinaryVariable) variable;
			int numberOfBits = binary.getNumberOfBits();
			if (numberOfBits <= maxOrder) {
				for (int i = 0; i < numberOfBits; i++) {
					binary.set(i, PRNG.nextBoolean());
				}
			} else {
				// Set random bits to true (number of selected bits is not
				// bigger than maxOrder)
				ArrayList<Integer> bitNumbers = new ArrayList<>(numberOfBits);
				for (int i = 0; i < numberOfBits; i++) {
					bitNumbers.add(i);
				}
				int positiveBitsInSolution = PRNG.nextInt(2, maxOrder);
				for (int i = 0; i < positiveBitsInSolution; i++) {
					if (bitNumbers.size() > 0) {
						int randomPosition = PRNG.nextInt(bitNumbers.size());
						binary.set(bitNumbers.get(randomPosition), true);
						bitNumbers.remove(randomPosition);
					}
				}
			}
		} else {
			super.initialize(variable);
		}
	}
}
