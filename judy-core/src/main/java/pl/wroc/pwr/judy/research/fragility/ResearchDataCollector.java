package pl.wroc.pwr.judy.research.fragility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.HashSet;
import java.util.Set;

public class ResearchDataCollector implements IResearchDataCollector {
	private static final String OPERATOR_SEPARATOR = ";";
	private static final String DATA_SEPARATOR = "\t";
	private static final Logger LOGGER = LogManager.getLogger(ResearchDataCollector.class);

	@Override
	public void saveResult(final ITargetClass targetClass, final IMutant mutant, final ITestResult testResult) {

		final StringBuilder sb = new StringBuilder();
		sb.append(DATA_SEPARATOR);
		sb.append(targetClass.getName());
		sb.append(DATA_SEPARATOR);
		sb.append(mutant.getMutantIndex());
		sb.append(DATA_SEPARATOR);
		sb.append(mutant.getId());
		sb.append(DATA_SEPARATOR);
		sb.append(getOperators(mutant));
		sb.append(DATA_SEPARATOR);
		sb.append(mutant.getLinesNumbers());
		sb.append(DATA_SEPARATOR);

		// saveTestResults(sb.toString(), testResult.getSuccessfulTestMethods(),
		// 0);
		saveTestResults(sb.toString(), testResult.getFailingTestMethods(), 1);
		// saveTestResults(sb.toString(), getMissingTestcases(targetClass,
		// testResult), 2);
	}

	private Set<String> getMissingTestcases(final ITargetClass targetClass, final ITestResult testResult) {
		final Set<String> missingTestcases = getExpectedTestcases(targetClass, testResult);
		missingTestcases.removeAll(testResult.getTestMethods());
		return missingTestcases;
	}

	private void saveTestResults(final String commonData, final Set<String> testcases, final int result) {
		final StringBuilder sb = new StringBuilder();
		for (final String testCase : testcases) {
			sb.append(commonData);
			sb.append(testCase);
			sb.append(DATA_SEPARATOR);
			sb.append(result);

			LOGGER.debug(sb.toString());

			sb.setLength(0);
		}
	}

	private String getOperators(final IMutant mutant) {
		final StringBuilder sb = new StringBuilder();
		for (final String operator : mutant.getOperatorsNames()) {
			sb.append(operator);
			sb.append(OPERATOR_SEPARATOR);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private Set<String> getExpectedTestcases(final ITargetClass targetClass, final ITestResult testResult) {
		final Set<String> expected = new HashSet<>();
		for (final ITestResult result : targetClass.getInitialTestResults()) {
			if (result.getClassName().equals(testResult.getClassName())) {
				expected.addAll(result.getTestMethods());
			}
		}
		return expected;
	}
}
