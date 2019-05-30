package pl.wroc.pwr.judy.common;

import java.net.URL;

/**
 * Cached access to bytecode of target classes and tests.
 *
 * @author pmiwaszko
 */
public interface IBytecodeCache {
	/**
	 * Return the search path.
	 */
	URL[] getURLs();

	/**
	 * Get bytecode for a given class name.
	 */
	byte[] get(String className);
}
