package pl.wroc.pwr.judy.work;

import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;

/**
 * General implementation of mutation work factory.
 *
 * @author pmiwaszko, tmuc
 */
public class MutationWorkFactory implements IMutationWorkFactory {
	private final int retries;
	private final List<String> classpath;
	private final ITesterFactory testerFactory;
	private final IMutationOperatorsFactory operatorsFactory;
	private final long infiniteLoopBreakTime;
	private MutationResultFormatter resultFormatter;
	public MatrixExecution MatrixE;

	/**
	 * MutationWorkFactory constructor.
	 *
	 * @param retries          number of supported retries
	 * @param resultFormatter  mutation result formatter
	 * @param classpath        classpath
	 * @param testerFactory    JUnit tester factory
	 * @param operatorsFactory mutation operators factory
	 */
	public MutationWorkFactory(final int retries, MutationResultFormatter resultFormatter,
							   final List<String> classpath, final ITesterFactory testerFactory,
							   final IMutationOperatorsFactory operatorsFactory, long infiniteLoopBreakTime, MatrixExecution MatrixE) {
		this.retries = retries;
		this.resultFormatter = resultFormatter;
		this.classpath = classpath;
		this.testerFactory = testerFactory;
		this.operatorsFactory = operatorsFactory;
		this.infiniteLoopBreakTime = infiniteLoopBreakTime;
		this.MatrixE = MatrixE;
	}

	@Override
	public IWork create(final long id, final ITargetClass targetClass, final IEnvironmentFactory envFactory, MatrixExecution MatrixE) {
		return createFomMutationWork(id, targetClass, envFactory);
	}

	private IWork createFomMutationWork(final long id, final ITargetClass targetClass,
										final IEnvironmentFactory envFactory) {
		return new MutationWork(id, retries, resultFormatter, targetClass, classpath, testerFactory, envFactory,
				operatorsFactory, infiniteLoopBreakTime, MatrixE);
	}
}
