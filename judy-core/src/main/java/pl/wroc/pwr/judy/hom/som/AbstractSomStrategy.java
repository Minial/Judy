package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.common.IMutationPoint;
import pl.wroc.pwr.judy.hom.MutationInfo;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Base class for all implementations of second order mutation generation
 * algorithms
 *
 * @author TM
 */
public abstract class AbstractSomStrategy implements SomFactory {

	@Override
	public abstract List<IMutant> create(List<IMutant> foms, List<IMutationOperator> operators);

	/**
	 * @param fom               - starting point - this mutant's operator, index and
	 *                          mutationPoint will be checked as first
	 * @param bytecode          - bytecode of mutant used as base for SOM generation
	 * @param mutationOperators - list of mutation operators
	 * @return mutation info with possible mutation point index, mutant index
	 * and operator
	 */
	// FIXME iterator zamiast indeksow?
	public MutationInfo findNearestMutationPoint(IMutant fom, byte[] bytecode, List<IMutationOperator> mutationOperators) {
		int operatorIndex = fom.getLastOperatorIndex();
		int pointIndex = fom.getMutionPointsIndexes().iterator().next();
		int mutationIndex = fom.getMutantIndex();

		IMutationOperator operator = null;
		List<IMutationPoint> mutationPoints;
		boolean add = operatorIndex < mutationOperators.size() / 2;

		boolean found = false;

		while (!found && operatorIndex >= 0 && operatorIndex < mutationOperators.size()) {
			operator = mutationOperators.get(operatorIndex);
			mutationPoints = operator.getMutationPoints(bytecode);
			if (pointIndex >= mutationPoints.size()) {
				pointIndex = mutationPoints.size() - 1;
			}

			if (!mutationPoints.isEmpty() && mutationIndex >= mutationPoints.get(pointIndex).getIndex()) {
				mutationIndex = mutationPoints.get(pointIndex).getIndex();
			}
			if (pointIndex >= 0) {
				found = true;
			} else {
				if (add) {
					operatorIndex++;
				} else {
					operatorIndex--;
				}
				pointIndex = 0;
				mutationIndex = 0;
			}
		}

		if (operatorIndex < 0 || operatorIndex >= mutationOperators.size()) {
			throw new NoSuchElementException("No mutation point found");
		}

		return new MutationInfo(operator, pointIndex, mutationIndex);
	}
}
