package pl.wroc.pwr.judy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class providing source code for result writer.
 *
 * @author gdziemidowicz
 * @author pmiwaszko
 */
public class SourceCodeCache {
	private final List<File> sourceDirs;

	private final Map<String, List<Line>> cache;

	/**
	 * Default size of cache.
	 */
	public static final int DEAFULT_CACHE_SIZE = 200;

	/**
	 * Default count of lines surrounding interesting line in a snippet.
	 */
	public static final int DEAFULT_LINES_COUNT = 3;

	/**
	 * <code>SourceCodeCache</code> constructor.
	 */
	public SourceCodeCache(String workspace, List<String> sourcepath, int size) {
		sourceDirs = new ArrayList<>(INITIAL_SOURCE_DIRS_SIZE);
		if (sourcepath != null) {
			for (String dir : sourcepath) {
				sourceDirs.add(new File(workspace, dir));
			}
		}
		cache = Collections.synchronizedMap(new Cache<String, List<Line>>(size));
	}

	/**
	 * <code>SourceCodeCache</code> constructor with default value of cache
	 * size.
	 */
	public SourceCodeCache(String workspace, List<String> sourcepath) {
		this(workspace, sourcepath, DEAFULT_CACHE_SIZE);
	}

	/**
	 * Get source code snippet from the given class.
	 *
	 * @param className  name of the class
	 * @param lineNumber central line number
	 * @param linesCount number of lines surrounding central line from both sides
	 * @return source code snippet
	 */
	public SourceCode get(String className, int lineNumber, int linesCount) {
		if (sourceDirs.isEmpty()) {
			return null;
		}
		String fileName = className.replace('.', '/').concat(".java");
		for (File dir : sourceDirs) {
			File file = new File(dir, fileName);
			if (file.exists()) {
				return new SourceCode(file, getContents(className, file, lineNumber, linesCount));
			}
		}
		return null;
	}

	/**
	 * Get source code snippet from the given class with default value of
	 * surrounding lines count.
	 *
	 * @param className  name of the class
	 * @param lineNumber central line number
	 * @return source code snippet
	 * @see #get(String, int, int)
	 */
	public SourceCode get(String className, int lineNumber) {
		return get(className, lineNumber, DEAFULT_LINES_COUNT);
	}

	private List<Line> getContents(String className, File file, int lineNumber, int linesCount) {
		if (cache.containsKey(className)) {
			return subList(cache.get(className), lineNumber, linesCount);
		}
		ArrayList<Line> contents = new ArrayList<>();
		try {
			try (BufferedReader input = new BufferedReader(new FileReader(file))) {
				int i = 1;
				String line;
				while ((line = input.readLine()) != null) {
					contents.add(new Line(i++, line));
				}
				contents.trimToSize();
				cache.put(className, contents);
			}
		} catch (IOException ex) {
			cache.put(className, null);
			return null;
		}

		return subList(contents, lineNumber, linesCount);
	}

	private List<Line> subList(List<Line> list, int lineNumber, int linesCount) {
		int start = lineNumber - linesCount - 1 < 0 ? 0 : lineNumber - linesCount - 1;
		int stop = lineNumber + linesCount >= list.size() ? list.size() : lineNumber + linesCount;

		if (stop < start) {
			return null;
		}

		return list.subList(start, stop);
	}

	private static final int INITIAL_SOURCE_DIRS_SIZE = 5;

	/**
	 * Line of source code in a snippet.
	 *
	 * @author pmiwaszko
	 */
	public static final class Line {
		private final int number;
		private final String content;

		Line(int number, String content) {
			this.number = number;
			this.content = content;
		}

		/**
		 * Get line number.
		 */
		public int getNumber() {
			return number;
		}

		/**
		 * Get content of the line.
		 */
		public String getContent() {
			return content;
		}
	}

	/**
	 * Source code snippet.
	 *
	 * @author pmiwaszko
	 */
	public static final class SourceCode {
		private final File file;
		private final List<Line> lines;

		SourceCode(File file, List<Line> lines) {
			this.file = file;
			this.lines = lines;
		}

		/**
		 * Get containing file.
		 */
		public File getFile() {
			return file;
		}

		/**
		 * Get list of lines.
		 */
		public List<Line> getLines() {
			return Collections.unmodifiableList(lines);
		}
	}
}
