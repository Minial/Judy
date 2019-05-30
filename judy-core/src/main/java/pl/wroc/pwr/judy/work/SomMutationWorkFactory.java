package pl.wroc.pwr.judy.work;

import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.hom.som.ISomGeneratorFactory;
import pl.wroc.pwr.judy.hom.som.LastToFirstABCStrategy;
import pl.wroc.pwr.judy.hom.som.SomStrategy;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;

public class SomMutationWorkFactory implements IMutationWorkFactory {
	private final int retries;
	private final List<String> classpath;
	private final ITesterFactory testerFactory;
	private final IMutationOperatorsFactory operatorsFactory;
	private final long infiniteLoopBreakTime;
	private MutationResultFormatter resultFormatter;
	private ISomGeneratorFactory somGeneratorFactory;
	public MatrixExecution MatrixE;

	/**
	 * MutationWorkFactory constructor.
	 *
	 * @param retries             number of supported retries
	 * @param resultFormatter     mutation result formatter
	 * @param classpath           classpath
	 * @param testerFactory       JUnit tester factory
	 * @param operatorsFactory    mutation operators factory
	 * @param somGeneratorFactory second order mutants generation algorithm
	 */
	public SomMutationWorkFactory(final int retries, MutationResultFormatter resultFormatter,
								  final List<String> classpath, final ITesterFactory testerFactory,
								  final IMutationOperatorsFactory operatorsFactory, long infiniteLoopBreakTime,
								  ISomGeneratorFactory somGeneratorFactory, MatrixExecution MatrixE) {
		this.retries = retries;
		this.resultFormatter = resultFormatter;
		this.classpath = classpath;
		this.testerFactory = testerFactory;
		this.operatorsFactory = operatorsFactory;
		this.somGeneratorFactory = somGeneratorFactory;
		this.infiniteLoopBreakTime = infiniteLoopBreakTime;
		this.MatrixE = MatrixE;// add matrix
	}

	@Override
	public IWork create(long id, ITargetClass targetClass, IEnvironmentFactory envFactory, MatrixExecution MatrixE_) {
		return new SomMutationWork(id, retries, resultFormatter, targetClass, classpath, testerFactory, envFactory,
				operatorsFactory, infiniteLoopBreakTime, somGeneratorFactory.createGenerator(), somGeneratorFactory.getName(), MatrixE_);//ajouter matrice ici ?
	}

}
