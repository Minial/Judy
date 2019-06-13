package pl.wroc.pwr.judy.work;

import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.hom.HomConfig;
import pl.wroc.pwr.judy.hom.HomStrategy;
import pl.wroc.pwr.judy.hom.filter.DummyFilter;
import pl.wroc.pwr.judy.hom.filter.IMutantFilter;
import pl.wroc.pwr.judy.hom.filter.TrivialMutantFilter;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;

public class HomMutationWorkFactory implements IMutationWorkFactory {
	private final int retries;
	private final List<String> classpath;
	private final ITesterFactory testerFactory;
	private final IMutationOperatorsFactory operatorsFactory;
	private final long infiniteLoopBreakTime;
	public MatrixExecution MatrixE;
	public MatrixCoverage MatrixC;

	private MutationResultFormatter resultFormatter;

	private HomConfig config;

	/**
	 * MutationWorkFactory constructor.
	 *
	 * @param retries          number of supported retries
	 * @param resultFormatter  mutation result formatter
	 * @param classpath        classpath
	 * @param testerFactory    JUnit tester factory
	 * @param operatorsFactory mutation operators factory
	 * @param config           HOM mutation configuration
	 */
	public HomMutationWorkFactory(final int retries, MutationResultFormatter resultFormatter,
								  final List<String> classpath, final ITesterFactory testerFactory,
								  IMutationOperatorsFactory operatorsFactory, long infiniteLoopBreakTime, HomConfig config, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		this.retries = retries;
		this.resultFormatter = resultFormatter;
		this.classpath = classpath;
		this.testerFactory = testerFactory;
		this.operatorsFactory = operatorsFactory;
		this.config = config;
		this.infiniteLoopBreakTime = infiniteLoopBreakTime;
		this.MatrixE = MatrixE;// add matrix
		this.MatrixC=MatrixC;
	}

	@Override
	public IWork create(long id, ITargetClass targetClass, IEnvironmentFactory envFactory, MatrixExecution MatrixE_, MatrixCoverage MatrixC) {
		if (HomStrategy.UP_FRONT.equals(config.getStrategy())) {
			return createUpFrontHomMutationWork(id, targetClass, envFactory);
		} else {
			if (HomStrategy.ON_THE_FLY.equals(config.getStrategy())) {
				return createOnTheFlyHomMutationWork(id, targetClass, envFactory);
			} else {
				return createNotEquivalentHomMutationWork(id, targetClass, envFactory);
			}
		}
	}

	private IWork createOnTheFlyHomMutationWork(final long id, final ITargetClass targetClass,
												final IEnvironmentFactory envFactory) {
		IMutantFilter mutantFilter = config.shouldSkipTrivialMutantFilter() ? new DummyFilter()
				: new TrivialMutantFilter();
		return new OnTheFlyHomMutationWork(id, retries, resultFormatter, targetClass, classpath, testerFactory,
				envFactory, operatorsFactory, infiniteLoopBreakTime, config, mutantFilter, MatrixE, MatrixC);
	}

	private IWork createUpFrontHomMutationWork(final long id, final ITargetClass targetClass,
											   final IEnvironmentFactory envFactory) {
		IMutantFilter mutantFilter = config.shouldSkipTrivialMutantFilter() ? new DummyFilter()
				: new TrivialMutantFilter();
		return new UpFrontHomMutationWork(id, retries, resultFormatter, targetClass, classpath, testerFactory,
				envFactory, operatorsFactory, infiniteLoopBreakTime, config, mutantFilter, MatrixE, MatrixC);
	}
	private IWork createNotEquivalentHomMutationWork(final long id, final ITargetClass targetClass,
											   final IEnvironmentFactory envFactory) {
		IMutantFilter mutantFilter = config.shouldSkipTrivialMutantFilter() ? new DummyFilter()
				: new TrivialMutantFilter();
		return new NotEquivalentHomMutationWork(id, retries, resultFormatter, targetClass, classpath, testerFactory,
				envFactory, operatorsFactory, infiniteLoopBreakTime, config, mutantFilter, MatrixE, MatrixC);
	}
}
