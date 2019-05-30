package pl.wroc.pwr.judy.tester;

import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.io.Serializable;

/**
 * Testers factory for instantiating Testers
 */
public interface ITesterFactory extends Serializable {
	/**
	 * Creates coverage tester instance
	 *
	 * @return tester instance
	 */
	ITester createCoverageTester(ICoverageClassLoaderFactory classLoaderFactory);

	/**
	 * Creates mutation tester instance
	 *
	 * @return tester instance
	 */
	ITester createMutationTester();
}
