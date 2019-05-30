package pl.wroc.pwr.judy.helpers;

import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.utils.BytecodeCache;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.util.Collections.addAll;

/**
 * Convenient utilities for test classes.
 *
 * @author pmiwaszko
 */
public final class TestHelper {
	private static final String JAR_FILE = "test-jar.jar";
	private static final String CLASSES_LOCATION = "../classes/";
	private static final String TEST_CLASSES_LOCATION = "../test-classes/";

	private TestHelper() {
		// utility class
	}

	/**
	 * Get bytecode cache initialized with classes and tests from this project.
	 * <p/>
	 * Note that this method is quite of a dirty hack and requires certain
	 * directories layout.
	 *
	 * @return bytecode cache
	 * @throws IOException
	 */
	public static IBytecodeCache getBytecodeCache() throws IOException {
		return new BytecodeCache(getURLs());
	}

	/**
	 * Get bytecode cache initialized with tests from this project.
	 *
	 * @return bytecode cache
	 * @throws IOException
	 */
	public static IBytecodeCache getTestsBytecodeCache() throws IOException {
		return new BytecodeCache(getTestsURLs());
	}

	/**
	 * Get bytecode cache initialized with classes from this project.
	 *
	 * @return bytecode cache
	 * @throws IOException
	 */
	public static IBytecodeCache getClassesBytecodeCache() throws IOException {
		return new BytecodeCache(getClassesURLs());
	}

	/**
	 * Get bytecode cache initialized with jar from this project's resources.
	 *
	 * @return bytecode cache
	 * @throws IOException
	 */
	public static IBytecodeCache getJarBytecodeCache() throws IOException {
		return new BytecodeCache(getJarURLs());
	}

	/**
	 * Converts iterable collection into array of strings.
	 *
	 * @param list collection
	 * @return array of strings
	 */
	public static String[] asStringArray(Iterable<?> list) {
		List<String> temp = new ArrayList<>();
		for (Object o : list) {
			temp.add(o.toString());
		}
		return temp.toArray(new String[temp.size()]);
	}

	/**
	 * Return sorted copy of array.
	 *
	 * @param input array to sort
	 * @return sorted copy of array
	 */
	public static Object[] sorted(Object[] input) {
		Object[] array = Arrays.copyOf(input, input.length);
		Arrays.sort(array);
		return array;
	}

	/**
	 * Return sorted copy of list.
	 *
	 * @param input list to sort
	 * @return sorted copy of list
	 */
	public static <T extends Comparable<? super T>> List<T> sorted(List<T> input) {
		List<T> list = new LinkedList<>(input);
		Collections.sort(list);
		return list;
	}

	/**
	 * Create list from given values.
	 *
	 * @param <T>    type of values
	 * @param values
	 * @return list of values
	 */
	public static <T> List<T> list(T... values) {
		List<T> list = new LinkedList<>();
		addAll(list, values);
		return list;
	}

	private static List<URL> getClassesURLs() throws IOException {
		List<URL> urls = new LinkedList<>();
		String url = TestHelper.class.getProtectionDomain().getCodeSource().getLocation() + CLASSES_LOCATION;
		urls.add(new URL(url));
		return urls;
	}

	private static List<URL> getJarURLs() throws IOException {
		List<URL> urls = new LinkedList<>();
		URL loc = new URL(TestHelper.class.getProtectionDomain().getCodeSource().getLocation() + TEST_CLASSES_LOCATION
				+ JAR_FILE);
		urls.add(loc);
		return urls;
	}

	private static List<URL> getTestsURLs() throws IOException {
		List<URL> urls = new LinkedList<>();
		String url = TestHelper.class.getProtectionDomain().getCodeSource().getLocation() + TEST_CLASSES_LOCATION;
		urls.add(new URL(url));
		return urls;
	}

	private static List<URL> getURLs() throws IOException {
		List<URL> urls = new LinkedList<>();
		String url = TestHelper.class.getProtectionDomain().getCodeSource().getLocation() + CLASSES_LOCATION;
		urls.add(new URL(url));
		url = TestHelper.class.getProtectionDomain().getCodeSource().getLocation() + TEST_CLASSES_LOCATION;
		urls.add(new URL(url));
		return urls;
	}
}
