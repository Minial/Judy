package pl.wroc.pwr.judy.work;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.cluster.AbstractWork;
import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.*;
import pl.wroc.pwr.judy.general.IMutantEvaluator;
import pl.wroc.pwr.judy.general.MutantEvaluator;
import pl.wroc.pwr.judy.tester.ITester;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.utils.BytecodeCache;
import pl.wroc.pwr.judy.utils.ClassKind;
import pl.wroc.pwr.judy.utils.Timer;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.LinkedList;
import java.util.List;

/**
 * Default implementation of mutation task.
 *
 * @author pmiwaszko
 */
public class MutationWork extends AbstractWork {
	private static final long serialVersionUID = -4599267510647357755L;
	private final ITargetClass targetClass;
	private final List<String> classpath;
	private final IEnvironmentFactory envFactory;
	private final ITesterFactory testerFactory;
	private final IMutationOperatorsFactory operatorsFactory;
	private final long maxInfiniteLoopGuardTimeout;
	private transient IBytecodeCache cache;
	private transient List<IMutationOperator> operators;
	private transient ITester tester;
	private transient ClassKind kind;
	private String workspace;
	public MatrixExecution MatrixE;
	public MatrixCoverage MatrixC;

	private transient IDurationStatistic statistic;

	private IMutantEvaluator evaluator;
	private MutationResultFormatter resultFormatter;

	private static final Logger LOGGER = LogManager.getLogger(MutationWork.class);

	/**
	 * Creates mutation work creating and evaluating first order mutants
	 *
	 * @param clientId         client id
	 * @param retries          number of supported retries
	 * @param resultFormatter  maximum source class name length
	 * @param targetClass      target class to mutate
	 * @param classpath        classpath
	 * @param testerFactory    JUnit tester factory
	 * @param envFactory       environment factory
	 * @param operatorsFactory mutation operators factory
	 * @param MatrixE		   Execution matrix
	 */
	public MutationWork(long clientId, int retries, MutationResultFormatter resultFormatter, ITargetClass targetClass,
						List<String> classpath, ITesterFactory testerFactory, IEnvironmentFactory envFactory,
						IMutationOperatorsFactory operatorsFactory, long maxInfiniteLoopGuardTimeout, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		super(clientId, retries);
		setResultFormatter(resultFormatter);
		this.targetClass = targetClass;
		this.classpath = classpath;
		this.testerFactory = testerFactory;
		this.envFactory = envFactory;
		this.operatorsFactory = operatorsFactory;
		kind = ClassKind.UNKNOWN;
		this.maxInfiniteLoopGuardTimeout = maxInfiniteLoopGuardTimeout;
		this.MatrixE=MatrixE;
		this.MatrixC=MatrixC;
	}

	@Override
	public IClassMutationResult run(Object workspace) throws Exception {
		init((String) workspace);

		computeInitialTestsDuration();

		if (kind.isMutable()) {
			LOGGER.debug(String.format("Mutants generation for %s started", targetClass.getName()));
			List<IMutant> mutants = mutate();
			LOGGER.debug(String.format("Mutants generation for %s finished (%d created)", targetClass.getName(),
					mutants.size()));

			evaluator.evaluate(mutants, true, MatrixE, MatrixC);
			
		} else {
			LOGGER.debug("Mutation not applicable to class: " + targetClass);
		}
		tester.shutdown();
		evaluator.trimSize();
		return new ClassMutationResult(kind, targetClass, evaluator.getAliveMutants(), evaluator.getKilledMutants(),
				statistic, getResultFormatter().getSummary(collectSummaryData(), targetClass, kind, statistic));
	}

	protected MutationSummary collectSummaryData() {
		MutationSummary summary = new MutationSummary();
		summary.setKilled(evaluator.getKilledMutants().size());
		summary.setAlive(evaluator.getAliveMutants().size());
		return summary;
	}

	protected void computeInitialTestsDuration() {
		for (ITestResult r : targetClass.getInitialTestResults()) {
			statistic.addInitialTest(r.getDuration());
		}
	}

