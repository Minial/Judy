package pl.wroc.pwr.judy;

import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.hom.som.ISomGeneratorFactory;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.work.MutationResultFormatter;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Configuration of JudyExec client. Implementations of this class can e.g.
 * parse application's arguments.
 *
 * @author pmiwaszko, TM
 */
public interface IClientConfig {
	/**
	 * Check if distributed platform should be used.
	 *
	 * @return true if distributed platform is enabled
	 */
	boolean useCluster();

	/**
	 * Check if second order mutants generation should be used.
	 *
	 * @return true is SOM mutants generation is enabled
	 */
	boolean useSom();

	/**
	 * Get names of target classes to be mutated. Names are package names, e.g.
	 * <code>pl.wroc.pwr.judy.Example</code>.
	 *
	 * @return target class names
	 */
	List<String> getTargetClasses();

	/**
	 * Get names of test classes against which mutated target classes will be
	 * checked. Names are package names, e.g.
	 * <code>pl.wroc.pwr.judy.TestCase</code>.
	 *
	 * @return test class names
	 */
	List<String> getTestClasses();

	/**
	 * Get workspace path. Workspace is a directory containing all target and
	 * test <code>.class</code> files, libraries and optional sources. All other
	 * paths are relative to workspace.
	 *
	 * @return workspace path
	 */
	String getWorkspace();

	/**
	 * Get list of paths to class directories and libraries. Class directories
	 * contains <code>.class</code> files for both tests and target classes.
	 * Libraries are <code>.jars</code> required by target or test classes. All
	 * path are <strong>relative to workspace</strong>.
	 *
	 * @return classpaths
	 */
	List<String> getClasspath();

	/**
	 * Get number of retries of rejected mutation work. When working in
	 * Terracotta environment every target class is mutated on a separate (and
	 * possibly remote) worker. This number specifies how many times should it
	 * be retried when an error occurs.
	 *
	 * @return max work retries
	 */
	int getMaxWorkRetries();

	/**
	 * Get instance of initial tests runner. Initial tests runner is executed on
	 * a client before mutation works are distributed to workers. Generally it
	 * should check if all test are passing, but other analysis make take place
	 * at the same time.
	 *
	 * @return initial tests runner
	 */
	IInitialTestsRunner getInitialTestRunner();

	/**
	 * Get factory of mutation works.
	 *
	 * @return mutation work factory
	 */
	IMutationWorkFactory getWorkFactory();

	/**
	 * Get factory of unit tests runners, e.g. factory of JUnit testers or
	 * factory of TestNG testers.
	 *
	 * @return JUnit testers factory
	 */
	ITesterFactory getTesterFactory();

	/**
	 * Get factory of mutation operators.
	 *
	 * @return mutation operators factory
	 */
	IMutationOperatorsFactory getOperatorsFactory();

	/**
	 * Check if information about killed mutants should be included into a final
	 * report.
	 *
	 * @return true if final report should contain killed mutants
	 */
	boolean showKilledMutants();

	/**
	 * Get writer of a final report. This could save report in e.g. XML format.
	 *
	 * @return final report writer
	 */
	IMutationResultWriter getResultWriter();

	/**
	 * Get original command that started Judy
	 *
	 * @return Judy invocation command
	 */
	String getCommand();

	/**
	 * Get number of thread when running in single JVM mode
	 *
	 * @return number of threads
	 */
	int getThreadsCount();

	/**
	 * Gets how long Infinite Loop Guard should wait before breaking the loop
	 *
	 * @return waiting time in miliseconds
	 */
	long getMaxInfiniteLoopGuardTimeout();

	/**
	 * Get chosen algorithm for generate second order mutants
	 *
	 * @return SOM generation algorithm factory
	 */
	ISomGeneratorFactory getSomAlgorithmFactory();

	/**
	 * @return true if best HOM research is enabled
	 */
	boolean useHom();

	/**
	 * Gets formatter of mutation results presented in user interface
	 *
	 * @return formatter
	 */
	MutationResultFormatter getResultFormatter();

	/**
	 * Gets threads executor
	 *
	 * @return executor
	 */
	ExecutorService getExecutor();

}
