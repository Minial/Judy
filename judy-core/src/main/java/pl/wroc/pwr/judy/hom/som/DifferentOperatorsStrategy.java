package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.hom.MutationInfo;
import pl.wroc.pwr.judy.operators.MutationPoint;
import pl.wroc.pwr.judy.work.Mutant;

import java.util.ArrayList;
import java.util.List;

/**
 * DifferentOperators algorithm for generating SOMs. Combining pairs of first
 * order mutants from different mutation operators. Every first order mutant is
 * used only once. Maximum number of SOMs is half of FOMs.
 */
public class DifferentOperatorsStrategy extends AbstractSomStrategy {

	@Override
	public List<IMutant> create(List<IMutant> foms, List<IMutationOperator> operators) {
		List<IMutant> mutants = new ArrayList<>();

		while (foms.size() > 1) {
			IMutant fom1 = foms.get(0);
			foms.remove(fom1);
			for (int i = 0; i < foms.size(); i++) {
				IMutant fom2 = foms.get(i);
				if (areOperatorsClassicAndNonClassic(fom1, fom2) || areClassicOperatorsDifferent(fom1, fom2)
						|| areNonClassicOperatorsDifferent(fom1, fom2)) {

					int newPointIndex = fom2.getMutionPointsIndexes().get(0);
					int newMutantIndex = fom2.getMutantIndex();

					MutationInfo mInfo = findNearestMutationPoint(fom2, fom1.getBytecode().getBytecode(), operators);

					IMutationOperator operator = mInfo.getOperator();

					final IMutantBytecode newMutantBytecode = operator
							.mutate(fom1.getBytecode().getBytecode(),
									new MutationPoint(mInfo.getMutationPointIndex(), mInfo.getMutantIndex()))
							.iterator().next();

					IMutant mutant = new Mutant(fom1.getOperatorsNames().get(0), fom1.getMutionPointsIndexes().get(0),
							fom1.getMutantIndex(), fom1.getTargetClassName(), fom1.getLinesNumbers().get(0),
							fom1.getDescription(), newMutantBytecode, fom1.getLastOperatorIndex());

					mutant.addMutation(newMutantBytecode.getLineNumber(), operator.getName(), newPointIndex,
							newMutantIndex);
					mutants.add(mutant);
					foms.remove(fom2);
					break;
				}
			}
		}
		return mutants;
	}

	private boolean areNonClassicOperatorsDifferent(IMutant fom1, IMutant fom2) {
		return !isOperatorClassic(fom1) && !isOperatorClassic(fom2)
				&& !getNonClassicOperatorName(fom1).equals(getNonClassicOperatorName(fom2));
	}

	private boolean areClassicOperatorsDifferent(IMutant fom1, IMutant fom2) {
		return isOperatorClassic(fom1) && isOperatorClassic(fom2) && !areOperatorsFromTheSameGroup(fom1, fom2);
	}

	private boolean areOperatorsClassicAndNonClassic(IMutant fom1, IMutant fom2) {
		return fom1.getOperatorsNames().get(0).contains("_") != fom2.getOperatorsNames().get(0).contains("_");
	}

	private boolean isOperatorClassic(IMutant fom) {
		return fom.getOperatorsNames().get(0).contains("_");
	}

	private boolean areOperatorsFromTheSameGroup(IMutant fom1, IMutant fom2) {
		return getClassicOperatorGroup(fom2).equals(getClassicOperatorGroup(fom1));
	}

	private String getClassicOperatorGroup(IMutant fom) {
		return fom.getOperatorsNames().get(0).split("_")[0];
	}

	private String getNonClassicOperatorName(IMutant fom) {
		return fom.getOperatorsNames().get(0);
	}

}
