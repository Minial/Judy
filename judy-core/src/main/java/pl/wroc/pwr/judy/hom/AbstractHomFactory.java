package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.utils.Timer;
import pl.wroc.pwr.judy.work.Mutant;

import java.util.List;

public abstract class AbstractHomFactory implements HomFactory {
	private IDurationStatistic statistic;

	/**
	 * Creates HOM factory counting mutant generation time
	 *
	 * @param statistic duration statistics
	 */
	public AbstractHomFactory(IDurationStatistic statistic) {
		this.statistic = statistic;
	}

	@Override
	public IMutant create(List<IMutant> foms, List<IMutationOperator> operators) throws IncompatibleMutationException {
		IMutant hom = new Mutant(foms.get(0));

		for (int i = 1; i < foms.size(); i++) {
			IMutant fom = foms.get(i);
			int mutationPointIndex = fom.getMutionPointsIndexes().get(0);
			int mutantIndex = fom.getMutantIndex();

			Timer timer = new Timer();
			IMutationOperator operator = operators.get(fom.getLastOperatorIndex());
			IMutantBytecode mutationBytecode = operator.mutate(hom.getBytecode().getBytecode(), mutationPointIndex,
					mutantIndex);
			statistic.addMutantGenration(timer.getDuration());

			if (allMutationsIntroduced(mutationBytecode) && isMutationValid(hom, mutationBytecode)) {
				hom.setBytecode(mutationBytecode);
				hom.addMutation(mutationBytecode.getLineNumber(), operator.getName(), mutationPointIndex, mutantIndex);
			} else {
				throw new IncompatibleMutationException(operator.getName(), mutationPointIndex, mutantIndex);
			}
		}

		return hom;
	}

	protected abstract boolean isMutationValid(IMutant hom, IMutantBytecode mutationBytecode);

	private boolean allMutationsIntroduced(IMutantBytecode newMutantBytecode) {
		return newMutantBytecode != null && newMutantBytecode.getLineNumber() != -1;
	}
}
