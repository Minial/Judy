package pl.wroc.pwr.judy.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jacoco.core.instr.Instrumenter;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.general.ICoverage;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * Custom class loader used for simple test coverage analysis.
 *
 * @author pmiwaszko
 */
public class TestClassLoader extends URLClassLoader {
	private final String testClassName;
	private final IBytecodeCache cache;
	private final ICoverage coverage;
	private final Map<String, String> inheritance;
	private Instrumenter instrumenter;

	public static final String[] IGNORED_PREFIXES = {"java.", "javax.", "com.sun.", "junit.", "org.junit.",
			"org.mockito.", "org.hamcrest.", "org.powermock.", "org.springframework.test", "org.xml.sax."};

	static final Logger LOGGER = LogManager.getLogger(TestClassLoader.class);

	/**
	 * @param testClassName test class name
	 * @param cache         bytecode cache
	 * @param coverage      coverage data
	 * @param inheritance   inheritance data
	 */
	public TestClassLoader(String testClassName, IBytecodeCache cache, ICoverage coverage,
						   Map<String, String> inheritance) {
		super(cache.getURLs());
		this.testClassName = testClassName;
		this.cache = cache;
		this.coverage = coverage;
		this.inheritance = inheritance;
	}

	@Override
	protected synchronized Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
		Class<?> loadedClass = findLoadedClass(className);

		if (loadedClass == null) {
			byte[] bytecode = cache.get(className);

			if (bytecode != null) {
				if (instrumenter != null) {
					try {
						bytecode = instrumenter.instrument(bytecode, className);
					} catch (IOException e) {
						LOGGER.info(e);
					}
				}
				loadedClass = defineClass(className, bytecode, 0, bytecode.length);
			} else {
				// load with parent class loader
				loadedClass = super.loadClass(className, false);
			}
		}

		if (resolve) {
			resolveClass(loadedClass);
		}

		// mark loaded class
		addLoadedClass(className, loadedClass);

		return loadedClass;
	}

	/**
	 * Store loaded class name.
	 */
	private void addLoadedClass(String className, Class<?> loadedClass) {
		if (className != null && !className.equals(testClassName)) {
			// analyze inheritance hierarchy
			if (!inheritance.containsKey(className)) {
				Class<?> superclass = loadedClass.getSuperclass();
				if (superclass != null) {
					inheritance.put(className, superclass.getName());
				}
			}

			// compute coverage
			for (String prefix : IGNORED_PREFIXES) {
				if (className.startsWith(prefix)) {
					return;
				}
			}
			coverage.addClass(className, testClassName);
		}
	}

	/**
	 * Sets code instrumenter used on loaded bytecode
	 *
	 * @param instrumenter instrumenter
	 */
	public void setInstrumenter(Instrumenter instrumenter) {
		this.instrumenter = instrumenter;
	}

	/**
	 * Gets instrumenter used on loaded bytecode
	 *
	 * @return instrumenter
	 */
	public Instrumenter getInstrumenter() {
		return instrumenter;
	}

	/**
	 * Gets bytecode cache
	 *
	 * @return bytecode cache
	 */
	public IBytecodeCache getCache() {
		return cache;
	}

	/**
	 * Gets coverage data
	 *
	 * @return coverage data
	 */
	public ICoverage getCoverage() {
		return coverage;
	}
}
