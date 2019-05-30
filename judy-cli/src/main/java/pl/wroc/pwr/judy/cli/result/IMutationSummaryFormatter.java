package pl.wroc.pwr.judy.cli.result;

import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationResult;

public interface IMutationSummaryFormatter {

	/**
	 * Gets formatted mutation result summary
	 *
	 * @param result  mutation results
	 * @param testRun initial tests run results
	 * @return summary text
	 */
	String getSummary(IMutationResult result, IInitialTestsRun testRun);

}
