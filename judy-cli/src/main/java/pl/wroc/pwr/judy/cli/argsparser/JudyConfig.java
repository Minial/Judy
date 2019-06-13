package pl.wroc.pwr.judy.cli.argsparser;

import pl.wroc.pwr.judy.IClientConfig;
import pl.wroc.pwr.judy.IInitialTestsRunner;
import pl.wroc.pwr.judy.IMutationResultWriter;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.cli.ConfigException;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.general.InitialTestRunner;
import pl.wroc.pwr.judy.hom.HomConfig;
import pl.wroc.pwr.judy.hom.som.ISomGeneratorFactory;
import pl.wroc.pwr.judy.hom.som.SomGeneratorFactory;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.tester.TesterFactory;
import pl.wroc.pwr.judy.work.HomMutationWorkFactory;
import pl.wroc.pwr.judy.work.MutationResultFormatter;
import pl.wroc.pwr.judy.work.MutationWorkFactory;
import pl.wroc.pwr.judy.work.SomMutationWorkFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static com.google.common.util.concurrent.MoreExecutors.getExitingExecutorService;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * This implementation of {@link IClientConfig} interface takes program
 * arguments and parses them using {@link JudyArgsParser}.
 *
 * @author pmiwaszko
 */
public class JudyConfig implements IClientConfig {

	private final String workspace;
	private final List<String> classpath;
	private final List<String> sourceClasses;
	private final List<String> testClasses;
	private final IInitialTestsRunner initialTestRunner;
	private final ITesterFactory testerFactory;
	private final IMutationWorkFactory workFactory;
	private final IMutationOperatorsFactory operatorsFactory;
	private final int maxWorkRetries;
	private final boolean showKilledMutants;
	private final IMutationResultWriter resultWriter;
	private final int threadsCount;
	private final long maxInfiniteLoopGuardTimeout;
	private final String command;
	private final boolean useSom;
	private final ISomGeneratorFactory somAlgorithmFactory;
	private final boolean useCluster;
	public MatrixExecution MatrixE;
	public MatrixCoverage MatrixC;

	private final boolean useHom;

	private final MutationResultFormatter resultFormatter;
	private HomConfig homConfig;
	private static final ExecutorService executor = getExitingExecutorService(getPoolExecutor());
	private static final ExecutorService singleExecutor = newSingleThreadExecutor();

