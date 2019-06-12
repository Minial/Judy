package pl.wroc.pwr.judy.general;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;

public interface IMutantEvaluator {
	/**
	 * Evaluates collection of mutants - checks whether they are killed by tests
	 * or not
	 *
	 * @param mutants mutants to evaluate
	 * @param include whether to save alive/killed mutants
	 * @param MatrixExecution
	 */
	void evaluate(List<IMutant> mutants, boolean include, MatrixExecution MatrixE);
	
	/**
	 * Evaluates collection of mutants - checks whether they are killed by tests
	 * or not
	 *
	 * @param mutants mutants to evaluate
	 * @param include whether to save alive/killed mutants
	 */
	void evaluate(List<IMutant> mutants, boolean include);

	/**
	 * Gets mutants killed by tests during evaluation process
	 *
	 * @return killed mutants
	 */
	List<IMutant> getKilledMutants();

	/**
	 * Gets mutants which survived evaluation process (were not killed by tests)
	 *
	 * @return alive mutants
	 */
	List<IMutant> getAliveMutants();

	/**
	 * Trims evaluator data collection size in order to save up memmory
	 */
	void trimSize();
}
