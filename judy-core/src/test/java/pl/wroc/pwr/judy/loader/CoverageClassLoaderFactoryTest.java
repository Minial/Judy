package pl.wroc.pwr.judy.loader;

import org.jacoco.core.instr.Instrumenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.general.ICoverage;
import pl.wroc.pwr.judy.general.MapCoverage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CoverageClassLoaderFactoryTest {

	private CoverageClassLoaderFactory factory;

	@Before
	public void setUp() {
		String testClassName = "testClass";
		IBytecodeCache cache = Mockito.mock(IBytecodeCache.class);
		Mockito.when(cache.getURLs()).thenReturn(new URL[2]);
		ICoverage coverage = new MapCoverage();
		Map<String, String> inheritance = new HashMap<>();

		factory = new CoverageClassLoaderFactory(testClassName, cache, coverage, inheritance);
	}

	@Test
	public void shouldCreateSimpleClassLoader() throws Exception {
		ClassLoader loader = factory.createLoader();

		checkClassLoader(loader);
		checkInstrumenter(null, loader);
	}

	@Test
	public void shouldCreateInstrumentedClassLoader() throws Exception {
		Instrumenter instrumenter = Mockito.mock(Instrumenter.class);
		ClassLoader loader = factory.createLoader(instrumenter);

		checkClassLoader(loader);
		checkInstrumenter(instrumenter, loader);
	}

	private void checkClassLoader(ClassLoader loader) {
		assertNotNull(loader);
		assertTrue(loader instanceof TestClassLoader);
	}

	private void checkInstrumenter(Instrumenter instrumenter, ClassLoader loader) {
		TestClassLoader tcl = (TestClassLoader) loader;
		assertEquals(instrumenter, tcl.getInstrumenter());
	}
}
