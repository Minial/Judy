package pl.wroc.pwr.judy.utils;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Simple implementation of fixed size Least-Recently-Used cache.
 *
 * @author pmiwaszko
 */
public class Cache<K, V> extends LinkedHashMap<K, V> {
	/**
	 * Default size fo cache.
	 */
	public static final int DEFAULT_SIZE = 30;

	private static final long serialVersionUID = 150L;
	private final int maxSize;

	/**
	 * <code>Cache</code> constructor creating cache of default size,
	 * {@link #DEFAULT_SIZE}.
	 */
	public Cache() {
		this(DEFAULT_SIZE);
	}

	/**
	 * <code>Cache</code> constructor.
	 */
	public Cache(int size) {
		super(size + 1, 1.0f, true);
		maxSize = size;
	}

	@Override
	protected boolean removeEldestEntry(Entry<K, V> eldest) {
		return size() > maxSize;
	}
}
