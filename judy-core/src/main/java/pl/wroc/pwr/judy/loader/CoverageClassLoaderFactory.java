package pl.wroc.pwr.judy.loader;

import org.jacoco.core.instr.Instrumenter;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.general.ICoverage;

import java.util.Map;

public class CoverageClassLoaderFactory implements ICoverageClassLoaderFactory {

	private String testClassName;
	private IBytecodeCache cache;
	private ICoverage coverage;
	private Map<String, String> inheritance;

	/**
	 * @param testClassName test class name
	 * @param cache         source and test classes cache
	 * @param coverage      source class to test classes coverage map
	 * @param inheritance   source class to super classes map
	 */
	public CoverageClassLoaderFactory(String testClassName, IBytecodeCache cache, ICoverage coverage,
									  Map<String, String> inheritance) {
		this.testClassName = testClassName;
		this.cache = cache;
		this.coverage = coverage;
		this.inheritance = inheritance;
	}

	@Override
	public TestClassLoader createLoader() {
		return new TestClassLoader(testClassName, cache, coverage, inheritance);
	}

	@Override
	public TestClassLoader createLoader(Instrumenter instrumenter) {
		TestClassLoader loader = new TestClassLoader(testClassName, cache, coverage, inheritance);
		loader.setInstrumenter(instrumenter);
		return loader;
	}
}
