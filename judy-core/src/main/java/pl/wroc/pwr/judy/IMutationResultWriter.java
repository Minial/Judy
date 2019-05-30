package pl.wroc.pwr.judy;

import pl.wroc.pwr.judy.common.WriterException;

/**
 * Common interface for all writers of final mutation report.
 *
 * @author pmiwaszko
 */
public interface IMutationResultWriter {
	/**
	 * Save mutation result.
	 *
	 * @param result the result to save
	 * @throws WriterException if error while saving result occurs
	 */
	void write(IMutationResult result) throws WriterException;

	/**
	 * Save mutation result.
	 *
	 * @param includeKilledMutatns specify if information about killed mutants should be included
	 *                             into a final report
	 * @throws WriterException if error while saving result occurs
	 */
	void write(IMutationResult mutationResult, boolean includeKilledMutatns) throws WriterException;

	/**
	 * Get path used to save a report.
	 */
	String getPath();
}
