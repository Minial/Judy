package pl.wroc.pwr.judy.utils;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.helpers.TestHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BytecodeCacheTest {

	@Test
	public void testCreateBytecodeCache() throws MalformedURLException {
		final List<URL> urls = new ArrayList<>();
		urls.add(new URL("file:/C:test/testClassPathFile1"));
		urls.add(new URL("file:/C:test/testClassPathFile2"));
		final BytecodeCache cache = new BytecodeCache(urls);
		final URL[] expectedUrls = urls.toArray(new URL[urls.size()]);
		assertArrayEquals(expectedUrls, cache.getURLs());

	}

	@Test
	public void testGetURLs() throws IOException {
		final URL[] expectedUrls = new URL[2];
		final List<String> classPaths = new ArrayList<>();
		classPaths.add("Test.class");
		classPaths.add("Test2.class");
		final String workspace = "workSpace";
		File file = new File(workspace, classPaths.get(0));
		file = new File(file.getCanonicalPath());
		expectedUrls[0] = file.toURI().toURL();
		File file2 = new File(workspace, classPaths.get(1));
		file2 = new File(file2.getCanonicalPath());
		expectedUrls[1] = file2.toURI().toURL();
		BytecodeCache cache = new BytecodeCache(12, workspace, classPaths);
		assertArrayEquals(expectedUrls, cache.getURLs());
	}

	@Test
	public void testGetBytecode() throws IOException {
		final IBytecodeCache cache = TestHelper.getTestsBytecodeCache();
		byte[] class3asByteArray = cache.get("resources/ClassInfoCacheResource");
		InputStream class3 = this.getClass().getResourceAsStream("/resources/ClassInfoCacheResource.class");
		byte[] expectedByteArray = IOUtils.toByteArray(class3);
		assertArrayEquals(expectedByteArray, class3asByteArray);
	}

	@Test
	public void testGetBytecodeNoClass() throws IOException {
		final IBytecodeCache cache = TestHelper.getTestsBytecodeCache();
		assertNull(this.getClass().getResourceAsStream("/resources/NoSuchClassResource.class"));
		assertNull(cache.get("resources/NoSuchClassResource"));
	}

	@Test
	public void shouldGetClassFromJarFileOnClasspath() throws Exception {
		IBytecodeCache cache = TestHelper.getJarBytecodeCache();
		assertNull(this.getClass().getResourceAsStream("/resources/NoSuchClassResource.class"));
		assertNotNull(cache.get("pl.wroc.pwr.judy.jartest.InsideJar"));
	}
}
