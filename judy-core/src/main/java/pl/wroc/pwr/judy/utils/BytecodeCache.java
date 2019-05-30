package pl.wroc.pwr.judy.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.loader.TestClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Default implementation of bytecode cache interface. For special cases like
 * equivalence mutations analysis custom implementation, e.g. with on-the-fly
 * instrumentation, might be required.
 * <p/>
 * Note that URLs given as constructor parameters are possibly different for
 * client and workers environments.
 *
 * @author pmiwaszko
 */
public class BytecodeCache implements IBytecodeCache {
	private static final String JAR_EXTENSION = ".jar";

	/**
	 * Default size of cache.
	 */
	public static final int DEFAULT_CACHE_SIZE = 300;

	private static final String[] IGNORED = TestClassLoader.IGNORED_PREFIXES;
	private final List<URL> urls;
	private final Map<String, byte[]> cache;
	private static final Logger LOGGER = LogManager.getLogger(BytecodeCache.class);

	/**
	 * <code>BytecodeCache</code> constructor.
	 */
	public BytecodeCache(int size, String workspace, List<String> classpaths) {
		cache = Collections.synchronizedMap(new Cache<String, byte[]>(size));
		urls = new ArrayList<>(classpaths.size());
		for (String path : classpaths) {
			addURL(workspace, path);
		}
	}

	/**
	 * <code>BytecodeCache</code> constructor.
	 */
	public BytecodeCache(String workspace, List<String> classpath) {
		this(DEFAULT_CACHE_SIZE, workspace, classpath);
	}

	/**
	 * <code>BytecodeCache</code> constructor.
	 */
	public BytecodeCache(int size, List<URL> urls) {
		cache = Collections.synchronizedMap(new Cache<String, byte[]>(size));
		this.urls = urls;
	}

	/**
	 * <code>BytecodeCache</code> constructor.
	 */
	public BytecodeCache(List<URL> urls) {
		this(DEFAULT_CACHE_SIZE, urls);
	}

	@Override
	public byte[] get(String className) {
		// do not cache standard classes
		if (className != null) {
			for (String prefix : IGNORED) {
				if (className.startsWith(prefix)) {
					return null;
				}
			}
		}
		byte[] bytecode = cache.get(className);
		if (bytecode == null) {
			bytecode = loadBytecode(className);
			if (bytecode != null) {
				cache.put(className, bytecode);
			}
		}
		return bytecode;
	}

	@Override
	public URL[] getURLs() {
		return urls.toArray(new URL[urls.size()]);
	}

	/**
	 * Add path to classpath.
	 */
	protected void addURL(String workspace, String path) {
		File file = new File(workspace, path);
		try {
			file = new File(file.getCanonicalPath());
		} catch (IOException x) {
			// use the non-canonicalized filename
		}
		try {
			urls.add(file.toURI().toURL());
		} catch (IOException e) {
			LOGGER.warn("IOException", e);
		}
	}

	/**
	 * Loads bytecode from file.
	 */
	protected byte[] loadBytecode(String className) {
		byte[] bytecode = null;
		String path = getPath(className);
		Iterator<URL> urlIterator = urls.iterator();
		while (urlIterator.hasNext() && bytecode == null) {
			try {
				URL url = urlIterator.next();

				if (isJarFile(url)) {
					bytecode = loadBytecodeFromJar(className, url);
				} else {
					final URL newUrl = new URL(url, path);
					final File file = FileUtils.toFile(newUrl);
					if (file != null && file.isFile()) {
						bytecode = FileUtils.readFileToByteArray(file);
					}
				}
			} catch (IOException e) {
				LOGGER.error("IOException", e);
			}
		}
		return bytecode;
	}

	public boolean isJarFile(URL url) {
		return url.getFile().endsWith(JAR_EXTENSION);
	}

	private String getPath(String className) {
		return className.replace('.', '/').concat(".class");
	}

	public byte[] loadBytecodeFromJar(String className, URL url) throws IOException {
		byte[] bytecode = null;
		JarFile jar = new JarFile(url.getFile());
		JarEntry entry = jar.getJarEntry(getPath(className));
		if (entry != null) {
			bytecode = readClassBytecode(jar, entry);
		}
		jar.close();
		return bytecode;
	}

	private byte[] readClassBytecode(JarFile jar, JarEntry entry) throws IOException {
		InputStream is = jar.getInputStream(entry);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int nextValue = is.read();
		while (-1 != nextValue) {
			byteStream.write(nextValue);
			nextValue = is.read();
		}
		byte[] bytecode = byteStream.toByteArray();
		is.close();
		byteStream.close();
		return bytecode;
	}
}
