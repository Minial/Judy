package pl.wroc.pwr.judy.tester;

import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.concurrent.ExecutorService;

public class TesterFactory implements ITesterFactory {
	private static final long serialVersionUID = 1L;
	private ITestThreadFactory threadFactory = new TestThreadFactory();
	private ExecutorService executor;
	public MatrixExecution MatrixE;
	public MatrixCoverage MatrixC;

	/**
	 * @param executor threads executor
	 */
	public TesterFactory(ExecutorService executor, MatrixExecution MatrixE) {
		this.executor = executor;
	}

	@Override
	public ITester createCoverageTester(ICoverageClassLoaderFactory classLoaderFactory) {
		return new JUnitCoverageTester(classLoaderFactory, threadFactory, executor, MatrixC);
	}

	@Override
	public ITester createMutationTester() {
		return new JUnitMutationTester(threadFactory, executor, MatrixE);
	}

}
