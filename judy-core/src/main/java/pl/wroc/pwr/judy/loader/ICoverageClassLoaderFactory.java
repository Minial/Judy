package pl.wroc.pwr.judy.loader;

import org.jacoco.core.instr.Instrumenter;

public interface ICoverageClassLoaderFactory extends ClassLoaderFactory {
	/**
	 * Creates basic class loader
	 *
	 * @return class loader
	 */
	@Override
	TestClassLoader createLoader();

	/**
	 * Creates class loader allowing to test source classes code coverage using
	 * jacoco
	 *
	 * @param instrumenter jacoco coverage instrumenter
	 * @return class loader with instrumenter set
	 */
	TestClassLoader createLoader(Instrumenter instrumenter);
}
