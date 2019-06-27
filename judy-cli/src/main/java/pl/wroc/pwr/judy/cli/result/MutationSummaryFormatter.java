package pl.wroc.pwr.judy.cli.result;

import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MutationSummaryFormatter implements IMutationSummaryFormatter {

	private static final float HUNDRED_PERCENT = 100f;
	private static final int INITIAL_SB_CAPACITY = 256;
	private static final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	private final NumberFormat format;

	/**
	 * Creates mutation result summary printer
	 */
	public MutationSummaryFormatter() {
		format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
	}

	@Override
	public String getSummary(IMutationResult results, IInitialTestsRun testRun, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		String summary = "";
		if (results != null && results.getResults() != null) {
			StringBuilder sb = new StringBuilder(INITIAL_SB_CAPACITY);
			summarizeClass(sb, results.getClassesCount());
			summarizeScore(sb, results.getKilledMutantsCount(), results.getMutantsCount(), results.getKilledByWorkTimeoutMutantsCount(), results.getKilledByInfiniteLoopGuardMutantsCount());
			summarizeDuration(sb, results.getDuration(), testRun.getDuration());
			summarizeParallel(sb, results.getMutantGenerationDuration(), results.getTestsDuration());
			summarizeTestCount(sb, testRun.getTestsCount(), results.getTestsCount());
			summarizeQuality(sb, MatrixE, MatrixC);

			summary = sb.toString();
		}
		return summary;
	}

	/**
	 * Append summary of tests duration
	 *
	 * @param sb                   string builder
	 * @param mutationDuration     mutation analysis duration in milliseconds
	 * @param initialTestsDuration initial tests run duration in milliseconds
	 */
	public void summarizeDuration(StringBuilder sb, long mutationDuration, long initialTestsDuration) {
		newLine(sb, "Duration                 [s] : " + timeUnit.toSeconds(mutationDuration + initialTestsDuration));
		newLine(sb, "  initial tests duration [s] : " + timeUnit.toSeconds(initialTestsDuration));
		newLine(sb, "  mutation duration      [s] : " + timeUnit.toSeconds(mutationDuration));
	}

	/**
	 * Append tests summary
	 *
	 * @param sb                string builder
	 * @param initialTestsCount number initial test runs
	 * @param testsCount        number of test runs
	 */
	public void summarizeTestCount(StringBuilder sb, int initialTestsCount, int testsCount) {
		newLine(sb, "Test classes used");
		newLine(sb, "  initial tests runs         : " + initialTestsCount);
		newLine(sb, "  tests runs                 : " + testsCount);
	}

	/**
	 * Append parallel execution time summary
	 *
	 * @param sb                 string builder
	 * @param generationDuration mutant generation duration in milliseconds
	 * @param testDuration       mutant evaluation (test run) duration in milliseconds
	 */
	public void summarizeParallel(StringBuilder sb, long generationDuration, long testDuration) {
		newLine(sb, "\nAnalysis duration (parallel)");
		newLine(sb, "  generation duration       [s] : " + timeUnit.toSeconds(generationDuration));
		newLine(sb, "  test duration             [s] : " + timeUnit.toSeconds(testDuration));
	}

	/**
	 * Append mutation score summary
	 *
	 * @param sb            string builder
	 * @param killedMutants number of killed mutants
	 * @param allMutants    number of mutants
	 */
	public void summarizeScore(StringBuilder sb, int killedMutants, int allMutants, int killedMutantsByTimeout, int killedMutantsByInfiniteLoopGuard) {
		newLine(sb, "Score                    [%] : " + format.format(countMutationScore(killedMutants, allMutants)));
		newLine(sb, "  all mutants                : " + allMutants);
		newLine(sb, "  killed mutants             : " + killedMutants);
		newLine(sb, "    by timeout               : " + killedMutantsByTimeout);
		newLine(sb, "    by infinite loop guard   : " + killedMutantsByInfiniteLoopGuard);
	}

	private float countMutationScore(int killedMutants, int allMutants) {
		return allMutants == 0 ? 0f : killedMutants * HUNDRED_PERCENT / allMutants;
	}

	private void summarizeQuality(StringBuilder sb, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		int Nt = MatrixE.sizeOfMutantsNotEquivalents();
		int T = MatrixE.sizeOfTests();
		int M = MatrixE.sizeOfMutants();
		int Qm = 0;
		for(int i = 0 ; i<M;i++) {
			int SumOfCt=0;
			for(int j = 0 ; j<MatrixE.ListOfTestsForAMutant(i);j++) {
				if (MatrixE.checkTrigger(i,j)==true) {
					for(int k = 0 ; k<M ; k++) {
						if(MatrixE.checkTrigger(k,j)==true) {
							SumOfCt++;
						}
					}
				}
			}
			Qm = Qm+(1-SumOfCt/(Nt*T));
		}
		newLine(sb, "Quality					   : " + Qm);
	}
	
	
	/**
	 * Append source classes (target classes) summary
	 *
	 * @param sb         string builder
	 * @param classCount number of source classes
	 */
	public void summarizeClass(StringBuilder sb, int classCount) {
		newLine(sb, "");
		newLine(sb, "Classes                      : " + classCount);
	}

	private void newLine(StringBuilder builder, String text) {
		builder.append(text);
		builder.append("\n");
	}
}
