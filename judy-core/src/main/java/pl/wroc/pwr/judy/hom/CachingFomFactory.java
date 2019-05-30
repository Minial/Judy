package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.utils.Timer;
import pl.wroc.pwr.judy.work.Mutant;

import java.util.List;

public class CachingFomFactory implements FomFactory {
	private byte[] bytecode;
	private String targetClassName;
	private List<IMutationOperator> operators;

	private IMutantCache cache = new MutantCache();
	private IDurationStatistic statistic;

	/**
	 * Creates FOM factory operating on target class' bytecode and counting
	 * mutant generation time
	 *
	 * @param bytecode        target class's bytecode
	 * @param targetClassName target class name
	 * @param operators       mutation operators
	 * @param statistic       duration statistics
	 */
	public CachingFomFactory(byte[] bytecode, String targetClassName, List<IMutationOperator> operators,
							 IDurationStatistic statistic) {
		this.bytecode = bytecode;
		this.targetClassName = targetClassName;
		this.operators = operators;
		this.statistic = statistic;
	}

	private IMutant mutate(IMutationOperator operator, int pointIndex, int mutantIndex) {
		Timer timer = new Timer();
		IMutantBytecode mutant = operator.mutate(bytecode, pointIndex, mutantIndex);
		statistic.addMutantGenration(timer.getDuration());

		return new Mutant(operator.getName(), pointIndex, mutantIndex, targetClassName, mutant.getLineNumber(),
				mutant.getDescription(), mutant, operators.indexOf(operator));
	}

	@Override
	public IMutant createFom(MutationInfo info) {
		IMutant mutant = cache.get(info);
		if (mutant == null) {
			IMutationOperator operator = info.getOperator();
			mutant = mutate(operator, info.getMutationPointIndex(), info.getMutantIndex());
			cache.add(info, mutant);
		}
		return mutant;
	}
}
