package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.hom.MutationInfo;
import pl.wroc.pwr.judy.operators.MutationPoint;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOrderStrategy extends AbstractSomStrategy {
	@Override
	public List<IMutant> create(List<IMutant> foms, List<IMutationOperator> operators) {
		return doMutant(foms, operators);
	}

	/**
	 * @param foms list of first order mutants
	 * @return index of next fom to use
	 */
	protected abstract int getNextFomIndex(List<IMutant> foms);

	public List<IMutant> doMutant(List<IMutant> foms, List<IMutationOperator> operators) {
		List<IMutant> mutants = new ArrayList<>();
		while (foms.size() > 1) {
			IMutant fom1 = foms.get(0);
			int nextIndex = getNextFomIndex(foms);

			IMutant fom2 = foms.get(nextIndex);

			int newPointIndex = fom2.getMutionPointsIndexes().get(0);
			int newMutantIndex = fom2.getMutantIndex();

			MutationInfo mInfo = findNearestMutationPoint(fom2, fom1.getBytecode().getBytecode(), operators);

			IMutationOperator operator = mInfo.getOperator();
			final List<IMutantBytecode> newMutantBytecodes = operator.mutate(fom1.getBytecode().getBytecode(),
					new MutationPoint(mInfo.getMutationPointIndex(), 1));
			IMutant mutant = fom1;

			IMutantBytecode newMutantBytecode = newMutantBytecodes.iterator().next();

			mutant.setBytecode(newMutantBytecode);
			mutant.addMutation(newMutantBytecode.getLineNumber(), operator.getName(), newPointIndex, newMutantIndex);
			mutants.add(mutant);

			foms.remove(fom1);

			if (foms.size() != 2) {
				foms.remove(fom2);
			}

		}
		return mutants;
	}

}
