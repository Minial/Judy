package pl.wroc.pwr.judy.loader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IBytecodeCache;

import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MutationClassLoaderFactoryTest {

	private MutationClassLoaderFactory factory;

	@Before
	public void setUp() {
		String mutantClassName = "testClass";
		byte[] mutantBytecode = new byte[1];

		IBytecodeCache cache = Mockito.mock(IBytecodeCache.class);
		Mockito.when(cache.getURLs()).thenReturn(new URL[2]);

		factory = new MutationClassLoaderFactory(mutantClassName, mutantBytecode, cache, 0);
	}

	@Test
	public void shouldCreateSimpleClassLoader() throws Exception {
		ClassLoader loader = factory.createLoader();

		assertNotNull(loader);
		assertTrue(loader instanceof MutationClassLoader);
	}
}
