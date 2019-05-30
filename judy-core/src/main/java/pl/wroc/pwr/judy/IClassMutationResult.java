package pl.wroc.pwr.judy;

import pl.wroc.pwr.cluster.IResult;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.utils.ClassKind;

import java.util.List;

/**
 * Result of mutation analysis for a single class.
 *
 * @author pmiwaszko
 */
public interface IClassMutationResult extends IResult {
	/**
	 * Get mutated class.
	 */
	String getClassName();

	/**
	 * Get kind of class.
	 *
	 * @see pl.wroc.pwr.judy.utils.ClassKind
	 */
	ClassKind getClassKind();

	/**
	 * Get mutants count.
	 */
	int getMutantsCount();

	/**
	 * Get count of killed mutants.
	 */
	int getKilledMutantsCount();

	/**
	 * Get mutation score.
	 */
	double getMutationScore();

	/**
	 * Get list of killed mutants.
	 */
	List<IMutant> getKilledMutants();

	/**
	 * Get list of alive (not killed) mutants.
	 */
	List<IMutant> getAliveMutants();

	/**
	 * Get time and duration statistics of class analysis.
	 */
	IDurationStatistic getStatistic();

	String getSummary();

	/**
	 * Get mutated class.
	 */
	ITargetClass getTargetClass();

	/**
	 * Get all mutants
	 *
	 * @return mutants
	 */
	List<IMutant> getMutants();
}
