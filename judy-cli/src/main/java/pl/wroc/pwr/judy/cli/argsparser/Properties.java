package pl.wroc.pwr.judy.cli.argsparser;

import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Persistent set of properties.
 *
 * @author pmiwaszko
 */
public class Properties {

	private final Map<String, String> properties;
	private static final Logger LOGGER = getLogger(Properties.class);

	/**
	 * <code>Properties</code> constructor.
	 */
	public Properties() {
		properties = new ConcurrentHashMap<>();
	}

	/**
	 * Load set of properties from given file.
	 *
	 * @throws IOException
	 */
	public void load(final String path) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			String key = null;
			String value = null;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().startsWith("#") && !line.trim().startsWith("//")) { // ignore
					// comments
					final int index = line.indexOf('=');
					if (index != -1) {
						put(key, value); // put previously read property
						key = line.substring(0, index).trim();
						value = line.substring(index + 1);
					} else {
						value += line;
					}
				}
			}
			put(key, value); // put last property
		}
	}

	private void put(final String key, final String value) {
		if (key != null && !key.trim().isEmpty() && value != null) {
			properties.put(key, value.trim());
		}
	}

	/**
	 * Get property for given key or <code>null</code> if no property is
	 * defined.
	 */
	public String get(final String key) {
		return properties.get(key);
	}

	/**
	 * Get property for given key or default value if no property is defined.
	 */
	public String get(final String key, final String defaultValue) {
		final String value = properties.get(key);
		return value == null ? defaultValue : value;
	}

	/**
	 * Get property for a given key in form of a list or empty list if no
	 * property is defined.
	 */
	public List<String> getList(final String key) {
		final String value = get(key);
		if (value == null) {
			return Collections.emptyList();
		}
		final List<String> list = new LinkedList<>();
		for (final String element : value.split(";")) {
			list.add(element.trim());
		}
		return list;
	}

	/**
	 * Print list of properties on standard output;
	 */
	public void list() {
		for (final Entry<String, String> property : properties.entrySet()) {
			LOGGER.info(property.getKey() + " = " + property.getValue());
		}
	}

}
