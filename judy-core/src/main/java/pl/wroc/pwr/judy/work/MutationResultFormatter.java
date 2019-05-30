package pl.wroc.pwr.judy.work;

import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.utils.ClassKind;

import java.util.Observer;

public interface MutationResultFormatter {

	/**
	 * Summarizes mutation execution and formats the output
	 *
	 * @param mutationSummary summary of mutation execution
	 * @param targetClass     target class (class being mutated)
	 * @param kind            target class kind
	 * @param statistic       tests and mutation execution statistics
	 * @return summary string
	 */
	String getSummary(MutationSummary mutationSummary, ITargetClass targetClass, ClassKind kind,
					  IDurationStatistic statistic);

	/**
	 * Returns header content for formatted mutation results
	 *
	 * @return header text
	 */
	String getHeader();

	/**
	 * Returns object responsible for observing Judy work progress. The object
	 * will be notified about every class mutation result.
	 *
	 * @return object which will be notified about every class mutation result
	 */
	Observer getWorkObserver();

}
