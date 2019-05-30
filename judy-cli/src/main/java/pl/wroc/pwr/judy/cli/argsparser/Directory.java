package pl.wroc.pwr.judy.cli.argsparser;

import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Representation of directory containing <code>.class</code> or <code>.jar
 * </code> files.
 * <p/>
 * <b>Directory's description</b>
 * <p/>
 * Directory must be contained in workspace and its path given as relative to
 * workspace. Path specification is a first part of directory's description.
 * Other parts are optional and apply only when listing classes (
 * {@link Directory#listClasses()}). Second part may contain regular expression
 * for filtering classes (package names of found classes are matched against
 * given regex). In next parts names (simple or package) of excluded classes can
 * be given. Parts are separated by <code>;</code> sign.
 * <p/>
 * Example:<br/>
 * <code>build\classes;.*util.*;Excluded1;pl.pwr.wroc.judy.Excluded2</code> <br/>
 * Classes containing <code>util</code> in their package name from directory
 * <code>build\classes</code> will be listed omitting classes <code>Excluded1
 * </code> and <code>Excluded2</code>.
 *
 * @author pmiwaszko
 */
public class Directory {
	private static final Logger LOGGER = getLogger(Directory.class);
	private static final int EXTENSION_LENGTH = ".class".length();
	private static final char DOT = '.';
	private final String workspace;
	private final String path;
	private final String regex;
	private final Set<String> excluded;

	/**
	 * <code>Directory</code> constructor.
	 *
	 * @param workspace    canonical path to workspace
	 * @param description  description of the directory, see {@link Directory} for
	 *                     details
	 * @param defaultRegex regular exception that should be used if description does not
	 *                     contain one
	 */
	public Directory(final String workspace, final String description, final String defaultRegex) {
		this.workspace = workspace;

		final String[] values = description.split(";");
		path = values[0];

		if (values.length > 1 && !values[1].trim().isEmpty()) {
			regex = values[1];
		} else {
			regex = defaultRegex;
		}

		excluded = new HashSet<>();
		for (int i = 2; i < values.length; i++) {
			if (!values[i].trim().isEmpty()) {
				excluded.add(values[i]);
			}
		}
	}

	/**
	 * <code>Directory</code> constructor.
	 *
	 * @param workspace   canonical path to workspace
	 * @param description description of the directory, see {@link Directory} for
	 *                    details
	 */
	public Directory(final String workspace, final String description) {
		this(workspace, description, ".*");
	}

	/**
	 * List Java classes in the directory. Classes are listed based on
	 * recursively listed <code>.class</code> files.
	 *
	 * @return list of package class names
	 */
	public List<String> listClasses() {
		final List<String> classes = new LinkedList<>();
		final File directory = new File(workspace, path);
		processDirectory(classes, directory, directory.getAbsolutePath().length() + 1);

		return classes;
	}

	/**
	 * List <code>.jar</code> files in the directory. This operation is not
	 * recursive. Regex and exclusions from the directory description are
	 * ignored.
	 *
	 * @return list of paths relative to workspace
	 */
	public List<String> listJars() {
		final List<String> jars = new LinkedList<>();
		final File directory = new File(workspace, path);
		File[] files = directory.listFiles();
		if (files != null) {
			for (final File file : files) {
				if (file.getName().toLowerCase().endsWith(".jar")) {
					final String relativePath = getRelativePath(file);
					if (relativePath != null) {
						jars.add(relativePath);
					}
				}
			}
		}
		return jars;
	}

	private void processDirectory(final List<String> classes, final File directory, final int prefix) {
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (final File item : files) {
					if (item.isDirectory()) {
						processDirectory(classes, item, prefix);
					} else {
						processFile(classes, item, prefix);
					}
				}
			}
		}
	}

	private void processFile(final List<String> classes, final File file, final int prefix) {
		final String fileName = file.getName();
		if (isClassFile(fileName)) {
			final String name = getClassName(file, prefix);
			final String simpleName = name.substring(name.lastIndexOf(DOT) < 0 ? 0 : name.lastIndexOf(DOT) + 1);
			if (isMatching(name, simpleName)) {
				classes.add(name);
			}
		}
	}

	private static boolean isClassFile(final String fileName) {
		return fileName.toLowerCase().matches("\\w*\\.class");
	}

	/**
	 * Get package name of the class, e.g. <code>java.lang.Object</code>.
	 */
	private static String getClassName(final File file, final int prefix) {
		final String path = file.getAbsolutePath();
		return path.substring(prefix, path.length() - EXTENSION_LENGTH).replace(File.separatorChar, DOT);
	}

	private boolean isMatching(final String packageName, final String simpleName) {
		return packageName.matches(regex)
				&& !(excluded != null && (excluded.contains(packageName) || excluded.contains(simpleName)));
	}

	private String getRelativePath(final File file) {
		try {
			final String canonicalPath = file.getCanonicalPath();
			return canonicalPath.substring(workspace.length() + 1);
		} catch (final IOException e) {
			LOGGER.error("I/O error occured when getting relative path for file " + file.getAbsolutePath());
		}
		return null;
	}

}
