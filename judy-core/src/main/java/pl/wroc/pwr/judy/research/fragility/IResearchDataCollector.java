package pl.wroc.pwr.judy.research.fragility;

import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IMutant;

/**
 * Collects and logs the data regarding mutation evaluation on mutant level
 *
 * @author TM
 */
public interface IResearchDataCollector {
	/**
	 * @param targetClass class which is being mutated
	 * @param mutant      mutant introduced in target class source code
	 * @param testResult  test results from mutant evaluation
	 */
	void saveResult(ITargetClass targetClass, IMutant mutant, ITestResult testResult);
}
