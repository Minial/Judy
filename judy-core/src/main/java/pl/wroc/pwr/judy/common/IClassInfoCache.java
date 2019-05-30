package pl.wroc.pwr.judy.common;

/**
 * Cached access to information about classes.
 *
 * @author pmiwaszko
 */
public interface IClassInfoCache {
	/**
	 * Get information about class with given name.
	 */
	IClassInfo get(String cname);
}