	protected void init(String workspace) {
		this.workspace = workspace;
		statistic = new DurationStatistic();
		setCache(new BytecodeCache(workspace, classpath));
		IEnvironment env = envFactory.create(getCache());
		tester = testerFactory.createMutationTester();
		operators = operatorsFactory.create(env);
		kind = ClassKind.getKind(targetClass, getCache(), env);

		evaluator = new MutantEvaluator(targetClass, getCache(), tester, statistic, maxInfiniteLoopGuardTimeout);
	}

	protected void initReset() {
		statistic = new DurationStatistic();
		setCache(new BytecodeCache(workspace, classpath));
		IEnvironment env = envFactory.create(getCache());
		tester = testerFactory.createMutationTester();
		operators = operatorsFactory.create(env);
		kind = ClassKind.getKind(targetClass, getCache(), env);
		evaluator = new MutantEvaluator(targetClass, getCache(), tester, statistic, maxInfiniteLoopGuardTimeout);
	}

	protected List<IMutant> mutate() {
		final byte[] bytecode = getBytecode();
		return generateFoms(bytecode);
	}

	protected byte[] getBytecode() {
		final byte[] bytecode = getCache().get(targetClass.getName());
		return bytecode;
	}

	protected List<IMutant> generateFoms(final byte[] bytecode) {
		List<IMutant> foms = new LinkedList<>();

		for (IMutationOperator operator : operators) {
			final List<IMutationPoint> mutationPoints = operator.getMutationPoints(bytecode);

			for (IMutationPoint point : mutationPoints) {
				final List<IMutantBytecode> mutantBytecodes = mutate(bytecode, operator, point);
				int index = 0;
				for (IMutantBytecode mutant : mutantBytecodes) {
					foms.add(new Mutant(operator.getName(), point.getIndex(), index++, targetClass.getName(), mutant
							.getLineNumber(), mutant.getDescription(), mutant, operators.indexOf(operator)));
				}
			}
		}
		return foms;
	}

	private List<IMutantBytecode> mutate(final byte[] bytecode, IMutationOperator operator, IMutationPoint point) {
		Timer timer = new Timer();
		final List<IMutantBytecode> mutantBytecodes = operator.mutate(bytecode, point);
		statistic.addMutantGenration(timer.getDuration());
		return mutantBytecodes;
	}

	/**
	 * Gets mutant evaluator
	 *
	 * @return evaluator
	 */
	protected IMutantEvaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * @return the statistic
	 */
	public IDurationStatistic getStatistic() {
		return statistic;
	}

	/**
	 * @param statistic the statistic to set
	 */
	public void setStatistic(IDurationStatistic statistic) {
		this.statistic = statistic;
	}

	/**
	 * @return the kind
	 */
	public ClassKind getKind() {
		return kind;
	}

	/**
	 * @param kind the kind to set
	 */
	public void setKind(ClassKind kind) {
		this.kind = kind;
	}

	/**
	 * @return the targetClass
	 */
	public ITargetClass getTargetClass() {
		return targetClass;
	}

	/**
	 * @return the operators
	 */
	public List<IMutationOperator> getOperators() {
		return operators;
	}

	/**
	 * @param operators the operators to set
	 */
	public void setOperators(List<IMutationOperator> operators) {
		this.operators = operators;
	}

	/**
	 * @return the cache
	 */
	public IBytecodeCache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(IBytecodeCache cache) {
		this.cache = cache;
	}

	/**
	 * @return the resultFormatter
	 */
	public MutationResultFormatter getResultFormatter() {
		return resultFormatter;
	}

	/**
	 * @param resultFormatter the resultFormatter to set
	 */
	public void setResultFormatter(MutationResultFormatter resultFormatter) {
		this.resultFormatter = resultFormatter;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<String> getClasspath() {
		return classpath;
	}

	public IEnvironmentFactory getEnvFactory() {
		return envFactory;
	}

	public ITesterFactory getTesterFactory() {
		return testerFactory;
	}

	public IMutationOperatorsFactory getOperatorsFactory() {
		return operatorsFactory;
	}

	public long getMaxInfiniteLoopGuardTimeout() {
		return maxInfiniteLoopGuardTimeout;
	}

	public ITester getTester() {
		return tester;
	}

	public void setTester(ITester tester) {
		this.tester = tester;
	}

	public void setEvaluator(IMutantEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public static Logger getLOGGER() {
		return LOGGER;
	}

	public String getWorkspace() {
		return workspace;
	}
}
