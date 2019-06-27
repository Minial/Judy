package pl.wroc.pwr.judy.cli.result;

import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

public interface IMutationSummaryFormatter {

	/**
	 * Gets formatted mutation result summary
	 *
	 * @param result  mutation results
	 * @param testRun initial tests run results
	 * @return summary text
	 */
	String getSummary(IMutationResult result, IInitialTestsRun testRun, MatrixExecution MatrixE, MatrixCoverage MatrixC);

}