	private static ThreadPoolExecutor getPoolExecutor() {
		ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) newCachedThreadPool();
		poolExecutor.allowCoreThreadTimeOut(true);
		return poolExecutor;
	}

	/**
	 * JudyConfig constructor.
	 *
	 * @param args application arguments
	 * @throws ConfigException on config exception and in case of missing directory
	 */
	public JudyConfig(final String[] args, MatrixExecution MatrixE, MatrixCoverage MatrixC) throws ConfigException {
		final JudyArgsParser parser = new JudyArgsParser(args);
		useCluster = parser.getUseCluster();
		useSom = parser.getUseSom();
		workspace = parser.getWorkspace();

		classpath = parser.getClasspath();
		sourceClasses = parser.getSourceClasses();
		testClasses = parser.getTestClasses();

		maxWorkRetries = parser.getMaxWorkRetries();
		showKilledMutants = parser.getShowKilledMutants();
		resultWriter = parser.getResultWriter();

		threadsCount = parser.getThreadsCount();
		maxInfiniteLoopGuardTimeout = parser.getMaxInfiniteLoopGuardTimeout();

		somAlgorithmFactory = setupSomGeneratorFactory(parser.getSomAlgorithm());

		command = parser.getArguments();

		useHom = parser.getUseHom();
		homConfig = initHomConfig(parser);

		resultFormatter = parser.getResultFormatter();

		this.MatrixE = MatrixE;
		this.MatrixC=MatrixC;
		testerFactory = createTesterFactory(parser, MatrixE);

		initialTestRunner = setupInitialTestRunner();
		operatorsFactory = parser.getOperatorsFactory();
		workFactory = setupWorkFactory();

	}

	private ITesterFactory createTesterFactory(final JudyArgsParser parser, MatrixExecution MatrixE) {
		ExecutorService executor = JudyConfig.singleExecutor;
		if (parser.isRunTestsInParallel()) {
			executor = JudyConfig.executor;
		}
		return new TesterFactory(executor, MatrixE);

	}

	private HomConfig initHomConfig(final JudyArgsParser parser) {
		HomConfig cfg = new HomConfig();
		cfg.setObjectivesFactory(parser.getObjectivesFactory());
		cfg.setStrategy(parser.getHomStrategy());
		cfg.setMaxEvaluations(parser.getMaxHomEvaluations());
		cfg.setSkippingSameLineMutations(parser.isSkippingSameLineMutations());
		cfg.setMaxMutationOrder(parser.getMaxMutationOrder());
		cfg.setSkipTrivialMutantFilter(parser.shouldSkipTrivialMutantFilter());
		return cfg;
	}

	private ISomGeneratorFactory setupSomGeneratorFactory(String algorithm) {
		return useSom ? new SomGeneratorFactory(algorithm) : null;
	}

	private IInitialTestsRunner setupInitialTestRunner() {
		return new InitialTestRunner(workspace, testClasses, classpath, testerFactory);
	}

	private IMutationWorkFactory setupWorkFactory() {
		if (useHom) {
			return new HomMutationWorkFactory(maxWorkRetries, getResultFormatter(), classpath, testerFactory,
					operatorsFactory, maxInfiniteLoopGuardTimeout, homConfig, MatrixE, MatrixC);
		} else if (useSom) {
			return new SomMutationWorkFactory(maxWorkRetries, getResultFormatter(), classpath, testerFactory,
					operatorsFactory, maxInfiniteLoopGuardTimeout, somAlgorithmFactory, MatrixE, MatrixC);
		} else {
			return new MutationWorkFactory(maxWorkRetries, getResultFormatter(), classpath, testerFactory,
					operatorsFactory, maxInfiniteLoopGuardTimeout, MatrixE, MatrixC);
		}
	}

	@Override
	public boolean useCluster() {
		return useCluster;
	}

	@Override
	public String getWorkspace() {
		return workspace;
	}

	@Override
	public List<String> getClasspath() {
		return classpath;
	}

	@Override
	public List<String> getTargetClasses() {
		return sourceClasses;
	}

	@Override
	public List<String> getTestClasses() {
		return testClasses;
	}

	@Override
	public IInitialTestsRunner getInitialTestRunner() {
		return initialTestRunner;
	}

	@Override
	public IMutationWorkFactory getWorkFactory() {
		return workFactory;
	}

	@Override
	public IMutationOperatorsFactory getOperatorsFactory() {
		return operatorsFactory;
	}

	@Override
	public ITesterFactory getTesterFactory() {
		return testerFactory;
	}

	@Override
	public int getMaxWorkRetries() {
		return maxWorkRetries;
	}

	@Override
	public boolean showKilledMutants() {
		return showKilledMutants;
	}

	@Override
	public IMutationResultWriter getResultWriter() {
		return resultWriter;
	}

	@Override
	public int getThreadsCount() {
		return threadsCount;
	}

	@Override
	public long getMaxInfiniteLoopGuardTimeout() {
		return maxInfiniteLoopGuardTimeout;
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public boolean useSom() {
		return useSom;
	}

	@Override
	public ISomGeneratorFactory getSomAlgorithmFactory() {
		return somAlgorithmFactory;
	}

	@Override
	public boolean useHom() {
		return useHom;
	}

	@Override
	public MutationResultFormatter getResultFormatter() {
		return resultFormatter;
	}

	@Override
	public ExecutorService getExecutor() {
		return JudyConfig.executor;
	}
}
