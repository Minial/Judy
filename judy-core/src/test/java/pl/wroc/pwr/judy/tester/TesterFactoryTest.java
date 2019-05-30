package pl.wroc.pwr.judy.tester;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;

import static org.junit.Assert.assertTrue;

public class TesterFactoryTest {

	private ITesterFactory factory;
	private ICoverageClassLoaderFactory coverageClassLoaderFactory;

	@Before
	public void setUp() throws Exception {
		factory = new TesterFactory(null);
		coverageClassLoaderFactory = Mockito.mock(ICoverageClassLoaderFactory.class);
	}

	@Test
	public void shouldCreateCoverageTester() throws Exception {
		ITester tester = factory.createCoverageTester(coverageClassLoaderFactory);
		assertTrue(tester instanceof JUnitCoverageTester);
	}

	@Test
	public void shouldCreateMutationTester() throws Exception {
		ITester tester = factory.createMutationTester();
		assertTrue(tester instanceof JUnitMutationTester);
	}

}
